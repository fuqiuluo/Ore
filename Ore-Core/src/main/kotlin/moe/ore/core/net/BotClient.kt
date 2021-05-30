package moe.ore.core.net

import io.netty.buffer.ByteBuf
import kotlin.Throws
import io.netty.channel.ChannelHandlerContext
import moe.ore.core.net.listener.MassageListener
import java.lang.InterruptedException
import java.util.HashMap
import java.util.Objects

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
class BotClient private constructor() {
    companion object {
        val instance: BotClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BotClient()
        }
    }

    //    map key is hashcode
    private val packHandlerMap: HashMap<Int, PackRequest> = HashMap()
    private val connection: BotConnection = BotConnection()
    private val massageListener: MassageListener

    fun send(requestBody: ByteArray): Boolean {
        return connection.send(requestBody)
    }

    @Throws(InterruptedException::class)
    fun connect(): BotClient {
        connection.setMassageListener(massageListener)
        connection.connect()
        return this
    }

    fun unregisterHandler(handler: PackRequest) {
        if (packHandlerMap.containsKey(handler.hashCode())) {
            packHandlerMap.remove(handler.hashCode(), handler)
        }
    }

    fun newPackRequest(cmdName: String, requestId: Long, requestBody: ByteArray): PackRequest {
        return PackRequest(this, cmdName, requestId, requestBody)
    }

    fun registerHandler(handler: PackRequest) {
        packHandlerMap[handler.hashCode()] = handler
    }


    init {
        massageListener = object : MassageListener() {
            override fun onMassage(ctx: ChannelHandlerContext, msg: Any) {
                println("channelRead = $ctx, msg = $msg")
                val byteBuf = msg as ByteBuf
                // TODO: 2021/5/30 一顿操作之后 大概伪代码
                val cmdName = byteBuf.readBytes(10).toString()
                val requestId = byteBuf.readBytes(10).toString()
                // TODO: 2021/5/30 Objects.hash(cmdName, requestId)待测试同参是否一致
                val packRequest = packHandlerMap[Objects.hash(cmdName, requestId)]
                packRequest?.callData(byteBuf.array())
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
        }
    }
}