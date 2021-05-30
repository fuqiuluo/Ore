package moe.ore.core.net.listener

import kotlin.Throws
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandler.Sharable
import moe.ore.core.net.BotConnection
import java.lang.Exception

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
@Sharable
class ExceptionListener(private val botConnection: BotConnection) : ChannelHandlerAdapter() {

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable?) {
        //todo  关闭网络一段时间后 再次打开网络就会报错这个        java.io.IOException: Connection reset by peer
        println("exceptionCaught")
        cause?.printStackTrace()
        ctx.close() //关掉吗？
        //       botConnection.connect();
//        super.exceptionCaught(ctx, cause); 不往下分发
    }
}