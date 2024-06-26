@file:Suppress("PLUGIN_IS_NOT_ENABLED", "EXPERIMENTAL_API_USAGE", "ArrayInDataClass")
package moe.ore.msg.protocol.protobuf

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.protobuf.Protobuf

@Serializable
internal data class MsgBody(
    @ProtoNumber(1) @JvmField var richText: RichText,
    @ProtoNumber(2) @JvmField var msgContent: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(3) @JvmField var msgEncryptContent: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<MsgBody>

@Serializable
internal data class RichText(
    @ProtoNumber(1) @JvmField var attr: Attr? = null,
    @ProtoNumber(2) @JvmField var elems: ArrayList<Elem>? = null,
    @ProtoNumber(3) @JvmField var not_online_file: NotOnlineFile? = null,
    @ProtoNumber(4) @JvmField var ptt: Ptt? = null,
    @ProtoNumber(5) @JvmField var tmp_ptt: TmpPtt? = null,
    @ProtoNumber(6) @JvmField var trans_211_tmp_msg: Trans211TmpMsg? = null,
): Protobuf<RichText>

@Serializable
internal data class Trans211TmpMsg(
    @ProtoNumber(1) @JvmField var bytes_msg_body: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var uint32_c2c_cmd: UInt = 0u,
): Protobuf<Trans211TmpMsg>

@Serializable
internal data class Ptt(
    @ProtoNumber(1) @JvmField var uint32_file_type: UInt = 0u,
    @ProtoNumber(2) @JvmField var uint64_src_uin: ULong = 0u,
    @ProtoNumber(3) @JvmField var bytes_file_uuid: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var bytes_file_md5: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(5) @JvmField var bytes_file_name: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(6) @JvmField var uint32_file_size: UInt = 0u,
    @ProtoNumber(7) @JvmField var bytes_reserve: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(8) @JvmField var uint32_file_id: UInt = 0u,
    @ProtoNumber(9) @JvmField var uint32_server_ip: UInt = 0u,
    @ProtoNumber(10) @JvmField var uint32_server_port: UInt = 0u,
    @ProtoNumber(11) @JvmField var bool_valid: Boolean = false,
    @ProtoNumber(12) @JvmField var bytes_signature: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(13) @JvmField var bytes_shortcut: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(14) @JvmField var bytes_file_key: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(15) @JvmField var uint32_magic_ptt_index: UInt = 0u,
    @ProtoNumber(16) @JvmField var uint32_voice_switch: UInt = 0u,
    @ProtoNumber(17) @JvmField var bytes_ptt_url: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(18) @JvmField var bytes_group_file_key: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(19) @JvmField var uint32_time: UInt = 0u,
    @ProtoNumber(20) @JvmField var bytes_down_para: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(29) @JvmField var uint32_format: UInt = 0u,
    @ProtoNumber(30) @JvmField var bytes_pb_reserve: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(31) @JvmField var rpt_bytes_ptt_urls: List<String>? = null,
    @ProtoNumber(32) @JvmField var uint32_download_flag: UInt = 0u,
): Protobuf<Ptt>

@Serializable
internal data class TmpPtt(
    @ProtoNumber(1) @JvmField var uint32_file_type: UInt = 0u,
    @ProtoNumber(2) @JvmField var bytes_file_uuid: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(3) @JvmField var bytes_file_md5: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var bytes_file_name: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(5) @JvmField var uint32_file_size: UInt = 0u,
    @ProtoNumber(6) @JvmField var uint64_ptt_times: UInt = 0u,
    @ProtoNumber(7) @JvmField var uint32_user_type: UInt = 0u,
    @ProtoNumber(8) @JvmField var uint32_ptttrans_flag: UInt = 0u,
    @ProtoNumber(9) @JvmField var uint32_busi_type: UInt = 0u,
    @ProtoNumber(10) @JvmField var uint64_msg_id: ULong = 0u,
    @ProtoNumber(30) @JvmField var bytes_pb_reserve: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(31) @JvmField var ptt_encode_data: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<TmpPtt>

@Serializable
internal data class NotOnlineFile(
    @ProtoNumber(1) @JvmField var uint32_file_type: UInt = 0u,
    @ProtoNumber(2) @JvmField var bytes_sig: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(3) @JvmField var bytes_file_uuid: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var bytes_file_md5: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(5) @JvmField var bytes_file_name: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(6) @JvmField var uint64_file_size: ULong = 0u,
    @ProtoNumber(7) @JvmField var bytes_note: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(8) @JvmField var uint32_reserved: UInt = 0u,
    @ProtoNumber(9) @JvmField var uint32_subcmd: UInt = 0u,
    @ProtoNumber(10) @JvmField var uint32_micro_cloud: UInt = 0u,
    @ProtoNumber(11) @JvmField var rpt_bytes_file_urls: List<String>? = null,
    @ProtoNumber(12) @JvmField var uint32_download_flag: UInt = 0u,
    @ProtoNumber(50) @JvmField var uint32_danger_evel: UInt = 0u,
    @ProtoNumber(51) @JvmField var uint32_life_time: UInt = 0u,
    @ProtoNumber(52) @JvmField var uint32_upload_time: UInt = 0u,
    @ProtoNumber(53) @JvmField var uint32_abs_file_type: UInt = 0u,
    @ProtoNumber(54) @JvmField var uint32_client_type: UInt = 0u,
    @ProtoNumber(55) @JvmField var uint32_expire_time: UInt = 0u,
    @ProtoNumber(56) @JvmField var bytes_pb_reserve: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(57) @JvmField var str_fileidcrc_media: String = "",
): Protobuf<NotOnlineFile>

@Serializable
internal data class Elem(
    @ProtoNumber(1) @JvmField var text: TextMsg? = null,
    @ProtoNumber(2) @JvmField var face: FaceMsg? = null,
    @ProtoNumber(3) @JvmField var online_image: OnlineImage? = null,
    @ProtoNumber(4) @JvmField var not_online_image: NotOnlineImage? = null,
    @ProtoNumber(5) @JvmField var trans_elem_info: TransElem? = null,
    @ProtoNumber(6) @JvmField var market_face: MarketFace? = null,
    @ProtoNumber(7) @JvmField var elem_flags: ElemFlags? = null,
    @ProtoNumber(8) @JvmField var customFace: CustomFace? = null,
    @ProtoNumber(9) @JvmField var elem_flags2: ElemFlags2? = null,
    @ProtoNumber(10) @JvmField var fun_face: FunFace? = null,
    @ProtoNumber(11) @JvmField var secret_file: SecretFileMsg? = null,
    @ProtoNumber(12) @JvmField var rich_msg: RichMsg? = null,
    @ProtoNumber(13) @JvmField var group_file: GroupFile? = null,
    @ProtoNumber(14) @JvmField var pub_group: PubGroup? = null,
    @ProtoNumber(15) @JvmField var market_trans: MarketTrans? = null,
    @ProtoNumber(16) @JvmField var extra_info: ExtraInfo? = null,
    @ProtoNumber(17) @JvmField var shake_window: ShakeWindow? = null,
    @ProtoNumber(18) @JvmField var pub_account: PubAccount? = null,
    @ProtoNumber(19) @JvmField var video_file: VideoFile? = null,
    @ProtoNumber(20) @JvmField var tips_info: TipsInfo? = null,
    @ProtoNumber(21) @JvmField var anon_group_msg: AnonymousGroupMsg? = null,
    @ProtoNumber(22) @JvmField var qq_live_old: QQLiveOld? = null,
    @ProtoNumber(23) @JvmField var life_online: LifeOnlineAccount? = null,
    @ProtoNumber(24) @JvmField var qqwallet_msg: QQWalletMsg? = null,
    @ProtoNumber(25) @JvmField var crm_elem: CrmElem? = null,
    @ProtoNumber(26) @JvmField var conference_tips_info: ConferenceTipsInfo? = null,
    @ProtoNumber(27) @JvmField var redbag_info: RedBagInfo? = null,
    @ProtoNumber(28) @JvmField var low_version_tips: LowVersionTips? = null,
    @ProtoNumber(29) @JvmField var bankcode_ctrl_info: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(30) @JvmField var near_by_msg: NearByMessageType? = null,
    @ProtoNumber(31) @JvmField var custom_elem: CustomElem? = null,
    @ProtoNumber(32) @JvmField var location_info: LocationInfo? = null,
    @ProtoNumber(33) @JvmField var pub_acc_info: PubAccInfo? = null,
    @ProtoNumber(34) @JvmField var small_emoji: SmallEmoji? = null,
    @ProtoNumber(35) @JvmField var fsj_msg_elem: FSJMessageElem? = null,
    @ProtoNumber(36) @JvmField var ark_app: ArkAppElem? = null,

    @ProtoNumber(37) @JvmField var generalFlags: GeneralFlags? = null,

    @ProtoNumber(38) @JvmField var hc_flash_pic: CustomFace? = null,
    @ProtoNumber(39) @JvmField var deliver_gift_msg: DeliverGiftMsg? = null,
    @ProtoNumber(40) @JvmField var bitapp_msg: BitAppMsg? = null,
    @ProtoNumber(41) @JvmField var open_qq_data: OpenQQData? = null,
    @ProtoNumber(42) @JvmField var apollo_msg: ApolloActMsg? = null,
    @ProtoNumber(43) @JvmField var group_pub_acc_info: GroupPubAccountInfo? = null,
    @ProtoNumber(44) @JvmField var bless_msg: BlessingMessage? = null,
    @ProtoNumber(45) @JvmField var src_msg: SourceMsg? = null,
    @ProtoNumber(46) @JvmField var lola_msg: LolaMsg? = null,
    @ProtoNumber(47) @JvmField var group_business_msg: GroupBusinessMsg? = null,
    @ProtoNumber(48) @JvmField var msg_workflow_notify: WorkflowNotifyMsg? = null,
    @ProtoNumber(49) @JvmField var pat_elem: PatsElem? = null,
    @ProtoNumber(50) @JvmField var group_post_elem: GroupPostElem? = null,
    @ProtoNumber(51) @JvmField var light_app: LightAppElem? = null,
    @ProtoNumber(52) @JvmField var eim_info: EIMInfo? = null,
    @ProtoNumber(53) @JvmField var commonElem: CommonElem? = null,
): Protobuf<Elem>

@Serializable
internal data class CommonElem(
    @ProtoNumber(1) @JvmField var serviceType: UInt = 0u,
    @ProtoNumber(2) @JvmField var elem: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(3) @JvmField var businessType: UInt? = null,
): Protobuf<CommonElem>

@Serializable
internal data class EIMInfo(
    @ProtoNumber(1) @JvmField var uint64_root_id: ULong = 0u,
    @ProtoNumber(2) @JvmField var uint32_flag: UInt = 0u,
): Protobuf<EIMInfo>

@Serializable
internal data class LightAppElem(
    @ProtoNumber(1) @JvmField var bytes_data: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var bytes_msg_resid: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<LightAppElem>

@Serializable
internal data class GroupPostElem(
    @ProtoNumber(1) @JvmField var uint32_trans_type: UInt = 0u,
    @ProtoNumber(2) @JvmField var bytes_trans_msg: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<GroupPostElem>

@Serializable
internal data class PatsElem(
    @ProtoNumber(1) @JvmField var uint32_pat_type: UInt = 0u,
    @ProtoNumber(2) @JvmField var uint32_pat_count: UInt = 0u,
): Protobuf<PatsElem>

@Serializable
internal data class WorkflowNotifyMsg(
    @ProtoNumber(1) @JvmField var bytes_ext_msg: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var uint64_create_uin: ULong = 0u,
): Protobuf<WorkflowNotifyMsg>

@Serializable
internal data class GroupBusinessMsg(
    @ProtoNumber(1) @JvmField var uint32_flags: UInt = 0u,
    @ProtoNumber(2) @JvmField var bytes_head_url: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(3) @JvmField var bytes_head_clk_url: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var bytes_nick: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(5) @JvmField var bytes_nick_color: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(6) @JvmField var bytes_rank: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(7) @JvmField var bytes_rank_color: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(8) @JvmField var bytes_rank_bgcolor: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<GroupBusinessMsg>

@Serializable
internal data class LolaMsg(
    @ProtoNumber(1) @JvmField var bytes_msg_resid: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var bytes_encode_content: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(3) @JvmField var bytes_long_msg_url: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var bytes_download_key: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<LolaMsg>

@Serializable
internal data class SourceMsg(
    @ProtoNumber(1) @JvmField var uint32_orig_seqs: List<Int>? = null,
    @ProtoNumber(2) @JvmField var uint64_sender_uin: ULong = 0u,
    @ProtoNumber(3) @JvmField var uint32_time: ULong = 0u,
    @ProtoNumber(4) @JvmField var uint32_flag: UInt = 0u,
    @ProtoNumber(5) @JvmField var elems: ArrayList<Elem>? = null,
    @ProtoNumber(6) @JvmField var uint32_type: UInt = 0u,
    @ProtoNumber(7) @JvmField var bytes_richMsg: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(8) @JvmField var bytes_pb_reserve: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(9) @JvmField var bytes_src_msg: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(10) @JvmField var uint64_to_uin: ULong = 0u,
    @ProtoNumber(11) @JvmField var bytes_troop_name: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<SourceMsg>

@Serializable
internal data class BlessingMessage(
    @ProtoNumber(1) @JvmField var uint32_msg_type: UInt = 0u,
    @ProtoNumber(2) @JvmField var uint32_ex_flag: UInt = 0u,
): Protobuf<BlessingMessage>

@Serializable
internal data class GroupPubAccountInfo(
    @ProtoNumber(1) @JvmField var uint64_pub_account: ULong = 0u,
): Protobuf<GroupPubAccountInfo>

@Serializable
internal data class ApolloActMsg(
    @ProtoNumber(1) @JvmField var uint32_action_id: UInt = 0u,
    @ProtoNumber(2) @JvmField var bytes_action_name: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(3) @JvmField var bytes_action_text: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var uint32_flag: UInt = 0u,
    @ProtoNumber(5) @JvmField var uint32_peer_uin: UInt = 0u,
    @ProtoNumber(6) @JvmField var uint32_sender_ts: UInt = 0u,
    @ProtoNumber(7) @JvmField var uint32_peer_ts: UInt = 0u,
    @ProtoNumber(8) @JvmField var int32_sender_status: Int = 0,
    @ProtoNumber(9) @JvmField var int32_peer_status: Int = 0,
    @ProtoNumber(10) @JvmField var diytext_id: UInt = 0u,
    @ProtoNumber(11) @JvmField var diytext_content: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(12) @JvmField var input_text: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(13) @JvmField var bytes_pb_reserve: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<ApolloActMsg>

@Serializable
internal data class OpenQQData(
    @ProtoNumber(1) @JvmField var bytes_car_qq_data: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<OpenQQData>

@Serializable
internal data class BitAppMsg(
    @ProtoNumber(1) @JvmField var bytes_buf: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<BitAppMsg>

@Serializable
internal data class DeliverGiftMsg(
    @ProtoNumber(1) @JvmField var bytes_gray_tip_content: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var uint32_animation_package_id: UInt = 0u,
    @ProtoNumber(3) @JvmField var bytes_animation_package_url_a: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var bytes_animation_package_url_i: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(5) @JvmField var bytes_remind_brief: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(6) @JvmField var uint32_gift_id: UInt = 0u,
    @ProtoNumber(7) @JvmField var uint32_gift_count: UInt = 0u,
    @ProtoNumber(8) @JvmField var bytes_animation_brief: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(9) @JvmField var uint64_sender_uin: ULong = 0u,
    @ProtoNumber(10) @JvmField var uint64_receiver_uin: ULong = 0u,
    @ProtoNumber(11) @JvmField var bytes_stmessage_title: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(12) @JvmField var bytes_stmessage_subtitle: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(13) @JvmField var bytes_stmessage_message: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(14) @JvmField var uint32_stmessage_giftpicid: UInt = 0u,
    @ProtoNumber(15) @JvmField var bytes_stmessage_comefrom: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(16) @JvmField var uint32_stmessage_exflag: UInt = 0u,
    @ProtoNumber(17) @JvmField var bytes_to_all_gift_id: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(18) @JvmField var bytes_comefrom_link: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(19) @JvmField var bytes_pb_reserve: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(20) @JvmField var bytes_receiver_name: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(21) @JvmField var bytes_receiver_pic: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(22) @JvmField var bytes_stmessage_gifturl: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<DeliverGiftMsg>

@Serializable
internal data class GeneralFlags(
    @ProtoNumber(1) @JvmField var uint32_bubble_diy_text_id: UInt = 0u,
    @ProtoNumber(2) @JvmField var uint32_group_flag_new: UInt = 0u,
    @ProtoNumber(3) @JvmField var uint64_uin: ULong = 0u,
    @ProtoNumber(4) @JvmField var bytes_rp_id: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(5) @JvmField var uint32_prp_fold: UInt = 0u,
    @ProtoNumber(6) @JvmField var long_text_flag: UInt = 0u,
    @ProtoNumber(7) @JvmField var long_text_resid: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(8) @JvmField var uint32_group_type: UInt = 0u,
    @ProtoNumber(9) @JvmField var uint32_to_uin_flag: UInt = 0u,
    @ProtoNumber(10) @JvmField var uint32_glamour_level: UInt = 0u,
    @ProtoNumber(11) @JvmField var uint32_member_level: UInt = 0u,
    @ProtoNumber(12) @JvmField var uint64_group_rank_seq: ULong = 0u,
    @ProtoNumber(13) @JvmField var uint32_olympic_torch: UInt = 0u,
    @ProtoNumber(14) @JvmField var babyq_guide_msg_cookie: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(15) @JvmField var uin32_expert_flag: UInt = 0u,
    @ProtoNumber(16) @JvmField var uint32_bubble_sub_id: UInt = 0u,
    @ProtoNumber(17) @JvmField var pendantId: ULong = 0u,
    @ProtoNumber(18) @JvmField var bytes_rp_index: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(19) @JvmField var reserve: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<GeneralFlags>

@Serializable
internal class GeneralFlagsReserveAttr(
    @ProtoNumber(1) @JvmField var uint32_global_group_level: UInt = 0u,
    @ProtoNumber(2) @JvmField var uint32_nearby_charm_level: UInt = 0u,
    @ProtoNumber(3) @JvmField var redbag_msg_sender_uin: ULong = 0u,
    @ProtoNumber(4) @JvmField var uint32_title_id: UInt = 0u,
    @ProtoNumber(5) @JvmField var uint32_robot_msg_flag: UInt = 0u,
    @ProtoNumber(6) @JvmField var want_gift_sender_uin: ULong = 0u,
    @ProtoNumber(7) @JvmField var float_sticker_x: Float = 0f,
    @ProtoNumber(8) @JvmField var float_sticker_y: Float = 0f,
    @ProtoNumber(9) @JvmField var float_sticker_width: Float = 0f,
    @ProtoNumber(10) @JvmField var float_sticker_height: Float = 0f,
    @ProtoNumber(11) @JvmField var uint32_sticker_rotate: UInt = 0u,
    @ProtoNumber(12) @JvmField var uint64_sticker_host_msgseq: ULong = 0u,
    @ProtoNumber(13) @JvmField var uint64_sticker_host_msguid: ULong = 0u,
    @ProtoNumber(14) @JvmField var uint64_sticker_host_time: ULong = 0u,
    @ProtoNumber(15) @JvmField var mobileCustomFont: UInt = 0u,
    @ProtoNumber(16) @JvmField var uint32_tail_key: UInt = 0u,
    @ProtoNumber(17) @JvmField var uint32_show_tail_flag: UInt = 0u,
    @ProtoNumber(18) @JvmField var uint32_doutu_msg_type: UInt = 0u,
    @ProtoNumber(19) @JvmField var uint32_doutu_combo: UInt = 0u,
    @ProtoNumber(20) @JvmField var uint32_custom_featureid: UInt = 0u,
    @ProtoNumber(21) @JvmField var uint32_golden_msg_type: UInt = 0u,
    @ProtoNumber(22) @JvmField var bytes_golden_msg_info: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(23) @JvmField var uint32_bot_message_class_id: UInt = 0u,
    @ProtoNumber(24) @JvmField var bytes_subscription_url: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(25) @JvmField var uint32_pendant_diy_id: UInt = 0u,
    @ProtoNumber(26) @JvmField var uint32_timed_message: UInt = 0u,
    @ProtoNumber(27) @JvmField var uint32_holiday_flag: UInt = 0u,
    @ProtoNumber(29) @JvmField var bytes_kpl_info: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(30) @JvmField var uint32_face_id: UInt = 0u,
    @ProtoNumber(31) @JvmField var diyFontTimestamp: UInt = 0u,
    @ProtoNumber(32) @JvmField var uint32_red_envelope_type: UInt = 0u,
    @ProtoNumber(33) @JvmField var bytes_shortVideoId: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(34) @JvmField var uint32_req_font_effect_id: UInt = 0u,
    @ProtoNumber(35) @JvmField var uint32_love_language_flag: UInt = 0u,
    @ProtoNumber(36) @JvmField var uint32_aio_sync_to_story_flag: UInt = 0u,
    @ProtoNumber(37) @JvmField var uint32_upload_image_to_qzone_flag: UInt = 0u,
    @ProtoNumber(39) @JvmField var bytes_upload_image_to_qzone_param: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(40) @JvmField var bytes_group_confess_sig: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(41) @JvmField var subFontId: ULong = 0u,
    @ProtoNumber(42) @JvmField var uint32_msg_flag_type: UInt = 0u,
    @ProtoNumber(43) @JvmField var rpt_uint32_custom_featureid: List<Int>? = null,
    @ProtoNumber(44) @JvmField var uint32_rich_card_name_ver: UInt = 0u,
    @ProtoNumber(47) @JvmField var uint32_msg_info_flag: UInt = 0u,
    @ProtoNumber(48) @JvmField var uint32_service_msg_type: UInt = 0u,
    @ProtoNumber(49) @JvmField var uint32_service_msg_remind_type: UInt = 0u,
    @ProtoNumber(50) @JvmField var bytes_service_msg_name: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(51) @JvmField var uint32_vip_type: UInt = 0u,
    @ProtoNumber(52) @JvmField var uint32_vip_level: UInt = 0u,
    @ProtoNumber(53) @JvmField var bytes_pb_ptt_waveform: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(54) @JvmField var uint32_user_bigclub_level: UInt = 0u,
    @ProtoNumber(55) @JvmField var uint32_user_bigclub_flag: UInt = 0u,
    @ProtoNumber(56) @JvmField var uint32_nameplate: UInt = 0u,
    @ProtoNumber(57) @JvmField var uint32_auto_reply: UInt = 0u,
    @ProtoNumber(58) @JvmField var uint32_req_is_bigclub_hidden: UInt = 0u,
    @ProtoNumber(59) @JvmField var uint32_show_in_msg_list: UInt = 0u,
    @ProtoNumber(60) @JvmField var bytes_oac_msg_extend: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(61) @JvmField var uint32_group_member_flag_ex2: UInt = 0u,
    @ProtoNumber(62) @JvmField var uint32_group_ringtone_id: UInt = 0u,
    @ProtoNumber(63) @JvmField var bytes_robot_general_trans: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(64) @JvmField var uint32_troop_pobing_template: UInt = 0u,
    @ProtoNumber(65) @JvmField var bytes_hudong_mark: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(66) @JvmField var uint32_group_info_flag_ex3: UInt = 0u,
    @ProtoNumber(67) @JvmField var uint32_comment_flag: UInt = 0u,
    @ProtoNumber(68) @JvmField var uint64_comment_location: ULong = 0u,
    @ProtoNumber(69) @JvmField var bytes_pass_through: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(70) @JvmField var uint32_group_savedb_flag: UInt = 0u,
    @ProtoNumber(71) @JvmField var uint32_nameplate_vip_type: UInt = 0u,
    @ProtoNumber(72) @JvmField var uint32_gray_name_plate: UInt = 0u,
    @ProtoNumber(73) @JvmField var bytes_user_vip_info: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(74) @JvmField var uint32_important_msg_type: UInt = 0u,
    @ProtoNumber(75) @JvmField var uint32_important_msg_enum: UInt = 0u,
    @ProtoNumber(76) @JvmField var uint32_device_type: UInt = 0u,
    @ProtoNumber(77) @JvmField var uint32_unsafe_msg_flag: UInt = 0u,
    @ProtoNumber(78) @JvmField var bytes_group_msg_busi_buf: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(79) @JvmField var uint32_group_info_flag_ex4: UInt = 0u,
    @ProtoNumber(80) @JvmField var uint32_kings_honor_level: UInt = 0u,
    @ProtoNumber(81) @JvmField var uint32_group_rich_flag: UInt = 0u,
    @ProtoNumber(82) @JvmField var bytes_yuheng_task_msg_buf: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<GeneralFlagsReserveAttr>

@Serializable
internal data class ArkAppElem(
    @ProtoNumber(1) @JvmField var app_name: String = "",
    @ProtoNumber(2) @JvmField var min_version: String = "",
    @ProtoNumber(3) @JvmField var xml_template: String = "",
    @ProtoNumber(4) @JvmField var bytes_data: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<ArkAppElem>

@Serializable
internal data class FSJMessageElem(
    @ProtoNumber(1) @JvmField var uint32_msg_type: UInt = 0u,
): Protobuf<FSJMessageElem>

@Serializable
internal data class SmallEmoji(
    @ProtoNumber(1) @JvmField var packIdSum: UInt = 0u,
    @ProtoNumber(2) @JvmField var imageType: UInt = 0u,
): Protobuf<SmallEmoji>

@Serializable
internal data class PubAccInfo(
    @ProtoNumber(1) @JvmField var uint32_is_inter_num: UInt = 0u,
    @ProtoNumber(2) @JvmField var string_msg_template_id: String = "",
    @ProtoNumber(3) @JvmField var string_long_msg_url: String = "",
    @ProtoNumber(4) @JvmField var bytes_download_key: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<PubAccInfo>

@Serializable
internal data class LocationInfo(
    @ProtoNumber(1) @JvmField var double_longitude: Double = 0.0,
    @ProtoNumber(2) @JvmField var double_latitude: Double = 0.0,
    @ProtoNumber(3) @JvmField var bytes_desc: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<LocationInfo>

@Serializable
internal data class CustomElem(
    @ProtoNumber(1) @JvmField var bytes_desc: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var bytes_data: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(3) @JvmField var enum_type: Int = 0,
    @ProtoNumber(4) @JvmField var bytes_ext: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(5) @JvmField var bytes_sound: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<CustomElem>

@Serializable
internal data class NearByMessageType(
    @ProtoNumber(1) @JvmField var uint32_type: UInt = 0u,
    @ProtoNumber(2) @JvmField var uint32_identify_type: UInt = 0u,
): Protobuf<NearByMessageType>

@Serializable
internal data class LowVersionTips(
    @ProtoNumber(1) @JvmField var uint32_business_id: UInt = 0u,
    @ProtoNumber(2) @JvmField var uint32_session_type: UInt = 0u,
    @ProtoNumber(3) @JvmField var uint64_session_uin: ULong = 0u,
    @ProtoNumber(4) @JvmField var uint64_sender_uin: ULong = 0u,
    @ProtoNumber(5) @JvmField var str_text: String = "",
): Protobuf<LowVersionTips>

@Serializable
internal data class RedBagInfo(
    @ProtoNumber(1) @JvmField var redbag_type: UInt = 0u,
): Protobuf<RedBagInfo>

@Serializable
internal data class ConferenceTipsInfo(
    @ProtoNumber(1) @JvmField var uint32_session_type: UInt = 0u,
    @ProtoNumber(2) @JvmField var uint64_session_uin: ULong = 0u,
    @ProtoNumber(3) @JvmField var str_text: String = "",
): Protobuf<ConferenceTipsInfo>

@Serializable
internal data class CrmElem(
    @ProtoNumber(1) @JvmField var crm_buf: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var bytes_msg_resid: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(3) @JvmField var uint32_qidian_flag: UInt = 0u,
    @ProtoNumber(4) @JvmField var uint32_push_flag: UInt = 0u,
    @ProtoNumber(5) @JvmField var uint32_count_flag: UInt = 0u,
): Protobuf<CrmElem>

@Serializable
internal data class QQWalletMsg(
    @ProtoNumber(1) @JvmField var aio_body: QQWalletAioBody? = null,
): Protobuf<QQWalletMsg>

@Serializable
internal data class QQWalletAioBody(
    @ProtoNumber(1) @JvmField var uint64_senduin: ULong = 0u,
    @ProtoNumber(2) @JvmField var sender: QQWalletAioElem? = null,
    @ProtoNumber(3) @JvmField var receiver: QQWalletAioElem? = null,
    @ProtoNumber(4) @JvmField var sint32_channelid: Int = 0,
    @ProtoNumber(5) @JvmField var sint32_templateid: Int = 0,
    @ProtoNumber(6) @JvmField var uint32_resend: UInt = 0u,
    @ProtoNumber(7) @JvmField var uint32_msg_priority: UInt = 0u,
    @ProtoNumber(8) @JvmField var sint32_redtype: Int = 0,
    @ProtoNumber(9) @JvmField var bytes_billno: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(10) @JvmField var bytes_authkey: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(11) @JvmField var sint32_sessiontype: Int = 0,
    @ProtoNumber(12) @JvmField var sint32_msgtype: Int = 0,
    @ProtoNumber(13) @JvmField var sint32_envelopeid: Int = 0,
    @ProtoNumber(14) @JvmField var bytes_name: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(15) @JvmField var sint32_conftype: Int = 0,
    @ProtoNumber(16) @JvmField var sint32_msg_from: Int = 0,
    @ProtoNumber(17) @JvmField var bytes_pc_body: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(18) @JvmField var string_index: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(19) @JvmField var uint32_redchannel: UInt = 0u,
    @ProtoNumber(20) @JvmField var uint64_grap_uin: List<Long>? = null,
    @ProtoNumber(21) @JvmField var bytes_pb_reserve: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<QQWalletAioBody>

@Serializable
internal data class QQWalletAioElem(
    @ProtoNumber(1) @JvmField var uint32_background: UInt = 0u,
    @ProtoNumber(2) @JvmField var uint32_icon: UInt = 0u,
    @ProtoNumber(3) @JvmField var bytes_title: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var bytes_subtitle: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(5) @JvmField var bytes_content: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(6) @JvmField var bytes_linkurl: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(7) @JvmField var bytes_blackstripe: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(8) @JvmField var bytes_notice: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(9) @JvmField var uint32_title_color: UInt = 0u,
    @ProtoNumber(10) @JvmField var uint32_subtitle_color: UInt = 0u,
    @ProtoNumber(11) @JvmField var bytes_actions_priority: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(12) @JvmField var bytes_jump_url: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(13) @JvmField var bytes_native_ios: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(14) @JvmField var bytes_native_android: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(15) @JvmField var bytes_iconurl: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(16) @JvmField var uint32_content_color: UInt = 0u,
    @ProtoNumber(17) @JvmField var uint32_content_bgcolor: UInt = 0u,
    @ProtoNumber(18) @JvmField var bytes_aio_image_left: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(19) @JvmField var bytes_aio_image_right: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(20) @JvmField var bytes_cft_image: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(21) @JvmField var bytes_pb_reserve: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<QQWalletAioElem>

@Serializable
internal data class LifeOnlineAccount(
    @ProtoNumber(1) @JvmField var uint64_unique_id: ULong = 0u,
    @ProtoNumber(2) @JvmField var uint32_op: UInt = 0u,
    @ProtoNumber(3) @JvmField var uint32_show_time: UInt = 0u,
    @ProtoNumber(4) @JvmField var uint32_report: UInt = 0u,
    @ProtoNumber(5) @JvmField var uint32_ack: UInt = 0u,
    @ProtoNumber(6) @JvmField var uint64_bitmap: ULong = 0u,
    @ProtoNumber(7) @JvmField var gdt_imp_data: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(8) @JvmField var gdt_cli_data: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(9) @JvmField var view_id: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<LifeOnlineAccount>

@Serializable
internal data class QQLiveOld(
    @ProtoNumber(1) @JvmField var uint32_sub_cmd: UInt = 0u,
    @ProtoNumber(2) @JvmField var str_show_text: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(3) @JvmField var str_param: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var str_introduce: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<QQLiveOld>

@Serializable
internal data class AnonymousGroupMsg(
    @ProtoNumber(1) @JvmField var uint32_flags: UInt = 0u,
    @ProtoNumber(2) @JvmField var str_anon_id: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(3) @JvmField var str_anon_nick: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var uint32_head_portrait: UInt = 0u,
    @ProtoNumber(5) @JvmField var uint32_expire_time: UInt = 0u,
    @ProtoNumber(6) @JvmField var uint32_bubble_id: UInt = 0u,
    @ProtoNumber(7) @JvmField var str_rank_color: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<AnonymousGroupMsg>

@Serializable
internal data class TipsInfo(
    @ProtoNumber(1) @JvmField var text: String = "",
): Protobuf<TipsInfo>

@Serializable
internal data class VideoFile(
    @ProtoNumber(1) @JvmField var bytes_file_uuid: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var bytes_file_md5: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(3) @JvmField var bytes_file_name: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var uint32_file_format: UInt = 0u,
    @ProtoNumber(5) @JvmField var uint32_file_time: UInt = 0u,
    @ProtoNumber(6) @JvmField var uint32_file_size: UInt = 0u,
    @ProtoNumber(7) @JvmField var uint32_thumb_width: UInt = 0u,
    @ProtoNumber(8) @JvmField var uint32_thumb_height: UInt = 0u,
    @ProtoNumber(9) @JvmField var bytes_thumb_file_md5: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(10) @JvmField var bytes_source: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(11) @JvmField var uint32_thumb_file_size: UInt = 0u,
    @ProtoNumber(12) @JvmField var uint32_busi_type: UInt = 0u,
    @ProtoNumber(13) @JvmField var uint32_from_chat_type: UInt = 0u,
    @ProtoNumber(14) @JvmField var uint32_to_chat_type: UInt = 0u,
    @ProtoNumber(15) @JvmField var bool_support_progressive: Boolean = false,
    @ProtoNumber(16) @JvmField var uint32_file_width: UInt = 0u,
    @ProtoNumber(17) @JvmField var uint32_file_height: UInt = 0u,
    @ProtoNumber(18) @JvmField var uint32_sub_busi_type: UInt = 0u,
    @ProtoNumber(19) @JvmField var uint32_video_attr: UInt = 0u,
    @ProtoNumber(20) @JvmField var rpt_bytes_thumb_file_urls: List<ByteArray>? = null,
    @ProtoNumber(21) @JvmField var rpt_bytes_video_file_urls: List<ByteArray>? = null,
    @ProtoNumber(22) @JvmField var uint32_thumb_download_flag: UInt = 0u,
    @ProtoNumber(23) @JvmField var uint32_video_download_flag: UInt = 0u,
    @ProtoNumber(24) @JvmField var bytes_pb_reserve: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<VideoFile>

@Serializable
internal data class PubAccount(
    @ProtoNumber(1) @JvmField var bytes_buf: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var uint64_pub_account_uin: ULong = 0u,
): Protobuf<PubAccount>

@Serializable
internal data class ShakeWindow(
    @ProtoNumber(1) @JvmField var uint32_type: UInt = 0u,
    @ProtoNumber(2) @JvmField var uint32_reserve: UInt = 0u,
    @ProtoNumber(3) @JvmField var uint64_uin: ULong = 0u,
): Protobuf<ShakeWindow>

@Serializable
internal data class ExtraInfo(
    @ProtoNumber(1) @JvmField var bytes_nick: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var bytes_group_card: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(3) @JvmField var uint32_level: UInt = 0u,
    @ProtoNumber(4) @JvmField var uint32_flags: UInt = 0u,
    @ProtoNumber(5) @JvmField var uint32_group_mask: UInt = 0u,
    @ProtoNumber(6) @JvmField var uint32_msg_tail_id: UInt = 0u,
    @ProtoNumber(7) @JvmField var bytes_sender_title: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(8) @JvmField var bytes_apns_tips: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(9) @JvmField var uint64_uin: ULong = 0u,
    @ProtoNumber(10) @JvmField var uint32_msg_state_flag: UInt = 0u,
    @ProtoNumber(11) @JvmField var uint32_apns_sound_type: UInt = 0u,
    @ProtoNumber(12) @JvmField var uint32_new_group_flag: UInt = 0u,
): Protobuf<ExtraInfo>

@Serializable
internal data class MarketTrans(
    @ProtoNumber(1) @JvmField var int32_flag: Int = 0,
    @ProtoNumber(2) @JvmField var bytes_xml: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(3) @JvmField var bytes_msg_resid: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var uint32_ability: UInt = 0u,
    @ProtoNumber(5) @JvmField var uint32_min_ability: UInt = 0u,
): Protobuf<MarketTrans>

@Serializable
internal data class PubGroup(
    @ProtoNumber(1) @JvmField var bytes_nickname: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var uint32_gender: UInt = 0u,
    @ProtoNumber(3) @JvmField var uint32_age: UInt = 0u,
    @ProtoNumber(4) @JvmField var uint32_distance: UInt = 0u,
): Protobuf<PubGroup>

@Serializable
internal data class GroupFile(
    @ProtoNumber(1) @JvmField var bytes_filename: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var uint64_file_size: ULong = 0u,
    @ProtoNumber(3) @JvmField var bytes_file_id: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var bytes_batch_id: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(5) @JvmField var bytes_file_key: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(6) @JvmField var bytes_mark: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(7) @JvmField var uint64_sequence: ULong = 0u,
    @ProtoNumber(8) @JvmField var bytes_batch_item_id: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(9) @JvmField var uint32_feed_msg_time: UInt = 0u,
    @ProtoNumber(10) @JvmField var bytes_pb_reserve: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<GroupFile>

@Serializable
internal data class RichMsg(
    @ProtoNumber(1) @JvmField var bytes_template_1: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var uint32_service_id: UInt = 0u,
    @ProtoNumber(3) @JvmField var bytes_msg_resid: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var uint32_rand: UInt = 0u,
    @ProtoNumber(5) @JvmField var uint32_seq: UInt = 0u,
    @ProtoNumber(6) @JvmField var uint32_flags: UInt = 0u,
): Protobuf<RichMsg>

@Serializable
internal data class SecretFileMsg(
    @ProtoNumber(1) @JvmField var bytes_file_key: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var uint64_from_uin: ULong = 0u,
    @ProtoNumber(3) @JvmField var uint64_to_uin: ULong = 0u,
    @ProtoNumber(4) @JvmField var uint32_status: UInt = 0u,
    @ProtoNumber(5) @JvmField var uint32_ttl: UInt = 0u,
    @ProtoNumber(6) @JvmField var uint32_type: UInt = 0u,
    @ProtoNumber(7) @JvmField var uint32_encrypt_prehead_length: UInt = 0u,
    @ProtoNumber(8) @JvmField var uint32_encrypt_type: UInt = 0u,
    @ProtoNumber(9) @JvmField var bytes_encrypt_key: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(10) @JvmField var uint32_read_times: UInt = 0u,
    @ProtoNumber(11) @JvmField var uint32_left_time: UInt = 0u,
    @ProtoNumber(12) @JvmField var not_online_image: NotOnlineImage? = null,
    @ProtoNumber(13) @JvmField var elem_flags2: ElemFlags2? = null,
    @ProtoNumber(14) @JvmField var uint32_opertype: UInt = 0u,
    @ProtoNumber(15) @JvmField var str_fromphonenum: String = "",
): Protobuf<SecretFileMsg>

@Serializable
internal data class FunFace(
    @ProtoNumber(1) @JvmField var msg_turntable: Turntable? = null,
    @ProtoNumber(2) @JvmField var msg_bomb: Bomb? = null,
): Protobuf<FunFace>

@Serializable
internal data class Bomb(
    @ProtoNumber(1) @JvmField var bool_burst: Boolean = false,
): Protobuf<Bomb>

@Serializable
internal data class Turntable(
    @ProtoNumber(1) @JvmField var rpt_uint64_uin_list: List<Long>? = null,
    @ProtoNumber(2) @JvmField var uint64_hit_uin: ULong = 0u,
    @ProtoNumber(3) @JvmField var str_hit_uin_nick: String = "",
): Protobuf<Turntable>

@Serializable
internal data class ElemFlags2(
    @ProtoNumber(1) @JvmField var uint32_color_text_id: UInt = 0u,
    @ProtoNumber(2) @JvmField var uint64_msg_id: ULong = 0u,
    @ProtoNumber(3) @JvmField var uint32_whisper_session_id: UInt = 0u,
    @ProtoNumber(4) @JvmField var uint32_ptt_change_bit: UInt = 0u,
    @ProtoNumber(5) @JvmField var uint32_vip_status: UInt = 0u,
    @ProtoNumber(6) @JvmField var uint32_compatible_id: UInt = 0u,
    @ProtoNumber(7) @JvmField var rpt_insts: ArrayList<Inst>? = null,
    @ProtoNumber(8) @JvmField var uint32_msg_rpt_cnt: UInt = 0u,
    @ProtoNumber(9) @JvmField var src_inst: Inst? = null,
    @ProtoNumber(10) @JvmField var uint32_longtitude: UInt = 0u,
    @ProtoNumber(11) @JvmField var uint32_latitude: UInt = 0u,
    @ProtoNumber(12) @JvmField var uint32_custom_font: UInt = 0u,
    @ProtoNumber(13) @JvmField var pc_support_def: PcSupportDef? = null,
    @ProtoNumber(14) @JvmField var uint32_crm_flags: UInt = 0u,
): Protobuf<ElemFlags2>

@Serializable
internal data class Inst(
    @ProtoNumber(1) @JvmField var uint32_app_id: UInt = 0u,
    @ProtoNumber(2) @JvmField var uint32_inst_id: UInt = 0u,
): Protobuf<Inst>

@Serializable
internal data class PcSupportDef(
    @ProtoNumber(1) @JvmField var uint32_pc_ptl_begin: UInt = 0u,
    @ProtoNumber(2) @JvmField var uint32_pc_ptl_end: UInt = 0u,
    @ProtoNumber(3) @JvmField var uint32_mac_ptl_begin: UInt = 0u,
    @ProtoNumber(4) @JvmField var uint32_mac_ptl_end: UInt = 0u,
    @ProtoNumber(5) @JvmField var rpt_ptls_support: List<Int>? = null,
    @ProtoNumber(6) @JvmField var rpt_ptls_not_support: List<Int>? = null,
): Protobuf<PcSupportDef>

@Serializable
internal data class CustomFace(
    @ProtoNumber(1) @JvmField var bytes_guid: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var filePath: String = "",
    @ProtoNumber(3) @JvmField var str_shortcut: String = "",
    @ProtoNumber(4) @JvmField var bytes_buffer: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(5) @JvmField var bytes_flag: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(6) @JvmField var bytes_old_data: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(7) @JvmField var fileId: UInt = 0u,
    @ProtoNumber(8) @JvmField var serverIp: UInt = 0u,
    @ProtoNumber(9) @JvmField var serverPort: UInt = 0u,
    @ProtoNumber(10) @JvmField var filetType: UInt = 0u, // 66
    @ProtoNumber(11) @JvmField var signature: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(12) @JvmField var useful: UInt = 0u,
    @ProtoNumber(13) @JvmField var md5: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(14) @JvmField var str_thumb_url: String = "",
    @ProtoNumber(15) @JvmField var str_big_url: String = "",
    @ProtoNumber(16) @JvmField var origUrl: String = "",
    @ProtoNumber(17) @JvmField var bizType: UInt? = null,
    @ProtoNumber(18) @JvmField var repeat_index: UInt = 0u,
    @ProtoNumber(19) @JvmField var repeat_image: UInt = 0u,
    @ProtoNumber(20) @JvmField var imageType: UInt = 0u,
    @ProtoNumber(21) @JvmField var index: UInt = 0u,
    @ProtoNumber(22) @JvmField var width: UInt = 0u,
    @ProtoNumber(23) @JvmField var height: UInt = 0u,
    @ProtoNumber(24) @JvmField var source: UInt = 0u,
    @ProtoNumber(25) @JvmField var size: UInt = 0u,
    @ProtoNumber(26) @JvmField var origin: UInt = 0u,
    @ProtoNumber(27) @JvmField var uint32_thumb_width: UInt = 0u,
    @ProtoNumber(28) @JvmField var uint32_thumb_height: UInt = 0u,
    @ProtoNumber(29) @JvmField var uint32_show_len: UInt = 0u,
    @ProtoNumber(30) @JvmField var downloadLen: UInt = 0u,
    @ProtoNumber(31) @JvmField var str_400_url: String = "",
    @ProtoNumber(32) @JvmField var uint32_400_width: UInt = 0u,
    @ProtoNumber(33) @JvmField var uint32_400_height: UInt = 0u,
    @ProtoNumber(34) @JvmField var bytes_pb_reserve: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<CustomFace>

@Serializable
internal data class ElemFlags(
    @ProtoNumber(1) @JvmField var bytes_flags1: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var bytes_business_data: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<ElemFlags>

@Serializable
internal data class MarketFace(
    @ProtoNumber(1) @JvmField var bytes_face_name: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var uint32_item_type: UInt = 0u,
    @ProtoNumber(3) @JvmField var uint32_face_info: UInt = 0u,
    @ProtoNumber(4) @JvmField var bytes_face_id: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(5) @JvmField var uint32_tab_id: UInt = 0u,
    @ProtoNumber(6) @JvmField var uint32_sub_type: UInt = 0u,
    @ProtoNumber(7) @JvmField var bytes_key: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(8) @JvmField var bytes_param: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(9) @JvmField var uint32_media_type: UInt = 0u,
    @ProtoNumber(10) @JvmField var uint32_image_width: UInt = 0u,
    @ProtoNumber(11) @JvmField var uint32_image_height: UInt = 0u,
    @ProtoNumber(12) @JvmField var bytes_mobileparam: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(13) @JvmField var bytes_pb_reserve: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<MarketFace>

@Serializable
internal data class TransElem(
    @ProtoNumber(1) @JvmField var elem_type: UInt = 0u,
    @ProtoNumber(2) @JvmField var elem_value: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<TransElem>

@Serializable
internal data class NotOnlineImage(
    @ProtoNumber(1) @JvmField var file_path: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var file_len: UInt = 0u,
    @ProtoNumber(3) @JvmField var download_path: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var old_ver_send_file: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(5) @JvmField var img_type: UInt = 0u,
    @ProtoNumber(6) @JvmField var previews_image: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(7) @JvmField var pic_md5: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(8) @JvmField var pic_height: UInt = 0u,
    @ProtoNumber(9) @JvmField var pic_width: UInt = 0u,
    @ProtoNumber(10) @JvmField var res_id: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(11) @JvmField var flag: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(12) @JvmField var str_thumb_url: String = "",
    @ProtoNumber(13) @JvmField var original: UInt = 0u,
    @ProtoNumber(14) @JvmField var str_big_url: String = "",
    @ProtoNumber(15) @JvmField var str_orig_url: String = "",
    @ProtoNumber(16) @JvmField var biz_type: UInt = 0u,
    @ProtoNumber(17) @JvmField var result: UInt = 0u,
    @ProtoNumber(18) @JvmField var index: UInt = 0u,
    @ProtoNumber(19) @JvmField var op_face_buf: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(20) @JvmField var old_pic_md5: Boolean = false,
    @ProtoNumber(21) @JvmField var uint32_thumb_width: UInt = 0u,
    @ProtoNumber(22) @JvmField var uint32_thumb_height: UInt = 0u,
    @ProtoNumber(23) @JvmField var uint32_file_id: UInt = 0u,
    @ProtoNumber(24) @JvmField var uint32_show_len: UInt = 0u,
    @ProtoNumber(25) @JvmField var uint32_download_len: UInt = 0u,
    @ProtoNumber(26) @JvmField var str_400_url: String = "",
    @ProtoNumber(27) @JvmField var uint32_400_width: UInt = 0u,
    @ProtoNumber(28) @JvmField var uint32_400_height: UInt = 0u,
    @ProtoNumber(29) @JvmField var bytes_pb_reserve: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<NotOnlineImage>

@Serializable
internal data class OnlineImage(
    @ProtoNumber(1) @JvmField var guid: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(2) @JvmField var file_path: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(3) @JvmField var old_ver_send_file: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<OnlineImage>

@Serializable
internal data class FaceMsg(
    @ProtoNumber(1) @JvmField var index: UInt = 0u,
    @ProtoNumber(2) @JvmField var old: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(11) @JvmField var buf: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<FaceMsg>

@Serializable
internal data class TextMsg(
    @ProtoNumber(1) @JvmField var str: String = "",
    @ProtoNumber(2) @JvmField var link: String = "",
    @ProtoNumber(3) @JvmField var attr6Buf: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var attr_7_buf: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(11) @JvmField var buf: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(12) @JvmField var bytes_pb_reserve: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<TextMsg>

@Serializable
internal data class Attr(
    @ProtoNumber(1) @JvmField var code_page: Int = 0,
    @ProtoNumber(2) @JvmField var time: UInt = 0u,
    @ProtoNumber(3) @JvmField var random: UInt = 0u,
    @ProtoNumber(4) @JvmField var color: UInt = 0u,
    @ProtoNumber(5) @JvmField var size: UInt = 0u,
    @ProtoNumber(6) @JvmField var effect: UInt = 0u,
    @ProtoNumber(7) @JvmField var char_set: UInt = 0u,
    @ProtoNumber(8) @JvmField var pitch_and_family: UInt = 0u,
    @ProtoNumber(9) @JvmField var font_name: String = "",
    @ProtoNumber(10) @JvmField var reserve_data: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<Attr>



