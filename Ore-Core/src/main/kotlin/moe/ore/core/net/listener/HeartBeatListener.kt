package moe.ore.core.net.listener

import kotlin.Throws
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandler.Sharable
import moe.ore.core.net.BotConnection
import io.netty.handler.timeout.IdleStateEvent
import io.netty.handler.timeout.IdleState
import io.netty.buffer.Unpooled
import java.lang.Exception

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
@Sharable
class HeartBeatListener(private val botConnection: BotConnection) : ChannelHandlerAdapter() {

    // TODO: 2021/5/29 QQ的心跳机制是怎么样的 必须在一个时间范围内发送，还是说有数据交互就可以暂时不发。
    @Throws(Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        System.err.println("userEventTriggered1 = $ctx, evt = $evt")
        if (evt is IdleStateEvent) {
            when {
                evt.state() == IdleState.READER_IDLE -> {
                    println("长期没收到服务器数据 或心跳了 是不是要重连一下？ 可能断网了")
                    //可以选择重新连接
            //                Client.getImConnection().connect(Client.HOST, Client.PORT);
                }
                evt.state() == IdleState.WRITER_IDLE -> {
                    println("该发心跳包了")
                    //发送心跳包
                    ctx.writeAndFlush(Unpooled.copiedBuffer("心跳~".toByteArray()))
                }
                evt.state() == IdleState.ALL_IDLE -> {
                    System.err.println("ALL????")
                    // TODO: 2021/5/29 没太懂这句是什么意思(长时间没有收发？那不就卡死了吗？程序无响应？触发后应该做什么操作 重连吗) -> No data was either received or sent for a while.
                    botConnection.connect()
                }
            }
        }
        //        super.userEventTriggered(ctx, evt); //彻底拦截事件独享
    }
}