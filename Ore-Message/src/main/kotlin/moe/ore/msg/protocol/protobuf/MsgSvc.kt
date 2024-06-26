@file:Suppress("PLUGIN_IS_NOT_ENABLED", "EXPERIMENTAL_API_USAGE", "ArrayInDataClass")
package moe.ore.msg.protocol.protobuf

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.protobuf.Protobuf

@Serializable
internal data class PbMsgReadedReportReq(
    @ProtoNumber(1) @JvmField var grpReadReport: List<PbGroupReadedReportReq>? = null,

    @ProtoNumber(2) @JvmField var disReadReport: ArrayList<PbDiscussReadedReportReq>? = null,

    @ProtoNumber(3) @JvmField var c2c_read_report: PbC2CReadedReportReq? = null,

    @ProtoNumber(4) @JvmField var bind_uin_read_report: PbBindUinMsgReadedConfirmReq? = null,
): Protobuf<PbMsgReadedReportReq>

@Serializable
internal data class PbMsgReadedReportResp(
    @ProtoNumber(1) @JvmField var grp_read_report: ArrayList<PbGroupReadedReportResp>? = null,
    @ProtoNumber(2) @JvmField var dis_read_report: ArrayList<PbDiscussReadedReportResp>? = null,
    @ProtoNumber(3) @JvmField var c2c_read_report: PbC2CReadedReportResp? = null,
    @ProtoNumber(4) @JvmField var bind_uin_read_report: PbBindUinMsgReadedConfirmResp? = null,
): Protobuf<PbMsgReadedReportResp>

@Serializable
internal data class PbBindUinMsgReadedConfirmResp(
    @ProtoNumber(1) @JvmField var result: UInt = 0u,
    @ProtoNumber(2) @JvmField var errmsg: String = "",
    @ProtoNumber(3) @JvmField var sync_cookie: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var bind_uin: ULong = 0u,
): Protobuf<PbBindUinMsgReadedConfirmResp>

@Serializable
internal data class PbC2CReadedReportResp(
    @ProtoNumber(1) @JvmField var result: UInt = 0u,
    @ProtoNumber(2) @JvmField var errmsg: String = "",
    @ProtoNumber(3) @JvmField var sync_cookie: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<PbC2CReadedReportResp>

@Serializable
internal data class PbDiscussReadedReportResp(
    @ProtoNumber(1) @JvmField var result: UInt = 0u,
    @ProtoNumber(2) @JvmField var errmsg: String = "",
    @ProtoNumber(3) @JvmField var conf_uin: ULong = 0u,
    @ProtoNumber(4) @JvmField var member_seq: ULong = 0u,
    @ProtoNumber(5) @JvmField var conf_seq: ULong = 0u,
): Protobuf<PbDiscussReadedReportResp>

@Serializable
internal data class PbGroupReadedReportResp(
    @ProtoNumber(1) @JvmField var result: UInt = 0u,
    @ProtoNumber(2) @JvmField var errmsg: String = "",
    @ProtoNumber(3) @JvmField var group_code: ULong = 0u,
    @ProtoNumber(4) @JvmField var member_seq: ULong = 0u,
    @ProtoNumber(5) @JvmField var group_msg_seq: ULong = 0u,
): Protobuf<PbGroupReadedReportResp>

@Serializable
internal data class PbC2CReadedReportReq(
    @ProtoNumber(1) @JvmField var sync_cookie: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var pair_info: ArrayList<UinPairReadInfo>? = null,
): Protobuf<PbC2CReadedReportReq>

@Serializable
internal data class UinPairReadInfo(
    @ProtoNumber(1) @JvmField var peer_uin: ULong = 0u,
    @ProtoNumber(2) @JvmField var last_read_time: ULong = 0u,
    @ProtoNumber(3) @JvmField var crm_sig: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var peer_type: Int = 0,
    @ProtoNumber(5) @JvmField var chat_type: UInt = 0u,
    @ProtoNumber(6) @JvmField var cpid: ULong = 0u,
    @ProtoNumber(7) @JvmField var aio_type: UInt = 0u,
    @ProtoNumber(9) @JvmField var uint64_to_tiny_id: ULong = 0u,
): Protobuf<UinPairReadInfo>

@Serializable
internal data class PbBindUinMsgReadedConfirmReq(
    @ProtoNumber(1) @JvmField var sync_cookie: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var bind_uin: ULong = 0u,
): Protobuf<PbBindUinMsgReadedConfirmReq>

@Serializable
internal data class PbDiscussReadedReportReq(
    @ProtoNumber(1) @JvmField var conf_uin: ULong = 0u,
    @ProtoNumber(2) @JvmField var last_read_seq: ULong = 0u,
): Protobuf<PbDiscussReadedReportReq>

@Serializable
internal data class PbGroupReadedReportReq(
    @ProtoNumber(1) @JvmField var groupCode: ULong = 0u,
    @ProtoNumber(2) @JvmField var lastReadSeq: ULong = 0u,
): Protobuf<PbGroupReadedReportReq>

@Serializable
data class PbSendMsgResp(
    @ProtoNumber(1) @JvmField var result: UInt = 0u,
    // @ProtoNumber(2) @JvmField var errMsg: String = "", 根本没有东西
    @ProtoNumber(3) @JvmField var sendTime: UInt = 0u,
    // @ProtoNumber(4) @JvmField var uint32_svrbusy_wait_time: UInt = 0u,
    // @ProtoNumber(5) @JvmField var msg_send_info: MsgSendInfo? = null,
    // @ProtoNumber(6) @JvmField var errtype: UInt = 0u,
    // @ProtoNumber(7) @JvmField var trans_svr_info: TransSvrInfo? = null,
    // @ProtoNumber(8) @JvmField var receipt_resp: ReceiptResp? = null,
    // @ProtoNumber(9) @JvmField var text_analysis_result: UInt = 0u,
    @ProtoNumber(10) @JvmField var msgInfoFlag: UInt = 0u,
): Protobuf<PbSendMsgResp>

@Serializable
internal class PbSendMsgReq(
    @ProtoNumber(1) @JvmField var routingHead: RoutingHead,
    @ProtoNumber(2) @JvmField var contentHead: ContentHead,
    @ProtoNumber(3) @JvmField var msgBody: MsgBody,
    @ProtoNumber(4) @JvmField var msgSeq: ULong = 0u,
    @ProtoNumber(5) @JvmField var msgRand: UInt = 0u,
    @ProtoNumber(6) @JvmField var sync_cookie: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(7) @JvmField var app_share: AppShareInfo? = null,
    @ProtoNumber(8) @JvmField var msgVia: UInt = 0u,
    @ProtoNumber(9) @JvmField var data_statist: UInt = 0u,
    @ProtoNumber(10) @JvmField var multi_msg_assist: MultiMsgAssist? = null,
    @ProtoNumber(11) @JvmField var input_notify_info: PbInputNotifyInfo? = null,
    @ProtoNumber(12) @JvmField var msgCtrl: MsgCtrl? = null,
    @ProtoNumber(13) @JvmField var receipt_req: ReceiptReq? = null,
    @ProtoNumber(14) @JvmField var multi_send_seq: UInt = 0u,
): Protobuf<PbSendMsgReq>

@Serializable
internal class PbInputNotifyInfo(
    @ProtoNumber(1) @JvmField var to_uin: ULong = 0u,
    @ProtoNumber(2) @JvmField var ime: UInt = 0u,
    @ProtoNumber(3) @JvmField var notify_flag: UInt = 0u,
    @ProtoNumber(4) @JvmField var bytes_pb_reserve: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(5) @JvmField var ios_push_wording: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<PbInputNotifyInfo>

@Serializable
internal class MultiMsgAssist(
    @ProtoNumber(1) @JvmField var repeated_routing: ArrayList<RoutingHead>? = null,
    @ProtoNumber(2) @JvmField var msg_use: Int = 0,
    @ProtoNumber(3) @JvmField var uint64_temp_id: ULong = 0u,
    @ProtoNumber(4) @JvmField var uint64_vedio_len: ULong = 0u,
    @ProtoNumber(5) @JvmField var bytes_redbag_id: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(6) @JvmField var uint64_redbag_amount: ULong = 0u,
    @ProtoNumber(7) @JvmField var uint32_has_readbag: UInt = 0u,
    @ProtoNumber(8) @JvmField var uint32_has_vedio: UInt = 0u,
): Protobuf<MultiMsgAssist>

@Serializable
internal class RoutingHead(
    @ProtoNumber(1) @JvmField var c2c: C2C? = null,
    @ProtoNumber(2) @JvmField var grp: Grp? = null,
    // @ProtoNumber(3) @JvmField var grp_tmp: GrpTmp? = null,
    @ProtoNumber(4) @JvmField var dis: Dis? = null,
    // @ProtoNumber(5) @JvmField var dis_tmp: DisTmp? = null,
    // @ProtoNumber(6) @JvmField var wpa_tmp: WPATmp? = null,
    // @ProtoNumber(7) @JvmField var secret_file: SecretFileHead? = null,
    // @ProtoNumber(8) @JvmField var public_plat: PublicPlat? = null,
    /*@ProtoNumber(9) @JvmField var trans_msg: TransMsg? = null,
    @ProtoNumber(10) @JvmField var address_list: AddressListTmp? = null,
    @ProtoNumber(11) @JvmField var rich_status_tmp: RichStatusTmp? = null,
    @ProtoNumber(12) @JvmField var trans_cmd: TransCmd? = null,
    @ProtoNumber(13) @JvmField var accost_tmp: AccostTmp? = null,
    @ProtoNumber(14) @JvmField var pub_group_tmp: PubGroupTmp? = null,
    @ProtoNumber(15) @JvmField var trans_0x211: Trans0x211? = null,
    @ProtoNumber(16) @JvmField var business_wpa_tmp: BusinessWPATmp? = null,
    @ProtoNumber(17) @JvmField var auth_tmp: AuthTmp? = null,
    @ProtoNumber(18) @JvmField var bsns_tmp: BsnsTmp? = null,
    @ProtoNumber(19) @JvmField var qq_querybusiness_tmp: QQQueryBusinessTmp? = null,
    @ProtoNumber(20) @JvmField var nearby_dating_tmp: NearByDatingTmp? = null,
    @ProtoNumber(21) @JvmField var nearby_assistant_tmp: NearByAssistantTmp? = null,
    @ProtoNumber(22) @JvmField var comm_tmp: CommTmp? = null,*/
): Protobuf<RoutingHead>

@Serializable
internal class C2C(
    @ProtoNumber(1) @JvmField var to_uin: ULong = 0u,
): Protobuf<C2C>

@Serializable
internal class Grp(
    @ProtoNumber(1) @JvmField var groupCode: ULong = 0u,
): Protobuf<Grp>

@Serializable
internal class Dis(
    @ProtoNumber(1) @JvmField var dis_uin: ULong = 0u,
): Protobuf<Dis>
