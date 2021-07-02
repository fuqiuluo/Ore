package moe.ore.core.protocol.wlogin.request

import moe.ore.core.net.packet.PacketType
import moe.ore.helper.EMPTY_BYTE_ARRAY

class WtLoginGetA1(uin: Long) : WtRequest(uin, CMD_EXCHANGE_EMP, 0x810, 8, 0x7)  {
    override fun ecdhEncryptBody(): ByteArray = EMPTY_BYTE_ARRAY

    override fun publicKey(): ByteArray = userStSig.wtSessionTicket.ticket()

    override fun teaKey(): ByteArray = userStSig.wtSessionTicketKey.ticket()

    override fun makeTlv(seq: Int): ByteArray {

    }

    override fun packetType(): PacketType = PacketType.ExChangeEmp
}