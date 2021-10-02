@file:Suppress("EXPERIMENTAL_API_USAGE", "PLUGIN_IS_NOT_ENABLED", "ArrayInDataClass")
package moe.ore.group.protobuf

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.protobuf.Protobuf

@Serializable
internal data class GroupMemberInfoReqBody(
    @ProtoNumber(1) @JvmField var groupCode: ULong = 0u,
    @ProtoNumber(2) @JvmField var uin: ULong = 0u,
    @ProtoNumber(3) @JvmField var newClient: Boolean = false,
    @ProtoNumber(4) @JvmField var clientType: UInt = 0u,
    @ProtoNumber(5) @JvmField var richCardNameVer: UInt = 0u,
): Protobuf<GroupMemberInfoReqBody>

@Serializable
internal data class GroupMemberInfoRspBody(
    @ProtoNumber(1) @JvmField var groupCode: ULong = 0u,
    @ProtoNumber(2) @JvmField var selfRole: UInt = 0u,
    @ProtoNumber(3) @JvmField var msgMemInfo: MemberInfo,
    @ProtoNumber(4) @JvmField var selfLocationShared: Boolean = false,
    @ProtoNumber(5) @JvmField var groupType: UInt = 0u,
): Protobuf<GroupMemberInfoRspBody>

@Serializable
data class MemberInfo(
    @ProtoNumber(1) @JvmField var uin: ULong = 0u,
    @ProtoNumber(2) @JvmField var result: UInt = 0u,
    @ProtoNumber(3) @JvmField var errMsg: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var isFriend: Boolean = false,
    @ProtoNumber(5) @JvmField var remark: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(6) @JvmField var isConcerned: Boolean = false,
    @ProtoNumber(7) @JvmField var credit: UInt = 0u,
    @ProtoNumber(8) @JvmField var card: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(9) @JvmField var sex: UInt = 0u,
    @ProtoNumber(10) @JvmField var location: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(11) @JvmField var nick: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(12) @JvmField var age: UInt = 0u,
    @ProtoNumber(13) @JvmField var troopLevel: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(14) @JvmField var joinTime: ULong = 0u,
    @ProtoNumber(15) @JvmField var lastSpeakTime: ULong = 0u,
    @ProtoNumber(16) @JvmField var msgCustomEntries: List<CustomEntry>? = null,
    @ProtoNumber(17) @JvmField var msgGbarConcerned: List<GBarInfo>? = null,
    @ProtoNumber(18) @JvmField var gbarTitle: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(19) @JvmField var gbarUrl: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(20) @JvmField var gbarCnt: UInt = 0u,
    @ProtoNumber(21) @JvmField var allowModCard: Boolean = false,
    @ProtoNumber(22) @JvmField var isVip: Boolean = false,
    @ProtoNumber(23) @JvmField var isYearVip: Boolean = false,
    @ProtoNumber(24) @JvmField var isSuperVip: Boolean = false,
    @ProtoNumber(25) @JvmField var isSuperQQ: Boolean = false,
    @ProtoNumber(26) @JvmField var vipLevel: UInt = 0u,
    @ProtoNumber(27) @JvmField var role: UInt = 0u,
    @ProtoNumber(28) @JvmField var locationShared: Boolean = false,
    @ProtoNumber(29) @JvmField var distance: ULong = 0u,
    @ProtoNumber(30) @JvmField var concernType: UInt = 0u,
    @ProtoNumber(31) @JvmField var specialTitle: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(32) @JvmField var specialTitleExpireTime: UInt = 0u,
    @ProtoNumber(33) @JvmField var msgFlowerEntry: FlowersEntry? = null,
    @ProtoNumber(34) @JvmField var msgTeamEntry: TeamEntry? = null,
    @ProtoNumber(35) @JvmField var phoneNum: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(36) @JvmField var job: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(37) @JvmField var medalId: UInt = 0u,
    @ProtoNumber(38) @JvmField var qqStoryInfoCard: RspGroupCardGetStory? = null,
    @ProtoNumber(39) @JvmField var level: UInt = 0u,
    @ProtoNumber(40) @JvmField var msgGameInfo: MemberGameInfo? = null,
    @ProtoNumber(41) @JvmField var groupHonor: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(42) @JvmField var groupHonorBit: UInt = 0u,
): Protobuf<MemberInfo>

@Serializable
data class FlowersEntry(
    @ProtoNumber(1) @JvmField var flowerCount: ULong = 0u,
): Protobuf<FlowersEntry>

@Serializable
data class TeamEntry(
    @ProtoNumber(1) @JvmField var depId: List<Long>? = null,
    @ProtoNumber(2) @JvmField var selfDepId: List<Long>? = null,
): Protobuf<TeamEntry>

@Serializable
data class RspGroupCardGetStory(
    @ProtoNumber(1) @JvmField var result: ErrorInfo? = null,
    @ProtoNumber(2) @JvmField var flag: UInt = 0u,
    @ProtoNumber(3) @JvmField var videoInfo: ArrayList<InfoCardVideoInfo>? = null,
): Protobuf<RspGroupCardGetStory>

@Serializable
data class ErrorInfo(
    @ProtoNumber(1) @JvmField var errorCode: UInt = 0u,
    @ProtoNumber(2) @JvmField var errorDesc: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<ErrorInfo>

@Serializable
data class InfoCardVideoInfo(
    @ProtoNumber(1) @JvmField var cover: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var vid: String = "",
    @ProtoNumber(3) @JvmField var feedId: String = "",
): Protobuf<InfoCardVideoInfo>

@Serializable
data class MemberGameInfo(
    @ProtoNumber(1) @JvmField var gameName: String = "",
    @ProtoNumber(2) @JvmField var levelName: String = "",
    @ProtoNumber(3) @JvmField var levelIcon: String = "",
    @ProtoNumber(4) @JvmField var gameFontColor: String = "",
    @ProtoNumber(5) @JvmField var gameBackgroundColor: String = "",
    @ProtoNumber(6) @JvmField var gameUrl: String = "",
    @ProtoNumber(7) @JvmField var descInfo: List<String>? = null,
): Protobuf<MemberGameInfo>

@Serializable
data class CustomEntry(
    @ProtoNumber(1) @JvmField var name: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var value: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(3) @JvmField var clicked: Boolean = false,
    @ProtoNumber(4) @JvmField var url: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(5) @JvmField var reportId: ULong = 0u,
): Protobuf<CustomEntry>

@Serializable
data class GBarInfo(
    @ProtoNumber(1) @JvmField var gbarId: UInt = 0u,
    @ProtoNumber(2) @JvmField var uinLevel: UInt = 0u,
    @ProtoNumber(3) @JvmField var headPortrait: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var gbarName: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<GBarInfo>