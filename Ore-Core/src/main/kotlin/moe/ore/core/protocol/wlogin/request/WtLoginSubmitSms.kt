package moe.ore.core.protocol.wlogin.request

import moe.ore.core.net.packet.PacketType
import moe.ore.helper.newBuilder
import moe.ore.helper.toByteArray
import moe.ore.helper.writeBytes

class WtLoginSubmitSms(uin: Long, private val code: String) : WtRequest(uin, CMD_LOGIN, 0x810, 7, 0x7) {
    override fun makeTlv(seq: Int) = newBuilder().apply {
        writeShort(8)
        writeBytes(tlv.t8())
        writeBytes(tlv.t104(userStSig.t104))
        writeBytes(tlv.t116())
        writeBytes(tlv.t174(userStSig.t174))
        writeBytes(tlv.t17c(code))
        writeBytes(tlv.t401(userStSig.G))
        writeBytes(tlv.t198())
        writeBytes(tlv.t542())
    }.toByteArray()

    override fun packetType(): PacketType = PacketType.LoginPacket
}