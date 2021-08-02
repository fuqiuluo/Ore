package moe.ore.group

import moe.ore.api.Ore
import moe.ore.core.bot.PacketServlet
import moe.ore.group.tars.*
import moe.ore.helper.cache.DisketteCache
import moe.ore.tars.TarsInputStream

const val TAG_TROOP_MANAGER = "TroopManager"

const val FRIEND_LIST_SERVANT = "mqq.IMService.FriendListServiceServantObj"

class TroopManager(ore: Ore) : PacketServlet(ore) {
    /**
     * 获取群简略资料
     * ps：仅可获取已添加的群
     */
    fun getMultiTroopInfo(vararg groupCode: Long): Result<List<TroopInfoV2>> {
        // require(groupCode.isNotEmpty()) { "getMultiTroopInfo.groupCode size must more than 0" }
        // delete require
        sendJceAndParse("friendlist.GetMultiTroopInfoReq", GetMultiTroopInfoReq().apply {
            this.uin = ore.uin
            groupCode.forEach {
                this.groupCode.add(it)
            }
            this.richInfo = 1
        }, GetMultiTroopInfoResp()) { isSuccess, resp, error ->
            return if (isSuccess && resp != null) {
                if (resp.result == 0) {
                    Result.success(resp.troopInfo!!)
                } else Result.failure(RuntimeException("replyCode is ${resp.result}"))
            } else {
                Result.failure(error!!)
            }
        }
        return Result.failure(UnknownError())
    }

    /**
     * 获取群列表
     * cache 是否获取缓存内的数据
     */
    fun getTroopList(cache: Boolean = true): Result<GetTroopListRespV2> {
        val disketteCache = manager.disketteCache.build("troop_list", 3 * 60 * 60)
        val load = disketteCache.load()
        if (cache && load != null) {
            return Result.success(GetTroopListRespV2().apply { readFrom(TarsInputStream(load)) })
        }
        sendJceAndParse("friendlist.GetTroopListReqV2", GetTroopListReqV2Simplify().apply {
            this.uin = ore.uin
            this.groupFlagExt = 1
            this.version = 9
            this.versionNum = 1
            this.getLongGroupName = 1
        }, GetTroopListRespV2()) { isSuccess, resp, error ->
            return if (isSuccess && resp != null) {
                if (resp.result == 0) {
                    Result.success(resp.also { disketteCache.put(it.toByteArray()).close() })
                } else Result.failure(RuntimeException("replyCode is ${resp.result}"))
            } else {
                Result.failure(error!!)
            }
        }
        return Result.failure(UnknownError())
    }


}

fun Ore.troopManager(): TroopManager {
    return (this.servletMap.getOrPut(TAG_TROOP_MANAGER) { TroopManager(this) }) as TroopManager
}

/**
 * 无关紧要的实现
 *
 * webApi接口实现操作
class TroopManagerWeb(
/**
 * 账号
 *
 * 用来获取cookie
*/
val uin : Long
) {
private val session = DataManager.manager(uin).session
private val pskey = session.pSKeyMap["qun.qq.com"]!!["pskey"]
private val skey = session.sKey.ticket()

/**
 * 获取群列表
*/
fun getGroupList() : List<SimpleGroup> {
val http = OkhttpUtil()
http.defaultUserAgent()
http.header("referer", "https://qun.qq.com/member.html")
http.cookie("uin=o$uin; skey=$skey; p_uin=o$uin; p_skey=$pskey;")
val result = http.post("https://qun.qq.com/cgi-bin/qun_mgr/get_group_list", mapOf(
"bkn" to QQUtil.getBkn(skey)
))!!.body!!.string()
val json = JsonUtil.fromObject(result)
val ret = arrayListOf<SimpleGroup>()
if(json["ec"].asInt == 0) {
val create = json["create"].asJsonArray
val join = json["join"].asJsonArray
create.forEach {
val info = it.asJsonObject
ret.add(SimpleGroup(
groupCode = info["gc"].asLong,
groupName = info["gn"].asString,
owner = info["owner"].asLong
))
}
join.forEach {
val info = it.asJsonObject
ret.add(SimpleGroup(
groupCode = info["gc"].asLong,
groupName = info["gn"].asString,
owner = info["owner"].asLong
))
}
}
return ret
}




}

fun Ore.troopManagerWeb() : TroopManagerWeb = TroopManagerWeb(uin)
 */