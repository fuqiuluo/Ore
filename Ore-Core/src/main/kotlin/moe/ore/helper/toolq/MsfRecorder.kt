package moe.ore.helper.toolq

import moe.ore.helper.bytes.hex2ByteArray
import java.util.concurrent.atomic.AtomicInteger
import kotlin.properties.Delegates
import kotlin.random.Random


class MsfRecorder {
    /**
     * 回滚重新登录次数
     */
    var rollbackTime = 0

    val publicKey = "04803E9940F3FD8E6474C3CC6994A2F972B1DA6BFDE8DDB4E775E36AB4E439DB8EA7A0E6CAFC722089F4921DFEAEFBA0F56932F3E6AA3ECF81154FD230AF32B18F".hex2ByteArray()

    val shareKey = "3F539B2549AB1F71421F2C3A66298D05".hex2ByteArray()

    // 资源服务器SEQ
    private val highWaySeq = AtomicInteger(Random.nextInt(100000))

    // 信息请求SEQ
    private val largeSeq = AtomicInteger(0)

    // 普通推送SEQ
    private val msfSsoSeq = AtomicInteger(0)

    // JCE请求ID
    private val requestId = AtomicInteger(1017089978)

    // 信息推送ID
    private val messageSeqId = AtomicInteger(22911)

    private val keyMap = hashMapOf<KeyType, ByteArray>()

    val domainPsKey = hashMapOf<String, String>()

    /**
     * 机器人的一些信息
     */
    var face by Delegates.notNull<Int>()
    var age by Delegates.notNull<Int>()
    var gender by Delegates.notNull<Int>()
    lateinit var nick : String

    fun setKey(keyType: KeyType, byteArray: ByteArray) {
        keyMap[keyType] = byteArray
    }

    fun getKey(keyType: KeyType) = keyMap[keyType]

    fun nextHwSeq(): Int {
        val seq = highWaySeq.incrementAndGet()
        if (seq > 1000000) {
            highWaySeq.set(Random.nextInt(1060000))
        }
        return seq
    }

    fun nextLargeSeq(): Int {
        val seq = largeSeq.addAndGet(4)
        if (seq > 1000000) {
            largeSeq.set(0)
        }
        return seq
    }

    fun nextSsoSeq(): Int {
        val seq = msfSsoSeq.addAndGet(2)
        if (seq > 1000000) {
            msfSsoSeq.set(1000)
        }
        return seq
    }

    fun nextRequestId(): Int {
        val id = requestId.addAndGet(2)
        if (id > Int.MAX_VALUE - 100) {
            requestId.set(1017089978)
        }
        return id
    }

    fun nextMessageSeq(): Int {
        val seq = messageSeqId.addAndGet(4)
        if (seq > 1000000) {
            messageSeqId.set(Random.nextInt(22911))
        }
        return seq
    }

}