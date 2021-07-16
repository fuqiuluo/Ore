package moe.ore.core.protocol.wlogin.request.qr

import kotlinx.io.core.*
import moe.ore.api.LoginResult
import moe.ore.api.OreStatus
import moe.ore.api.listener.CaptchaChannel
import moe.ore.api.listener.OreListener
import moe.ore.api.listener.SmsHelper
import moe.ore.core.OreBot
import moe.ore.core.OreManager
import moe.ore.core.bot.BytesTicket
import moe.ore.core.helper.DataManager
import moe.ore.core.net.BotClient
import moe.ore.core.net.listener.ClientListener
import moe.ore.core.net.packet.PacketSender
import moe.ore.core.net.packet.PacketSender.Companion.sync
import moe.ore.core.protocol.ProtocolInternal
import moe.ore.core.protocol.wlogin.WloginHelper
import moe.ore.helper.currentTimeSeconds
import moe.ore.helper.hex2ByteArray
import moe.ore.helper.thread.sleepQuietly
import moe.ore.helper.toByteReadPacket
import moe.ore.helper.toHexString
import moe.ore.util.TeaUtil
import okhttp3.internal.toHexString
import java.util.*

class QRLoginHelper(
    dataPath : String,
    val oreListener: OreListener
) : Thread() {
    val manager = DataManager.init(0, dataPath).apply {
        protocolType = ProtocolInternal.ProtocolType.ANDROID_WATCH
    }
    lateinit var shareKey : ByteArray
    private val client = BotClient(0)

    private var verifySig : ByteArray? = null

    /**
     * 二维码创建时间
     */
    var createTime = 0
    /**
     * 二维码保质期
     */
    var shelfLife : Int = 0

    var listener : QRLoginListener? = null

    /**
     * 开始登录
     */
    fun login() {
        client.connect()
        client.listener = object : ClientListener {
            override fun onConnect() {
                start()
            }

            override fun onFailConnect() {
                listener?.onFailConnect()
            }
        }
    }

    override fun run() {
        handle(getQRCode())
    }

    private fun handle(sender: PacketSender) {
        val from = sender sync 30 * 1000
        if(from == null) {
            listener?.onGetQRFailure()
        } else {
            from.body.readWloginPacket(shareKey) { result, subCmd, data ->
                val dataReader = data.toByteReadPacket()
                when(subCmd) {
                    0x12 -> {
                        dataReader.readInt() // 00 00 00 10
                        when (val status = dataReader.readByte().toInt()) {
                            0 -> {
                                val uin = dataReader.readLong()
                                val expireTime = dataReader.readInt()
                                val tlvMap = decodeTlv(dataReader.readBytes(dataReader.remaining.toInt() - 1).toByteReadPacket())
                                // 0x18 ==> t106
                                // 0x65 ==> t318
                                // 0x1e ==> gtKey
                                // 0x19 ==> t16a (noPicSig)

                                val createTime = System.currentTimeMillis()

                                val userSigInfo = manager.userSigInfo

                                userSigInfo.encryptA1 = BytesTicket(tlvMap[0x18]!!, createTime)
                                userSigInfo.gtKey = BytesTicket(tlvMap[0x1e]!!, createTime)
                                userSigInfo.noPicSig = BytesTicket(tlvMap[0x19]!!, createTime)
                                manager.session.t318 = tlvMap[0x65]!!

                                val ore = OreManager.addBot(uin, "", manager.dataPath) as OreBot
                                ore.oreListener = oreListener
                                DataManager.copyTo(0, uin)
                                ore.qrLogin()
                            }
                            17 -> listener?.onQRExpired()
                            48 -> {
                                sleepQuietly(500)
                                handle(checkQRCode())
                            }
                            53 -> {
                                // 有设备扫描了二维码 请等待设备按确认登录
                                listener?.onScanCode()
                                sleepQuietly(500)
                                handle(checkQRCode())
                            }
                            else -> error("unknown qr type : $status")
                        }
                    }
                    0x31 -> { if(result == 0) {
                        this.verifySig  = dataReader.readBytes(dataReader.readShort().toInt())
                        val tlvMap = decodeTlv(dataReader.readBytes(dataReader.remaining.toInt() - 1).toByteReadPacket())
                        // println("tlvMap: " + tlvMap.keys.map { "0x" + it.toHexString() })
                        val imgBytes = tlvMap[0x17]!!
                        this.createTime = currentTimeSeconds()
                        // 二维码过期时间
                        tlvMap[0x1c]?.let {
                            val reader = it.toByteReadPacket()
                            this.shelfLife = reader.readInt()
                            // val imgType = reader.readByte()
                            // 无法处理
                        }
                        listener?.onGetQRSuccess(imgBytes)
                        handle(checkQRCode())
                    } else listener?.onGetQRFailure() }
                    else -> error("unknown qrlogin type : $subCmd")
                }
            }
        }
    }


    private fun checkQRCode() = WtLoginCheckQR(verifySig!!).sendTo(client)

    private fun getQRCode() = WtLoginGetQR().apply {
        this@QRLoginHelper.shareKey = this.shareKey()
    }.sendTo(client)
}

interface QRLoginListener {
    /**
     * 二维码过期
     */
    fun onQRExpired()

    /**
     * 成功获取到二维码
     */
    fun onGetQRSuccess(imgBuf : ByteArray)

    /**
     * 获取二维码失败
     */
    fun onGetQRFailure()

    /**
     * 连接服务器失败
     */
    fun onFailConnect()

    /**
     * 有设备扫描了二维码，但没有确认登录
     */
    fun onScanCode()
}

internal inline fun ByteArray.readWloginPacket(key: ByteArray, block: (result: Int, subCmd : Int, ByteArray) -> Unit) {
    val reader = this.toByteReadPacket()
    reader.discardExact(1 + 2 + 2 + 2 + 2)
    // 02 (dis) xx xx (dis) 1f 41 (dis) 08 12 (dis) 00 01 (dis)
    reader.discardExact(4 + 2) // uin is 0 and dis 00 00
    val result = reader.readByte().toInt() and 0xff
    val dataReader = TeaUtil.decrypt(reader.readBytes(reader.remaining.toInt() - 1), key).toByteReadPacket()
    dataReader.discardExact(4) // 跳过 4 字节 || 这个是全长 不包括自己
    dataReader.discardExact(1) // 这个是result
    dataReader.discardExact(1 + 2) // 02 xx xx(short length)
    val cmd = dataReader.readShort().toInt()
    dataReader.discardExact(45)
    block(result, cmd, dataReader.readBytes(dataReader.remaining.toInt() - 1))
    //
    // block.invoke(result, cmd, verifySig, )
}

internal fun decodeTlv(bs: ByteReadPacket): Map<Int, ByteArray> {
    val size = bs.readUShort().toInt()
    val map = hashMapOf<Int, ByteArray>()
    runCatching {
        repeat(size) {
            val ver = bs.readUShort().toInt()
            val tSize = bs.readUShort().toInt()
            val content = bs.readBytes(tSize)
            map[ver] = content
        }
    }
    return map
}