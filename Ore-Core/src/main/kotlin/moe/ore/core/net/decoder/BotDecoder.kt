/*
 * English :
 *  The project is protected by the MPL open source agreement.
 * Open source agreement warning that prohibits deletion of project source code files.
 * The project is prohibited from acting in illegal areas.
 * All illegal activities arising from the use of this project are the responsibility of the second author, and the original author of the project is not responsible
 *
 *  中文：
 *  该项目由MPL开源协议保护。
 *  禁止删除项目源代码文件的开源协议警告内容。
 * 禁止使用该项目在非法领域行事。
 * 使用该项目产生的违法行为，由使用者或第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 *  オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 *
 */

package moe.ore.core.net.decoder

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import java.io.IOException

class BotDecoder : ByteToMessageDecoder() {
    private val charsetName = "UTF-8"

    override fun decode(ctx: ChannelHandlerContext, byteBuf: ByteBuf, list: MutableList<Any>) {
        // println("Start")
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

    private fun decodeResponse(channelBuffer: ByteBuf): PacketResponse? {
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
        val bytes = ByteArray(length).also { channelBuffer.readBytes(it) }
        return PacketResponse(bytes, charsetName)
    }
}