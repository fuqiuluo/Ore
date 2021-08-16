package moe.ore.core.protocol.wlogin.request

import moe.ore.core.net.packet.PacketType
import moe.ore.helper.*

class WtLoginDevicePass(uin: Long, private val t402: ByteArray) : WtRequest(uin, CMD_LOGIN, 0x810, 20, 0x7) {
    override fun packetType(): PacketType = PacketType.LoginPacket

    override fun makeTlv(seq: Int): ByteArray = newBuilder().apply {
        writeShort(5 + if (session.randSeed.isNotEmpty()) 1 else 0)
        writeBytes(tlv.t8())
        writeBytes(tlv.t104(session.t104))
        writeBytes(tlv.t116())
        writeBytes(tlv.t401(userStSig.G))
        writeBytes(tlv.t402(t402))
        // 要想合成G t402必须拥有
        if (session.randSeed.isNotEmpty()) {
            writeBytes(tlv.t403(session.randSeed))
        }
    }.toByteArray()
}