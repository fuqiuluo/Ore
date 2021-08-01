package moe.ore.group.tars

import moe.ore.tars.TarsBase
import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField

@TarsClass(requireRead = true)
data class TroopInfoV2(
    @TarsField(0) var groupUin: Long = 0,

    @TarsField(1) var groupCode: Long = 0,

    @TarsField(2) var groupName: String = "",

    @TarsField(3) var groupNMemo: String = "",

    @TarsField(4) var groupOwnerUin: Long = 0,

    @TarsField(5) var groupClassExt: Long = 0,

    @TarsField(6) var groupFace: Int = 0,

    @TarsField(7) var fingerMemo: String? = null,

    @TarsField(8) var groupOption: Byte = 0,

    @TarsField(9) var memberNum: Int = 0,

    @TarsField(10) var groupFlagExt: Long = 0,

    @TarsField(11) var certificationType: Long = 0
): TarsBase()