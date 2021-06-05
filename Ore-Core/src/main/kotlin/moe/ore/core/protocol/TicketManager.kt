package moe.ore.core.protocol

import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.HashMap

/**
 *@author 飞翔的企鹅
 *create 2021-06-06 00:01
 */
class TicketManager {
    class Sessions {
        val appData: String = "dd"
    }
    companion object {
        private var idIterator = HashMap<Long, AtomicInteger>()
        private var ssoSessions = HashMap<Long, Sessions>()

        fun getSession(uin: Long): Sessions {
            return ssoSessions.getOrDefault(uin, Sessions())
        }

        fun getIdIteratorNext(uin: Long): Int {
            var incrementAndGet: Int
            synchronized(this) {
                val atomicInteger = idIterator.getOrDefault(uin, AtomicInteger(Random().nextInt(100000)))
                incrementAndGet = atomicInteger.incrementAndGet()
                if (incrementAndGet > 1000000) {
                    atomicInteger.set(Random().nextInt(100000) + 60000)
                }
            }
            return incrementAndGet
        }
    }
}