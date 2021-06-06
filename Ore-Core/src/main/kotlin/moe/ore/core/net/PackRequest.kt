package moe.ore.core.net

import moe.ore.util.bytes.toHexString
import java.util.concurrent.TimeUnit
import java.util.Objects
import java.util.concurrent.ConcurrentHashMap

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
class PackRequest(private val botClient: BotClient, private val cmdName: String, private val requestId: Long, private val requestBody: ByteArray) {

    interface OnDataListener {
        fun onReceive(data: ByteArray?)
    }

    companion object {
        /**
         * 回调分发 获取请求Handler然后调用callData把数据返回
         * */
        @JvmStatic
        fun call(uin: Long, cmdName: String, requestId: Long, source: ByteArray) {
            CacheHandler.packHandlerMap["$uin,$$cmdName,$requestId"]?.callData(source)
        }
    }

    // 内部维护一个Static变量 相关的东西还是放在相关的类里面比较方便后期维护
    //改成字符串拼接作为mapkey发布调试时知道有哪些Handler在map里面
    class CacheHandler {
        companion object {
            val packHandlerMap: ConcurrentHashMap<String, PackRequest> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
                ConcurrentHashMap()
            }
        }
    }

    private var uin = botClient.getUin()
    private val reentrantLock = MutexLock()
    private var requestStyle = true
    private var source: ByteArray? = null
    private var dataListener: OnDataListener? = null
    private var isCallComplete = false
    private var startTimestamp = 0L
    private var maxLifeTime = 1000 * 10L

    fun await(timeout: Long = 1000 * 3): ByteArray? {
        checkOneRunLimit()
        setHandlerLifeTime(timeout)
        requestStyle = true
        if (!botClient.send(requestBody)) {
            return null
        }
        reentrantLock.lock()
        return if (reentrantLock.tryLock(timeout, TimeUnit.MILLISECONDS)) {
            source
        } else {
            unregisterHandler()
            return null
        }
    }

    init {
        registerHandler()
    }

    /**
     * 异步请求走Listener回调结果
     *
     * @param dataListener 可空 相当于异步请求不要结果
     */
    fun async(dataListener: OnDataListener?) {
        checkOneRunLimit()
        setHandlerLifeTime()
        requestStyle = false
        this.dataListener = dataListener
        if (!botClient.send(requestBody)) {
            this.dataListener?.onReceive(null)
        }
    }

    /**
     * 这里主要进行回调数据和移除Handler
     * 如果是同步请求也将在这里接锁线程的阻塞
     *Synchronized保护 防止以为抽风可能导致瞬间回调多次可能 反正只要一次结果
     */

    @Synchronized
    private fun callData(data: ByteArray?) {
        if (isCallComplete) {
            println("重复的回调执行 请检查原因 callData：" + data?.toHexString())
            return
        }
        isCallComplete = true
        unregisterHandler()
        source = data
        if (requestStyle) {
            reentrantLock.unlock()
        }
        dataListener?.onReceive(data)
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

    private fun setHandlerLifeTime(timeout: Long = maxLifeTime) {
        maxLifeTime = timeout * 2
        startTimestamp = System.currentTimeMillis()
    }

    /**
     * 超时移除Handler
     * */
    private fun checkHandlerLifeTime() {
        if (System.currentTimeMillis() > (startTimestamp + maxLifeTime)) {
            unregisterHandler()
        }
    }

    private fun registerHandler() {
        CacheHandler.packHandlerMap["$uin,$cmdName,$requestId"] = this
    }

    private fun unregisterHandler() {
        if (CacheHandler.packHandlerMap.containsKey("$uin,$cmdName,$requestId")) {
            if (!CacheHandler.packHandlerMap.remove("$uin,$cmdName,$requestId", this)) {
//                throw RuntimeException("找不到要移除的Handler")
                println("找不到要移除的Handler")
            }
        }
    }

    /**
     * 当程序发生错误(没收到服务器异步返回，同步超时已做释放)时你可能需要手动释放一下
     * 释放是指从map中移除自身 在此之后你任然可以操作这个类
     * */
    fun release() {
        unregisterHandler()
    }

    private fun checkOneRunLimit() {
        if (isCallComplete) {
            throw RuntimeException("一个Handler只能使用一次")
        }
    }
}