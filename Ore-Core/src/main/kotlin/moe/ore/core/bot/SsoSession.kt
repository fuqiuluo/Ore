package moe.ore.core.bot

import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.tars.TarsBase
import moe.ore.util.BytesUtil
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs

class SsoSession : TarsBase() {
    /**
     * 状态默认刷新间隔
     */
    var clientAutoStatusInterval : Long = 600

    /**
     * 电池默认主动上报间隔
     */
    var clientBatteryGetInterval: Long = 86400

    lateinit var t318: ByteArray

    val uinInfo = UinSimpleInfo()

    var msgCookie: ByteArray = BytesUtil.randomKey(4)

    var rollBackCount = 0

    // from t172
    var rollbackSig: ByteArray? = null

    var randSeed: ByteArray = EMPTY_BYTE_ARRAY

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

    // 1 days
//    lateinit var sKey: StringTicket

//    /**
//     * T511内的域名的pskey这里都有
//     */
//    val pSKeyMap = hashMapOf<String, HashMap<String, StringTicket>>()

    /**
     * 图片下载器Appid
     */
    var encryptAppid = 0x5f5e10e2L

    var appPri: Long = 4294967295L

    // 86400 单位：秒 保质期 一天
    var appPriChangeTime: Long = 0

    // from t167
    var imgType = 1


    var t104: ByteArray? = null

    var t174: ByteArray? = null

    private val seqFactory = AtomicInteger(Random().nextInt(100000) + 60000)

    @Synchronized
    fun nextSeqId(): Int {
        var id: Int
        synchronized(this) {
            id = seqFactory.addAndGet(2)
            if (id > 1000000) {
                seqFactory.set(Random().nextInt(100000) + 60000)
            }
        }
        return id
    }

    private val requestIdFactory = AtomicInteger(Random().nextInt(100000) + 60000)

    @Synchronized
    fun nextRequestId(): Int {
        var id: Int
        synchronized(this) {
            id = requestIdFactory.addAndGet(2)
            if (id > 1000000) {
                requestIdFactory.set(Random().nextInt(100000) + 60000)
            }
        }
        return id
    }

    private val msgSeqFactory = AtomicInteger(Random().nextInt(100000) + 60000)

    @Synchronized
    fun nextMsgSeq(): Int {
        var id: Int
        synchronized(this) {
            id = msgSeqFactory.addAndGet(2)
            if (id > 1000000) {
                msgSeqFactory.set(Random().nextInt(100000) + 60000)
            }
        }
        return id
    }
}

class UinSimpleInfo {
    var age: Byte = 0
    var face: Short = 0
    var gender: Byte = 0
    lateinit var nick: String
    lateinit var mainDisplayName: ByteArray
}