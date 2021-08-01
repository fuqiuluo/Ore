package moe.ore.group

import moe.ore.api.Ore
import moe.ore.core.bot.PacketServlet
import moe.ore.group.tars.GetMultiTroopInfoReq
import moe.ore.group.tars.GetMultiTroopInfoResp

const val FRIEND_LIST_SERVANT = "mqq.IMService.FriendListServiceServantObj"

class TroopManager(ore: Ore): PacketServlet(ore) {

    fun getTroopInfo(vararg groupCode: Long) {
        sendJceAndParse("friendlist.GetMultiTroopInfoReq", GetMultiTroopInfoReq().apply {
            this.uin = ore.uin
            groupCode.forEach {
                this.groupCode.add(it)
            }
            this.richInfo = 1
        }, GetMultiTroopInfoResp()) { isSuccess, resp, e ->
            resp!!.troopInfo?.forEach {
                println(it)
            }
        }
    }


}

fun Ore.troopManager() : TroopManager {
    return TroopManager(this)
}