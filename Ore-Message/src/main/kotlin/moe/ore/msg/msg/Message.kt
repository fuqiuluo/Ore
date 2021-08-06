@file:Suppress("EXPERIMENTAL_API_USAGE")

package moe.ore.msg.msg

import kotlinx.io.core.discardExact
import kotlinx.io.core.readUInt
import moe.ore.api.Ore
import moe.ore.core.helper.DataManager
import moe.ore.helper.ifNotNull
import moe.ore.helper.toByteReadPacket
import moe.ore.msg.code.*
import moe.ore.msg.protocol.protobuf.Grp
import moe.ore.msg.protocol.protobuf.PbSendMsgResp
import moe.ore.msg.protocol.protobuf.RichText
import moe.ore.msg.protocol.protobuf.RoutingHead
import moe.ore.msg.request.SendMsg

class MessageBuilder(private val ore: Ore): OreCode() {
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

internal fun RichText.toMsg(): String {
    val builder = OreCode()
    if(ptt != null) {
        TODO("voice message not support")
    }
    for (elem in this.elems!!) {
        elem.text.ifNotNull {
            if(it.attr6Buf.isNotEmpty()) {
                it.attr6Buf.toByteReadPacket().use { pat ->
                    pat.discardExact(2) // version
                    pat.discardExact(2) // startPos
                    pat.discardExact(2) // textLen
                    pat.discardExact(1) // flag
                    val uin = pat.readUInt().toLong()
                    // pat.discardExact(2) // 0
                    builder.add(At(uin))
                }
            } else {
                builder.add(Text(it.str))
            }
        }
        elem.face.ifNotNull {
            builder.add(Face(it.index.toInt()))
        }


    }
    return builder.toString()
}