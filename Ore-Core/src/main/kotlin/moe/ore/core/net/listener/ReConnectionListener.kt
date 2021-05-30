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
import moe.ore.core.net.PackRequest.OnDataListener
import moe.ore.core.net.PackRequest
import moe.ore.core.net.MassageListener
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
import java.util.*

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
@Sharable
class ReConnectionListener(private val botConnection: BotConnection) : ChannelHandlerAdapter() {
    val timer = Timer()

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        System.err.println("ReConnectionListener掉线了...")
        timer.schedule(object : TimerTask() {
            override fun run() {
                try {
                    if (!botConnection.channelFuture.channel().isActive) {
                        botConnection.connect()
                    } else {
                        timer.cancel()
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }, TimeUnit.SECONDS.toMillis(5))
        //        super.channelInactive(ctx);//不向下传递这个事件
    }
}