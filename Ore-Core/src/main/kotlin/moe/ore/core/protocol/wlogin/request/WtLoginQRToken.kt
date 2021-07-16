package moe.ore.core.protocol.wlogin.request

import moe.ore.core.net.packet.PacketType
import moe.ore.core.protocol.buildTlv
import moe.ore.core.protocol.wlogin.EncryptMethod
import moe.ore.helper.newBuilder
import moe.ore.helper.toByteArray
import moe.ore.helper.writeBytes

class WtLoginQRToken(uin : Long) : WtRequest(uin, CMD_LOGIN, 0x810, 9, 0x7, EncryptMethod.SPECIAL_QR) {
    override fun packetType(): PacketType = PacketType.LoginPacket

    override fun makeTlv(seq: Int) = newBuilder().apply {
        writeShort(24)
        writeBytes(tlv.t18())
        writeBytes(tlv.t1())
        writeBytes(buildTlv(0x106) { writeBytes(userStSig.encryptA1.ticket()) })
        writeBytes(tlv.t116())
        writeBytes(tlv.t100())
        writeBytes(tlv.t107())
        writeBytes(tlv.t108())
        writeBytes(tlv.t142())
        writeBytes(tlv.t144( userStSig.gtKey.ticket() ))
        writeBytes(tlv.t145())
        writeBytes(tlv.t147())
        writeBytes(tlv.t16a(userStSig.noPicSig.ticket()))
        writeBytes(tlv.t154(seq))
        writeBytes(tlv.t141())
        writeBytes(tlv.t8())
        writeBytes(tlv.t187())
        writeBytes(tlv.t188())
        writeBytes(tlv.t194())
        writeBytes(tlv.t191())
        writeBytes(tlv.t202())
        writeBytes(tlv.t177())
        writeBytes(tlv.t516())
        writeBytes(tlv.t521())
        writeBytes(tlv.t318(session.t318))
    }.toByteArray()
}