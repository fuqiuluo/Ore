/*
 * English :
 *  The project is protected by the MPL open source agreement.
 * Open source agreement warning that prohibits deletion of project source code files.
 * The project is prohibited from acting in illegal areas.
 * All illegal activities arising from the use of this project are the responsibility of the second author, and the original author of the project is not responsible
 *
 *  中文：
 *  该项目由MPL开源协议保护。
 *  禁止删除项目源代码文件的开源协议警告内容。
 * 禁止使用该项目在非法领域行事。
 * 使用该项目产生的违法行为，由第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 *  オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 *
 */

/*******************************************************************************
 *  2021 Ore Developer Warn
 *
 * English :
 * The project is protected by the MPL open source agreement.
 * Open source agreement warning that prohibits deletion of project source code files.
 * The project is prohibited from acting in illegal areas.
 * All illegal activities arising from the use of this project are the responsibility of the second author, and the original author of the project is not responsible
 *
 * 中文：
 * 该项目由MPL开源协议保护。
 * 禁止删除项目源代码文件的开源协议警告内容。
 * 禁止使用该项目在非法领域行事。
 * 使用该项目产生的违法行为，由第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 * オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 ******************************************************************************/

package moe.ore.core.net

import kotlin.Throws
import java.lang.InterruptedException
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.AbstractQueuedSynchronizer
import java.lang.IllegalMonitorStateException
import java.io.Serializable
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.Lock

/**
 * 互斥锁
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
internal class MutexLock : Lock, Serializable {
    // 内部类，自定义同步器
    private class Sync : AbstractQueuedSynchronizer() {
        // 是否处于占用状态
        public override fun isHeldExclusively(): Boolean {
            return state == 1
        }

        // 当状态为0的时候获取锁
        public override fun tryAcquire(acquires: Int): Boolean {
            assert(acquires == 1 // Otherwise unused
            )
            if (compareAndSetState(0, 1)) {
                exclusiveOwnerThread = Thread.currentThread()
                return true
            }
            return false
        }

        // 释放锁，将状态设置为0
        override fun tryRelease(releases: Int): Boolean {
            assert(releases == 1 // Otherwise unused
            )
            if (state == 0) throw IllegalMonitorStateException()
            exclusiveOwnerThread = null
            state = 0
            return true
        }

        // 返回一个Condition，每个condition都包含了一个condition队列
        fun newCondition(): Condition {
            return ConditionObject()
        }
    }

    // 仅需要将操作代理到Sync上即可
    private val sync = Sync()
    override fun lock() {
        sync.acquire(1)
    }

    override fun tryLock(): Boolean {
        return sync.tryAcquire(1)
    }

    override fun unlock() {
        sync.release(1)
    }

    override fun newCondition(): Condition {
        return sync.newCondition()
    }

    val isLocked: Boolean
        get() = sync.isHeldExclusively

    fun hasQueuedThreads(): Boolean {
        return sync.hasQueuedThreads()
    }

    @Throws(InterruptedException::class)
    override fun lockInterruptibly() {
        sync.acquireInterruptibly(1)
    }

    @Throws(InterruptedException::class)
    override fun tryLock(timeout: Long, unit: TimeUnit): Boolean {
        return sync.tryAcquireNanos(1, unit.toNanos(timeout))
    }
}