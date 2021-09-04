package moe.ore.helper.coroutine

/*
协程的库，但是经过讨论，放弃了
因为太大了

import kotlinx.coroutines.*
import java.util.*

class CoroutineManager(val uin: Long) {
    private val jobs = Vector<Job>()

    @JvmOverloads
    fun addTask(task: CoroutineTask, after: CompletionHandler? = null) {
        addTask({ task.invoke() }, after)
    }

    fun addTask(block: suspend CoroutineScope.() -> Unit) {
        addTask(block, null)
    }

    fun addTask(block: suspend CoroutineScope.() -> Unit, after: CompletionHandler?) {
        GlobalScope.launch(start = CoroutineStart.DEFAULT) {
            block.invoke(this)
        }.also { job -> job.invokeOnCompletion {
            jobs.remove(job)
            after?.invoke(it)
        } }.also { jobs.add(it) }.start()
    }

    fun close() {
        jobs.forEach {
            if(it.isActive && !it.isCancelled && !it.isCompleted) it.cancel()
        }
        coroutineManagerMap.remove(uin, this)
    }

    companion object {
        private val coroutineManagerMap = hashMapOf<Long, CoroutineManager>()

        operator fun get(uin: Long) = coroutineManagerMap.getOrPut(uin) { CoroutineManager(uin) }

        interface CoroutineTask {
            suspend fun invoke()
        }
    }
}
*/