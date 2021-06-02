package moe.ore.core.net

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import moe.ore.core.net.listener.MassageListener

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
class BotClient {

//    不应该做单例 因为考虑存在多个bot
//    companion object {
//        val instance: BotClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
//            BotClient()
//        }
//    }

//    private val timer = Timer()
//
//    init {
////        定时移没有收到结果的请求Handler 一般来说很少会有因收不到请求结果而无被法移除的Handler 感觉不是特别必要
//        timer.schedule(object : TimerTask() {
//            override fun run() {
////                println("packHandlerMap---$packHandlerMap")
//                // TODO: 2021/6/1 在循环中移除map自身的kv会不会和list那样出现异常？目前测试没有问题 有待长期观察
//                packHandlerMap.forEachValue(1) { it.checkHandlerLifeTime() }
//            }
//
//        }, 1000, 1000 * 5)
//    }

    //    map key is hashcode
//    private val packHandlerMap: ConcurrentHashMap<Int, PackRequest> = ConcurrentHashMap()
    private val connection: BotConnection = BotConnection(object : MassageListener() {
        override fun onMassage(ctx: ChannelHandlerContext, msg: Any) {
            println("channelRead = $ctx, msg = $msg")

            val byteBuf = msg as ByteBuf
            // TODO: 2021/5/30 一顿操作之后 大概伪代码
            val cmdName = byteBuf.readBytes(10).toString()
            val requestId = byteBuf.readLong()
            PackRequest.call(cmdName, requestId, byteBuf.array())
//                try {
//                    ByteBuf bb = (ByteBuf) msg;
//                    byte[] respByte = new byte[bb.readableBytes()];
//                    bb.readBytes(respByte);
//                    String respStr = new String(respByte);
//                    System.err.println("收到响应：" + respStr);
//                    ctx.writeAndFlush(Unpooled.copiedBuffer("msgggggggggg".getBytes()));
//                } finally {
            //todo 必须释放msg数据？没看文档还没搞懂
//                  ReferenceCountUtil.release(msg);
//                }
        }
    })

    fun send(requestBody: ByteArray): Boolean {
        return connection.send(requestBody)
    }

    fun connect(): BotClient {
        connection.connect()
        return this
    }

//    fun registerHandler(handler: PackRequest) {
//        packHandlerMap[handler.hashCode()] = handler
//    }
//
//    fun unregisterHandler(handler: PackRequest) {
//        if (packHandlerMap.containsKey(handler.hashCode())) {
//            packHandlerMap.remove(handler.hashCode(), handler)
//        }
//    }

    fun newPackRequest(cmdName: String, requestId: Long, requestBody: ByteArray): PackRequest {
        return PackRequest(this, cmdName, requestId, requestBody)
    }

}