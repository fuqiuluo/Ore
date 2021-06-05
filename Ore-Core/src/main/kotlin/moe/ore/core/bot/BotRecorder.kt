package moe.ore.core.bot

import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class BotRecorder {
    private val seqFactory = AtomicInteger(0)

    @Synchronized
    fun nextSeq(): Int {
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