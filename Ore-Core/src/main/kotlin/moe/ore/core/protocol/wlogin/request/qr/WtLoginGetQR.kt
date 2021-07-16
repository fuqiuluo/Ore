package moe.ore.core.protocol.wlogin.request.qr

import moe.ore.core.protocol.buildTlv
import moe.ore.helper.*

class WtLoginGetQR : OldWRequest(0, CMD_TRANS_EMP, 0xff, 0x31, 0x812, 0x7) {
    override fun makeData(seq : Int) = newBuilder().apply {
        writeShort(0)
        writeInt(0x10)
        writeLong(0) // 固定
        writeByte(8) // 固定
        writeBytesWithShortLen(EMPTY_BYTE_ARRAY) // length


        writeShort(6) // tlv size
        writeBytes(buildTlv(0x16){
            writeInt(5)
            writeInt(protocol.subAppId)
            writeInt(protocol.appId)
            writeBytes(device.guid)
            writeStringWithShortLen(protocol.packageName)
            writeStringWithShortLen(protocol.packageVersion)
            writeBytesWithShortLen(protocol.tencentSdkMd5)
        })
        writeBytes(buildTlv(0x1b){
            writeLong(0)
            writeInt(8)
            writeInt(4)
            writeInt(72)
            writeInt(2)
            writeInt(2)
            writeShort(0)
        })
        writeBytes(buildTlv(0x1d){
            writeByte(1)
            writeInt(protocol.miscBitmap)
            writeInt(0)
            writeInt(0)
            writeByte(0)
        })
        writeBytes(buildTlv(0x1f){
            writeByte(1)
            writeStringWithShortLen(device.osType)
            writeStringWithShortLen(device.androidVersion)
            writeShort(device.netType.value)
            writeShort(0)
            writeStringWithIntLen(device.apn)
        })
        writeBytes(buildTlv(0x33){
            writeBytes(device.guid)
        })
        writeBytes(buildTlv(0x35){
            // 00 35
            // 00 04
            // 00 00 00 68
            writeInt(0x68)
        })
    }.toByteArray()
}