package moe.ore.core.bot

import moe.ore.util.BytesUtil
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs

class SsoSession {
    val uinInfo = UinSimpleInfo()

    val msgCookie : ByteArray = BytesUtil.randomKey(4)

    var rollBackCount = 0

    // from t172
    var rollbackSig: ByteArray? = null

    lateinit var randSeed: ByteArray

    var randomKey = BytesUtil.randomKey(16)

    var pwd: ByteArray = try {
        val str = StringBuilder()
        for (b in SecureRandom.getSeed(16)) {
            val abs = abs(b % 26) + if (Random().nextBoolean()) 97 else 65
            str.append(abs.toChar())
        }
        str.toString()
    } catch (unused: Throwable) {
        "1234567890123456"
    }.toByteArray()

    // expamel 1, 0, 0, 127 是倒过来的哦！
    var clientIp = byteArrayOf(0, 0, 0, 0)

    /**
     * T511内的域名的pskey这里都有
     */
    val pSKeyMap = hashMapOf<String, HashMap<String, StringTicket>>()

    /**
     * 图片下载器Appid
     */
    var encryptAppid = 0x5f5e10e2L

    var appPri : Long = 4294967295L
    // 86400 单位：秒 保质期 一天
    var appPriChangeTime : Long = 0

    private val seqFactory = AtomicInteger(Random().nextInt(100000))

    @Synchronized
    fun nextPacketRequestId(): Int {
        var incrementAndGet: Int
        synchronized(this) {
            incrementAndGet = seqFactory.incrementAndGet()
            if (incrementAndGet > 1000000) {
                seqFactory.set(Random().nextInt(100000) + 60000)
            }
        }
        return incrementAndGet
    }
}

// WloginSimpleInfo
class UinSimpleInfo {
    var age: Byte = 0
    var face: Short = 0
    var gender: Byte = 0
    lateinit var imgFormat: ByteArray
    lateinit var imgType: ByteArray
    lateinit var imgUrl: ByteArray
    lateinit var nick: String
    lateinit var mainDisplayName: ByteArray
}