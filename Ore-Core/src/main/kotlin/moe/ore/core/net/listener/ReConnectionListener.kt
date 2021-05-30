package moe.ore.core.net.listener

import kotlin.Throws
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandler.Sharable
import moe.ore.core.net.BotConnection
import java.lang.InterruptedException
import java.util.concurrent.TimeUnit
import java.lang.Exception
import java.util.*

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
@Sharable
class ReConnectionListener(private val botConnection: BotConnection) : ChannelHandlerAdapter() {
    private val timer = Timer()

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        System.err.println("ReConnectionListener掉线了...")
        timer.schedule(object : TimerTask() {
            override fun run() {
                try {
                    if (!botConnection.channelFuture.channel().isActive) {
                        botConnection.connect()
                    } else {
                        timer.cancel()
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }, TimeUnit.SECONDS.toMillis(5))
    }
}