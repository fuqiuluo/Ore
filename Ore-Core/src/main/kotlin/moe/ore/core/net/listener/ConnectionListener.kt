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
class ConnectionListener(private val botConnection: BotConnection) : ChannelFutureListener {
    private val scheduler = Executors.newScheduledThreadPool(1)

    @Throws(Exception::class)
    override fun operationComplete(channelFuture: ChannelFuture) {
        if (!channelFuture.isSuccess) {
            scheduler.schedule(Runnable {
                System.err.println("ConnectionListener服务端链接不上，开始重连操作...")
                try {
                    if (botConnection.channelFuture.channel().isActive) {
                        scheduler.shutdown()
                        return@Runnable
                    }
                    botConnection.connect()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }, 5L, TimeUnit.SECONDS)
        } else {
            System.err.println("服务端链接成功...")
        }
    }
}