package moe.ore.group

import kotlinx.io.core.readBytes
import moe.ore.api.IPacketServlet
import moe.ore.api.Ore
import moe.ore.core.helper.DataManager
import moe.ore.group.request.GetMultiTroopInfo
import moe.ore.group.request.GetTroopList
import moe.ore.group.request.GetTroopMember
import moe.ore.group.tars.GetTroopListRespV2
import moe.ore.group.tars.TroopInfoV2
import moe.ore.group.tars.TroopMemberInfo
import moe.ore.helper.newBuilder
import moe.ore.helper.toByteArray
import moe.ore.helper.toByteReadPacket
import moe.ore.helper.writeBytesWithIntLen
import moe.ore.tars.TarsInputStream

const val TAG_TROOP_MANAGER = "TROOP_MANAGER"

const val FRIEND_LIST_SERVANT = "mqq.IMService.FriendListServiceServantObj"

class TroopManager(private val ore: Ore): IPacketServlet() {
    private val manager = DataManager.manager(ore.uin)
    private val diskCache = manager.diskCache

    /**
     * 获取群简略资料
     * ps：仅可获取已添加的群
     */
    fun getMultiTroopInfo(vararg groupCode: Long): Result<List<TroopInfoV2>> {
        GetMultiTroopInfo(ore).onSuccess {
            return Result.success(it.troopInfo!!)
        }.onFailure {
            return Result.failure(it)
        }
        return Result.failure(UnknownError())
    }

    /**
     * 获取群列表
     * cache 是否获取缓存内的数据
     */
    fun getTroopList(cache: Boolean = true): Result<GetTroopListRespV2> {
        val disketteCache = manager.diskCache.load("troop_list", 3 * 60 * 60)
        if (cache && !disketteCache.isExpired) return Result.success(disketteCache.getTars(GetTroopListRespV2()))
        GetTroopList(ore).onSuccess {
            disketteCache.put(it)
            return Result.success(it)
        }.onFailure {
            return Result.failure(it)
        }
        return Result.failure(UnknownError())
    }

    /**
     * 获取群成员列表
     */
    fun getTroopMemberList(groupCode: Long, cache: Boolean = true): Result<ArrayList<TroopMemberInfo>> {
        val disketteCache = manager.diskCache.load("troop_member_$groupCode", 3 * 60 * 60)
        if (cache && !disketteCache.isExpired) {
            return Result.success(disketteCache.get().let {
                val ret: ArrayList<TroopMemberInfo> = arrayListOf()
                val reader = it.toByteReadPacket()

                repeat(reader.readInt()) {
                    val size = reader.readInt()
                    val data = reader.readBytes(size)
                    ret.add(TroopMemberInfo().apply { readFrom(TarsInputStream(data)) })
                }

                return@let ret
            })
        }
        val ret: ArrayList<TroopMemberInfo> = arrayListOf()
        var nextUin = 0L
        while (true) {
            GetTroopMember(ore, groupCode, nextUin).onSuccess {
                if(it.result == 0) {
                    ret.addAll(it.troopMember!!)
                    // if(it.nextUin != 0L) getTroopMember(groupCode, nextUin, ret)
                    nextUin = it.nextUin
                    if(nextUin == 0L) {
                        disketteCache.put(newBuilder().also { builder ->
                            builder.writeInt(ret.size)
                            ret.forEachIndexed { _, any ->
                                builder.writeBytesWithIntLen(any.toByteArray())
                            }
                        }.toByteArray()) // save to cache

                        return Result.success(ret)
                    }
                } else {
                    return Result.failure(RuntimeException("replyCode is ${it.result}")) // 1 -> 没有添加这个群
                }
            }.onFailure {
                return Result.failure(it)
            }
        }
    }



}

fun Ore.troopManager(): TroopManager {
    return getServletOrPut(TAG_TROOP_MANAGER) { TroopManager(this) }
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