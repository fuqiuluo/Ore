package moe.ore.core.protocol.wlogin.request

import moe.ore.core.client.ClientPow
import moe.ore.core.net.packet.PacketType
import moe.ore.helper.newBuilder
import moe.ore.helper.toByteArray
import moe.ore.helper.writeBytes

class WtLoginSlider(uin : Long, private val ticket : String, private val t546 : ByteArray?) : WtRequest(uin, CMD_LOGIN, 0x810, 2, 0x7) {
    override fun makeTlv(seq: Int): ByteArray = newBuilder().apply {
        writeShort(5)
        writeBytes(tlv.t193(ticket))
        writeBytes(tlv.t8())
        writeBytes(tlv.t104(session.t104))
        writeBytes(tlv.t116())
        writeBytes(tlv.t547(ClientPow().calc(t546)))
    }.toByteArray()

    override fun packetType(): PacketType = PacketType.LoginPacket
}