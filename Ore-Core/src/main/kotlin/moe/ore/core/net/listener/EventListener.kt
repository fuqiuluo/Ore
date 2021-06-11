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

import kotlin.Throws
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandler.Sharable
import moe.ore.core.net.BotConnection
import java.net.SocketAddress
import io.netty.channel.ChannelPromise
import java.lang.Exception

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
@Sharable
class EventListener(private val botConnection: BotConnection) : ChannelHandlerAdapter() {
    @Throws(Exception::class)

    override fun handlerAdded(ctx: ChannelHandlerContext) {
        println("handlerAdded = $ctx")
        //初始化一些东西
        super.handlerAdded(ctx)
    }

    @Throws(Exception::class)
    override fun handlerRemoved(ctx: ChannelHandlerContext) {
        println("handlerRemoved = $ctx")
        //销毁
        super.handlerRemoved(ctx)
    }

    @Throws(Exception::class)
    override fun channelRegistered(ctx: ChannelHandlerContext) {
        println("channelRegistered = $ctx")
        super.channelRegistered(ctx)
    }

    @Throws(Exception::class)
    override fun channelUnregistered(ctx: ChannelHandlerContext) {
        println("channelUnregistered = $ctx")
        super.channelUnregistered(ctx)
    }

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        println("channelActive = $ctx")
//        botConnection.send()
        super.channelActive(ctx)
    }

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        println("channelInactive？？ = $ctx")
        super.channelInactive(ctx)
    }

    @Throws(Exception::class)
    override fun channelWritabilityChanged(ctx: ChannelHandlerContext) {
        println("channelWritabilityChanged = $ctx")
        super.channelWritabilityChanged(ctx)
    }

    @Throws(Exception::class)
    override fun bind(ctx: ChannelHandlerContext, localAddress: SocketAddress, promise: ChannelPromise) {
        println("bind = $ctx, localAddress = $localAddress, promise = $promise")
        super.bind(ctx, localAddress, promise)
    }

    @Throws(Exception::class)
    override fun connect(ctx: ChannelHandlerContext, remoteAddress: SocketAddress, localAddress: SocketAddress?, promise: ChannelPromise) {
        println("connect = $ctx, remoteAddress = $remoteAddress, localAddress = $localAddress, promise = $promise")
        super.connect(ctx, remoteAddress, localAddress, promise)
    }

    @Throws(Exception::class)
    override fun disconnect(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        println("disconnect = $ctx, promise = $promise")
        super.disconnect(ctx, promise)
    }

    @Throws(Exception::class)
    override fun close(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        println("close = $ctx, promise = $promise")
        super.close(ctx, promise)
    }

    @Throws(Exception::class)
    override fun deregister(ctx: ChannelHandlerContext, promise: ChannelPromise) {
        println("deregister = $ctx, promise = $promise")
        super.deregister(ctx, promise)
    }

    @Throws(Exception::class)
    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        println("channelReadComplete = $ctx")
        super.channelReadComplete(ctx)
    }

    @Throws(Exception::class)
    override fun read(ctx: ChannelHandlerContext) {
        println("read = $ctx")
        super.read(ctx)
    }

    @Throws(Exception::class)
    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
        println("write = $ctx, msg = $msg, promise = $promise")
        super.write(ctx, msg, promise)
    }

    @Throws(Exception::class)
    override fun flush(ctx: ChannelHandlerContext) {
        println("flush = $ctx")
        super.flush(ctx)
    }
}