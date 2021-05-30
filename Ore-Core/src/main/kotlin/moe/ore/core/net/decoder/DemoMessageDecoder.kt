package moe.ore.core.net.decoder

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

// TODO: 2021/5/29 消息转换器 在解析到对应的cmd与之匹配就做实体类转换什么的
class DemoMessageDecoder : MessageToMessageDecoder<ByteBuf>() {
    @Throws(Exception::class)
    override fun decode(ctx: ChannelHandlerContext, msg: ByteBuf, out: MutableList<Any>) {
        try {
            if (msg.readBytes(4).toString() == CMD_NAME) {
                out.add(Demo(msg.readInt())) //一旦add进去就标识被处理了
            }
        } finally {
            //todo 这里释放msg嘛
            ReferenceCountUtil.release(msg)
        }
    }

    companion object {
        private const val CMD_NAME = "MessageSvc.PushNotify" //demo
    }
}