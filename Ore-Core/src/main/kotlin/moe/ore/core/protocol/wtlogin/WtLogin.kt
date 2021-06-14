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
    private val manager = DataManager.manager(uin)
    val device = manager.deviceInfo
    val tlv: Tlv by lazy { Tlv(uin) }

    abstract fun build(seq: Int): ByteArray

    fun sendTo(botClient: BotClient): Int {
        val seq = manager.recorder.nextSeq()
        val body = makeBody(seq)
        val to = ToService(seq, commandName, body)
        to.sendTo(botClient)
        // println("是否发包堵塞呢？")
        return seq
    }

    private fun makeBody(seq: Int): ByteArray {
        val builder = createBuilder()
        builder.writePacket(createBuilder().apply {
            writeByte(0x2)

            val tlvBody = TeaUtil.encrypt(build(seq), ECDH_SHARE_KEY)
            // println(tlvBody.toHexString())

            writeShort(tlvBody.size + 4 + 49 + ECDH_PUBLIC_KEY.size)
            writeShort(0x1f41)
            writeShort(commandId)
            writeShort(1)
            writeLongToBuf32(uin)

            writeByte(3)
            writeByte(encryptType.toByte())
            writeInt(0)
            writeByte(2)
            writeInt(0)
            writeInt(0)
            writeByte(2)
            writeByte(1)
            // 03 87 00 00 00 00 02 00 00 00 00 00 00 00 00 02 01

            writeBytes(device.randKey)
            writeShort(305)
            writeShort(ECDH_VERSION.toShort())
            writeBytesWithShortSize(ECDH_PUBLIC_KEY)
            writeBytes(tlvBody)

            writeByte(0x3)
        })
        return builder.toByteArray()
    }

    companion object {
        const val LOGIN = "wtlogin.login"
    }
}