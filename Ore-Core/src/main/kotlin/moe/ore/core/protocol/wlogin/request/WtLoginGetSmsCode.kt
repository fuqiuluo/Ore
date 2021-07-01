package moe.ore.core.protocol.wlogin.request

import moe.ore.core.net.packet.PacketType
import moe.ore.helper.newBuilder
import moe.ore.helper.toByteArray
import moe.ore.helper.writeBytes

class WtLoginGetSmsCode(uin: Long) : WtRequest(uin, CMD_LOGIN, 0x810, 8, 0x7)  {
    override fun packetType(): PacketType = PacketType.LoginPacket

    override fun makeTlv(seq: Int): ByteArray = newBuilder().apply {
        writeShort(7)
        writeBytes(tlv.t8())
        writeBytes(tlv.t104(userStSig.t104))
        writeBytes(tlv.t116())
        writeBytes(tlv.t174(userStSig.t174))
        writeBytes(tlv.t17a())
        writeBytes(tlv.t197())
        writeBytes(tlv.t542())
    }.toByteArray()

}