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
import kotlin.Throws
import io.netty.buffer.Unpooled
import java.util.concurrent.Executors
import io.netty.channel.ChannelFuture
import java.lang.InterruptedException
import java.util.concurrent.TimeUnit
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
class BotConnection(private val usefulListener: UsefulListener, val uin: Long) {
    lateinit var channelFuture: ChannelFuture
    private var nioEventLoopGroup: NioEventLoopGroup = NioEventLoopGroup()
    private val eventListener: EventListener = EventListener(this)
    private val heartBeatListener: HeartBeatListener = HeartBeatListener(this)

    // 1分钟内没有发送心跳 1分钟+10秒没有收到数据返回 1分钟+20秒没有如何操作
    private val idleStateHandler: IdleStateHandler =
        IdleStateHandler(1000 * (60 + 10), 1000 * 60, 1000 * (60 + 20), TimeUnit.MILLISECONDS)
    private val reConnectionAndExceptionListener: ReConnectionAndExceptionListener =
        ReConnectionAndExceptionListener(this)

    //    max1个线程池 不允许再多
    private val scheduler = Executors.newScheduledThreadPool(1)

    @Synchronized
    @Throws(InterruptedException::class)
    fun connect() {
        channelFuture = init(Bootstrap()).connect().addListener(reConnectionAndExceptionListener)
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
            channel.writeAndFlush(Unpooled.copiedBuffer(bytes))
            return true
        }
        return false
    }

    companion object {
        private val oicqServer = arrayOf("msfwifi.3g.qq.com" to 8080, "14.215.138.110" to 8080, "113.96.12.224" to 8080, "157.255.13.77" to 14000, "120.232.18.27" to 443, "183.3.235.162" to 14000, "163.177.89.195" to 443, "183.232.94.44" to 80, "203.205.255.224" to 8080, "203.205.255.221" to 8080, "msfwifiv6.3g.qq.com" to 8080, "[240e:ff:f101:10::109]" to 14000)
    }

    private fun init(bootstrap: Bootstrap): Bootstrap {
        if (nioEventLoopGroup.isShutdown) {
            nioEventLoopGroup = NioEventLoopGroup()
        }
        bootstrap.group(nioEventLoopGroup)
        bootstrap.channel(NioSocketChannel::class.java).option(ChannelOption.SO_KEEPALIVE, java.lang.Boolean.TRUE).option(ChannelOption.TCP_NODELAY, java.lang.Boolean.TRUE).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000).option(ChannelOption.AUTO_READ, java.lang.Boolean.TRUE)
        bootstrap.handler(object : ChannelInitializer<SocketChannel>() {
            public override fun initChannel(socketChannel: SocketChannel) {
                //  注意添加顺序决定执行的先后
                // TODO socketChannel.pipeline().addLast("reConnection", reConnectionAndExceptionListener)
                socketChannel.pipeline().addLast("ping", idleStateHandler)
                socketChannel.pipeline().addLast("heartbeat", heartBeatListener) // 注意心跳包要在IdleStateHandler后面注册 不然拦截不了事件分发
                // TODO socketChannel.pipeline().addLast("event", eventListener) //接受除了上面已注册的东西之外的事件
                socketChannel.pipeline().addLast("decoder", BotDecoder())
                socketChannel.pipeline().addLast("handler", usefulListener)
            }
        })
        val server = oicqServer[Random.nextInt(oicqServer.size)]
        return bootstrap.remoteAddress(InetSocketAddress(server.first, server.second))
    }
}