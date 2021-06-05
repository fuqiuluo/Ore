package moe.ore.core.net

import io.netty.bootstrap.Bootstrap
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
import io.netty.handler.timeout.IdleStateHandler
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.ChannelOption
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import moe.ore.core.net.decoder.BotDecoder
import moe.ore.core.net.listener.*
import java.net.InetSocketAddress
import kotlin.random.Random

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
class BotConnection {
    lateinit var channelFuture: ChannelFuture

    private val bootstrap: Bootstrap = Bootstrap()
    private val nioEventLoopGroup: NioEventLoopGroup = NioEventLoopGroup()
    private var massageListener: MassageListener? = null
    private val eventListener: EventListener = EventListener(this)
    private val heartBeatListener: HeartBeatListener = HeartBeatListener(this@BotConnection)
    private val idleStateHandler: IdleStateHandler = IdleStateHandler(5, 3, 10, TimeUnit.SECONDS)
    private val reConnectionListener: ReConnectionListener = ReConnectionListener(this@BotConnection)
    private val exceptionListener: ExceptionListener = ExceptionListener(this)
    private val connectionListener: ConnectionListener = ConnectionListener(this@BotConnection)
    private val scheduler = Executors.newScheduledThreadPool(1)

    @Synchronized
    @Throws(InterruptedException::class)
    fun connect() {
        channelFuture = bootstrap.connect().addListener(connectionListener)
        scheduler.execute {
            try {
                channelFuture.channel().closeFuture().sync()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                // TODO: 2021/5/29 如果需要重连 我海需要关闭它吗？
                // ioEventLoopGroup.shutdownGracefully();
            }
        }
    }

    fun send(bytes: ByteArray): Boolean {
        val channel = channelFuture.channel()
        if (channel.isActive && !nioEventLoopGroup.isShutdown) {
            channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer(bytes))
            return true
        }
        return false
    }

    companion object {
        private val oicqServer = arrayOf(
            "msfwifi.3g.qq.com" to 8080,
            "14.215.138.110" to 8080,
            "113.96.12.224" to 8080,
            "157.255.13.77" to 14000,
            "120.232.18.27" to 443,
            "183.3.235.162" to 14000,
            "163.177.89.195" to 443,
            "183.232.94.44" to 80,
            "203.205.255.224" to 8080,
            "203.205.255.221" to 8080,
            "msfwifiv6.3g.qq.com" to 8080,
            "[240e:ff:f101:10::109]" to 14000
        )
    }

    init {
        bootstrap.group(nioEventLoopGroup)
        bootstrap.channel(NioSocketChannel::class.java)
            .option(ChannelOption.SO_KEEPALIVE, java.lang.Boolean.TRUE)
            .option(ChannelOption.TCP_NODELAY, java.lang.Boolean.TRUE)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
            .option(ChannelOption.AUTO_READ, java.lang.Boolean.TRUE)
        bootstrap.handler(object : ChannelInitializer<SocketChannel>() {
            public override fun initChannel(socketChannel: SocketChannel) {
                //  注意添加顺序决定执行的先后
                socketChannel.pipeline()
                    .addLast("caughtException", exceptionListener)
                socketChannel.pipeline().addLast("reConnection", reConnectionListener)
                socketChannel.pipeline().addLast("ping", IdleStateHandler(5, 3, 10, TimeUnit.SECONDS))
                socketChannel.pipeline().addLast("heartbeat", heartBeatListener) // 注意心跳包要在IdleStateHandler后面注册 不然拦截不了事件分发
                socketChannel.pipeline().addLast("event", eventListener) //接受除了上面已注册的东西之外的事件
                socketChannel.pipeline().addLast("decoder", BotDecoder())
                socketChannel.pipeline().addLast("receive", massageListener)
                //socketChannel.pipeline().addLast(new DemoMessageDecoder(), new DemoMessageHandler());
            }
        })
        val server = oicqServer[Random.nextInt(0, oicqServer.size - 1)]
        bootstrap.remoteAddress(InetSocketAddress(server.first, server.second))
    }
}