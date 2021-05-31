package moe.ore.core.net.listener

import kotlin.Throws
import moe.ore.core.net.BotConnection
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelFuture
import java.lang.InterruptedException
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
class ConnectionListener(private val botConnection: BotConnection) : ChannelFutureListener {
    private val timer = Timer()
    @Throws(Exception::class)
    override fun operationComplete(channelFuture: ChannelFuture) {
        if (!channelFuture.isSuccess) {
            timer.schedule(object : TimerTask() {
                override fun run() {
                    System.err.println("ConnectionListener服务端链接不上，开始重连操作...")
                    try {
                        if (botConnection.channelFuture.channel().isActive) {
                            timer.cancel()
                            return
                        }
                        botConnection.connect()
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }, TimeUnit.SECONDS.toMillis(5))
        } else {
            System.err.println("服务端链接成功...")
        }
    }
}