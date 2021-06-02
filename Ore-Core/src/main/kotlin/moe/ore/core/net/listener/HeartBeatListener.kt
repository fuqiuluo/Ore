package moe.ore.core.net.listener

import kotlin.Throws
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandler.Sharable
import moe.ore.core.net.BotConnection
import io.netty.handler.timeout.IdleStateEvent
import io.netty.handler.timeout.IdleState
import io.netty.buffer.Unpooled
import moe.ore.core.net.BotClient
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
                    // todo 长期没收到服务器数据 可以选择重新连接?
                     botConnection.connect()
                }
                evt.state() == IdleState.WRITER_IDLE -> {
                    println("该发心跳包了")
                    //发送心跳包
                    ctx.writeAndFlush(Unpooled.copiedBuffer(makeHeartBeatPacket(BotClient.getNextSeq())))
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

    private fun makeHeartBeatPacket(seq: Int): ByteArray {
//        val builder = ByteBuilder()
//        builder.writeInt(0xA)
//        builder.writeByte(0)
//        builder.writeBytesWithSize(byteArrayOf(), 4)
//        builder.writeByte(0)
//        builder.writeStringWithSize(toolQ.account.user.toString(), 4)
//
//        val headBuilder = ByteBuilder()
//        headBuilder.writeInt(seq)
//        headBuilder.writeInt(toolQ.androidQQ.appId())
//        headBuilder.writeInt(toolQ.androidQQ.appId())
//        headBuilder.writeInt(16777216)
//        headBuilder.writeInt(0)
//        headBuilder.writeInt(256)
//        headBuilder.writeBytesWithSize(byteArrayOf(), 4)
//        headBuilder.writeStringWithSize("Heartbeat.Alive", 4)
//        headBuilder.writeBytesWithSize(byteArrayOf(), 4)
//        headBuilder.writeStringWithSize(Android.androidId, 4)
//        headBuilder.writeBytesWithSize(Android.getKsid(), 4)
//        headBuilder.writeStringWithShortSize(toolQ.androidQQ.agreementVersion(), 2)
//        headBuilder.writeStringWithSize("", 4)
//
//        builder.writeBytesWithSize(headBuilder.toByteArray(), 4)
//
//        builder.writeInt(4)
//
//        builder.writeSize(4)
//        return builder.toByteArray()
        TODO("心跳内容还未实现")
    }
}