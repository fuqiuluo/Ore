package moe.ore.group.tars

import moe.ore.group.FRIEND_LIST_SERVANT
import moe.ore.tars.TarsBase
import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField

@TarsClass(
    servantName = FRIEND_LIST_SERVANT,
    funcName = "GetTroopMemberListReq",
    reqName = "GTML"
)
data class GetTroopMemberListReq(
    @TarsField(0) var uin: Long = 0,

    @TarsField(1) var groupCode: Long = 0,

    @TarsField(2) var nextUin: Long = 0,

    @TarsField(3) var groupUin: Long = 0,

    @TarsField(4) var version: Long = 0,

    @TarsField(5) var reqType: Long = 0,

    @TarsField(6) var getListAppointTime: Long = 0,

    @TarsField(7) var richCardNameVersion: Byte = 0
): TarsBase()

@TarsClass(respName = "GTMLRESP")
data class GetTroopMemberListResp(
    @TarsField(0) var uin: Long = 0,

    @TarsField(1) var groupCode: Long = 0,

    @TarsField(2) var groupUin: Long = 0,

    @TarsField(3) var troopMember: ArrayList<TroopMemberInfo>? = null, //  max get 250

    @TarsField(4) var nextUin: Long = 0,

    @TarsField(5) var result: Int = 0,

    @TarsField(6) var errCode: Short = 0,

    @TarsField(7) var officeMode: Long = 0,

    @TarsField(8) var nextGetTime: Long = 0
): TarsBase()

