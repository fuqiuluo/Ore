@file:Suppress("PLUGIN_IS_NOT_ENABLED", "EXPERIMENTAL_API_USAGE", "ArrayInDataClass")
package moe.ore.msg.protocol.protobuf

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.protobuf.Protobuf

object ApolloGameStatus {
    @Serializable
    internal data class STMsgComm(
        @ProtoNumber(1) @JvmField var uint32_game_id: UInt = 0u,
        @ProtoNumber(2) @JvmField var str_room_id: String = "",
        @ProtoNumber(3) @JvmField var uint64_time_stamp: ULong = 0u,
        @ProtoNumber(4) @JvmField var uint32_session_id: UInt = 0u,
        @ProtoNumber(5) @JvmField var rpt_uint32_session_list: ArrayList<Int>? = null,
        @ProtoNumber(6) @JvmField var uint32_play_model: UInt = 0u,
        @ProtoNumber(7) @JvmField var str_game_comm_info: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(8) @JvmField var rpt_uint32_uin_list: ArrayList<Int>? = null,
        @ProtoNumber(9) @JvmField var uint32_room_vol: UInt = 0u,
    ): Protobuf<STMsgComm>

    @Serializable
    internal data class STGameJoinRoom(
        @ProtoNumber(1) @JvmField var uint32_room_vol: UInt = 0u,
        @ProtoNumber(2) @JvmField var rpt_uint32_uin_list: ArrayList<Int>? = null,
        @ProtoNumber(3) @JvmField var str_game_join_info: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<STGameJoinRoom>

    @Serializable
    internal data class STGameQuitRoom(
        @ProtoNumber(1) @JvmField var uint32_room_vol: UInt = 0u,
        @ProtoNumber(2) @JvmField var rpt_uint32_uin_list: ArrayList<Int>? = null,
        @ProtoNumber(3) @JvmField var str_game_quit_info: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<STGameQuitRoom>

    @Serializable
    internal data class STGameStart(
        @ProtoNumber(1) @JvmField var str_game_start_info: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<STGameStart>

    @Serializable
    internal data class STGameOver(
        @ProtoNumber(1) @JvmField var rpt_msg_winner_info: ArrayList<STScoreInfo>? = null,
        @ProtoNumber(2) @JvmField var uint32_over_type: UInt = 0u,
        @ProtoNumber(3) @JvmField var str_game_over_info: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<STGameOver>

    @Serializable
    internal data class STScoreInfo(
        @ProtoNumber(1) @JvmField var uint32_winner: UInt = 0u,
        @ProtoNumber(2) @JvmField var str_wording: String = "",
    ): Protobuf<STScoreInfo>

    @Serializable
    internal data class STGameInvalid(
        @ProtoNumber(1) @JvmField var str_game_invlid_info: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<STGameInvalid>

    @Serializable
    internal data class STGameCancel(
        @ProtoNumber(1) @JvmField var str_game_cancel_info: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<STGameCancel>

    @Serializable
    internal data class STCMGameMessage(
        @ProtoNumber(1) @JvmField var uint32_msg_cmd: UInt = 0u,
        @ProtoNumber(2) @JvmField var msg_comm: STMsgComm? = null,
        @ProtoNumber(3) @JvmField var msg_0x01: STGameJoinRoom? = null,
        @ProtoNumber(4) @JvmField var msg_0x02: STGameQuitRoom? = null,
        @ProtoNumber(5) @JvmField var msg_0x03: STGameStart? = null,
        @ProtoNumber(6) @JvmField var msg_0x04: STGameOver? = null,
        @ProtoNumber(7) @JvmField var msg_0x05: STGameInvalid? = null,
        @ProtoNumber(8) @JvmField var msg_0x06: STGameCancel? = null,
    ): Protobuf<STCMGameMessage>

    @Serializable
    internal data class STPushMsgElem0x1(
        @ProtoNumber(1) @JvmField var actionId: Int = 0,
        @ProtoNumber(2) @JvmField var actionType: Int = 0,
        @ProtoNumber(3) @JvmField var sendUin: ULong = 0u,
        @ProtoNumber(4) @JvmField var rcvUin: ULong = 0u,
        @ProtoNumber(5) @JvmField var aioType: Int = 0,
        @ProtoNumber(6) @JvmField var sessionId: ULong = 0u,
        @ProtoNumber(7) @JvmField var wordType: Int = 0,
        @ProtoNumber(8) @JvmField var diyWords: String = "",
    ): Protobuf<STPushMsgElem0x1>

    @Serializable
    internal data class STPushMsgElem0x2(
        @ProtoNumber(1) @JvmField var toUin: ULong = 0u,
        @ProtoNumber(2) @JvmField var actionId: Int = 0,
        @ProtoNumber(3) @JvmField var endTs: ULong = 0u,
        @ProtoNumber(4) @JvmField var wordType: Int = 0,
        @ProtoNumber(5) @JvmField var diyWords: String = "",
    ): Protobuf<STPushMsgElem0x2>

    @Serializable
    internal data class STPushMsgElem(
        @ProtoNumber(1) @JvmField var type: Int = 0,
        @ProtoNumber(2) @JvmField var showPlace: UInt = 0u,
        @ProtoNumber(3) @JvmField var priority: Int = 0,
        @ProtoNumber(11) @JvmField var pm1: STPushMsgElem0x1? = null,
        @ProtoNumber(12) @JvmField var pm2: STPushMsgElem0x2? = null,
        @ProtoNumber(13) @JvmField var pm3: ArrayList<STPushMsgElem0x3>? = null,
        @ProtoNumber(14) @JvmField var pm4: ArrayList<STPushMsgElem0x4>? = null,
    ): Protobuf<STPushMsgElem>

    @Serializable
    internal data class STPushMsgElem0x3(
        @ProtoNumber(1) @JvmField var busType: UInt = 0u,
        @ProtoNumber(2) @JvmField var busId: UInt = 0u,
        @ProtoNumber(3) @JvmField var dotId: UInt = 0u,
        @ProtoNumber(4) @JvmField var priority: UInt = 0u,
        @ProtoNumber(5) @JvmField var begTs: UInt = 0u,
        @ProtoNumber(6) @JvmField var endTs: UInt = 0u,
        @ProtoNumber(7) @JvmField var wording: String = "",
        @ProtoNumber(8) @JvmField var url: String = "",
        @ProtoNumber(9) @JvmField var theme: String = "",
        @ProtoNumber(10) @JvmField var actId: String = "",
        @ProtoNumber(11) @JvmField var spRegion: UInt = 0u,
        @ProtoNumber(12) @JvmField var folderIconUrl: String = "",
    ): Protobuf<STPushMsgElem0x3>

    @Serializable
    internal data class STPushMsgElem0x4(
        @ProtoNumber(1) @JvmField var appid: UInt = 0u,
        @ProtoNumber(2) @JvmField var name: String = "",
        @ProtoNumber(3) @JvmField var srcVer: UInt = 0u,
        @ProtoNumber(4) @JvmField var newVer: UInt = 0u,
    ): Protobuf<STPushMsgElem0x4>
}
