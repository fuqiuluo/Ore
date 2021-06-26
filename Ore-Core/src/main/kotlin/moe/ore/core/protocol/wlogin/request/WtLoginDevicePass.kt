package moe.ore.core.protocol.wlogin.request

import moe.ore.core.net.packet.PacketType
import moe.ore.helper.newBuilder
import moe.ore.helper.toByteArray
import moe.ore.helper.writeBytes
import moe.ore.helper.writeShort

class WtLoginDevicePass(uin : Long, private val g : ByteArray, private val t402 : ByteArray, private val t403 : ByteArray?) : WtRequest(uin, CMD_LOGIN, 0x810, 20, 0x7) {
    override fun packetType(): PacketType = PacketType.LoginPacket

    override fun makeTlv(seq: Int): ByteArray = newBuilder().apply {
        writeShort(5 + if(t403 == null) 0 else 1)
        writeBytes(tlv.t8())
        writeBytes(tlv.t104(userStSig.t104))
        writeBytes(tlv.t116())
        writeBytes(tlv.t401(g))
        writeBytes(tlv.t402(t402))
        // 要想合成G t402必须拥有
        t403?.let { writeBytes(tlv.t403(it)) }
    }.toByteArray()
}