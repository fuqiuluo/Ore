package moe.ore.group.tars

import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.tars.TarsBase
import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField

@TarsClass(requireWrite = true, requireRead = true)
data class TroopNumSimplify(
    @TarsField(0) var groupCode: Long = 0,

    @TarsField(1) var groupInfoSeq: Long = 0,

    @TarsField(2) var groupFlagExt: Long = 0,

    @TarsField(3) var groupRankSeq: Long = 0,

    @TarsField(4) var groupInfoExtSeq: Long = 0,
): TarsBase()

@TarsClass(requireWrite = true, requireRead = true)
data class TroopNum
@JvmOverloads
constructor(
    @TarsField(0) var groupUin: Long = 0,

    @TarsField(1) var groupCode: Long = 0,

    @TarsField(2) var flag: Byte = 0,

    @TarsField(3) var groupInfoSeq: Long = 0,

    @TarsField(4) var groupName: String? = null,

    @TarsField(5) var groupMemo: String? = null,

    @TarsField(6) var groupFlagExt: Long = 0,

    @TarsField(7) var groupRankSeq: Long = 0,

    @TarsField(8) var certificationType: Long = 0,

    @TarsField(9) var shutUpTimestamp: Long = 0,

    @TarsField(10) var myShutUpTimestamp: Long = 0,

    @TarsField(11) var cmdUinUinFlag: Long = 0,

    @TarsField(12) var additionalFlag: Long = 0,

    @TarsField(13) var groupTypeFlag: Long = 0,

    @TarsField(14) var groupSecType: Long = 0,

    @TarsField(15) var groupSecTypeInfo: Long = 0,

    @TarsField(16) var groupClassExt: Long = 0,

    @TarsField(17) var appPrivilegeFlag: Long = 0,

    @TarsField(18) var subScriptingUin: Long = 0,

    @TarsField(19) var memberNum: Long = 0,

    @TarsField(20) var memberNumSeq: Long = 0,

    @TarsField(21) var memberCardSeq: Long = 0,

    @TarsField(22) var groupFlagExt3: Long = 0,

    @TarsField(23) var groupOwnerUin: Long = 0,

    @TarsField(24) var isConfGroup: Byte = 0,

    @TarsField(25) var isModifyConfGroupFace: Byte = 0,

    @TarsField(26) var isModifyConfGroupName: Byte = 0,

    @TarsField(27) var cmdUinJoinTime: Long = 0,

    @TarsField(28) var companyId: Long = 0,

    @TarsField(29) var maxGroupMemberNum: Long = 0,

    @TarsField(30) var cmdUinGroupMask: Long = 0,

    @TarsField(31) var HLGuildAppId: Long = 0,

    @TarsField(32) var HLGuildSubType: Long = 0,

    @TarsField(33) var cmdUinRingtoneId: Long = 0,

    @TarsField(34) var cmdUinFlagEx2: Long = 0,

    @TarsField(35) var groupFlagExt4: Long = 0,

    @TarsField(36) var appealDeadline: Long = 0,

    @TarsField(37) var groupFlag: Long = 0,

    @TarsField(38) var groupRemark: ByteArray = EMPTY_BYTE_ARRAY
): TarsBase()

@TarsClass(requireWrite = true, requireRead = true)
data class FavoriteGroup(
    @TarsField(0) var groupCode: Long = 0,

    @TarsField(1) var timestamp: Long = 0,

    @TarsField(2) var snsFlag: Long = 0,

    @TarsField(3) var openTimestamp: Long = 0,
): TarsBase()