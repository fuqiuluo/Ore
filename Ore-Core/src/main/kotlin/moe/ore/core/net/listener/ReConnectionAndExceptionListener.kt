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

import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandler.Sharable
import moe.ore.core.net.BotConnection
import java.lang.InterruptedException
import java.util.concurrent.TimeUnit
import java.util.*

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
@Sharable
class ReConnectionAndExceptionListener(private val botConnection: BotConnection) : ChannelHandlerAdapter(), ChannelFutureListener {
    private val timer = Timer()

//    两个Override不会同时执行的 对应不同的断线场景 合并到了一起

    @Override
    override fun channelInactive(ctx: ChannelHandlerContext) {
        System.err.println("ChannelHandlerAdapter掉线了...")
        // reconnect()
    }

    @Override
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable?) {
        //todo  关闭网络一段时间后 再次打开网络就会报错这个  java.io.IOException: Connection reset by peer
        System.err.println("exceptionCaught..")
        cause?.printStackTrace()
        ctx.close()
        // reconnect()
        // TODO: 2021/6/1 有待测试异常之后的重连
    }

    @Override
    override fun operationComplete(channelFuture: ChannelFuture) {
        println("ChannelFutureListener掉线了...")
        if (!channelFuture.isSuccess) {
            // reconnect()
        } else {
            System.err.println("服务端链接成功...")
        }
    }

    private fun reconnect() {
        timer.schedule(object : TimerTask() {
            override fun run() {
                try {
                    if (botConnection.channelFuture.channel().isActive) {
                        timer.cancel()
                        return
                    }
                    System.err.println("正在重连中...")
                    botConnection.connect()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }, TimeUnit.SECONDS.toMillis(5))
    }
}