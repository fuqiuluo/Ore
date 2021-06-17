package moe.ore.core.bot

import moe.ore.core.helper.toByteArray
import moe.ore.helper.hex2ByteArray
import moe.ore.util.BytesUtil
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs

class SsoSession {
    //    var packetSessionId: ByteArray = 0x02B05B8B.toByteArray()
//    var ksid: ByteArray = "14751d8e7d633d9b06a392c357c675e5".hex2ByteArray()
    var randSeed: ByteArray = TODO("tlvMap[0x403]")
    var randomKey = BytesUtil.randomKey(16)

    // expamel 1, 0, 0, 127 是倒过来的哦！
    var clientIp = byteArrayOf(0, 0, 0, 0)
    val mpasswd: ByteArray = try {
        val str = StringBuilder()
        for (b in SecureRandom.getSeed(16)) {
            val abs = abs(b % 26) + if (Random().nextBoolean()) 97 else 65
            str.append(abs.toChar())
        }
        str.toString()
    } catch (unused: Throwable) {
        "1234567890123456"
    }.toByteArray()

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