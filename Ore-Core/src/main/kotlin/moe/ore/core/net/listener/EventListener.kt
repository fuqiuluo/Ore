package moe.ore.core.net.listener

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
import java.lang.Exception
import java.net.InetSocketAddress

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
@Sharable
class EventListener(private val botConnection: BotConnection) : ChannelHandlerAdapter() {
    @Throws(Exception::class)

    override fun handlerAdded(ctx: ChannelHandlerContext) {
        println("handlerAdded = $ctx")
        //初始化一些东西
        super.handlerAdded(ctx)
    }

    @Throws(Exception::class)
    override fun handlerRemoved(ctx: ChannelHandlerContext) {
        println("handlerRemoved = $ctx")
        //销毁
        super.handlerRemoved(ctx)
    }

    @Throws(Exception::class)
    override fun channelRegistered(ctx: ChannelHandlerContext) {
        println("channelRegistered = $ctx")
        super.channelRegistered(ctx)
    }

    @Throws(Exception::class)
    override fun channelUnregistered(ctx: ChannelHandlerContext) {
        println("channelUnregistered = $ctx")
        super.channelUnregistered(ctx)
    }

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        println("channelActive = $ctx")
//        botConnection.send()
        super.channelActive(ctx)
    }

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        println("channelInactive？？ = $ctx")
        super.channelInactive(ctx)
    }

    @Throws(Exception::class)
    override fun channelWritabilityChanged(ctx: ChannelHandlerContext) {
        println("channelWritabilityChanged = $ctx")
        super.channelWritabilityChanged(ctx)
    }

    @Throws(Exception::class)
    override fun bind(ctx: ChannelHandlerContext, localAddress: SocketAddress, promise: ChannelPromise) {
        println("bind = $ctx, localAddress = $localAddress, promise = $promise")
        super.bind(ctx, localAddress, promise)
    }

    @Throws(Exception::class)
    override fun connect(ctx: ChannelHandlerContext, remoteAddress: SocketAddress, localAddress: SocketAddress, promise: ChannelPromise) {
        println("connect = $ctx, remoteAddress = $remoteAddress, localAddress = $localAddress, promise = $promise")
        super.connect(ctx, remoteAddress, localAddress, promise)
    }

    @Throws(Exception::class)
    override fun disconnect(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        println("disconnect = $ctx, promise = $promise")
        super.disconnect(ctx, promise)
    }

    @Throws(Exception::class)
    override fun close(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        println("close = $ctx, promise = $promise")
        super.close(ctx, promise)
    }

    @Throws(Exception::class)
    override fun deregister(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        println("deregister = $ctx, promise = $promise")
        super.deregister(ctx, promise)
    }

    @Throws(Exception::class)
    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        println("channelReadComplete = $ctx")
        super.channelReadComplete(ctx)
    }

    @Throws(Exception::class)
    override fun read(ctx: ChannelHandlerContext) {
        println("read = $ctx")
        super.read(ctx)
    }

    @Throws(Exception::class)
    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
        println("write = $ctx, msg = $msg, promise = $promise")
        super.write(ctx, msg, promise)
    }

    @Throws(Exception::class)
    override fun flush(ctx: ChannelHandlerContext) {
        println("flush = $ctx")
        super.flush(ctx)
    }
}