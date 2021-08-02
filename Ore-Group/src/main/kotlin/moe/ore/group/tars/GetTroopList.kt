package moe.ore.group.tars

import moe.ore.group.FRIEND_LIST_SERVANT
import moe.ore.tars.TarsBase
import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField

@TarsClass(
    servantName = FRIEND_LIST_SERVANT,
    funcName = "GetTroopListReqV2Simplify",
    reqName = "GetTroopListReqV2Simplify"
)
class GetTroopListReqV2Simplify: TarsBase() {
    @TarsField(0) var uin: Long = 0 // qq

    @TarsField(1) var getMsfMsgFlag: Byte = 0 // 0

    @TarsField(2) var cookie: ByteArray? = null

    @TarsField(3) var troopNum: ArrayList<TroopNumSimplify> = arrayListOf()

    @TarsField(4) var groupFlagExt: Byte = 0 // 1

    @TarsField(5) var version: Int = 0 // 9

    @TarsField(6) var companyId: Long = 0 // 0

    @TarsField(7) var versionNum: Long = 0 // 1

    @TarsField(8) var getLongGroupName: Byte = 0 // 1
}

@TarsClass(
    respName = "GetTroopListRespV2"
)
data class GetTroopListRespV2(
    @TarsField(0) var uin: Long = 0,

    @TarsField(1) var troopCount: Short = 0,

    @TarsField(2) var result: Int = 0,

    @TarsField(3) var errCode: Int = 0,

    @TarsField(4) var cookie: ByteArray? = null,

    @TarsField(5) var troopList: ArrayList<TroopNum>?= null,

    @TarsField(6) var troopListDel: ArrayList<TroopNum>? = null,

    @TarsField(7) var troopRank: ArrayList<GroupRankInfo>? = null,

    @TarsField(8) var favoriteTroop: ArrayList<FavoriteGroup>? = null,

    @TarsField(9) var troopListExt: ArrayList<TroopNum>? = null,

    @TarsField(10) var groupInfoExt: ArrayList<Long>? = null
): TarsBase()