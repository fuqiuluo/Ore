package moe.ore.msg

import moe.ore.api.IPacketServlet
import moe.ore.api.Ore
import moe.ore.core.OreBot
import moe.ore.core.net.packet.FromService
import moe.ore.core.net.packet.LongHandler
import moe.ore.msg.event.TroopMsgEvent
import moe.ore.msg.msg.toMsg
import moe.ore.msg.protocol.protobuf.PbPushMsg
import moe.ore.protobuf.decodeProtobuf


const val TAG_MESSAGE_CENTER = "MESSAGE_CENTER"

class MessageCenter(
    private val ore: OreBot
): IPacketServlet() {
    private val config: CoreConfig = CoreConfig()

    private lateinit var troopMsgEvent: TroopMsgEvent

    init {
        // OnlinePush.PbPushGroupMsg || OnlinePush.PbPushDisMsg || OnlinePush.PbC2CMsgSync || OnlinePush.PbPushC2CMsg || OnlinePush.PbPushBindUinMsg
        ore.client.registerSpecialHandler(object: LongHandler("OnlinePush.PbPushGroupMsg") {
            override fun handle(from: FromService) {
                val pushMsg = decodeProtobuf<PbPushMsg>(from.body)

                val msg = pushMsg.msg
                val msgHead = msg.msgHead

                if(this@MessageCenter::troopMsgEvent.isInitialized) {
                    msg.msgBody.richText.toMsg().let {
                        if(it.isEmpty()) return@let
                        val troopInfo = msgHead.groupInfo!!

                        troopMsgEvent.onTroopMsg(
                            troopInfo.groupCode.toLong(),
                            msgHead.fromUin.toLong(),
                            troopInfo.groupName,
                            troopInfo.groupCard,
                            msgHead.msgTime.toLong(),
                            msgHead.msgSeq.toInt(),
                            it
                        )
                    }
                }

            }
        })
    }

    fun setTroopMsgEvent(event: TroopMsgEvent) {
        this.troopMsgEvent = event
    }

}

fun Ore.messageCenter(): MessageCenter {
    return getServletOrPut(TAG_MESSAGE_CENTER) { MessageCenter(this as OreBot) }
}

