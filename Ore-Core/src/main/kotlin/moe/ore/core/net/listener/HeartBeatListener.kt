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
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent
import moe.ore.api.OreStatus
import moe.ore.core.OreManager
import moe.ore.core.helper.DataManager
import moe.ore.core.net.BotConnection
import moe.ore.core.net.exc.HeartbeatTimeoutException
import moe.ore.core.protocol.ProtocolInternal
import moe.ore.helper.*

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 */
@Sharable
class HeartBeatListener(private val connection: BotConnection) : ChannelDuplexHandler() {

    @Throws(Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        // System.err.println("userEventTriggered1 = $ctx, evt = $evt")
        if (evt is IdleStateEvent && OreManager.getOreStatus(connection.uin) == OreStatus.Online) {
            when {
                evt.state() == IdleState.WRITER_IDLE -> {
                    // 心跳发送成功
                    println("send a heartbeat packet")
                    // 无任何写操作发生 即开始发送心跳包
                    connection.send(makeHeartBeatPacket())
                }
                evt.state() == IdleState.READER_IDLE -> {
                    println("not receive any packet, went to reconnect")
                    // 调出错误交给错误处理器处理
                    ctx.fireExceptionCaught(HeartbeatTimeoutException())
                }
                evt.state() == IdleState.ALL_IDLE -> {
                    // 有一定时间没有接收数据 也没有发送数据
                    println("not write and read, the ore will be thrown")
                    // OreManager.shutBot(connection.uin)
                    // 没有读操作 没有写操作
                    ctx.fireExceptionCaught(UnknownError())
                }
            }
        }
    }

    // 调用自动组包逻辑类太麻烦了，原地组心跳吧！！！
    private fun makeHeartBeatPacket(): ByteArray = newBuilder().apply {
        val ore = OreManager.getBot(connection.uin)!!
        val manager = DataManager.manager(connection.uin)
        val session = manager.session
        val protocol = ProtocolInternal[manager.protocolType]
        writeBlockWithIntLen({ it + 4 }) {
            writeInt(0xa)
            writeByte(0)
            writeBlockWithIntLen({ it + 4 }) {
                writeBytes(EMPTY_BYTE_ARRAY)
            }
            writeByte(0)
            writeBlockWithIntLen({it + 4}) {
                writeString(if(ore.status() == OreStatus.Online) ore.uin.toString() else "0")
            }
            writeBlockWithIntLen({ it + 4 }) {
                writeInt(session.nextSeqId())
                writeInt(protocol.appId)
                writeInt(protocol.appId)
                writeInt(16777216)
                writeInt(0)
                writeInt(256)
                writeBlockWithIntLen({ it + 4 }) {
                    writeBytes(EMPTY_BYTE_ARRAY)
                }
                writeBlockWithIntLen({ it + 4 }) {
                    writeString("Heartbeat.Alive")
                }
                writeBlockWithIntLen({ it + 4 }) {
                    writeBytes(EMPTY_BYTE_ARRAY)
                }
                writeBlockWithIntLen({ it + 4 }) {
                    writeString(manager.deviceInfo.androidId.ifEmpty { manager.deviceInfo.imei })
                }
                writeBlockWithIntLen({ it + 4 }) {
                    writeBytes(manager.deviceInfo.ksid)
                }
                writeBlockWithShortLen({ it + 2 }) {
                    writeString(protocol.protocolDetail)
                }
                writeBlockWithIntLen({ it + 4 }) {
                    // qimei
                }
            }
            writeInt(4)
        }
    }.toByteArray()
}