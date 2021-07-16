package moe.ore.core.protocol.wlogin.request.qr

import moe.ore.core.helper.DataManager
import moe.ore.core.net.BotClient
import moe.ore.core.net.packet.PacketSender
import moe.ore.core.net.packet.PacketType
import moe.ore.core.net.packet.ToService
import moe.ore.core.net.packet.sendTo
import moe.ore.core.protocol.ProtocolInternal
import moe.ore.core.protocol.buildTlv
import moe.ore.core.protocol.wlogin.request.WtRequest
import moe.ore.helper.*

class WtLoginGetQR(
    dataPath : String = "",
) {
    private var uin: Long = 0
    private val manager = DataManager.init(uin, dataPath).apply {
        protocolType = ProtocolInternal.ProtocolType.ANDROID_WATCH
    }
    private val device = manager.deviceInfo
    private val session = manager.session
    private val protocol = ProtocolInternal[ProtocolInternal.ProtocolType.ANDROID_WATCH]

    fun packetType() : PacketType = PacketType.WloginGetQRCode

    private fun makeBody(seq : Int): ByteArray = newBuilder().apply {
        val encryptBody =  newBuilder().apply {
            writeByte(1)
            writeByte(1)
            writeBytes(session.randomKey)
            writeShort(0x102)
        }.toByteArray()

        val publicKey = "04A1E0AA4DBD8FC08B2CA11DC5942AA4FDB29FCA71E66699D900ACA93D13DB86A632BA9357C20DF9913A648E244A591505".hex2ByteArray()
        val tlvBody = newBuilder().apply {
            writeTeaEncrypt("C5F805A74E0CFDA5999D6E75500B36A2".hex2ByteArray()) {
                val tlv = makeTlv()
                writeShort(0)
                writeByte(0xff.toByte())
                writeInt(16)
                writeHex("00000072000000")
                writeInt(currentTimeSeconds())
                writeByte(2)
                writeBlockWithShortLen({ it + 1 + 1 + 2 }) {
                    writeShort(0x31) // subCmd
                    writeHex("0000000000000000000000000000000000000000000300000032")
                    writeInt(0)
                    writeInt(0)
                    writeInt(0)
                    writeShort(0)
                    writeInt(16)
                    writeLong(0)
                    writeByte(8)
                    writeShort(0) // 0
                    writeBytes(tlv)
                }
                writeByte(3)

                // println(toByteArray().toHexString())
            }
        }

        writeByte(2)
        writeShort(29 + encryptBody.size + publicKey.size + 2 + tlvBody.size)
        writeShort(8001)
        writeShort(0x812)
        writeShort(1)
        writeLongToBuf32(uin)
        writeByte(3)
        writeByte(7)
        writeByte(0)
        writeInt(2)
        writeInt(0)
        writeInt(0)
        writeBytes(encryptBody)
        writeBytesWithShortLen(publicKey)
        writePacket(tlvBody)
        writeByte(3)
    }.toByteArray()

    private fun makeTlv() = newBuilder().apply {
        writeShort(6)
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
            writeHex("19B29FCE41AE41B87A471BE1EDBC1F3A")
        })
        writeBytes(buildTlv(0x35){
            // 00 35
        // 00 04
        // 00 00 00 68
            writeInt(0x68)
        })
    }.toByteArray()

    fun sendTo(client: BotClient) : PacketSender {
        val seq = 10000
        val to = ToService(seq, WtRequest.CMD_TRANS_EMP, makeBody(seq))
        to.packetType = packetType()
        return to.sendTo(client)
    }

}