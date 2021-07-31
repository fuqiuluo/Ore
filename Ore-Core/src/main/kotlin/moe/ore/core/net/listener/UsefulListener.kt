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

package moe.ore.core.net.listener

import io.netty.channel.*
import io.netty.channel.ChannelHandler.Sharable
import moe.ore.core.net.decoder.PacketResponse

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
@Sharable
abstract class UsefulListener : SimpleChannelInboundHandler<PacketResponse>(true), ChannelFutureListener {
//    @Throws(Exception::class)
//    override fun channelRead(ctx: ChannelHandlerContext, msg: Any?) {
//        msg?.let {
//            this.onMassage(msg as PacketResponse)
//        }
//    }



    override fun channelActive(ctx: ChannelHandlerContext?) {
        // println(System.currentTimeMillis())
        // 握手成功 并沒有载入服务器
    }

    override fun operationComplete(future: ChannelFuture?) {
        if (future != null && future.isSuccess) {
            // println("成功接入服务器")
            this.onConnect()
        } else {
            this.onFailConnect()
        }
    }

    protected abstract fun onConnect()

    protected abstract fun onFailConnect()

//    protected abstract fun onMassage(msg: PacketResponse)
}