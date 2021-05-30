package moe.ore.core.net.listener

import kotlin.Throws
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandler.Sharable
import java.lang.Exception

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
@Sharable
abstract class MassageListener : ChannelHandlerAdapter() {
    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
//        println("MassageListener.channelRead")
        onMassage(ctx, msg)
    }

    protected abstract fun onMassage(ctx: ChannelHandlerContext, msg: Any)
}