@file:Suppress("PLUGIN_IS_NOT_ENABLED", "EXPERIMENTAL_API_USAGE")
package moe.ore.msg.protocol.protobuf

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.protobuf.Protobuf

@Serializable
data class Msg(
    @ProtoNumber(1) @JvmField var msg_head: MsgHead? = null,
    @ProtoNumber(2) @JvmField var content_head: ContentHead? = null,
    @ProtoNumber(3) @JvmField var msg_body: MsgBody? = null,
    @ProtoNumber(4) @JvmField var appshare_info: AppShareInfo? = null,
): Protobuf<Msg>

@Serializable
data class MsgHead(
    @ProtoNumber(1) @JvmField var from_uin: ULong = 0u,
    @ProtoNumber(2) @JvmField var to_uin: ULong = 0u,
    @ProtoNumber(3) @JvmField var msg_type: UInt = 0u,
    @ProtoNumber(4) @JvmField var c2c_cmd: UInt = 0u,
    @ProtoNumber(5) @JvmField var msg_seq: ULong = 0u,
    @ProtoNumber(6) @JvmField var msg_time: ULong = 0u,
    @ProtoNumber(7) @JvmField var msg_uid: ULong = 0u,
    @ProtoNumber(8) @JvmField var c2c_tmp_msg_head: C2CTmpMsgHead? = null,
    @ProtoNumber(9) @JvmField var group_info: GroupInfo? = null,
    @ProtoNumber(10) @JvmField var from_appid: UInt = 0u,
    @ProtoNumber(11) @JvmField var from_instid: UInt = 0u,
    @ProtoNumber(12) @JvmField var user_active: UInt = 0u,
    @ProtoNumber(13) @JvmField var discuss_info: DiscussInfo? = null,
    @ProtoNumber(14) @JvmField var from_nick: String = "",
    @ProtoNumber(15) @JvmField var auth_uin: ULong = 0u,
    @ProtoNumber(16) @JvmField var auth_nick: String = "",
    @ProtoNumber(17) @JvmField var msg_flag: UInt = 0u,
    @ProtoNumber(18) @JvmField var auth_remark: String = "",
    @ProtoNumber(19) @JvmField var group_name: String = "",
    @ProtoNumber(20) @JvmField var mutiltrans_head: MutilTransHead? = null,
    @ProtoNumber(21) @JvmField var msg_inst_ctrl: InstCtrl? = null,
    @ProtoNumber(22) @JvmField var public_account_group_send_flag: UInt = 0u,
    @ProtoNumber(23) @JvmField var wseq_in_c2c_msghead: UInt = 0u,
    @ProtoNumber(24) @JvmField var cpid: ULong = 0u,
    @ProtoNumber(25) @JvmField var ext_group_key_info: ExtGroupKeyInfo? = null,
    @ProtoNumber(26) @JvmField var multi_compatible_text: String = "",
    @ProtoNumber(27) @JvmField var auth_sex: UInt = 0u,
    @ProtoNumber(28) @JvmField var is_src_msg: Boolean = false
): Protobuf<MsgHead>

@Serializable
data class ExtGroupKeyInfo(
    @ProtoNumber(1) @JvmField var cur_max_seq: UInt = 0u,
    @ProtoNumber(2) @JvmField var cur_time: ULong = 0u,
    @ProtoNumber(3) @JvmField var operate_by_parents: UInt = 0u,
    @ProtoNumber(4) @JvmField var bytes_ext_group_info: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<ExtGroupKeyInfo>

@Serializable
data class MutilTransHead(
    @ProtoNumber(1) @JvmField var status: UInt = 0u,
    @ProtoNumber(2) @JvmField var msgId: UInt = 0u,
): Protobuf<MutilTransHead>

@Serializable
data class DiscussInfo(
    @ProtoNumber(1) @JvmField var discuss_uin: ULong = 0u,
    @ProtoNumber(2) @JvmField var discuss_type: UInt = 0u,
    @ProtoNumber(3) @JvmField var discuss_info_seq: ULong = 0u,
    @ProtoNumber(4) @JvmField var discuss_remark: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(5) @JvmField var discuss_name: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<DiscussInfo>

@Serializable
data class GroupInfo(
    @ProtoNumber(1) @JvmField var group_code: ULong = 0u,
    @ProtoNumber(2) @JvmField var group_type: UInt = 0u,
    @ProtoNumber(3) @JvmField var group_info_seq: ULong = 0u,
    @ProtoNumber(4) @JvmField var group_card: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(5) @JvmField var group_rank: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(6) @JvmField var group_level: UInt = 0u,
    @ProtoNumber(7) @JvmField var group_card_type: UInt = 0u,
    @ProtoNumber(8) @JvmField var group_name: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<GroupInfo>

@Serializable
data class C2CTmpMsgHead(
    @ProtoNumber(1) @JvmField var c2c_type: UInt = 0u,
    @ProtoNumber(2) @JvmField var service_type: UInt = 0u,
    @ProtoNumber(3) @JvmField var group_uin: ULong = 0u,
    @ProtoNumber(4) @JvmField var group_code: ULong = 0u,
    @ProtoNumber(5) @JvmField var sig: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(6) @JvmField var sig_type: UInt = 0u,
    @ProtoNumber(7) @JvmField var from_phone: String = "",
    @ProtoNumber(8) @JvmField var to_phone: String = "",
    @ProtoNumber(9) @JvmField var lock_display: UInt = 0u,
    @ProtoNumber(10) @JvmField var direction_flag: UInt = 0u,
    @ProtoNumber(11) @JvmField var reserved: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<C2CTmpMsgHead>

@Serializable
data class ContentHead(
    @ProtoNumber(1) @JvmField var pkg_num: UInt = 0u,
    @ProtoNumber(2) @JvmField var pkg_index: UInt = 0u,
    @ProtoNumber(3) @JvmField var div_seq: UInt = 0u,
    @ProtoNumber(4) @JvmField var auto_reply: UInt = 0u,
): Protobuf<ContentHead>

@Serializable
data class AppShareInfo(
    @ProtoNumber(1) @JvmField var appshare_id: UInt = 0u,
    @ProtoNumber(2) @JvmField var appshare_cookie: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(3) @JvmField var appshare_resource: PluginInfo? = null,
): Protobuf<AppShareInfo>

@Serializable
data class PluginInfo(
    @ProtoNumber(1) @JvmField var res_id: UInt = 0u,
    @ProtoNumber(2) @JvmField var pkg_name: String = "",
    @ProtoNumber(3) @JvmField var new_ver: UInt = 0u,
    @ProtoNumber(4) @JvmField var res_type: UInt = 0u,
    @ProtoNumber(5) @JvmField var lan_type: UInt = 0u,
    @ProtoNumber(6) @JvmField var priority: UInt = 0u,
    @ProtoNumber(7) @JvmField var res_name: String = "",
    @ProtoNumber(8) @JvmField var res_desc: String = "",
    @ProtoNumber(9) @JvmField var res_url_big: String = "",
    @ProtoNumber(10) @JvmField var res_url_small: String = "",
    @ProtoNumber(11) @JvmField var res_conf: String = "",
): Protobuf<PluginInfo>