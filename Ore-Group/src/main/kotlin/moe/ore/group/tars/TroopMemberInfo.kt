package moe.ore.group.tars

import moe.ore.tars.TarsBase
import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField

@TarsClass
data class TroopMemberInfo(
    @TarsField(0) var memberUin: Long = 0,

    @TarsField(1) var faceId: Short = 0,

    @TarsField(2) var age: Byte = 0,

    @TarsField(3) var gender: Byte = 0,

    @TarsField(4) var nick: String? = null,

    @TarsField(5) var status: Byte = 0,

    @TarsField(6) var showMame: String? = null,

    @TarsField(8) var name: String? = null,

    @TarsField(9) var gender2: Byte = 0,

    @TarsField(10) var phone: String? = null,

    @TarsField(11) var email: String? = null,

    @TarsField(12) var memo: String? = null,

    @TarsField(13) var autoRemark: String? = null,

    @TarsField(14) var memberLevel: Long = 0,

    @TarsField(15) var joinTime: Long = 0,

    @TarsField(16) var lastSpeakTime: Long = 0,

    @TarsField(17) var creditTime: Long = 0,

    @TarsField(18) var flag: Long = 0,

    @TarsField(19) var flagExt: Long = 0,

    @TarsField(20) var point: Long = 0,

    @TarsField(21) var concerned: Byte = 0,

    @TarsField(22) var shielded: Byte = 0,

    @TarsField(23) var specialTitle: String? = null,

    @TarsField(24) var specialTitleExpireTime: Long = 0,

    @TarsField(25) var job: String? = null,

    @TarsField(26) var apolloFlag: Byte = 0,

    @TarsField(27) var apolloTimestamp: Long = 0,

    @TarsField(28) var globalGroupLevel: Long = 0,

    @TarsField(29) var titleId: Long = 0,

    @TarsField(30) var shutUpTimestamp: Long = 0,

    @TarsField(31) var globalGroupPoint: Long = 0,

    @TarsField(32) var qzUserInfo: QzoneUserInfo? = null,

    @TarsField(33) var richCardNameVer: Byte = 0,

    @TarsField(34) var vipType: Long = 0,

    @TarsField(35) var vipLevel: Long = 0,

    @TarsField(36) var bigClubLevel: Long = 0,

    @TarsField(37) var bigClubFlag: Long = 0,

    @TarsField(38) var nameplate: Long = 0,

    @TarsField(39) var groupHonor: ByteArray? = null,
): TarsBase()

@TarsClass
data class QzoneUserInfo(
    @TarsField(0) var starState: Int = 0,
    @TarsField(1) var extendInfo: Map<String, String>? = null,
): TarsBase()
