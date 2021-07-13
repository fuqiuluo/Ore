package moe.ore.core.protocol.wlogin

import kotlinx.io.core.*
import moe.ore.api.LoginResult
import moe.ore.api.OreStatus
import moe.ore.api.listener.CaptchaChannel
import moe.ore.api.listener.OreListener
import moe.ore.api.listener.SmsHelper
import moe.ore.core.bot.BytesTicket
import moe.ore.core.bot.LoginExtraData
import moe.ore.core.bot.StringTicket
import moe.ore.core.helper.DataManager
import moe.ore.core.net.BotClient
import moe.ore.core.net.packet.PacketSender
import moe.ore.core.net.packet.PacketSender.Companion.sync
import moe.ore.core.protocol.SvcRegisterHelper
import moe.ore.util.MD5
import moe.ore.util.TeaUtil
import okhttp3.internal.toHexString
import moe.ore.api.data.Result
import moe.ore.core.OreManager
import moe.ore.core.protocol.wlogin.request.*
import moe.ore.helper.*

class WloginHelper(val uin : Long,
                   private val client: BotClient,
                   val listener: OreListener? = null) : Thread() {
    private val eventHandler by lazy { EventHandler(listener, client, this) }
    private val manager = DataManager.manager(uin)
    private val threadManager = manager.threadManager
    private val ecdh = manager.ecdh

    private var wtMode : Int = 0

    override fun run() {
        when(wtMode) {
            MODE_PASSWORD_LOGIN -> handle(WtLoginPassword(uin).sendTo(client), ecdh.shareKey)
            MODE_EXCHANGE_EMP_SIG -> handle(WtLoginGetSig(uin).sendTo(client), manager.userSigInfo.wtSessionTicketKey.ticket())
            MODE_EXCHANGE_EMP_ST -> handle(WtLoginGetSt(uin).sendTo(client), ecdh.shareKey)
            MODE_TOKEN_LOGIN -> {
                OreManager.checkTicketAndRefresh(uin)
                val ret = SvcRegisterHelper(uin).register()
                if(ret == 0) {
                    OreManager.changeStatus(uin, OreStatus.Online)
                } else {
                    OreManager.changeStatus(uin, OreStatus.ReconnectFail)
                }
            }
        }
    }

    fun handle(sender: PacketSender, key: ByteArray = ecdh.shareKey) {
        val from = sender sync 20 * 1000
        if (from == null) {
            eventHandler.callback(LoginResult.ServerTimeout)
        } else {
            from.body.readLoginPacket(key) { result, tlvMap ->
                println("tlvMap: " + tlvMap.keys.map { "0x" + it.toHexString() })
                when(result) {
                    0 -> eventHandler.onSuccess(tlvMap[0x119])
                    1 -> eventHandler.onPasswordWrong()
                    2 -> eventHandler.onCaptcha(tlvMap)
                    6 -> eventHandler.onCaptchaError()
                    15 -> eventHandler.onTokenLoginError()
                    40 -> eventHandler.onFreeze()
                    180 -> eventHandler.onRollback(tlvMap[0x161])
                    204 -> eventHandler.onDevicePass(tlvMap)
                    237 -> eventHandler.onNetEnvWrong()
                    239 -> eventHandler.onDevicelock(tlvMap)
                    else -> {
                        tlvMap[0x146]?.let {
                            println(String(it))
                        }

                        tlvMap[0x508]?.let {
                            println(String(it))
                        }
                        error("unknown login result : $result")
                    }
                }
            }
        }
    }

    /**
     * 使用token登录
     */
    fun loginByToken() {
        this.wtMode = MODE_TOKEN_LOGIN
        threadManager.addTask(this)
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
    fun refreshSig() {
        this.wtMode = MODE_EXCHANGE_EMP_SIG
        threadManager.addTask(this)
    }

    /**
     * 刷新ClientToken
     */
    fun refreshSt() {
        this.wtMode = MODE_EXCHANGE_EMP_ST
        threadManager.addTask(this)
    }

    class EventHandler(
        private val listener: OreListener?,
        private val client: BotClient,
        private val helper: WloginHelper
    ) {
        private val manager = DataManager.manager(helper.uin)
        private val userStInfo = manager.userSigInfo
        private val session = manager.session
        private val device = manager.deviceInfo

        fun callback(result: LoginResult) {
            listener?.runCatching { listener.onLoginFinish(result) }
        }

        fun onSuccess(t119 : ByteArray?) {
            t119?.let { source ->
                val map = decodeTlv(TeaUtil.decrypt(source, when (helper.wtMode) {
                    MODE_PASSWORD_LOGIN -> device.tgtgt
                    MODE_EXCHANGE_EMP_SIG -> userStInfo.gtKey.ticket()
                    MODE_EXCHANGE_EMP_ST -> MD5.toMD5Byte(userStInfo.d2Key.ticket())
                    else -> error("unknown wtlogin mode")
                }).toByteReadPacket())

                println("T119 tlvMap: " + map.keys.map { "0x" + it.toHexString() })

                val now = System.currentTimeMillis()
                val shelfLife = 86400L // 默认保质期一天
                val bsTicket: (ByteArray) -> BytesTicket = { key ->
                    // println(String(key))
                    BytesTicket(key, now, shelfLife)
                }
                val strTicket: (String) -> StringTicket = { key ->
                    // println(key)
                    StringTicket(key.toByteArray(), now, shelfLife)
                }

                map[0x103]?.let { userStInfo.webSig = bsTicket(it) }

                map[0x203]?.let { userStInfo.da2 = bsTicket(it) }

                map[0x143]?.let {
                    userStInfo.d2 = bsTicket(it)
                }
                map[0x305]?.let {
                    println("载入d2key")
                    userStInfo.d2Key = bsTicket(it)
                }

                map[0x120]?.let {
                    // println("input skey")
                    session.sKey = strTicket(String(it))
                }

                map[0x106]?.let {
                    userStInfo.encryptA1 = bsTicket(it)
                }

                map[0x10c]?.let {
                    userStInfo.gtKey = bsTicket(it)
                }

                map[0x10a]?.let { userStInfo.tgt = bsTicket(it) }
                map[0x10d]?.let { userStInfo.tgtKey = bsTicket(it) }

                map[0x10e]?.let { userStInfo.stKey = bsTicket(it) }

                map[0x512]?.reader {
                    val size = readUShort().toInt()
                    repeat(size) {
                        val domain = readString(readUShort().toInt())
                        val pskey = readString(readUShort().toInt())
                        val p4token = readString(readUShort().toInt())
                        session.pSKeyMap[domain] = hashMapOf(
                            "pskey" to strTicket(pskey),
                            "p4token" to strTicket(p4token)
                        )
                        // println(domain + " ==> " + session.pSKeyMap[domain])
                    }
                }

                map[0x114]?.let {
                    userStInfo.st = bsTicket(it)
                }

                map[0x118]?.let {
                    session.uinInfo.mainDisplayName = it
                    // 用都没见腾讯用过
                }

                map[0x199]?.let {
                    userStInfo.payToken = it
                }

                map[0x11a]?.reader {
                    val simple = session.uinInfo
                    simple.face = readShort()
                    simple.age = readByte()
                    simple.gender = readByte()
                    simple.nick = readString(readUByte().toInt())
                }

                map[0x11d]?.reader {
                    session.encryptAppid = readUInt().toLong()
                    userStInfo.downloadStKey = bsTicket(readBytes(16))
                    userStInfo.downloadSt = bsTicket(readBytes(readUShort().toInt()))
                }

                map[0x11f]?.reader {
                    session.appPriChangeTime = readUInt().toLong()
                    session.appPri = readUInt().toLong()
                }

                map[0x322]?.let {
                    userStInfo.deviceToken = bsTicket(it)
                }

                /*
                map[0x522]?.let {
                    // 不知道是什么东西
                }
                map[0x163]?.let {
                    // 不知道是什么东西
                }
                map[0x550]?.let {
                    // println(it.toHexString())
                    // QQ内测的Token
                }
                */

                map[0x108]?.let {
                    device.ksid = it
                }

                map[0x528]?.let {
                    userStInfo.t528 = it
                }

                map[0x16d]?.let {
                    userStInfo.superKey = strTicket(String(it))
                }

                map[0x16a]?.let {
                    userStInfo.noPicSig = bsTicket(it)
                }

                map[0x130]?.reader {
                    val version = readShort()
                    val time = readUInt().toLong()
                    // val ipAddr = readUInt().toLong()
                    session.clientIp = readBytes(4)
                }

                map[0x133]?.let {
                    userStInfo.wtSessionTicket = bsTicket(it)
                }

                map[0x134]?.let {
                    userStInfo.wtSessionTicketKey = bsTicket(it)
                    // println("ticketKey;" + DataManager.manager(uin).wLoginSigInfo.wtSessionTicketKey.ticket().toHexString())

                }

                map[0x537]?.reader {
                    val version = readByte()
                    repeat(readUByte().toInt()) {
                        userStInfo.extraDataList.add(
                            LoginExtraData(
                                uin = readLong(),
                                ip = readBytes(4),
                                time = readInt(),
                                appId = readInt()
                            )
                        )
                    }
                }

                // key的刷新时间
                map[0x138]?.reader {
                    val count = readInt()
                    // val st = System.currentTimeMillis()
                    repeat(count) {
                        val ver = readShort().toInt()
                        val time = readUInt().toInt() * 1000L

                        // val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        // val timeStr = sdf.format(Date(time + st))
                        // println("【T${ver.toHexString()}】过期时间：" + timeStr)

                        when (ver) {
                            0x106 -> userStInfo.encryptA1.shelfLife = time // 3 days
                            0x10a -> {
                                userStInfo.tgt.shelfLife = time
                                userStInfo.tgtKey.shelfLife = time
                            }
                            0x11c -> {

                            }
                            0x102 -> {

                            }
                            0x103 -> {
                                userStInfo.webSig.shelfLife = time
                            }
                            0x120 -> {
                                session.sKey.shelfLife = time
                            }
                            0x136 -> {

                            }
                            0x143 -> {
                                userStInfo.d2.shelfLife = time
                                userStInfo.d2Key.shelfLife = time
                            }
                            0x164 -> {

                            }

                            else -> error("unknown tlv : $ver")
                        }
                        discardExact(4)
                    }
                }

//                // 执行flush保存数据
//                manager.flush()

                val ret = SvcRegisterHelper(uin = helper.uin).register()

                if(ret == 0) {
                    // 清空回滚
                    session.rollBackCount = 0
                    OreManager.changeStatus(helper.uin, OreStatus.Online)
                    callback(LoginResult.Success)
                } else callback(LoginResult.RegisterFail)


            }
        }

        fun onPasswordWrong() {
            callback(LoginResult.PasswordWrong)
        }

        fun onCaptcha(tlvMap: Map<Int, ByteArray>) {
            tlvMap[0x104]?.let { session.t104 = it }
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
                userStInfo.G = MD5.toMD5Byte(device.guid + session.pwd + it)
                // 字节组拼接
            }!!
            tlvMap[0x104]?.let { session.t104 = it }
            tlvMap[0x403]?.let { session.randSeed = it }
            helper.handle(WtLoginDevicePass(helper.uin, t402, session.randSeed).sendTo(client))
        }

        fun onRollback(t161 : ByteArray?) {
            t161?.reader {
                val tlv = decodeTlv(this)

                tlv[0x173]?.let {
                    println("t173 body : " + it.toHexString())
                }

                tlv[0x17f]?.let {
                    println("t17f body : " + it.toHexString())
                }

                tlv[0x172]?.let {
                    session.rollBackCount++
                    session.rollbackSig = it
                    client.connect()
                    /**
                     * 回滚重新连接服务器
                     */
                    // println("t173 body : " + it.toHexString())
                }

            }
        }

        fun onDevicelock(tlvMap: Map<Int, ByteArray>) {
            tlvMap[0x104]?.let { session.t104 = it }
            tlvMap[0x174]?.let { session.t174 = it }
            val smsInformation = tlvMap[0x178]!!.let {
                val reader = it.toByteReadPacket()
                SmsInformation(
                    countryCode = reader.readBytes(reader.readUShort().toInt()),
                    phoneNum = reader.readString(reader.readUShort().toInt()),
                    smsStatus = reader.readInt(),
                    availableMsgCnt = reader.readShort(),
                    timeLimited = reader.readShort()
                )
            } // 手机号什么的
            val noticeStr = String(tlvMap[0x17e]!!) // 提示信息
            val otherWay = String(tlvMap[0x204]!!) // 其它验证方式的链接
            // val verifyUrl = String(tlvMap[0x179]!!)
            tlvMap[0x17d]?.let {
                val reader = it.toByteReadPacket()
                val mbGuideType = reader.readShort()
                val mbGuideMsg = reader.readString(reader.readUShort().toInt())
                val mbGuideInfoType = reader.readShort()
                val mbGuideInfoMsg = reader.readString(reader.readUShort().toInt())
                // 换绑操作
            }
            listener?.onSms(object : SmsHelper() {
                private var lastGetTime = 0L
                private var timeLimited = 60 * 1000

                override fun noticeStr(): String = noticeStr

                override fun phoneNum(): String = smsInformation.phoneNum

                override fun otherWayUrl(): String = otherWay

                override fun sendSms(): Result<Boolean> {
                    // 设置一分钟只能获取一次验证码
                    if(lastGetTime + timeLimited <= System.currentTimeMillis()) {
                        lastGetTime = System.currentTimeMillis()
                        val sender = WtLoginGetSmsCode(helper.uin).sendTo(client)
                        (sender sync 20 * 1000)?.body?.readLoginPacket(helper.ecdh.shareKey) { result, tlvMap ->
                            println("SmsTlvMap: " + tlvMap.keys.map { "0x" + it.toHexString() })

                            tlvMap[0x146]?.let {
                                println(String(it))
                            }

                            tlvMap[0x508]?.let {
                                println(String(it))
                            }

                            return when(result) {
                                161 -> Result(161, "今日操作次数过多，请等待一天后再试。", false)
                                162 -> Result(162, "频繁发送验证码，请稍后再试。", false)
                                160 -> {
                                    tlvMap[0x17b]?.reader {
                                        val availableMsgCnt = readShort()
                                        timeLimited = readShort() * 1000
                                        println("剩余短信次数：%s，间隔时间：%s ms".format(availableMsgCnt, timeLimited))
                                    }
                                    Result.success("验证码发送成功。", true)
                                }
                                else -> error("unknown sms status : $result")
                            }
                        }
                    } else {
                        return Result.serverError("短信重新发送时间未到。", false)
                    }
                    return Result.unknownError("未知错误。", false)
                }

                override fun submitSms(code: String) {
                    helper.handle(WtLoginSubmitSms(helper.uin, code).sendTo(client))
                }

            })
        }

        fun onNetEnvWrong() {
            callback(LoginResult.NetEnvWrong)
        }

        fun onFreeze() {
            callback(LoginResult.AccountFreeze)
        }

        fun onCaptchaError() {
            /**
             * 滑块ticket错误导致的
             */
            callback(LoginResult.NetEnvWrong)
        }

        fun onTokenLoginError() {
            callback(LoginResult.TokenLoginError)
        }
    }

    companion object {
        const val MODE_PASSWORD_LOGIN = 0
        const val MODE_EXCHANGE_EMP_SIG = 1
        const val MODE_EXCHANGE_EMP_ST = 2
        const val MODE_TOKEN_LOGIN = 3
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

        // println("tlv[${ver.toHexString()}]: " + content.toHexString())
    }
    return map
}

data class SmsInformation(
    val countryCode : ByteArray,
    val phoneNum : String,
    val smsStatus : Int,
    val availableMsgCnt : Short,
    val timeLimited : Short
)