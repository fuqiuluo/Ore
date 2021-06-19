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

package moe.ore.core.net.packet

import moe.ore.core.helper.*
import moe.ore.core.helper.buildFirstLayer
import moe.ore.core.helper.buildSecondLayer
import moe.ore.core.net.BotClient
import moe.ore.core.util.QQUtil
import moe.ore.helper.toHexString

class ToService(val seq: Int, val commandName: String, val body: ByteArray) {
    var packetType: PacketType = PacketType.LoginPacket
}

enum class PacketType(val flag1: Int, val flag2: Byte) {
    /**
     * 登录包
     */
    LoginPacket(0xa, 0x2),
    ExChangeEmp(0xb, 0x2)
}

fun ToService.sendTo(client: BotClient) : PacketSender {
    val uin = client.uin
    val teaKey = when (packetType) {
        PacketType.ExChangeEmp, PacketType.LoginPacket -> DEFAULT_TEA_KEY
    }
    val out = buildFirstLayer(uin, teaKey, packetType, seq, buildSecondLayer(uin, commandName, body, packetType, seq))

    println(out.toHexString())

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
    fun call(block : ((FromService) -> Unit)? = null) {
        this.client.send(body)
        this.block = block
    }

    companion object {
        /**
         * 同步
         */
        infix fun PacketSender.sync(timeout : Long) : FromService? {
            this.client.send(body)
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FromService

        if (seq != other.seq) return false
        if (commandName != other.commandName) return false
        if (!body.contentEquals(other.body)) return false

        return true
    }

    override fun hashCode(): Int {
        return QQUtil.hash(seq, commandName)
    }
}