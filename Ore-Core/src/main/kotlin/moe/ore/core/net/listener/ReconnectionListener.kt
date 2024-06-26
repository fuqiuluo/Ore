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

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import moe.ore.api.OreStatus
import moe.ore.core.OreManager
import moe.ore.core.net.BotConnection
import moe.ore.core.net.exc.HeartbeatTimeoutException
import java.io.IOException

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
@Sharable
class ReconnectionListener(private val connection: BotConnection) : ChannelDuplexHandler() {
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        when (cause) {
            is IOException -> {
                /**
                 * 更换网络将导致 远程主机强制关闭连接
                 */
                println("net wrong, will reconnect")
                reconnect()
            }
            is HeartbeatTimeoutException -> {
                println("heartbeat wrong, will reconnect")
                reconnect()
            }
            is UnknownError -> {
                /**
                 * 未知的错误 先将机器人关闭 再甩出错误
                 */
                OreManager.shutBot(connection.uin)
                throw cause
            }
            else -> cause.printStackTrace()
        }
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        println("inactive, will reconnect")
        /**
         * 奇怪的断线 开始重连
         */
        if(OreManager.getOreStatus(connection.uin) == OreStatus.Online) {
            reconnect()
        }
    }

    private fun reconnect() {
        OreManager.changeStatus(connection.uin, OreStatus.Reconnecting)
        connection.connect()
    }
}