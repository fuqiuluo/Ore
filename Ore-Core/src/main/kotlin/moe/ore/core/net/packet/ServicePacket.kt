/*
 * English :
 *  The project is protected by the MPL open source agreement.
 * Open source agreement warning that prohibits deletion of project source code files.
 * The project is prohibited from acting in illegal areas.
 * All illegal activities arising from the use of this project are the responsibility of the second author, and the original author of the project is not responsible
 *
 *  中文：
 *  该项目由MPL开源协议保护。
 *  禁止删除项目源代码文件的开源协议警告内容。
 * 禁止使用该项目在非法领域行事。
 * 使用该项目产生的违法行为，由使用者或第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 *  オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 *
 */

@file:Suppress("EqualsOrHashCode")
package moe.ore.core.net.packet

import moe.ore.core.helper.DEFAULT_TEA_KEY
import moe.ore.core.helper.DataManager
import moe.ore.core.net.BotClient
import moe.ore.core.protocol.ProtocolInternal
import moe.ore.core.util.QQUtil
import moe.ore.helper.*
import moe.ore.util.TeaUtil
import java.util.*

class ToService(val seq: Int, val commandName: String, val body: ByteArray) {
    var packetType: PacketType = PacketType.LoginPacket

    var firstToken : ByteArray? = null

    var secondToken : ByteArray? = null
}

enum class PacketType(val flag1: Int, val flag2: Byte) {
    /**
     * 登录包
     */
    LoginPacket(0xa, 0x2),

    /**
     * 刷新st
     */
    ExChangeEmpSt(0xa, 0x2),

    /**
     * 上线包
     */
    SvcRegister(0xa, 0x1),

    /**
     * 刷新cookie
     */
    ExChangeEmpA1(0xb, 0x2),

    /**
     * 服务包
     */
    ServicePacket(0xb, 0x1),

    /**
     * 二维码登录
     */
    WloginGetQRCode(0x8, 0x2)
}

fun ToService.sendTo(client: BotClient) : PacketSender {
    val uin = client.uin
    val manager = DataManager.manager(uin)
    val userStSig = manager.userSigInfo
    val protocolInfo = if(uin == 0L) {
        ProtocolInternal[manager.protocolType]
    } else ProtocolInternal[manager.protocolType]
    val session = manager.session
    val deviceInfo = manager.deviceInfo

    val teaKey = when (packetType) {
        PacketType.WloginGetQRCode,
        PacketType.ExChangeEmpSt,
        PacketType.ExChangeEmpA1,
        PacketType.LoginPacket ->
            DEFAULT_TEA_KEY

        // =============================================
        PacketType.ServicePacket,
        PacketType.SvcRegister -> userStSig.d2Key.ticket()
    }

    val out = newBuilder().apply { writeBlockWithIntLen( { it + 4 } ) {
        writeInt(packetType.flag1)
        writeByte(packetType.flag2)
        when(packetType) {
            // write token
            PacketType.WloginGetQRCode,
            PacketType.ExChangeEmpSt,
            PacketType.LoginPacket,
            PacketType.SvcRegister

            -> {
                val token = firstToken ?: EMPTY_BYTE_ARRAY
                writeInt(token.size + 4)
                writeBytes(token)
            }
            // =============================================
            PacketType.ServicePacket,
            PacketType.ExChangeEmpA1 ->
                writeInt(seq)
        }
        writeByte(0)
        uin.toString().let {
            writeInt(it.length + 4)
            writeString(it)
        }
        writeBytes(TeaUtil.encrypt(newBuilder().apply {
            writeBlockWithIntLen({ it + 4 }) {
                when(packetType) {
                    PacketType.WloginGetQRCode,
                    PacketType.ExChangeEmpSt,
                    PacketType.SvcRegister,
                    PacketType.LoginPacket -> {
                        writeInt(seq)
                        writeInt(protocolInfo.appId)
                        writeInt(protocolInfo.appId)
                        writeInt(16777216)
                        writeInt(0)

                        val token = secondToken ?: EMPTY_BYTE_ARRAY
                        writeInt(if(secondToken == null) 0 else 256) // Token Type 如果有Token就是256
                        writeInt(token.size + 4)
                        writeBytes(token)

                        commandName.let {
                            writeInt(it.length + 4)
                            writeString(it)
                        }
                        session.msgCookie.let {
                            writeInt(it.size + 4)
                            writeBytes(it)
                        }
                        deviceInfo.androidId.let {
                            writeInt(it.length + 4)
                            writeString(it)
                        }
                        deviceInfo.ksid.let {
                            writeInt(it.size + 4)
                            writeBytes(it)
                        }
                        protocolInfo.protocolDetail.let {
                            writeShort(it.length + 2)
                            writeString(it)
                        }
                        // writeInt(4) // qimei 的位置
                    }
                    PacketType.ServicePacket, PacketType.ExChangeEmpA1 -> {
                        commandName.let {
                            writeInt(it.length + 4)
                            writeString(it)
                        }
                        session.msgCookie.let {
                            writeInt(it.size + 4)
                            writeBytes(it)
                        }
                        writeInt(4)
                    }
                }
            }
            // =============================================
            writeBlockWithIntLen({ it + 4 }) {
                writeBytes(body)
            }
        }.toByteArray(), teaKey))
    } }.toByteArray()

    /**
    kotlin.runCatching {
        println("teaKey : " + teaKey.toHexString())
        println("commandId : $commandName ==> ${out.toHexString()}")
    }**/

    return PacketSender(client, out, commandName, seq)
}

open class PacketSender (
    private val client: BotClient,
    private val body : ByteArray,
    val commandName: String,
    val seq : Int) {

    private var block : ((FromService) -> Unit)? = null

    private val handler: SingleHandler = client.registerCommonHandler(object : SingleHandler(seq, commandName) {
        override fun check(from: FromService): Boolean {
            if(super.check(from)) {
                block?.invoke(from)
                return true
            }
            return false
        }
    })

    /**
     * 异步
     */
    fun call(block : ((FromService) -> Unit)? = null, timeout : Long = 5 * 1000) {
        Timer().apply {
            schedule(object : TimerTask() {
                override fun run() {
                    client.unRegisterCommonHandler(handler.hashCode())
                    this@apply.cancel()
                }
            }, timeout)
        }
        this.block = block
        send()
    }

    private fun send() {
        println("F = Packet[cmd = $commandName, seq = $seq,body = ${body.toHexString()}]")
        this.client.send(body)
    }

    companion object {
        /**
         * 同步
         */
        infix fun PacketSender.sync(timeout : Long) : FromService? {
            send()
            // println("开始等")
            if(this.handler.wait(timeout)) {
                // println("等完了")
                return this.handler.fromService
            }
            return null
        }
    }
}

data class FromService(
    val seq: Int,
    val commandName: String,
    val body: ByteArray
) {
    var msgCookie : ByteArray = 0x02B05B8B.toByteArray()

    override fun hashCode(): Int {
        return QQUtil.hash(seq, commandName)
    }

    override fun toString(): String {
        return "FromService(seq=" + seq + ", commandName=" + commandName + ", body= " + body.toHexString() + " )"
    }
}