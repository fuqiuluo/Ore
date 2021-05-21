package moe.ore.helper.netty

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.Unpooled
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.timeout.IdleStateHandler
import moe.ore.helper.log.TLog
import moe.ore.util.DebugUtil
import org.omg.CORBA.TIMEOUT
import java.util.concurrent.TimeUnit

/**
 * Netty为基础的Tcp工具
 *
 * @property host String
 * @property port Int
 */
class NettyClient(private val host : String, private val port : Int, val needSendHeartbeat : Boolean = false) {

    var listener : NettyListener? = null

    private lateinit var channel : ChannelFuture

    /**
     * 接发包线程池容量，不介意太高，会内存溢出
     */
    private var boosEventLoopGroup : EventLoopGroup = NioEventLoopGroup(DebugUtil.getIoThreadPoolSize())

    private var bootstrap : Bootstrap = Bootstrap()
        .group(boosEventLoopGroup)
        .option(ChannelOption.TCP_NODELAY, true)
        // 屏蔽Nagle算法
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
        .channel(NioSocketChannel::class.java as Class<out Channel>?)
        .handler(object : ChannelInitializer<SocketChannel>() {
            override fun initChannel(ch: SocketChannel?) {
                if (ch != null) {
                    if (needSendHeartbeat) {
                        ch.pipeline().addLast("ping", IdleStateHandler(0, heartbeatInterval, 300, TimeUnit.SECONDS))
                    }

                    ch.pipeline()
                        .addLast("decoder", NettyDecoder())
                        .addLast("handler", ClientHandler(this@NettyClient))

                }
            }
        })

    fun connect() = connect(null)

    /**
     * 连接到服务器
     */
    fun connect(block : Thread?) {
        object : Thread("NettyConnectThread") {
            override fun run() {
                synchronized(this@NettyClient) {
                    try {
                        logger.info("start connecting to server[%s:%s]".format(host, port))
                        val chan : ChannelFuture = bootstrap.connect(host, port)
                        chan.addListener {
                            if (it.isSuccess) {
                                this@NettyClient.channel = chan
                                logger.info("server connection is successful")
                                block?.start()
                            } else {
                                logger.info("server connection is fail")
                            }
                        }.sync()
                    } catch (e : Exception) {
                        logger.warn(e)
                        this@NettyClient.boosEventLoopGroup.shutdownGracefully()
                    }
                }
            }
        }.start()
    }

    /**
     * 是否存活
     * @return Boolean
     */
    fun isAlive() : Boolean {
        return !boosEventLoopGroup.isShutdown && !boosEventLoopGroup.isShuttingDown
    }

    fun send(data : ByteArray) {
        this.channel.channel().writeAndFlush(Unpooled.copiedBuffer(data))
        logger.info("Send a packet in netty")
    }

    /**
     * 关闭连接
     */
    fun shut() {
        if (isAlive()) {

            boosEventLoopGroup.shutdownGracefully()
        }
    }

    override fun toString(): String {
        return "[Client]{host : $host, port : $port}"
    }

    companion object {
        @JvmStatic
        private val logger = TLog.getLogger("NettyClient")

        /**
         * 心跳间隔
         */
        const val heartbeatInterval = 60 * 2L
    }
}