package moe.ore.core.net

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import moe.ore.core.net.listener.MassageListener
/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
class BotClient(val uin: ULong) {

    private val connection: BotConnection = BotConnection(object : MassageListener() {
        override fun onMassage(ctx: ChannelHandlerContext, msg: Any) {
            println("channelRead = $ctx, msg = $msg")

            val byteBuf = msg as ByteBuf
            // TODO: 2021/5/30 一顿操作之后 大概伪代码
            val cmdName = byteBuf.readBytes(10).toString()
            val requestId = byteBuf.readLong()
            val uin = byteBuf.readLong()
            PackRequest.call(uin, cmdName, requestId, byteBuf.array())
        }
    }, uin)

    fun send(requestBody: ByteArray): Boolean {
        return connection.send(requestBody)
    }

    fun connect(): BotClient {
        connection.connect()
        return this
    }

    fun newPackRequest(cmdName: String, requestId: Long, requestBody: ByteArray): PackRequest {
        return PackRequest(this, cmdName, requestId, requestBody)
    }

}