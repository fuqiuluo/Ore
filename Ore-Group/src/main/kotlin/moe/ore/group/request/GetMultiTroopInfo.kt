package moe.ore.group.request

import moe.ore.core.bot.ContractJcePacketFactory
import moe.ore.group.tars.GetMultiTroopInfoReq
import moe.ore.group.tars.GetMultiTroopInfoResp

internal object GetMultiTroopInfo: ContractJcePacketFactory<GetMultiTroopInfoResp>("friendlist.GetMultiTroopInfoReq") {
    override fun request(uin: Long, args: Array<out Any>) = GetMultiTroopInfoReq().apply {
        this.uin = uin
        groupCode.forEach {
            this.groupCode.add(it)
        }
        this.richInfo = 1
    }

    override fun handle(data: ByteArray, seq: Int) = decodeUniPacket(data, GetMultiTroopInfoResp())
}
