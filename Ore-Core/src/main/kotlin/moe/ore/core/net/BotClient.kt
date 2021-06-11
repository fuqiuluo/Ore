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

package moe.ore.core.net

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import moe.ore.core.net.listener.MessageListener
/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 * uin 为BotClient唯一身份标识 代表是哪个号的bot实例
 */
class BotClient(val uin: Long) {

    private val connection: BotConnection = BotConnection(object : MessageListener() {
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