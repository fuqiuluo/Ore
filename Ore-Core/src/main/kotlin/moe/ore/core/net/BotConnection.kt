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
 * 使用该项目产生的违法行为，由使用者或使用者或第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 *  オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 *
 */

package moe.ore.core.net

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.Unpooled
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.timeout.IdleStateHandler
import moe.ore.core.net.decoder.BotDecoder
import moe.ore.core.net.listener.EventListener
import moe.ore.core.net.listener.HeartBeatListener
//import moe.ore.core.net.listener.IdleStateHandler
import moe.ore.core.net.listener.ReconnectionListener
import moe.ore.core.net.listener.UsefulListener
import moe.ore.core.util.QQUtil
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
class BotConnection(private val usefulListener: UsefulListener, val uin: Long) {
    lateinit var channelFuture: ChannelFuture
    private var nioEventLoopGroup: NioEventLoopGroup = NioEventLoopGroup()

    private val eventListener: EventListener = EventListener(this)
    val heartBeatListener: HeartBeatListener = HeartBeatListener(this)

    private val reconnectionHandler: ReconnectionListener = ReconnectionListener(this)

    //    max1个线程池 不允许再多
    private val scheduler = Executors.newScheduledThreadPool(1)

    // 单位：秒
    private var baseIdleTime = 2 * 60

    fun close() {
        if (this::channelFuture.isInitialized) {
            println("exec close the client")
            if (!channelFuture.isVoid || channelFuture.channel().isActive) {
                channelFuture.channel().close()
            }
        }
    }

    @Synchronized
    @Throws(InterruptedException::class)
    fun connect(host: String, port: Int) {
        // 断开原先的连接 重新建立连接
        this.close()
        scheduler.execute {
            try {
                channelFuture = init(Bootstrap()).connect(host, port)
                channelFuture.addListener(usefulListener).sync()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @Synchronized
    @Throws(InterruptedException::class)
    fun connect() {
        val server = QQUtil.getOicqServer() ?: oicqServer[Random.nextInt(0, oicqServer.size - 1)]
        // println("TencentServer: $server")
        // 手动解析域名
        this.connect(server.first, server.second)
    }

    fun send(bytes: ByteArray) {
        // println("Send: " + bytes.toHexString())
        channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer(bytes))
    }

    companion object {
        private val oicqServer = arrayOf(
                "msfwifi.3g.qq.com" to 8080,
                // "14.215.138.110" to 8080,
                // 这个服务器，连不上
//            "113.96.12.224" to 8080, "157.255.13.77" to 14000, "120.232.18.27" to 443, "183.3.235.162" to 14000, "163.177.89.195" to 443,
                // "183.232.94.44" to 80,
                // 不可接通的服务器
//            "203.205.255.224" to 8080, "203.205.255.221" to 8080 ,
                "msfwifiv6.3g.qq.com" to 8080
        )
    }

    private fun init(bootstrap: Bootstrap): Bootstrap {
        if (nioEventLoopGroup.isShutdown || nioEventLoopGroup.isShuttingDown) {
            nioEventLoopGroup = NioEventLoopGroup()
        }
        bootstrap.group(nioEventLoopGroup)
                .option(ChannelOption.SINGLE_EVENTEXECUTOR_PER_GROUP, false)
                .option(ChannelOption.TCP_NODELAY, java.lang.Boolean.TRUE)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .channel(NioSocketChannel::class.java as Class<out Channel>)
                .option(ChannelOption.SO_KEEPALIVE, java.lang.Boolean.TRUE)
                .option(ChannelOption.AUTO_READ, java.lang.Boolean.TRUE)
                .handler(object : ChannelInitializer<SocketChannel>() {
                    public override fun initChannel(socketChannel: SocketChannel) {
                        // 注意添加顺序决定执行的先后
                        socketChannel.pipeline().addLast("ping", IdleStateHandler(baseIdleTime.toLong() + 3, baseIdleTime.toLong(), baseIdleTime.toLong() + (3 * 2), TimeUnit.SECONDS))
                        socketChannel.pipeline().addLast("heartbeat", heartBeatListener) // 注意心跳包要在IdleStateHandler后面注册 不然拦截不了事件分发
                        socketChannel.pipeline().addLast("decoder", BotDecoder())
                        socketChannel.pipeline().addLast("handler", usefulListener)
                        socketChannel.pipeline().addLast("caughtHandler", reconnectionHandler)
                        socketChannel.pipeline().addLast("event", eventListener) //接受除了上面已注册的东西之外的事件
                    }
                })
        return bootstrap
    }

    /**
     * 设置新的心跳
     */
    fun setNewIdleStateHandlerTime(newBaseIdleTime: Int) {
        println("change heartbeat time to $newBaseIdleTime")
        this.baseIdleTime = newBaseIdleTime
        val socketChannel = channelFuture.channel()
        runCatching {
            socketChannel.pipeline().remove("ping")
        }
        socketChannel.pipeline().addFirst("ping", IdleStateHandler(baseIdleTime.toLong() + 3, baseIdleTime.toLong(), baseIdleTime.toLong() + (3 * 2), TimeUnit.SECONDS))
    }
}