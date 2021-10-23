package moe.ore.msg.request

import moe.ore.api.Ore
import moe.ore.core.bot.ContractJcePacketFactory
import moe.ore.msg.protocol.tars.DelMsgInfo
import moe.ore.msg.protocol.tars.MsgInfo
import moe.ore.msg.protocol.tars.SvcRespPushMsg
import moe.ore.tars.TarsBase

internal object PushResp: ContractJcePacketFactory<TarsBase>("OnlinePush.RespPush", false) {
    override fun handle(data: ByteArray, seq: Int) = error("push resp is not required")

    override fun request(uin: Long, args: Array<out Any>): TarsBase {
        val svrip = args[0] as Int
        val info = args[1] as MsgInfo
        return SvcRespPushMsg(
            uin, arrayListOf(DelMsgInfo(
                info.fromUin,
                info.msgTime,
                info.msgSeq,
                info.msgCookies,
                0, 0, 0, 0, 0, 0, 0,
            )), svrip
        )
    }

    operator fun invoke(ore: Ore, svrip: Int, msgInfo: MsgInfo) = super.invoke(ore, svrip, msgInfo)
}