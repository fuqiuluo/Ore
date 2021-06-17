package moe.ore.core.protocol.wtlogin

import moe.ore.helper.createBuilder
import moe.ore.helper.toByteArray
import moe.ore.helper.writeBytes

class WtLoginTicket(val ticket : String, val t546 : ByteArray?, uin : Long) : WtLogin(uin, LOGIN, 0x810, 0x87) {
    override fun build(seq: Int): ByteArray {
        return createBuilder().apply {

            writeBytes(tlv.t104(userStSig.t104))


        }.toByteArray()
    }
}