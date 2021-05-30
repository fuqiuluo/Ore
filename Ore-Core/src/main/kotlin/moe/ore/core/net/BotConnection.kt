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
import moe.ore.core.net.listener.*
import java.net.InetSocketAddress

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
    private val exceptionListener: ExceptionListener = ExceptionListener()
    private val connectionListener: ConnectionListener = ConnectionListener(this@BotConnection)
    private val scheduler = Executors.newScheduledThreadPool(1)
    fun setMassageListener(massageListener: MassageListener?) {
        this.massageListener = massageListener
    }

    @Synchronized
    @Throws(InterruptedException::class)
    fun connect() {
        channelFuture = bootstrap.connect().addListener(connectionListener)
        scheduler.execute {
            try {
                channelFuture.channel().closeFuture().sync()
                //                    System.err.println("啦啦啦啦啦啦啦啦绿");
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                // TODO: 2021/5/29 如果需要重连 我海需要关闭它吗？
//                    nioEventLoopGroup.shutdownGracefully();
            }
        }
        //        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    System.err.println("vvvvvvvvvvvvvvvvvvvvvv");
//                    channelFuture = connect(host, port);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        new Timer().schedule(timerTask, TimeUnit.SECONDS.toMillis(5));
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
        // TODO: 2021/5/30 从配置类里面获取Host
        private val HOST_LIST = arrayOf("msfwifiv6.3g.qq.com:8080", "[240e:ff:f101:10::109]:14000", "msfwifi.3g.qq.com:8080", "14.215.138.110:8080", "113.96.12.224:8080", "157.255.13.77:14000", "120.232.18.27:443", "183.3.235.162:14000", "163.177.89.195:443", "183.232.94.44:80", "203.205.255.224:8080", "203.205.255.221:8080")
        const val HOST = "127.0.0.1"
        const val PORT = 2048
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
//                注意添加顺序决定执行的先后
                socketChannel.pipeline().addLast(exceptionListener)
                socketChannel.pipeline().addLast(reConnectionListener)
                socketChannel.pipeline().addLast(IdleStateHandler(5, 3, 10, TimeUnit.SECONDS))
                socketChannel.pipeline().addLast(heartBeatListener) // 注意心跳包要在IdleStateHandler后面注册 不然拦截不了事件分发
                socketChannel.pipeline().addLast(eventListener) //接受除了上面已注册的东西之外的事件
                socketChannel.pipeline().addLast(massageListener)
                //                socketChannel.pipeline().addLast(new DemoMessageDecoder(), new DemoMessageHandler());
            }
        })
        bootstrap.remoteAddress(InetSocketAddress(HOST, PORT))
        //        String s = HOST_LIST[new Random().nextInt(HOST_LIST.length)];
//        String[] split = s.split(":");
//        bootstrap.remoteAddress(new InetSocketAddress(split[0], Integer.parseInt(split[1])));
    }
}