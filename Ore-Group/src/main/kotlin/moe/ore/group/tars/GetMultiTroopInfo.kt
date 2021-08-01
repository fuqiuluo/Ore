package moe.ore.group.tars

import moe.ore.group.FRIEND_LIST_SERVANT
import moe.ore.tars.TarsBase
import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField

@TarsClass(
    requireWrite = true,
    servantName = FRIEND_LIST_SERVANT,
    funcName = "GetMultiTroopInfoReq",
    reqName = "GMTIREQ"
)
class GetMultiTroopInfoReq: TarsBase() {
    @TarsField(0) var uin: Long = 0

    @TarsField(1) var groupCode: ArrayList<Long> = arrayListOf()

    @TarsField(2) var richInfo: Byte = 0
}

@TarsClass(
    requireRead = true,
    respName = "GMTIRESP"
)
class GetMultiTroopInfoResp: TarsBase() {
    @TarsField(0, true) var uin: Long = 0

    @TarsField(1, true) var result: Int = 0

    @TarsField(2, true) var errCode: Int = 0

    @TarsField(3, true) var troopInfo: ArrayList<TroopInfoV2>? = null

    @TarsField(4) var groupClassXmlPath: String = ""
}

