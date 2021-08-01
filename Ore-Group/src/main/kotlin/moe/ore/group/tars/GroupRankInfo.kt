package moe.ore.group.tars

import moe.ore.tars.TarsBase
import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField

@TarsClass(requireRead = true, requireWrite = true)
data class GroupRankInfo(
    @TarsField(0) var groupCode: Long = 0,

    @TarsField(1) var groupRankSysFlag: Byte = 0,

    @TarsField(2) var groupRankUserFlag: Byte = 0,

    @TarsField(3) var rankMap: ArrayList<LevelRankPair>? = null,

    @TarsField(4) var groupRankSeq: Long = 0,

    @TarsField(5) var ownerName: String = "",

    @TarsField(6) var adminName: String = "",

    @TarsField(7) var officeMode: Long = 0,

    @TarsField(8) var groupRankUserFlagNew: Long = 0,

    @TarsField(9) var rankMapNew: ArrayList<LevelRankPair>? = null,
): TarsBase()

@TarsClass(requireRead = true, requireWrite = true)
data class LevelRankPair(
    @TarsField(0) var level: Long = 0,

    @TarsField(1) var rank: String = ""
): TarsBase()