package moe.ore.helper.netty

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import java.io.IOException




class NettyDecoder : ByteToMessageDecoder() {
    private val charsetName = "UTF-8"

    override fun decode(ctx: ChannelHandlerContext, byteBuf: ByteBuf, list: MutableList<Any>) {
        do {
            val saveReaderIndex = byteBuf.readerIndex()
            val msg = decodeResponse(byteBuf)
            if (msg == null) {
                byteBuf.readerIndex(saveReaderIndex)
                break
            } else {
                if (saveReaderIndex == byteBuf.readerIndex()) {
                    throw IOException("Decode without read data.")
                }
                list.add(msg)
            }
        } while (byteBuf.isReadable)
    }

    private fun decodeResponse(channelBuffer: ByteBuf): NettyResponse? {
        if (channelBuffer.readableBytes() < 4) {
            return null
        }
        val length = channelBuffer.readInt() - 4
        if (length > 10 * 1024 * 1024 || length <= 0) {
            throw RuntimeException(
                "the length header of the package must be between 0~10M bytes. data length:"
                        + Integer.toHexString(length)
            )
        }
        if (channelBuffer.readableBytes() < length) {
            return null
        }
        val bytes = ByteArray(length)
        channelBuffer.readBytes(bytes)
        val response = NettyResponse(bytes)
        response.charsetName = charsetName
        return response
    }
}