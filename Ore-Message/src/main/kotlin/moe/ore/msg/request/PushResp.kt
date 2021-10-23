package moe.ore.msg.request

import moe.ore.api.Ore
import moe.ore.core.helper.sendPacket
import moe.ore.msg.protocol.tars.DelMsgInfo
import moe.ore.msg.protocol.tars.MsgInfo
import moe.ore.msg.protocol.tars.SvcRespPushMsg
import moe.ore.util.TarsUtil

internal object PushResp {
    operator fun invoke(ore: Ore, seq: Int, rid: Int, svrip: Int, info: MsgInfo) {
        val uni = TarsUtil.getUni(3)
        uni.requestId = rid
        uni.put(SvcRespPushMsg(
            ore.uin, arrayListOf(DelMsgInfo(
                info.fromUin,
                info.msgTime,
                info.msgSeq,
                info.msgCookies,
                0, 0, 0, 0, 0, 0, 0,
            )), svrip
        ))
        ore.sendPacket("OnlinePush.RespPush", uni.encode(), seq = seq).call()
    }
}