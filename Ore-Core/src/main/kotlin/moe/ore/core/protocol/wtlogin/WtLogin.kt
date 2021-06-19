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

import moe.ore.core.helper.DataManager
import moe.ore.core.net.BotClient
import moe.ore.core.net.packet.PacketSender
import moe.ore.core.net.packet.PacketType
import moe.ore.core.net.packet.ToService
import moe.ore.core.net.packet.sendTo
import moe.ore.core.protocol.ECDH_PUBLIC_KEY
import moe.ore.core.protocol.ECDH_SHARE_KEY
import moe.ore.core.protocol.ECDH_VERSION
import moe.ore.core.protocol.Tlv
import moe.ore.helper.*
import moe.ore.util.TeaUtil

abstract class WtLogin(
    val uin: Long,
    private val commandName: String,
    private val commandId: Int,
    private val encryptType: Int
) {
    var packetType = PacketType.LoginPacket

    val manager = DataManager.manager(uin)
    val device = manager.deviceInfo
    val session = manager.session
    val userStSig = manager.wLoginSigInfo
    val tlv: Tlv by lazy { Tlv(uin) }

    open fun publicKey() : ByteArray {
        return ECDH_PUBLIC_KEY
    }

    open fun encryptKey() : ByteArray {
        return ECDH_SHARE_KEY
    }

    open fun buildAttach() = createBuilder().apply {
        writeByte(2)
        writeByte(1)
        writeBytes(session.randomKey)
        writeShort(0x131)
        writeShort(ECDH_VERSION.toShort())
    }

    abstract fun buildTlvBody(seq: Int): ByteArray

    fun sendTo(botClient: BotClient): PacketSender {
        val seq = manager.session.nextPacketRequestId()
        val body = makeBody(seq)
        val to = ToService(seq, commandName, body)
        to.packetType = packetType
        // println("是否发包堵塞呢？")
        return to.sendTo(botClient)
    }

    private fun makeBody(seq: Int): ByteArray {
        val builder = createBuilder()
        builder.writePacket(createBuilder().apply {
            writeByte(0x2)

            val tlvBody = TeaUtil.encrypt(buildTlvBody(seq), encryptKey())
            val attach = buildAttach()
            val pubKey = publicKey()

            writeShort(tlvBody.size + 4 + pubKey.size + 27 + attach.size)
            writeShort(8001)
            writeShort(commandId)
            writeShort(1)
            writeLongToBuf32(uin)

            writeByte(3)
            writeByte(encryptType.toByte())
            writeByte(0)
            writeInt(2) // 天王老子来了这个2也是int 自己逆向qq去看，傻卵
            writeInt(0)
            writeInt(0)

            writePacket(attach)

            writeBytesWithShortLen(pubKey)

            writeBytes(tlvBody)

            writeByte(0x3)
        })
        return builder.toByteArray()
    }

    companion object {
        const val LOGIN = "wtlogin.login"
        const val EXCHANGE_EMP = "wtlogin.exchange_emp"
    }
}