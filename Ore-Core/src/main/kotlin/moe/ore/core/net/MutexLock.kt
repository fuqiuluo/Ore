package moe.ore.core.net

import io.netty.handler.codec.MessageToMessageDecoder
import io.netty.buffer.ByteBuf
import kotlin.Throws
import io.netty.channel.ChannelHandlerContext
import moe.ore.core.net.decoder.DemoMessageDecoder
import moe.ore.core.net.decoder.Demo
import io.netty.util.ReferenceCountUtil
import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandler.Sharable
import moe.ore.core.net.BotConnection
import java.net.SocketAddress
import io.netty.channel.ChannelPromise
import io.netty.handler.timeout.IdleStateEvent
import io.netty.handler.timeout.IdleState
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.Executors
import io.netty.channel.ChannelFuture
import java.lang.Runnable
import java.lang.InterruptedException
import java.util.concurrent.TimeUnit
import kotlin.jvm.JvmStatic
import moe.ore.core.net.BotClient
import java.util.Arrays
import moe.ore.core.net.PackRequest.OnDataListener
import java.util.HashMap
import moe.ore.core.net.PackRequest
import moe.ore.core.net.MassageListener
import java.util.Objects
import java.util.concurrent.locks.AbstractQueuedSynchronizer
import java.lang.IllegalMonitorStateException
import moe.ore.core.net.MutexLock
import kotlin.jvm.Synchronized
import io.netty.channel.nio.NioEventLoopGroup
import moe.ore.core.net.listener.HeartBeatListener
import io.netty.handler.timeout.IdleStateHandler
import moe.ore.core.net.listener.ReConnectionListener
import moe.ore.core.net.listener.ConnectionListener
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.ChannelOption
import io.netty.channel.ChannelInitializer
import java.io.Serializable
import java.net.InetSocketAddress
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