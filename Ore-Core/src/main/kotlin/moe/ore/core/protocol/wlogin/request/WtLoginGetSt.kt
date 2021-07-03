package moe.ore.core.protocol.wlogin.request

import moe.ore.core.net.packet.PacketType
import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.helper.newBuilder
import moe.ore.helper.toByteArray
import moe.ore.helper.writeBytes

class WtLoginGetSt(uin: Long) : WtRequest(uin, CMD_EXCHANGE_EMP, 0x810, 11, 0x7)  {
    // override fun ecdhEncryptBody(): ByteArray = EMPTY_BYTE_ARRAY

    // override fun publicKey(): ByteArray = userStSig.wtSessionTicket.ticket()

    // override fun teaKey(): ByteArray = userStSig.wtSessionTicketKey.ticket()

    // override fun packetType(): PacketType = PacketType.ExChangeEmpA1

    override fun makeTlv(seq: Int): ByteArray = newBuilder().apply {
        writeShort(20)
        writeBytes(tlv.t100())
        writeBytes(tlv.t10a(userStSig.tgt.ticket()))
        writeBytes(tlv.t116())
        writeBytes(tlv.t108())
        // writeBytes(tlv.t109()) to t144
        writeBytes(tlv.t143(userStSig.d2.ticket()))
        writeBytes(tlv.t112())
        writeBytes(tlv.t144(userStSig.tgtKey.ticket()))
        writeBytes(tlv.t145())
        writeBytes(tlv.t147())
        writeBytes(tlv.t142())
        writeBytes(tlv.t154(seq))
        writeBytes(tlv.t18())
        writeBytes(tlv.t141())
        writeBytes(tlv.t8())
        writeBytes(tlv.t511())
        writeBytes(tlv.t172())
        writeBytes(tlv.t177())
        writeBytes(tlv.t187())
        writeBytes(tlv.t188())
        writeBytes(tlv.t202())
    }.toByteArray()

}