package moe.ore.core.protocol.wlogin.request.qr

import moe.ore.core.helper.DataManager
import moe.ore.core.net.BotClient
import moe.ore.core.net.packet.PacketSender
import moe.ore.core.net.packet.PacketType
import moe.ore.core.net.packet.ToService
import moe.ore.core.net.packet.sendTo
import moe.ore.core.protocol.ProtocolInternal
import moe.ore.helper.*

abstract class OldWRequest(
    val uin : Long,
    val cmdName : String,
    val m2Cmd : Int,
    val subCmd : Short,
    val commandId : Int,
    val encryptType : Byte // 07 | 87
) {
    private val manager = DataManager.manager(uin)
    val device = manager.deviceInfo
    val session = manager.session
    val protocol = ProtocolInternal[manager.protocolType]

    open fun packetType() : PacketType = PacketType.WloginGetQRCode

    open fun publicKey() = "04A1E0AA4DBD8FC08B2CA11DC5942AA4FDB29FCA71E66699D900ACA93D13DB86A632BA9357C20DF9913A648E244A591505".hex2ByteArray()
    open fun shareKey() = "C5F805A74E0CFDA5999D6E75500B36A2".hex2ByteArray()

    private val encryptBody =  newBuilder().apply {
        writeByte(1)
        writeByte(1)
        writeBytes(session.randomKey)
        writeShort(0x102)
    }.toByteArray()

    private fun makeBody(seq : Int): ByteArray = newBuilder().apply {
        val dataBody = newBuilder().apply {
            writeTeaEncrypt(shareKey()) {
                val data = makeData(seq)
                writeByte(0)
                writeShort(m2Cmd)
                writeInt(0x10)
                writeInt(114)
                writeByte(0)
                writeByte(0)
                writeByte(0)
                writeInt(currentTimeSeconds())

                writeByte(2)
                writeBlockWithShortLen({ it + 1 + 1 + 2 }) {
                    writeShort(subCmd)
                    // subCmd
                    writeLong(0)
                    writeLong(0)
                    writeInt(0)
                    writeShort(3)
                    writeInt(0x32)
                    writeInt(seq) // seq
                    writeLong(0)
                    //=====================
                    writeBytes(data)
                    //======================
                }
                writeByte(3)

            }
        }

        writeByte(2)
        writeShort(29 + encryptBody.size + publicKey().size + 2 + dataBody.size)
        writeShort(8001)
        writeShort(commandId) // 0x812
        writeShort(1)
        writeLongToBuf32(uin)
        writeByte(3)
        writeByte(encryptType)
        writeByte(0)
        writeInt(2)
        writeInt(0)
        writeInt(0)
        writeBytes(encryptBody)
        writeBytesWithShortLen(publicKey())
        writePacket(dataBody)
        writeByte(3)
    }.toByteArray()

    fun sendTo(client: BotClient) : PacketSender {
        val seq = session.nextSeqId()
        val to = ToService(seq, cmdName, makeBody(seq))
        to.packetType = packetType()
        return to.sendTo(client)
    }

    abstract fun makeData(seq: Int) : ByteArray

    companion object {
        const val CMD_TRANS_EMP = "wtlogin.trans_emp"
    }
}