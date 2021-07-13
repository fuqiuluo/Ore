package moe.ore.core.bot

import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsOutputStream
import moe.ore.tars.TarsStructBase
import moe.ore.util.BytesUtil
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs

class SsoSession : TarsStructBase() {
    val uinInfo = UinSimpleInfo()

    var msgCookie: ByteArray = BytesUtil.randomKey(4)

    var rollBackCount = 0

    // from t172
    var rollbackSig: ByteArray? = null

    var randSeed: ByteArray = ByteArray(0)

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

    // 1 days
    lateinit var sKey: StringTicket

    /**
     * T511内的域名的pskey这里都有
     */
    val pSKeyMap = hashMapOf<String, HashMap<String, StringTicket>>()

    /**
     * 图片下载器Appid
     */
    var encryptAppid = 0x5f5e10e2L

    var appPri: Long = 4294967295L

    // 86400 单位：秒 保质期 一天
    var appPriChangeTime: Long = 0

    // from t167
    var imgType = 1

    private val seqFactory = AtomicInteger(Random().nextInt(100000))

    var t104: ByteArray? = null

    var t174: ByteArray? = null

    @Synchronized
    fun nextSeqId(): Int {
        var id: Int
        synchronized(this) {
            id = seqFactory.incrementAndGet()
            if (id > 1000000) {
                seqFactory.set(Random().nextInt(100000) + 60000)
            }
        }
        return id
    }

    private val requestIdFactory = AtomicInteger(Random().nextInt(100000))

    @Synchronized
    fun nextRequestId(): Int {
        var id: Int
        synchronized(this) {
            id = requestIdFactory.incrementAndGet()
            if (id > 1000000) {
                requestIdFactory.set(Random().nextInt(100000) + 60000)
            }
        }
        return id
    }

    override fun writeTo(output: TarsOutputStream) {
        output.write(randSeed, 1)
        output.write(randomKey, 2)
        output.write(pwd, 3)
        output.write(msgCookie, 4)

    }

    override fun readFrom(input: TarsInputStream) {
        randSeed = input.read(randSeed, 1, false)
        randomKey = input.read(randomKey, 2, false)
        pwd = input.read(pwd, 3, false)
        msgCookie = input.read(msgCookie, 4, false)

    }
}

class UinSimpleInfo {
    var age: Byte = 0
    var face: Short = 0
    var gender: Byte = 0
    lateinit var nick: String
    lateinit var mainDisplayName: ByteArray
}