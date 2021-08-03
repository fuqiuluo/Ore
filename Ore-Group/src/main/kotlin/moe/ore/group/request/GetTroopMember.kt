package moe.ore.group.request

import moe.ore.api.Ore
import moe.ore.core.bot.ContractJcePacketFactory
import moe.ore.core.util.QQUtil
import moe.ore.group.tars.GetTroopMemberListReq
import moe.ore.group.tars.GetTroopMemberListResp

object GetTroopMember: ContractJcePacketFactory<GetTroopMemberListResp>("friendlist.getTroopMemberList") {
    override fun handle(data: ByteArray, seq: Int) = decodeUniPacket(data, GetTroopMemberListResp())

    override fun request(uin: Long, args: Array<out Any>) = GetTroopMemberListReq().apply {
        val groupCode = args[0] as Long
        val nextUin = args[1] as Long

        this.uin = uin
        this.version = 3
        this.groupCode = groupCode
        this.groupUin = QQUtil.groupCode2GroupUin(groupCode)
        this.nextUin = nextUin
        this.reqType = 1
        this.getListAppointTime = (System.currentTimeMillis() * 0.001).toLong()
        this.richCardNameVersion = 1
    }

    operator fun invoke(ore: Ore, groupCode: Long, nextUin: Long) = super.invoke(ore, groupCode, nextUin)
}