package moe.ore.msg

import moe.ore.api.IPacketServlet
import moe.ore.api.Ore
import moe.ore.core.OreBot
import moe.ore.core.net.packet.FromService
import moe.ore.core.net.packet.LongHandler

const val TAG_MESSAGE_CENTER = "MESSAGE_CENTER"

class MessageCenter(
    private val ore: OreBot
): IPacketServlet() {
    init {
        // OnlinePush.PbPushGroupMsg || OnlinePush.PbPushDisMsg || OnlinePush.PbC2CMsgSync || OnlinePush.PbPushC2CMsg || OnlinePush.PbPushBindUinMsg
        ore.client.registerSpecialHandler(object: LongHandler("OnlinePush.PbPushGroupMsg") {
            override fun handle(from: FromService) {


            }
        })
    }



}

fun Ore.messageCenter(): MessageCenter {
    return getServletOrPut(TAG_MESSAGE_CENTER) { MessageCenter(this as OreBot) }
}