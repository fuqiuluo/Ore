package moe.ore.core.protocol.wlogin.request.qr

import moe.ore.helper.*

class WtLoginCheckQR(
    val verifySig : ByteArray
): OldWRequest(0, CMD_TRANS_EMP, 0x62, 0x12, 0x812, 0x7) {
    override fun makeData(seq: Int) = newBuilder().apply {
        writeByte(0)
        writeShort(0x501)
        writeInt(0x68)
        writeInt(0x10)
        writeBytesWithShortLen(verifySig) // length
        writeByte(0)
        writeLong(8)
        writeInt(0)
    }.toByteArray()
}