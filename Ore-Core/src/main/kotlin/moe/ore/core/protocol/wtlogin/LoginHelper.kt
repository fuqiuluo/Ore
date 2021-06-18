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

package moe.ore.core.protocol.wtlogin

import kotlinx.io.core.*
import moe.ore.api.LoginResult
import moe.ore.api.listener.CaptchaChannel
import moe.ore.api.listener.OreListener
import moe.ore.core.helper.DataManager
import moe.ore.core.net.BotClient
import moe.ore.core.net.packet.PacketSender
import moe.ore.core.net.packet.PacketSender.Companion.sync
import moe.ore.core.protocol.ECDH_SHARE_KEY
import moe.ore.helper.*
import moe.ore.helper.thread.ThreadManager
import moe.ore.util.TeaUtil
import okhttp3.internal.toHexString

/**
 * 登录器
 * @property uin Long
 * @property listener OreListener?
 * @constructor
 */
internal class LoginHelper(private val uin: Long, private val client: BotClient, private val listener: OreListener?) : Thread() {

    private val threadManager = ThreadManager.getInstance(uin)
    private val manager = DataManager.manager(uin)
    private val userStInfo = manager.wLoginSigInfo
    private val device = manager.deviceInfo
    private val session = manager.session

    override fun run() {
        invoke()
    }

    private fun invoke() {
        // println(Thread.currentThread().name)
        // 禁止使用nio线程进行堵塞等包操作
        handle(sender = WtLoginPassword(uin).sendTo(client))
    }

    private fun handle(sender: PacketSender) {
        val from = sender sync 20 * 1000
        if (from == null) {
            callback(LoginResult.ServerTimeout)
        } else {
            from.body.readLoginPacket { result, tlvMap ->
                tlvMap[0x104]?.let { userStInfo.t104 = it }
                tlvMap[0x403]?.let { session.randSeed = it }
                val t402 = tlvMap[0x402]?.also {
                    userStInfo.G = device.guid + session.mpasswd + it
                    // 字节组拼接
                }

                println("result: $result tlvMap: " + tlvMap.keys.map { "0x" + it.toHexString() })
                when (result) {
                    0 -> onSuccess(tlvMap)
                    1 -> onPasswordWrong()
                    2 -> onCaptcha(tlvMap[0x192]!!, tlvMap[0x546])
                    180 -> onRollBack(tlvMap[0x161])
                    204 -> onDevicePass(t402, tlvMap[0x403])
                    else -> error("unknown login result : $result")
                }
            }
        }
    }

    private inline fun onCaptcha(t192 : ByteArray, t546 : ByteArray?) {
        println(t546?.size)
        val url = String(t192)
        listener?.onCaptcha(object : CaptchaChannel(url) {
            override fun submitTicket(ticket: String) {
                handle(WtLoginTicket(ticket, t546, uin).sendTo(client))
            }
        })
    }

    private inline fun onRollBack(t161 : ByteArray?) {
        t161?.let { it ->
            val tlv = it.toByteReadPacket().withUse { parseTlv(this) }
            tlv[0x173]?.let {
                println("T173 : " + it.toHexString())
            }
            tlv[0x17f]?.let {
                println("T17F : " + it.toHexString())
            }
            // 等待遇到实际的包 再进行分析

            /**
            tlv[0x173]?.let {
                it.reader {
                    val type = readByte()
                    val host = readString(readUShort().toInt())
                    val port = readShort()
                }
            }
            tlv[0x17f]?.let {
                it.reader {
                    val type = readByte()
                    val host = readString(readUShort().toInt())
                    val port = readShort()
                }
            } **/
            tlv[0x172]?.let {
                session.rollBackCount++
                session.rollbackSig = it
                // println("0x172 call rollbackSig")
                client.connect()
            }
        }

    }

    private inline fun onSuccess(tlvMap: Map<Int, ByteArray>) {


    }

    private inline fun onDevicePass(t402 : ByteArray?, t403 : ByteArray?) {
        handle(sender = WtLoginDeviceLockPass(t402, t403, uin).sendTo(client))
    }

    /**
     * 密码错误
     */
    private inline fun onPasswordWrong() {
        callback(LoginResult.PasswordWrong)
    }

    private fun callback(loginResult: LoginResult) = threadManager.addTask { listener?.onLoginFinish(loginResult) }

    companion object {
        @JvmStatic
        private inline fun ByteArray.readLoginPacket(block: (Int, Map<Int, ByteArray>) -> Unit) {
            val reader = this.toByteReadPacket()
            reader.discardExact(1 + 2 + 2 + 2 + 2)
            // 02 (dis) xx xx (dis) 1f 41 (dis) 08 01 (dis) 00 01 (dis)
            val uin = reader.readUInt().toLong()
            // println(uin)
            val manager = DataManager.manager(uin)
            reader.discardExact(2)
            // 00 00 (dis)
//            val subCommand = reader.readShort().toInt() // subCommand discardExact 2
//            println(subCommand)
            val result = reader.readByte().toInt() and 0xff
            // 235 协议版本过低
            val teaKey = if (result == 180) manager.deviceInfo.randKey else ECDH_SHARE_KEY
            val tlvBody = TeaUtil.decrypt(reader.readBytes(reader.remaining.toInt() - 1), teaKey).toByteReadPacket().also { it.discardExact(3) }
            block.invoke(result, parseTlv(tlvBody))
        }

        @JvmStatic
        private fun parseTlv(bs: ByteReadPacket): Map<Int, ByteArray> {
            val size = bs.readShort().toInt()
            val map = hashMapOf<Int, ByteArray>()
            repeat(size) {
                val ver = bs.readUShort().toInt()
                val tSize = bs.readUShort().toInt()
                val content = bs.readBytes(tSize)
                map[ver] = content
            }
            return map
        }
    }
}