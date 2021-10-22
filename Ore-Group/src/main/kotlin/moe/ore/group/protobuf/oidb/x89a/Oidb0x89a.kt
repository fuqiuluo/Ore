@file:Suppress("EXPERIMENTAL_API_USAGE", "PLUGIN_IS_NOT_ENABLED", "ArrayInDataClass", "INAPPLICABLE_JVM_FIELD_WARNING")
package moe.ore.group.protobuf.oidb.x89a

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.protobuf.Protobuf

@Serializable
internal data class GroupNewGuidelinesInfo(
    @ProtoNumber(1) @JvmField var bool_enabled: Boolean = false,
    @ProtoNumber(2) @JvmField var string_content: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<GroupNewGuidelinesInfo>

@Serializable
internal data class GroupGeoInfo(
    @ProtoNumber(1) @JvmField var uint32_city_id: UInt = 0u,
    @ProtoNumber(2) @JvmField var uint64_longtitude: ULong = 0u,
    @ProtoNumber(3) @JvmField var uint64_latitude: ULong = 0u,
    @ProtoNumber(4) @JvmField var string_geo_content: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(5) @JvmField var uint64_poi_id: ULong = 0u,
): Protobuf<GroupGeoInfo>

@Serializable
internal data class GroupExInfoOnly(
    @ProtoNumber(1) @JvmField var uint32_tribe_id: UInt = 0u,
    @ProtoNumber(2) @JvmField var uint32_money_for_add_group: UInt = 0u,
): Protobuf<GroupExInfoOnly>

@Serializable
internal data class GroupCardPrefix(
    @ProtoNumber(1) @JvmField var bytes_introduction: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var rpt_bytes_prefix: List<ByteArray>? = null,
): Protobuf<GroupCardPrefix>

@Serializable
internal data class GroupInfo(
    @ProtoNumber(1) @JvmField var uint32_group_ext_adm_num: UInt = 0u,
    @ProtoNumber(2) @JvmField var uint32_flag: UInt = 0u,
    @ProtoNumber(3) @JvmField var string_group_name: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var string_group_memo: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(5) @JvmField var string_group_finger_memo: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(6) @JvmField var string_group_aio_skin_url: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(7) @JvmField var string_group_board_skin_url: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(8) @JvmField var string_group_cover_skin_url: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(9) @JvmField var uint32_group_grade: UInt = 0u,
    @ProtoNumber(10) @JvmField var uint32_active_member_num: UInt = 0u,
    @ProtoNumber(11) @JvmField var uint32_certification_type: UInt = 0u,
    @ProtoNumber(12) @JvmField var string_certification_text: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(13) @JvmField var string_group_rich_finger_memo: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(14) @JvmField var st_group_newguidelines: GroupNewGuidelinesInfo? = null,
    @ProtoNumber(15) @JvmField var uint32_group_face: UInt = 0u,
    @ProtoNumber(16) @JvmField var uint32_add_option: UInt = 0u,
    @ProtoNumber(17) @JvmField var shutUpTime: Int = 0,
    @ProtoNumber(18) @JvmField var uint32_group_type_flag: UInt = 0u,
    @ProtoNumber(19) @JvmField var rpt_string_group_tag: List<ByteArray>? = null,
    @ProtoNumber(20) @JvmField var msg_group_geo_info: GroupGeoInfo? = null,
    @ProtoNumber(21) @JvmField var uint32_group_class_ext: UInt = 0u,
    @ProtoNumber(22) @JvmField var string_group_class_text: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(23) @JvmField var uint32_app_privilege_flag: UInt = 0u,
    @ProtoNumber(24) @JvmField var uint32_app_privilege_mask: UInt = 0u,
    @ProtoNumber(25) @JvmField var st_group_ex_info: GroupExInfoOnly? = null,
    @ProtoNumber(26) @JvmField var uint32_group_sec_level: UInt = 0u,
    @ProtoNumber(27) @JvmField var uint32_group_sec_level_info: UInt = 0u,
    @ProtoNumber(28) @JvmField var uint64_subscription_uin: ULong = 0u,
    @ProtoNumber(29) @JvmField var uint32_allow_member_invite: UInt = 0u,
    @ProtoNumber(30) @JvmField var string_group_question: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(31) @JvmField var string_group_answer: ByteArray = EMPTY_BYTE_ARRAY,

    @ProtoNumber(32) @JvmField var uint32_group_flagext3: UInt = 0u,
    @ProtoNumber(33) @JvmField var uint32_group_flagext3_mask: UInt = 0u,

    @ProtoNumber(34) @JvmField var uint32_group_open_appid: UInt = 0u,
    @ProtoNumber(35) @JvmField var uint32_no_finger_open_flag: UInt = 0u,
    @ProtoNumber(36) @JvmField var uint32_no_code_finger_open_flag: UInt = 0u,
    @ProtoNumber(37) @JvmField var uint64_root_id: ULong = 0u,
    @ProtoNumber(38) @JvmField var uint32_msg_limit_frequency: UInt = 0u,
    @ProtoNumber(39) @JvmField var uint32_hl_guild_appid: UInt = 0u,
    @ProtoNumber(40) @JvmField var uint32_hl_guild_sub_type: UInt = 0u,
    @ProtoNumber(41) @JvmField var uint32_hl_guild_orgid: UInt = 0u,
    @ProtoNumber(42) @JvmField var uint32_group_flagext4: UInt = 0u,
    @ProtoNumber(43) @JvmField var uint32_group_flagext4_mask: UInt = 0u,
    @ProtoNumber(44) @JvmField var bytes_group_school_info: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(45) @JvmField var st_group_card_prefix: GroupCardPrefix? = null,
): Protobuf<GroupInfo>

@Serializable
internal data class ReqBody(
    @ProtoNumber(1) @JvmField var groupCode: Long = 0,
    @ProtoNumber(2) @JvmField var groupInfo: GroupInfo? = null,
    @ProtoNumber(3) @JvmField var uint64_original_operator_uin: ULong = 0u,
    @ProtoNumber(4) @JvmField var uint32_req_group_open_appid: UInt = 0u,
): Protobuf<ReqBody>

@Serializable
internal data class RspBody(
    @ProtoNumber(1) @JvmField var uint64_group_code: ULong = 0u,
    @ProtoNumber(2) @JvmField var str_errorinfo: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<RspBody>
