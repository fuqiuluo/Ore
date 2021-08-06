package moe.ore.group.request

import moe.ore.core.bot.ContractJcePacketFactory
import moe.ore.group.tars.GetTroopListReqV2Simplify
import moe.ore.group.tars.GetTroopListRespV2

object GetTroopList: ContractJcePacketFactory<GetTroopListRespV2>("friendlist.GetTroopListReqV2") {
    override fun handle(data: ByteArray, seq: Int) = decodeUniPacket(data, GetTroopListRespV2())

    override fun request(uin: Long, args: Array<out Any>) = GetTroopListReqV2Simplify().apply {
        this.uin = uin
        this.groupFlagExt = 1
        this.version = 9
        this.versionNum = 1
        this.getLongGroupName = 1
    }
}