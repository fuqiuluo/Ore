@file:Suppress("PLUGIN_IS_NOT_ENABLED", "EXPERIMENTAL_API_USAGE", "ArrayInDataClass")
package moe.ore.msg.protocol.protobuf

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.protobuf.Protobuf

object TroopTips0x857 {
    @Serializable
    internal data class AIOGrayTipsInfo(
        @ProtoNumber(1) @JvmField var showLastest: UInt = 0u,
        @ProtoNumber(2) @JvmField var content: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(3) @JvmField var opt_uint32_remind: UInt = 0u,
        @ProtoNumber(4) @JvmField var opt_bytes_brief: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(5) @JvmField var uint64_receiver_uin: ULong = 0u,
        @ProtoNumber(6) @JvmField var uint32_reliao_admin_opt: UInt = 0u,
        @ProtoNumber(7) @JvmField var uint32_robot_group_opt: UInt = 0u,
    ): Protobuf<AIOGrayTipsInfo>

    @Serializable
    internal data class MessageBoxInfo(
        @ProtoNumber(1) @JvmField var opt_bytes_content: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(2) @JvmField var opt_bytes_title: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(3) @JvmField var opt_bytes_button: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<MessageBoxInfo>

    @Serializable
    internal data class FloatedTipsInfo(
        @ProtoNumber(1) @JvmField var opt_bytes_content: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<FloatedTipsInfo>

    @Serializable
    internal data class AIOTopTipsInfo(
        @ProtoNumber(1) @JvmField var opt_bytes_content: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(2) @JvmField var opt_uint32_icon: UInt = 0u,
        @ProtoNumber(3) @JvmField var opt_enum_action: Int = 0,
        @ProtoNumber(4) @JvmField var opt_bytes_url: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(5) @JvmField var opt_bytes_data: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(6) @JvmField var opt_bytes_data_i: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(7) @JvmField var opt_bytes_data_a: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(8) @JvmField var opt_bytes_data_p: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<AIOTopTipsInfo>

    @Serializable
    internal data class RedGrayTipsInfo(
        @ProtoNumber(1) @JvmField var opt_uint32_show_lastest: UInt = 0u,
        @ProtoNumber(2) @JvmField var uint64_sender_uin: ULong = 0u,
        @ProtoNumber(3) @JvmField var uint64_receiver_uin: ULong = 0u,
        @ProtoNumber(4) @JvmField var bytes_sender_rich_content: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(5) @JvmField var bytes_receiver_rich_content: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(6) @JvmField var bytes_authkey: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(7) @JvmField var sint32_msgtype: Int = 0,
        @ProtoNumber(8) @JvmField var uint32_lucky_flag: UInt = 0u,
        @ProtoNumber(9) @JvmField var uint32_hide_flag: UInt = 0u,
        @ProtoNumber(10) @JvmField var bytes_pc_body: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(11) @JvmField var uint32_icon: UInt = 0u,
        @ProtoNumber(12) @JvmField var uint64_lucky_uin: ULong = 0u,
        @ProtoNumber(13) @JvmField var uint32_time: UInt = 0u,
        @ProtoNumber(14) @JvmField var uint32_random: UInt = 0u,
        @ProtoNumber(15) @JvmField var bytes_broadcast_rich_content: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(16) @JvmField var bytes_idiom: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(17) @JvmField var uint32_idiom_seq: UInt = 0u,
        @ProtoNumber(18) @JvmField var bytes_idiom_alpha: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(19) @JvmField var bytes_jumpurl: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(20) @JvmField var uint32_subchannel: UInt = 0u,
        @ProtoNumber(21) @JvmField var bytes_poem_rule: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<RedGrayTipsInfo>

    @Serializable
    internal data class GroupNotifyInfo(
        @ProtoNumber(1) @JvmField var opt_uint32_auto_pull_flag: UInt = 0u,
        @ProtoNumber(2) @JvmField var opt_bytes_feeds_id: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<GroupNotifyInfo>

    @Serializable
    internal data class WithDrawWordingInfo(
        @ProtoNumber(1) @JvmField var int32_item_id: Int = 0,
        @ProtoNumber(2) @JvmField var string_item_name: String = "",
    ): Protobuf<WithDrawWordingInfo>

    @Serializable
    internal data class MessageRecallReminder(
        @ProtoNumber(1) @JvmField var uint64_uin: ULong = 0u,
        @ProtoNumber(2) @JvmField var bytes_nickname: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(3) @JvmField var uint32_recalled_msg_list: ArrayList<MessageMeta>? = null,
        @ProtoNumber(4) @JvmField var str_reminder_content: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(5) @JvmField var bytes_userdef: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(6) @JvmField var uint32_group_type: UInt = 0u,
        @ProtoNumber(7) @JvmField var uint32_op_type: UInt = 0u,
        @ProtoNumber(8) @JvmField var uint64_admin_uin: ULong = 0u,
        @ProtoNumber(9) @JvmField var msg_wording_info: WithDrawWordingInfo? = null,
    ): Protobuf<MessageRecallReminder> {
        @Serializable
        internal data class MessageMeta(
            @ProtoNumber(1) @JvmField var uint32_seq: UInt = 0u,
            @ProtoNumber(2) @JvmField var uint32_time: ULong = 0u,
            @ProtoNumber(3) @JvmField var uint32_msg_random: UInt = 0u,
            @ProtoNumber(4) @JvmField var uint32_msg_type: UInt = 0u,
            @ProtoNumber(5) @JvmField var uint32_msg_flag: UInt = 0u,
            @ProtoNumber(6) @JvmField var uint64_author_uin: ULong = 0u,
            @ProtoNumber(7) @JvmField var uint32_is_anony_msg: UInt = 0u,
        ): Protobuf<MessageMeta>
    }

    @Serializable
    internal data class ThemeStateNotify(
        @ProtoNumber(1) @JvmField var uint32_state: UInt = 0u,
        @ProtoNumber(2) @JvmField var bytes_feeds_id: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(3) @JvmField var bytes_theme_name: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(4) @JvmField var uint64_action_uin: ULong = 0u,
        @ProtoNumber(5) @JvmField var uint64_create_uin: ULong = 0u,
    ): Protobuf<ThemeStateNotify>

    @Serializable
    internal data class NotifyObjmsgUpdate(
        @ProtoNumber(1) @JvmField var bytes_objmsg_id: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(2) @JvmField var uint32_update_type: UInt = 0u,
        @ProtoNumber(3) @JvmField var bytes_ext_msg: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<NotifyObjmsgUpdate>

    @Serializable
    internal data class WereWolfPush(
        @ProtoNumber(1) @JvmField var uint32_push_type: UInt = 0u,
        @ProtoNumber(2) @JvmField var uint64_game_room: ULong = 0u,
        @ProtoNumber(3) @JvmField var enum_game_state: UInt = 0u,
        @ProtoNumber(4) @JvmField var uint32_game_round: UInt = 0u,
        @ProtoNumber(5) @JvmField var rpt_roles: ArrayList<Role>? = null,
        @ProtoNumber(6) @JvmField var uint64_speaker: ULong = 0u,
        @ProtoNumber(7) @JvmField var uint64_judge_uin: ULong = 0u,
        @ProtoNumber(8) @JvmField var bytes_judge_words: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(9) @JvmField var enum_operation: UInt = 0u,
        @ProtoNumber(10) @JvmField var uint64_src_user: ULong = 0u,
        @ProtoNumber(11) @JvmField var uint64_dst_user: ULong = 0u,
        @ProtoNumber(12) @JvmField var rpt_dead_users: ArrayList<Long>? = null,
        @ProtoNumber(13) @JvmField var uint32_game_result: UInt = 0u,
        @ProtoNumber(14) @JvmField var uint32_timeout_sec: UInt = 0u,
        @ProtoNumber(15) @JvmField var uint32_kill_confirmed: UInt = 0u,
        @ProtoNumber(16) @JvmField var bytes_judge_nickname: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(17) @JvmField var rpt_voted_tie_users: ArrayList<Long>? = null,
    ): Protobuf<WereWolfPush> {
        @Serializable
        internal data class GameRecord(
            @ProtoNumber(1) @JvmField var uint32_total: UInt = 0u,
            @ProtoNumber(2) @JvmField var uint32_win: UInt = 0u,
            @ProtoNumber(3) @JvmField var uint32_lose: UInt = 0u,
            @ProtoNumber(4) @JvmField var uint32_draw: UInt = 0u,
        ): Protobuf<GameRecord>

        @Serializable
        internal data class Role(
            @ProtoNumber(1) @JvmField var uint64_uin: ULong = 0u,
            @ProtoNumber(2) @JvmField var enum_type: UInt = 0u,
            @ProtoNumber(3) @JvmField var enum_state: UInt = 0u,
            @ProtoNumber(4) @JvmField var uint32_can_speak: UInt = 0u,
            @ProtoNumber(5) @JvmField var uint32_can_listen: UInt = 0u,
            @ProtoNumber(6) @JvmField var uint32_position: UInt = 0u,
            @ProtoNumber(7) @JvmField var uint32_can_vote: UInt = 0u,
            @ProtoNumber(8) @JvmField var uint32_can_voted: UInt = 0u,
            @ProtoNumber(9) @JvmField var uint32_already_checked: UInt = 0u,
            @ProtoNumber(10) @JvmField var uint32_already_saved: UInt = 0u,
            @ProtoNumber(11) @JvmField var uint32_already_poisoned: UInt = 0u,
            @ProtoNumber(12) @JvmField var uint32_player_state: UInt = 0u,
            @ProtoNumber(13) @JvmField var enum_dead_op: UInt = 0u,
            @ProtoNumber(14) @JvmField var enum_operation: UInt = 0u,
            @ProtoNumber(15) @JvmField var uint64_dst_user: ULong = 0u,
            @ProtoNumber(16) @JvmField var uint32_operation_round: UInt = 0u,
            @ProtoNumber(17) @JvmField var msg_game_record: GameRecord? = null,
            @ProtoNumber(18) @JvmField var uint32_is_werewolf: UInt = 0u,
            @ProtoNumber(19) @JvmField var uint64_defended_user: ULong = 0u,
            @ProtoNumber(20) @JvmField var uint32_is_sheriff: UInt = 0u,
        ): Protobuf<Role>
    }

    @Serializable
    internal data class GoldMsgTipsElem(
        @ProtoNumber(1) @JvmField var type: UInt = 0u,
        @ProtoNumber(2) @JvmField var billno: String = "",
        @ProtoNumber(3) @JvmField var result: UInt = 0u,
        @ProtoNumber(4) @JvmField var amount: UInt = 0u,
        @ProtoNumber(5) @JvmField var total: UInt = 0u,
        @ProtoNumber(6) @JvmField var interval: UInt = 0u,
        @ProtoNumber(7) @JvmField var finish: UInt = 0u,
        @ProtoNumber(8) @JvmField var uin: ArrayList<Long>? = null,
        @ProtoNumber(9) @JvmField var action: UInt = 0u,
    ): Protobuf<GoldMsgTipsElem>

    @Serializable
    internal data class MiniAppNotify(
        @ProtoNumber(1) @JvmField var bytes_msg: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<MiniAppNotify>

    @Serializable
    internal data class LuckyBagNotify(
        @ProtoNumber(1) @JvmField var bytes_msg_tips: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<LuckyBagNotify>

    @Serializable
    internal data class TroopFormGrayTipsInfo(
        @ProtoNumber(1) @JvmField var uint64_writer_uin: ULong = 0u,
        @ProtoNumber(2) @JvmField var uint64_creator_uin: ULong = 0u,
        @ProtoNumber(3) @JvmField var bytes_rich_content: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(4) @JvmField var bytes_opt_bytes_url: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(5) @JvmField var bytes_creator_nick: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<TroopFormGrayTipsInfo>

    @Serializable
    internal data class PersonalSetting(
        @ProtoNumber(1) @JvmField var uint32_theme_id: UInt = 0u,
        @ProtoNumber(2) @JvmField var uint32_player_id: UInt = 0u,
        @ProtoNumber(3) @JvmField var uint32_font_id: UInt = 0u,
    ): Protobuf<PersonalSetting>

    @Serializable
    internal data class MediaChangePushInfo(
        @ProtoNumber(1) @JvmField var uint32_msg_type: UInt = 0u,
        @ProtoNumber(2) @JvmField var bytes_msg_info: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(3) @JvmField var bytes_version_ctrl: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(4) @JvmField var uint64_group_id: ULong = 0u,
        @ProtoNumber(5) @JvmField var uint64_oper_uin: ULong = 0u,
        @ProtoNumber(6) @JvmField var bytes_gray_tips: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(7) @JvmField var uint64_msg_seq: ULong = 0u,
        @ProtoNumber(8) @JvmField var uint32_join_nums: UInt = 0u,
        @ProtoNumber(9) @JvmField var msg_per_setting: PersonalSetting? = null,
        @ProtoNumber(10) @JvmField var uint32_play_mode: UInt = 0u,
        @ProtoNumber(11) @JvmField var is_join_when_start: Boolean = false,
        @ProtoNumber(99) @JvmField var uint32_media_type: UInt = 0u,
        @ProtoNumber(100) @JvmField var bytes_ext_info: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<MediaChangePushInfo>

    @Serializable
    internal data class GeneralGrayTipInfo(
        @ProtoNumber(1) @JvmField var uint64_busi_type: ULong = 0u,
        @ProtoNumber(2) @JvmField var uint64_busi_id: ULong = 0u,
        @ProtoNumber(3) @JvmField var uint32_ctrl_flag: UInt = 0u,
        @ProtoNumber(4) @JvmField var uint32_c2c_type: UInt = 0u,
        @ProtoNumber(5) @JvmField var uint32_service_type: UInt = 0u,
        @ProtoNumber(6) @JvmField var uint64_templ_id: ULong = 0u,
        @ProtoNumber(7) @JvmField var rpt_msg_templ_param: ArrayList<TemplParam>? = null,
        @ProtoNumber(8) @JvmField var bytes_content: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(10) @JvmField var uint64_tips_seq_id: ULong = 0u,
        @ProtoNumber(100) @JvmField var bytes_pb_reserv: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<GeneralGrayTipInfo>

    @Serializable
    internal data class TemplParam(
        @ProtoNumber(1) @JvmField var bytes_name: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(2) @JvmField var bytes_value: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<TemplParam>

    @Serializable
    internal data class VideoChangePushInfo(
        @ProtoNumber(1) @JvmField var uint64_seq: ULong = 0u,
        @ProtoNumber(2) @JvmField var uint32_action_type: UInt = 0u,
        @ProtoNumber(3) @JvmField var uint64_group_id: ULong = 0u,
        @ProtoNumber(4) @JvmField var uint64_oper_uin: ULong = 0u,
        @ProtoNumber(5) @JvmField var bytes_gray_tips: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(6) @JvmField var uint32_join_nums: UInt = 0u,
        @ProtoNumber(7) @JvmField var uint32_join_state: UInt = 0u,
        @ProtoNumber(100) @JvmField var bytes_ext_info: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<VideoChangePushInfo>

    @Serializable
    internal data class LbsShareChangePushInfo(
        @ProtoNumber(1) @JvmField var uint32_msg_type: UInt = 0u,
        @ProtoNumber(2) @JvmField var bytes_msg_info: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(3) @JvmField var bytes_version_ctrl: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(4) @JvmField var uint64_group_id: ULong = 0u,
        @ProtoNumber(5) @JvmField var uint64_oper_uin: ULong = 0u,
        @ProtoNumber(6) @JvmField var bytes_gray_tips: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(7) @JvmField var uint64_msg_seq: ULong = 0u,
        @ProtoNumber(8) @JvmField var uint32_join_nums: UInt = 0u,
        @ProtoNumber(99) @JvmField var uint32_push_type: UInt = 0u,
        @ProtoNumber(100) @JvmField var bytes_ext_info: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<LbsShareChangePushInfo>

    @Serializable
    internal data class SingChangePushInfo(
        @ProtoNumber(1) @JvmField var uint64_seq: ULong = 0u,
        @ProtoNumber(2) @JvmField var uint32_action_type: UInt = 0u,
        @ProtoNumber(3) @JvmField var uint64_group_id: ULong = 0u,
        @ProtoNumber(4) @JvmField var uint64_oper_uin: ULong = 0u,
        @ProtoNumber(5) @JvmField var bytes_gray_tips: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(6) @JvmField var uint32_join_nums: UInt = 0u,
    ): Protobuf<SingChangePushInfo>

    @Serializable
    internal data class GroupInfoChange(
        @ProtoNumber(1) @JvmField var uint32_group_honor_switch: Int? = null,
        @ProtoNumber(2) @JvmField var uint32_group_member_level_switch: Int? = null,

        @ProtoNumber(3) @JvmField var groupFlagExt4: Int? = null,

        @ProtoNumber(4) @JvmField var uint32_appeal_deadline: Int? = null,
        @ProtoNumber(5) @JvmField var uint32_group_flag: Int? = null,

        @ProtoNumber(7) @JvmField var groupFlagExt3: Int? = null,

        @ProtoNumber(8) @JvmField var uint32_group_class_ext: Int? = null,
        @ProtoNumber(9) @JvmField var uint32_group_info_ext_seq: Int? = null,
    ): Protobuf<GroupInfoChange>

    @Serializable
    internal data class GroupAnnounceTBCInfo(
        @ProtoNumber(1) @JvmField var feeds_id: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(2) @JvmField var group_id: ULong = 0u,
        @ProtoNumber(3) @JvmField var action: UInt = 0u,
    ): Protobuf<GroupAnnounceTBCInfo>

    @Serializable
    internal data class QQVedioGamePushInfo(
        @ProtoNumber(1) @JvmField var uint32_msg_type: UInt = 0u,
        @ProtoNumber(2) @JvmField var uint64_group_code: ULong = 0u,
        @ProtoNumber(3) @JvmField var uint64_oper_uin: ULong = 0u,
        @ProtoNumber(4) @JvmField var bytes_version_ctrl: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(5) @JvmField var bytes_ext_info: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<QQVedioGamePushInfo>

    @Serializable
    internal data class QQGroupDigestMsg(
        @ProtoNumber(1) @JvmField var group_code: ULong = 0u,
        @ProtoNumber(2) @JvmField var msg_seq: UInt = 0u,
        @ProtoNumber(3) @JvmField var msg_random: UInt = 0u,
        @ProtoNumber(4) @JvmField var op_type: Int = 0,
        @ProtoNumber(5) @JvmField var msg_sender: ULong = 0u,
        @ProtoNumber(6) @JvmField var digest_oper: ULong = 0u,
        @ProtoNumber(7) @JvmField var op_time: UInt = 0u,
        @ProtoNumber(8) @JvmField var lastest_msg_seq: UInt = 0u,
        @ProtoNumber(9) @JvmField var oper_nick: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(10) @JvmField var sender_nick: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(11) @JvmField var ext_info: Int = 0,
    ): Protobuf<QQGroupDigestMsg>

    @Serializable
    internal data class StudyRoomMemberChangePush(
        @ProtoNumber(1) @JvmField var member_count: UInt = 0u,
    ): Protobuf<StudyRoomMemberChangePush>

    @Serializable
    internal data class QQVaLiveNotifyMsg(
        @ProtoNumber(1) @JvmField var bytes_uid: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(2) @JvmField var notify_type: Int = 0,
        @ProtoNumber(3) @JvmField var bytes_ext1: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(4) @JvmField var bytes_ext2: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(5) @JvmField var bytes_ext3: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<QQVaLiveNotifyMsg>

    @Serializable
    internal data class GroupAsyncNotify(
        @ProtoNumber(1) @JvmField var uint32_msg_type: UInt = 0u,
        @ProtoNumber(2) @JvmField var uint64_msg_seq: ULong = 0u,
    ): Protobuf<GroupAsyncNotify>

    @Serializable
    internal data class QQGroupDigestMsgSummary(
        @ProtoNumber(1) @JvmField var digest_oper: ULong = 0u,
        @ProtoNumber(2) @JvmField var op_type: Int = 0,
        @ProtoNumber(3) @JvmField var op_time: UInt = 0u,
        @ProtoNumber(4) @JvmField var digest_nick: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(5) @JvmField var succ_cnt: Int = 0,
        @ProtoNumber(6) @JvmField var summary_info: ArrayList<QQGroupDigestSummaryInfo>? = null,
    ): Protobuf<QQGroupDigestMsgSummary>

    @Serializable
    internal data class QQGroupDigestSummaryInfo(
        @ProtoNumber(1) @JvmField var msg_seq: UInt = 0u,
        @ProtoNumber(2) @JvmField var msg_random: UInt = 0u,
        @ProtoNumber(3) @JvmField var error_code: UInt = 0u,
    ): Protobuf<QQGroupDigestSummaryInfo>

    @Serializable
    internal data class NotifyMsgBody(
        @ProtoNumber(1) @JvmField var enumType: Int = 0,
        @ProtoNumber(2) @JvmField var msgTime: ULong = 0u,
        @ProtoNumber(3) @JvmField var msgExpires: ULong = 0u,
        @ProtoNumber(4) @JvmField var groupCode: ULong = 0u,
        @ProtoNumber(5) @JvmField var aioGrayTipsInfo: AIOGrayTipsInfo? = null,
        @ProtoNumber(6) @JvmField var opt_msg_messagebox: MessageBoxInfo? = null,
        @ProtoNumber(7) @JvmField var opt_msg_floatedtips: FloatedTipsInfo? = null,
        @ProtoNumber(8) @JvmField var opt_msg_toptips: AIOTopTipsInfo? = null,
        @ProtoNumber(9) @JvmField var opt_msg_redtips: RedGrayTipsInfo? = null,
        @ProtoNumber(10) @JvmField var opt_msg_group_notify: GroupNotifyInfo? = null,
        @ProtoNumber(11) @JvmField var opt_msg_recall: MessageRecallReminder? = null,
        @ProtoNumber(12) @JvmField var opt_msg_theme_notify: ThemeStateNotify? = null,
        @ProtoNumber(13) @JvmField var uint32_service_type: UInt = 0u,
        @ProtoNumber(14) @JvmField var opt_msg_objmsg_update: NotifyObjmsgUpdate? = null,
        @ProtoNumber(15) @JvmField var opt_msg_werewolf_push: WereWolfPush? = null,
        @ProtoNumber(16) @JvmField var opt_stcm_game_state: ApolloGameStatus.STCMGameMessage? = null,
        @ProtoNumber(17) @JvmField var apllo_msg_push: ApolloGameStatus.STPushMsgElem? = null,
        @ProtoNumber(18) @JvmField var opt_msg_goldtips: GoldMsgTipsElem? = null,
        @ProtoNumber(20) @JvmField var opt_msg_miniapp_notify: MiniAppNotify? = null,
        @ProtoNumber(21) @JvmField var senderUin: ULong = 0u,
        @ProtoNumber(22) @JvmField var opt_msg_luckybag_notify: LuckyBagNotify? = null,
        @ProtoNumber(23) @JvmField var opt_msg_troopformtips_push: TroopFormGrayTipsInfo? = null,
        @ProtoNumber(24) @JvmField var opt_msg_media_push: MediaChangePushInfo? = null,
        @ProtoNumber(26) @JvmField var opt_general_gray_tip: GeneralGrayTipInfo? = null,
        @ProtoNumber(27) @JvmField var opt_msg_video_push: VideoChangePushInfo? = null,
        @ProtoNumber(28) @JvmField var opt_lbs_share_change_plus_info: LbsShareChangePushInfo? = null,
        @ProtoNumber(29) @JvmField var opt_msg_sing_push: SingChangePushInfo? = null,
        @ProtoNumber(30) @JvmField var groupInfoChange: GroupInfoChange? = null,
        @ProtoNumber(31) @JvmField var opt_group_announce_tbc_info: GroupAnnounceTBCInfo? = null,
        @ProtoNumber(32) @JvmField var opt_qq_vedio_game_push_info: QQVedioGamePushInfo? = null,
        @ProtoNumber(33) @JvmField var opt_qq_group_digest_msg: QQGroupDigestMsg? = null,
        @ProtoNumber(34) @JvmField var opt_study_room_member_msg: StudyRoomMemberChangePush? = null,
        @ProtoNumber(35) @JvmField var opt_qq_live_notify: QQVaLiveNotifyMsg? = null,
        @ProtoNumber(36) @JvmField var opt_group_async_notidy: GroupAsyncNotify? = null,
        @ProtoNumber(37) @JvmField var opt_uint64_group_cur_msg_seq: ULong = 0u,
        @ProtoNumber(38) @JvmField var opt_group_digest_msg_summary: QQGroupDigestMsgSummary? = null,
    ): Protobuf<NotifyMsgBody>
}