package moe.ore.core.net

import io.netty.bootstrap.Bootstrap
import kotlin.Throws
import io.netty.buffer.Unpooled
import java.util.concurrent.Executors
import io.netty.channel.ChannelFuture
import java.lang.InterruptedException
import java.util.concurrent.TimeUnit
import moe.ore.core.net.listener.MassageListener
import kotlin.jvm.Synchronized
import io.netty.channel.nio.NioEventLoopGroup
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
class BotConnection constructor(massageListener: MassageListener) {

    lateinit var channelFuture: ChannelFuture

    private val bootstrap: Bootstrap = Bootstrap()
    private val nioEventLoopGroup: NioEventLoopGroup = NioEventLoopGroup()
    private val eventListener: EventListener = EventListener(this@BotConnection)
    private val heartBeatListener: HeartBeatListener = HeartBeatListener(this@BotConnection)

    // TODO: 2021/6/1 设置一个合理的心跳时间
    private val idleStateHandler: IdleStateHandler = IdleStateHandler(5, 3, 10, TimeUnit.SECONDS)
    private val reConnectionListener: ReConnectionListener = ReConnectionListener(this@BotConnection)
    private val exceptionListener: ExceptionListener = ExceptionListener(this@BotConnection)
    private val connectionListener: ConnectionListener = ConnectionListener(this@BotConnection)
    private val scheduler = Executors.newScheduledThreadPool(1)

    @Synchronized
    fun connect() {
        channelFuture = bootstrap.connect().addListener(connectionListener)
        scheduler.execute {
            try {
                channelFuture.channel().closeFuture().sync()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
            }
        }
    }

    fun send(bytes: ByteArray): Boolean {
        val channel = channelFuture.channel()
        if (channel.isActive && !nioEventLoopGroup.isShutdown) {
            channel.writeAndFlush(Unpooled.copiedBuffer(bytes))
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
        bootstrap.channel(NioSocketChannel::class.java).option(ChannelOption.SO_KEEPALIVE, java.lang.Boolean.TRUE).option(ChannelOption.TCP_NODELAY, java.lang.Boolean.TRUE).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000).option(ChannelOption.AUTO_READ, java.lang.Boolean.TRUE)
        bootstrap.handler(object : ChannelInitializer<SocketChannel>() {
            public override fun initChannel(socketChannel: SocketChannel) {
//                注意添加顺序决定执行的先后
                socketChannel.pipeline().addLast(exceptionListener)
                socketChannel.pipeline().addLast(reConnectionListener)
                socketChannel.pipeline().addLast(idleStateHandler)
                socketChannel.pipeline().addLast(heartBeatListener) // 注意心跳包要在IdleStateHandler后面注册 不然拦截不了事件分发
                socketChannel.pipeline().addLast(eventListener) //接受除了上面已注册的东西之外的事件
                socketChannel.pipeline().addLast(massageListener)
                // socketChannel.pipeline().addLast(new DemoMessageDecoder(), new DemoMessageHandler());
            }
        })
        bootstrap.remoteAddress(InetSocketAddress(HOST, PORT))
//        String s = HOST_LIST[new Random().nextInt(HOST_LIST.length)];
//        String[] split = s.split(":");
//        bootstrap.remoteAddress(new InetSocketAddress(split[0], Integer.parseInt(split[1])));
    }
}