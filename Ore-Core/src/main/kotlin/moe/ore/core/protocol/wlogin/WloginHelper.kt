package moe.ore.core.protocol.wlogin

import kotlinx.io.core.*
import moe.ore.api.LoginResult
import moe.ore.api.listener.CaptchaChannel
import moe.ore.api.listener.OreListener
import moe.ore.core.helper.DataManager
import moe.ore.core.net.BotClient
import moe.ore.core.net.packet.PacketSender
import moe.ore.core.net.packet.PacketSender.Companion.sync
import moe.ore.core.protocol.wlogin.request.WtLoginDevicePass
import moe.ore.core.protocol.wlogin.request.WtLoginPassword
import moe.ore.core.protocol.wlogin.request.WtLoginSlider
import moe.ore.helper.toByteReadPacket
import moe.ore.util.MD5
import moe.ore.util.TeaUtil

class WloginHelper(val uin : Long,
                   private val client: BotClient,
                   listener: OreListener? = null) : Thread() {
    private val eventHandler by lazy { EventHandler(listener, client, this) }
    private val manager = DataManager.manager(uin)
    private val threadManager = manager.threadManager
    private val ecdh = manager.ecdh

    private var wtMode : Int = 0

    override fun run() {

        when(wtMode) {
            MODE_PASSWORD_LOGIN -> handle(WtLoginPassword(uin).sendTo(client))

        }
    }

    fun handle(sender: PacketSender, key: ByteArray = ecdh.shareKey) {
        val from = sender sync 20 * 1000
        if (from == null) {
            eventHandler.callback(LoginResult.ServerTimeout)
        } else {
            from.body.readLoginPacket(key) { result, tlvMap ->
                when(result) {
                    0 -> eventHandler.onSuccess(tlvMap)
                    1 -> eventHandler.onPasswordWrong(tlvMap)
                    2 -> eventHandler.onCaptcha(tlvMap)
                    204 -> eventHandler.onDevicePass(tlvMap)
                    else -> error("unknown login result : $result")
                }
            }
        }
    }

    /**
     * 使用token登录
     */
    fun loginByToken() {

    }

    /**
     * 使用密码登录
     */
    fun loginByPassword() {
        this.wtMode = MODE_PASSWORD_LOGIN
        threadManager.addTask(this)
    }

    /**
     * 刷新Cookie
     */
    fun refreshA1() {

    }

    /**
     * 刷新ClientToken
     */
    fun refreshSt() {

    }

    companion object {
        const val MODE_PASSWORD_LOGIN = 0
    }
}

class EventHandler(
    private val listener: OreListener?,
    private val client: BotClient,
    private val helper: WloginHelper
) {
    private val manager = DataManager.manager(helper.uin)
    private val userStSig = manager.userSigInfo
    private val session = manager.session
    private val device = manager.deviceInfo

    fun callback(result: LoginResult) {
        listener?.runCatching { onLoginFinish(result) }
    }

    fun onSuccess(tlvMap: Map<Int, ByteArray>) {
        callback(LoginResult.Success)
    }

    fun onPasswordWrong(tlvMap: Map<Int, ByteArray>) {
        callback(LoginResult.PasswordWrong)
    }

    fun onCaptcha(tlvMap: Map<Int, ByteArray>) {
        tlvMap[0x104]?.let { userStSig.t104 = it }
        val url = tlvMap[0x192].let {
            requireNotNull(it)
            String(it)
        }
        // println(url)
        listener?.onCaptcha(object : CaptchaChannel(url) {
            override fun submitTicket(ticket: String) {
                helper.handle(WtLoginSlider(helper.uin, ticket, tlvMap[0x546]).sendTo(client))
            }
        })
    }

    fun onDevicePass(tlvMap: Map<Int, ByteArray>) {
        // t402只有产生设备锁的时候产生
        val t402 = tlvMap[0x402]?.also {
            userStSig.G = MD5.toMD5Byte(device.guid + session.pwd + it)
            // 字节组拼接
        }!!
        tlvMap[0x104]?.let { userStSig.t104 = it }
        helper.handle(WtLoginDevicePass(helper.uin, userStSig.G, t402, tlvMap[0x403]).sendTo(client))
    }
}


internal inline fun ByteArray.readLoginPacket(key: ByteArray, block: (Int, Map<Int, ByteArray>) -> Unit) {
    val reader = this.toByteReadPacket()
    reader.discardExact(1 + 2 + 2 + 2 + 2)
    // 02 (dis) xx xx (dis) 1f 41 (dis) 08 01 (dis) 00 01 (dis)
    val uin = reader.readUInt().toLong()
    // println(uin)
    val manager = DataManager.manager(uin)
    reader.discardExact(2)
    // 00 00 (dis)
//            val subCommand = reader.readShort().toInt() // subCommand discardExact 2
//            println(subCommand)
    val result = reader.readByte().toInt() and 0xff
    // 235 协议版本过低
    val teaKey = if (result == 180) manager.session.randomKey else key
    val tlvBody = TeaUtil.decrypt(reader.readBytes(reader.remaining.toInt() - 1), teaKey).toByteReadPacket().also { it.discardExact(3) }
    block.invoke(result, decodeTlv(tlvBody))
}

internal fun decodeTlv(bs: ByteReadPacket): Map<Int, ByteArray> {
    val size = bs.readUShort().toInt()
    val map = hashMapOf<Int, ByteArray>()
    repeat(size) {
        val ver = bs.readUShort().toInt()
        val tSize = bs.readUShort().toInt()
        val content = bs.readBytes(tSize)
        map[ver] = content
    }
    return map
}

