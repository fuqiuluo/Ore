package moe.ore.helper.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent
import moe.ore.helper.log.TLog

class ClientHandler(private val client : NettyClient) : ChannelInboundHandlerAdapter() {
    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
        ctx.channel().eventLoop().execute {
            client.listener?.channelActive()
        }
    }

    /**
     * 设定IdleStateHandler心跳检测每x秒进行一次读检测，
     * 如果x秒内ChannelRead()方法未被调用则触发一次userEventTrigger()方法
     *
     * @param ctx ChannelHandlerContext
     * @param evt IdleStateEvent
     */
    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
        super.userEventTriggered(ctx, evt)
        if (evt is IdleStateEvent) {
            if (evt.state() == IdleState.WRITER_IDLE) {
                //发送心跳
                if (client.needSendHeartbeat) {
                    client.listener?.let { client.send(it.heartbeat()) }
                }
            }
        }
    }

    /**
     * 接包的地方┗|｀O′|┛ 嗷~~
     * @param ctx ChannelHandlerContext
     * @param msg Any
     */
    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        if (msg != null && ctx != null) {
            val response = msg as NettyResponse
            ctx.channel().eventLoop().execute {
                client.listener?.receive(response.body)
            }
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        ctx?.close()
        cause?.let {
            logger.warn(it)
        }
    }

    /**
     * 断开连接事件
     * @param ctx ChannelHandlerContext
     */
    override fun channelInactive(ctx: ChannelHandlerContext?) {
        super.channelInactive(ctx)
        // 这个就不用多线程执行了嗷，因为nio的线程池被shut了
        client.listener?.channelInactive()
        logger.info("$client disconnect")
    }

    companion object {
        @JvmStatic
        private val logger = TLog.getLogger("ClientHandler")
    }
}