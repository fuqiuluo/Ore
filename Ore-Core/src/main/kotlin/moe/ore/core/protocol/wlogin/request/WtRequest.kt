package moe.ore.core.protocol.wlogin.request

import moe.ore.core.helper.DataManager
import moe.ore.core.net.BotClient
import moe.ore.core.net.packet.PacketSender
import moe.ore.core.net.packet.PacketType
import moe.ore.core.net.packet.ToService
import moe.ore.core.net.packet.sendTo
import moe.ore.core.protocol.ECDH_VERSION
import moe.ore.core.protocol.Tlv
import moe.ore.helper.*

abstract class WtRequest(
    val uin: Long,
    private val commandName: String,
    private val commandId: Int,
    private val subCmd : Int,
    private val ecdhType: Int
) {
    val manager = DataManager.manager(uin)
    private val account = manager.botAccount
    private val device = manager.deviceInfo
    private val ecdh = manager.ecdh
    // private val protocolInfo = ProtocolInternal[manager.protocolType]

    val session = manager.session
    val userStSig = manager.userSigInfo

    val tlv = Tlv(
        account = account,
        deviceInfo = device,
        session = session,
        protocolType = manager.protocolType
    )

    abstract fun makeTlv(seq : Int) : ByteArray

    open fun ecdhEncryptBody() : ByteArray = newBuilder().apply {
        writeByte(2)
        writeByte(1)
        writeBytes(session.randomKey)
        writeShort(0x131)
        writeShort(ECDH_VERSION.toShort())
    }.toByteArray()

    open var firstToken : ByteArray? = null
    open var secondToken : ByteArray? = null

    open fun packetType() : PacketType = PacketType.LoginPacket

    open fun publicKey() : ByteArray = ecdh.publicKey

    open fun teaKey() : ByteArray = ecdh.shareKey

    fun sendTo(client: BotClient) : PacketSender {
        val seq = session.nextSeqId()
        val to = ToService(seq, commandName, makeBody(seq))
        to.packetType = packetType()
        to.firstToken = firstToken
        to.secondToken = secondToken

        // println("key : " + teaKey().toHexString())

        return to.sendTo(client)
    }

    private fun makeBody(seq: Int) : ByteArray = newBuilder().apply {
        val encryptBody = ecdhEncryptBody()
        val publicKey = publicKey()
        val tlvBody = newBuilder().apply {
            writeTeaEncrypt(teaKey()) {
                writeShort(subCmd)
                val tlv = makeTlv(seq)
                // println("TLV ： " + tlv.toHexString())
                writeBytes(tlv)
            }
        }

        writeByte(2)
        writeShort(27 + encryptBody.size + 2 + publicKey.size + 2 + tlvBody.size)
        writeShort(8001)
        writeShort(commandId)
        writeShort(1)
        writeLongToBuf32(account.uin)

        writeByte(3)
        writeByte(ecdhType.toByte())
        writeByte(0)
        writeInt(2) // 天王老子来了这个2也是int 自己逆向qq去看，傻卵
        writeInt(0)
        writeInt(0)

        writeBytes(encryptBody)

        writeBytesWithShortLen(publicKey)

        writePacket(tlvBody)

        writeByte(3)
    }.toByteArray()

    companion object {
        const val CMD_LOGIN = "wtlogin.login"
        const val CMD_EXCHANGE_EMP = "wtlogin.exchange_emp"
    }
}