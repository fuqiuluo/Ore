package moe.ore.core.protocol.wtlogin

import moe.ore.helper.createBuilder
import moe.ore.helper.toByteArray
import moe.ore.helper.writeBytes
import moe.ore.helper.writeShort

class WtLoginRefreshSMSData(uin: Long) : WtLogin(uin, LOGIN, 0x810, 0x7) {
    override fun build(seq: Int): ByteArray {
        val builder = createBuilder()
        builder.writeShort(8)
        builder.writeShort(0)

        builder.writeBytes(tlv.t8())
        builder.writeBytes(tlv.t104(userStSig.t104))
        builder.writeBytes(tlv.t116())
        builder.writeBytes(tlv.t174(userStSig.t174))
        builder.writeBytes(tlv.t17a())

        return builder.toByteArray()
    }
}