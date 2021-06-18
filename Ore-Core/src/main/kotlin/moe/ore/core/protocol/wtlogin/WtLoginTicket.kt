package moe.ore.core.protocol.wtlogin

import moe.ore.core.client.ClientPow
import moe.ore.helper.createBuilder
import moe.ore.helper.toByteArray
import moe.ore.helper.toHexString
import moe.ore.helper.writeBytes

class WtLoginTicket(private val ticket : String, private val t546 : ByteArray?, uin : Long) : WtLogin(uin, LOGIN, 0x810, 0x87) {
    override fun build(seq: Int): ByteArray {
        return createBuilder().apply {
            writeShort(2)
            writeShort(5)
            writeBytes(tlv.t193(ticket)) // 1
            writeBytes(tlv.t8()) // 2
            writeBytes(tlv.t104(userStSig.t104)) // 3
            writeBytes(tlv.t116()) // 4
            println(t546?.toHexString())
            writeBytes(tlv.t547(ClientPow().calc(t546)))
        }.toByteArray()
    }
}