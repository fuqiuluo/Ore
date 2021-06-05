package moe.ore.core.net.decoder

import io.netty.handler.codec.MessageToMessageDecoder
import io.netty.buffer.ByteBuf
import kotlin.Throws
import io.netty.channel.ChannelHandlerContext
import io.netty.util.ReferenceCountUtil
import java.lang.Exception

// TODO: 2021/5/29 消息转换器 在解析到对应的cmd与之匹配就做实体类转换什么的
class DemoMessageDecoder : MessageToMessageDecoder<ByteBuf>() {
    @Throws(Exception::class)
    override fun decode(ctx: ChannelHandlerContext, msg: ByteBuf, out: MutableList<Any>) {
        try {



            if (msg.readBytes(4).toString() == CMD_NAME) {
                out.add(Demo(msg.readInt())) //一旦add进去就标识被处理了
            }
        } finally {
            //todo 这里释放msg嘛
            ReferenceCountUtil.release(msg)
        }
    }

    companion object {
        private const val CMD_NAME = "MessageSvc.PushNotify" //demo
    }
}