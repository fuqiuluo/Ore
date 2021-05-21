package moe.ore.helper.toolq.packet

import moe.ore.helper.thread.ThreadManager
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

open class Waiter(val cmd : String,
             val seq : Int,
             val isOnce : Boolean = true
             ) {

    var isUsed = false
    private val queue: BlockingQueue<Boolean> = ArrayBlockingQueue(1)

    lateinit var packet : Packet

    open fun check(packet: Packet) : Boolean {
        return packet.cmd == cmd && packet.seq == seq
    }

    fun push() {
        queue.add(true)
    }

    fun wait(timeout : Int = 3 * 1000) : Boolean {
        try {
            if(isUsed) return true
            ThreadManager.instance.addTask {
                val start = System.currentTimeMillis()
                while (!isUsed) {
                    if (System.currentTimeMillis() >= start + timeout) {
                        isUsed = true
                        queue.add(false)
                        break
                    }
                }
            }
            return queue.take()
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return false
    }
}