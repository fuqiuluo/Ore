package moe.ore.msg.helper

import java.util.*

/**
 * 同步中心
 */
internal class SyncManager {
    val onlinePushSyncer = OnlinePushSyncer()

    companion object {
        @JvmStatic
        private val syncerMap = hashMapOf<Long, SyncManager>()

        @JvmStatic
        fun getOrPut(uin: Long): SyncManager {
            return syncerMap.getOrPut(uin) { SyncManager() }
        }
    }
}

internal class OnlinePushSyncer : SyncLock<OnlinePushSyncer.SyncOnlinePush>() {
    fun sync(msgType: Int, msgSeq: Short): Boolean {
        return if(!syncList.any {
            it.msgType == msgType && msgSeq == it.msgSeq
        }) { addCache(SyncOnlinePush(msgType, msgSeq))
            true
        } else false
    }

    internal data class SyncOnlinePush(
        val msgType: Int,
        val msgSeq: Short
    )
}

internal abstract class SyncLock<T>(maxCacheSize: Int = 100) {
    protected val syncList = Vector<T>(maxCacheSize)

    protected fun addCache(data: T) {
        syncList.addElement(data)
        if (syncList.size >= 100)
            syncList.removeElementAt(0)
    }
}