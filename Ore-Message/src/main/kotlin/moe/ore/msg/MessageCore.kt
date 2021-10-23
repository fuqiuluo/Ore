package moe.ore.msg

import moe.ore.api.Ore
import moe.ore.core.OreBot
import moe.ore.core.helper.DataManager
import moe.ore.core.net.packet.FromService
import moe.ore.core.servlet.MSFServlet
import moe.ore.msg.cache.ImageCache
import moe.ore.msg.event.TroopEvent
import moe.ore.msg.event.TroopMsgEvent
import moe.ore.msg.helper.MsgHandleHelper
import moe.ore.msg.helper.PushHandleHelper
import moe.ore.msg.msg.MsgType.TROOP
import moe.ore.msg.protocol.protobuf.PbPushMsg
import moe.ore.msg.protocol.tars.SvcReqPushMsg
import moe.ore.msg.request.MsgReaded
import moe.ore.msg.request.PushResp
import moe.ore.protobuf.decodeProtobuf

const val TAG_MESSAGE_CENTER = "MESSAGE_CENTER"

class MessageCenter(
    private val ore: OreBot
): MSFServlet(arrayOf(
    "OnlinePush.PbPushGroupMsg", // 群消息
    "OnlinePush.ReqPush", // 群禁言通知
    // OnlinePush.PbPushGroupMsg || OnlinePush.PbPushDisMsg || OnlinePush.PbC2CMsgSync || OnlinePush.PbPushC2CMsg || OnlinePush.PbPushBindUinMsg
)) {
    private val config: CoreConfig = CoreConfig()
    private val manager = DataManager.manager(ore.uin)

    /**
     * 消息处理辅助器
     */
    private val msgHandlerHelper = MsgHandleHelper(ore)

    /**
     * 通知消息处理辅助器
     */
    private val pushHandleHelper = PushHandleHelper(ore, config)

    init {
        super.init(ore.client)
        ImageCache.init(ore.uin, manager.dataPath)
    }

    /** msg event **/
    var troopMsgEvent: TroopMsgEvent? = null
    set(value) {
        value?.ore = ore
        field = value
    }

    /** event **/
    var troopEvent: TroopEvent? = null
        set(value) {
            value?.ore = ore
            field = value
        }

    override fun onReceive(from: FromService) {
        when(from.commandName) {
            "OnlinePush.PbPushGroupMsg" -> {
                val pushMsg = decodeProtobuf<PbPushMsg>(from.body)
                val msg = pushMsg.msg
                val msgHead = msg.msgHead

                if(troopMsgEvent != null && msgHead.fromUin != ore.uin.toULong()) { // 自己的消息不处理
                    val troopInfo = msgHead.groupInfo!!
                    val codeMsg = msgHandlerHelper.toMsg(msg.msgBody.richText, TROOP)
                    if(codeMsg.isNotEmpty()) {
                        troopMsgEvent!!.onTroopMsg(
                            troopInfo.groupCode.toLong(),
                            msgHead.fromUin.toLong(),
                            troopInfo.groupName,
                            troopInfo.groupCard,
                            msgHead.msgTime.toLong(),
                            msgHead.msgSeq.toLong(),
                            codeMsg
                        )
                    }

                    if (config.autoReaded)
                        MsgReaded(ore, MsgReaded.ReportMode.GRP, listOf(MsgReaded.MsgReport(troopInfo.groupCode, msgHead.msgSeq)))
                }
            }
            "OnlinePush.ReqPush" -> {
                val push = decodePacket(from.body, "req", SvcReqPushMsg())
                push.msgInfos?.forEach { msgInfo ->
                    when(msgInfo.msgType) {
                        732 -> {
                            troopEvent?.let { pushHandleHelper.push732(it, msgInfo.vMsg) }
                        }

                    }
                    PushResp(ore, push.svrip, msgInfo)
                }


            }
        }
    }
}

fun Ore.messageCenter(): MessageCenter {
    return getServletOrPut(TAG_MESSAGE_CENTER) { MessageCenter(this as OreBot) }
}

