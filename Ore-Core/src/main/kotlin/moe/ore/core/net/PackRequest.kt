package moe.ore.core.net

import kotlin.Throws
import java.lang.InterruptedException
import java.util.concurrent.TimeUnit
import java.util.Objects
import kotlin.jvm.Synchronized

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
class PackRequest(private val botClient: BotClient, private val cmdName: String, private val requestId: Long, private val requestBody: ByteArray?) {

    interface OnDataListener {
        fun onReceive(data: ByteArray?)
    }

    private val reentrantLock = MutexLock()
    private var requestStyle = true
    private var source: ByteArray? = null
    private var dataListener: OnDataListener? = null
    private var isCallComplete = false

    @Synchronized
    @Throws(InterruptedException::class)
    fun await(timeout: Long = 1000 * 3): ByteArray? {
        requestStyle = true
        if (requestBody != null) botClient.send(requestBody)
        reentrantLock.lock()
        return if (reentrantLock.tryLock(timeout, TimeUnit.MILLISECONDS)) {
            source
        } else null
    }

    init {
        botClient.registerHandler(this);
    }

    /**
     * 异步请求走Listener回调结果
     *
     * @param dataListener 可空 相当于异步请求不要结果
     */
    @Synchronized
    fun async(dataListener: OnDataListener?) {
        requestStyle = false
        this.dataListener = dataListener
        if (requestBody != null) botClient.send(requestBody)
    }

    /**
     * 消息中心设置data后才能解锁等待或回调数据
     */
    @Synchronized
    fun callData(data: ByteArray?) {
        if (isCallComplete) return
        isCallComplete = true
        botClient.unregisterHandler(this)
        source = data
        if (requestStyle) {
            reentrantLock.unlock()
        }
        if (dataListener != null) dataListener!!.onReceive(data)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as PackRequest
        return requestId == that.requestId && cmdName == that.cmdName
    }

    override fun hashCode(): Int {
        return Objects.hash(cmdName, requestId)
    }
}