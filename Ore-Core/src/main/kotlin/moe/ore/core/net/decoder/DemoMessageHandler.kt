package moe.ore.core.net.decoder

import kotlin.Throws
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelHandlerAdapter
import java.lang.Exception

class DemoMessageHandler : ChannelHandlerAdapter() {

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is Demo) {
            println(msg.value)
        }
    }
}