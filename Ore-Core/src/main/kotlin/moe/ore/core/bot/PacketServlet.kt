@file:Suppress("NOTHING_TO_INLINE")
package moe.ore.core.bot

import moe.ore.api.IPacketServlet
import moe.ore.api.Ore
import moe.ore.core.helper.DataManager
import moe.ore.core.helper.sendJcePacket
import moe.ore.core.helper.sendOidbPacket
import moe.ore.core.net.packet.PacketSender
import moe.ore.core.net.packet.PacketSender.Companion.sync
import moe.ore.protobuf.Protobuf
import moe.ore.tars.TarsBase
import moe.ore.util.TarsUtil

open class PacketServlet(val ore: Ore) : IPacketServlet {
    val manager = DataManager.manager(ore.uin)
    val session = manager.session

    inline fun <T: TarsBase> sendJce(commandName: String, src: T): PacketSender {
        return ore.sendJcePacket(commandName, src, session.nextRequestId())
    }

    inline fun <reified T: Protobuf<T>> sendOidb(commandName: String, commandId: Int, src: T, serviceId: Int = 0): PacketSender {
        return ore.sendOidbPacket(commandName, commandId, src, serviceId)
    }

    inline fun <T: TarsBase, R: TarsBase> sendJceAndParse(
        commandName: String,
        src: T,
        resp: R,
        listener: (isSuccess: Boolean, resp: R?, e: Throwable?) -> Unit
    ) {
        kotlin.runCatching {
            val sender = sendJce(commandName, src)
            val from = (sender sync 5000)!!
            return@runCatching TarsUtil.decodeRequest(resp, from.body)
        }.onSuccess {
            listener.invoke(true, it, null)
        }.onFailure {
            listener.invoke(false, null, it)
        }
    }





}