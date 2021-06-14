package moe.ore.helper.thread

import java.util.concurrent.BlockingQueue
import kotlin.random.Random

/**
 * @author luoluo
 * @date 2020/10/2 0:48
 */
abstract class ResultThread<T>(name: String = "ResultThread[${Random.nextInt()}]") : Thread(name), IResultThread<T> {
    init {
        isDaemon = true
    }

    private var queue: BlockingQueue<T>? = null

    override fun run() {
        try {
            val t = on()
            queue?.add(t)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun setQueue(queue: BlockingQueue<T>) {
        this.queue = queue
    }

    @Throws(Throwable::class)
    abstract override fun on(): T
}