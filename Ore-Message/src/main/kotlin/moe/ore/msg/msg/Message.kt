@file:Suppress("EXPERIMENTAL_API_USAGE")

package moe.ore.msg.msg

import moe.ore.api.Ore
import moe.ore.core.helper.DataManager
import moe.ore.msg.cache.ImageCache
import moe.ore.msg.code.BaseCode
import moe.ore.msg.code.OreCode
import moe.ore.msg.protocol.protobuf.Grp
import moe.ore.msg.protocol.protobuf.PbSendMsgResp
import moe.ore.msg.protocol.protobuf.RoutingHead
import moe.ore.msg.request.SendMsg
import java.io.File

class MessageBuilder(private val ore: Ore): OreCode() {
    fun image(file: File): OreCode {
        ImageCache.saveTroopImage(ore.uin, file).onSuccess {
            image(it.name)
        }
        return this
    }

    fun addMsg(msg: String) {
        addAll(OreCode.parse(msg))
    }

    fun build(): MessageSender {
        return MessageSender(ore, this)
    }
}

class MessageSender(
    val ore: Ore,
    val msg: List<BaseCode>
) {
    private val manager = DataManager.manager(ore.uin)
    private val session = manager.session

    fun sendToTroop(troopCode: Long): Result<PbSendMsgResp> {
        val routingHead = RoutingHead(
            grp = Grp(troopCode.toULong())
        )
        return send(routingHead, MsgType.TROOP)
    }

    private fun send(routingHead: RoutingHead, msgType: MsgType): Result<PbSendMsgResp> {
        return SendMsg(
            ore,
            session.nextMsgSeq(),
            routingHead,
            MsgEncoder(ore, msgType, routingHead, msg)()
        )
    }
}

