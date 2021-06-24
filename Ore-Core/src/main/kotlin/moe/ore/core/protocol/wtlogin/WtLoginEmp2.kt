package moe.ore.core.protocol.wtlogin

import kotlinx.io.core.BytePacketBuilder
import moe.ore.core.net.packet.PacketType
import moe.ore.core.protocol.ECDH_PUBLIC_KEY
import moe.ore.core.protocol.buildTlv
import moe.ore.helper.*

/**
 *@author 飞翔的企鹅
 *create 2021-06-16 09:33
 */
class WtLoginEmp2(uin: Long) : WtLogin(uin, EXCHANGE_EMP, 0x810, 0x45) {
    init {
//         packetType = PacketType.ExChangeEmp
    }

    /**
     * 附加为空
     */
    override fun buildAttach(): BytePacketBuilder = createBuilder()

    override fun publicKey() = ECDH_PUBLIC_KEY

    override fun encryptKey() = session.randomKey

    override fun buildTlvBody(seq: Int) = createBuilder().apply {
        writeShort(11)
        writeShort(17)
        writeBytes(tlv.t100(appId = 1))
        writeBytes(tlv.t10a())
        writeBytes(tlv.t116())
        writeBytes(tlv.t108())
        writeBytes(tlv.t144(userStSig.gtKey.ticket()))
        writeBytes(tlv.t143())
        writeBytes(tlv.t142())
        writeBytes(tlv.t154(seq))
        writeBytes(tlv.t18())
        writeBytes(tlv.t141())
        writeBytes(tlv.t8())
        writeBytes(tlv.t147())
        writeBytes(tlv.t177())
        writeBytes(tlv.t187())
        writeBytes(tlv.t188())
        writeBytes(tlv.t194())
        writeBytes(tlv.t511())
    }.toByteArray()
}
