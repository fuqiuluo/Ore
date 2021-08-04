@file:Suppress("PLUGIN_IS_NOT_ENABLED", "EXPERIMENTAL_API_USAGE", "ArrayInDataClass")
package moe.ore.msg.protocol.protobuf

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.protobuf.Protobuf

@Serializable
internal data class Msg(
    @ProtoNumber(1) @JvmField var msgHead: MsgHead,
    @ProtoNumber(2) @JvmField var contentHead: ContentHead? = null, // 应用场景：一条消息用多个包的时候
    @ProtoNumber(3) @JvmField var msgBody: MsgBody? = null,
    @ProtoNumber(4) @JvmField var appShareInfo: AppShareInfo? = null,
): Protobuf<Msg>

@Serializable
internal data class MsgHead(
    @ProtoNumber(1) @JvmField var fromUin: ULong = 0u, // sender has
    @ProtoNumber(2) @JvmField var toUin: ULong = 0u, // bot uin has
    @ProtoNumber(3) @JvmField var msgType: UInt = 0u, // troop 82 has
    @ProtoNumber(4) @JvmField var c2cCmd: UInt = 0u,
    @ProtoNumber(5) @JvmField var msgSeq: ULong = 0u, // for reply message has
    @ProtoNumber(6) @JvmField var msgTime: ULong = 0u, // has
    @ProtoNumber(7) @JvmField var msgUid: ULong = 0u, // has
    @ProtoNumber(8) @JvmField var c2c_tmp_msg_head: C2CTmpMsgHead? = null,
    @ProtoNumber(9) @JvmField var groupInfo: GroupInfo? = null, // has
    @ProtoNumber(10) @JvmField var fromAppid: UInt = 0u, // 如果是电脑则为10001
    @ProtoNumber(11) @JvmField var fromInstId: UInt = 0u,
    @ProtoNumber(12) @JvmField var user_active: UInt = 0u,
    @ProtoNumber(13) @JvmField var discuss_info: DiscussInfo? = null,
    @ProtoNumber(14) @JvmField var from_nick: String = "",
    @ProtoNumber(15) @JvmField var auth_uin: ULong = 0u,
    @ProtoNumber(16) @JvmField var auth_nick: String = "",
    @ProtoNumber(17) @JvmField var msgFlag: UInt = 0u, // has
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
internal data class ExtGroupKeyInfo(
    @ProtoNumber(1) @JvmField var cur_max_seq: UInt = 0u,
    @ProtoNumber(2) @JvmField var cur_time: ULong = 0u,
    @ProtoNumber(3) @JvmField var operate_by_parents: UInt = 0u,
    @ProtoNumber(4) @JvmField var bytes_ext_group_info: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<ExtGroupKeyInfo>

@Serializable
internal data class MutilTransHead(
    @ProtoNumber(1) @JvmField var status: UInt = 0u,
    @ProtoNumber(2) @JvmField var msgId: UInt = 0u,
): Protobuf<MutilTransHead>

@Serializable
internal data class DiscussInfo(
    @ProtoNumber(1) @JvmField var discuss_uin: ULong = 0u,
    @ProtoNumber(2) @JvmField var discuss_type: UInt = 0u,
    @ProtoNumber(3) @JvmField var discuss_info_seq: ULong = 0u,
    @ProtoNumber(4) @JvmField var discuss_remark: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(5) @JvmField var discuss_name: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<DiscussInfo>

@Serializable
internal data class GroupInfo(
    @ProtoNumber(1) @JvmField var groupCode: ULong = 0u, // has
    @ProtoNumber(2) @JvmField var groupType: UInt = 0u, // has
    @ProtoNumber(3) @JvmField var groupInfoSeq: ULong = 0u, // has
    @ProtoNumber(4) @JvmField var groupCard: String = "",
    @ProtoNumber(5) @JvmField var groupRank: String = "",
    @ProtoNumber(6) @JvmField var groupLevel: UInt = 0u,
    @ProtoNumber(7) @JvmField var groupCardType: UInt = 0u,
    @ProtoNumber(8) @JvmField var groupName: String = "",
): Protobuf<GroupInfo>

@Serializable
internal data class C2CTmpMsgHead(
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
internal data class ContentHead(
    @ProtoNumber(1) @JvmField var pkg_num: UInt = 0u,
    @ProtoNumber(2) @JvmField var pkg_index: UInt = 0u,
    @ProtoNumber(3) @JvmField var div_seq: UInt = 0u,
    @ProtoNumber(4) @JvmField var auto_reply: UInt = 0u,
): Protobuf<ContentHead>

@Serializable
internal data class AppShareInfo(
    @ProtoNumber(1) @JvmField var appshare_id: UInt = 0u,
    @ProtoNumber(2) @JvmField var appshare_cookie: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(3) @JvmField var appshare_resource: PluginInfo? = null,
): Protobuf<AppShareInfo>

@Serializable
internal data class PluginInfo(
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