package moe.ore.group

import moe.ore.api.Ore
import moe.ore.core.helper.DataManager
import moe.ore.core.util.QQUtil
import moe.ore.group.data.SimpleGroup
import moe.ore.util.JsonUtil
import moe.ore.util.OkhttpUtil

/**
 * webApi接口实现操作
 */
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
