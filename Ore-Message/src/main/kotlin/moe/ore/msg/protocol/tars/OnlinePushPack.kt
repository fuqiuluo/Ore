package moe.ore.msg.protocol.tars

import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.tars.TarsBase
import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField

@TarsClass(servantName = "OnlinePush", funcName = "SvcRespPushMsg", reqName = "resp")
internal data class SvcRespPushMsg(
    @JvmField @TarsField(0) val uin: Long = 0,
    @JvmField @TarsField(1) val delInfos: ArrayList<DelMsgInfo>? = null,
    @JvmField @TarsField(2) val svrip: Int = 0,
    @JvmField @TarsField(3) val pushToken: ByteArray? = null,
    @JvmField @TarsField(4) val serviceType: Int = 0,
): TarsBase()

@TarsClass
internal data class DelMsgInfo(
    @JvmField @TarsField(0) val fromUin: Long = 0,
    @JvmField @TarsField(1) val msgTime: Long = 0,
    @JvmField @TarsField(2) val msgSeq: Short = 0,
    @JvmField @TarsField(3) val msgCookies: ByteArray = EMPTY_BYTE_ARRAY,
    @JvmField @TarsField(4) val cmd: Short? = null,
    @JvmField @TarsField(5) val msgType: Long? = null,
    @JvmField @TarsField(6) val appId: Long? = null,
    @JvmField @TarsField(7) val sendTime: Long = 0,
    @JvmField @TarsField(9) val ssoSeq: Int? = null,
    @JvmField @TarsField(9) val ssoIp: Int? = null,
    @JvmField @TarsField(10) val clientIp: Int? = null,
): TarsBase()

@TarsClass(requireWrite = false, servantName = "OnlinePush", funcName = "SvcReqPushMsg")
internal data class SvcReqPushMsg(
    @JvmField @TarsField(0) val uin: Long = 0,
    @JvmField @TarsField(1) val msgTime: Long = 0,
    @JvmField @TarsField(2) val msgInfos: ArrayList<MsgInfo>? = null,
    @JvmField @TarsField(3) val svrip: Int = 0,
    @JvmField @TarsField(4) val syncCookie: ByteArray = EMPTY_BYTE_ARRAY,
    @JvmField @TarsField(5) val uinPairMsg: ArrayList<UinPairMsg>? = null,
    @JvmField @TarsField(6) val previews: Map<String, ByteArray>? = null,
    @JvmField @TarsField(7) val userActive: Int = 0,
    @JvmField @TarsField(12) val generalFlag: Int = 0,
): TarsBase()

@TarsClass
internal data class MsgInfo(
    @JvmField @TarsField(0) val fromUin: Long = 0,
    @JvmField @TarsField(1) val msgTime: Long = 0,
    @JvmField @TarsField(2) val msgType: Int = 0,
    @JvmField @TarsField(3) val msgSeq: Short = 0,
    @JvmField @TarsField(4) val strMsg: String? = null,
    @JvmField @TarsField(5) val realMsgTime: Int = 0,
    @JvmField @TarsField(6) val vMsg: ByteArray = EMPTY_BYTE_ARRAY,
    @JvmField @TarsField(7) val appShareId: Long = 0,
    @JvmField @TarsField(8) val msgCookies: ByteArray = EMPTY_BYTE_ARRAY,
    @JvmField @TarsField(9) val appShareCookies: ByteArray = EMPTY_BYTE_ARRAY,
    @JvmField @TarsField(10) val msgUid: Long = 0,
    @JvmField @TarsField(11) val lastChangeTime: Long = 0,
    @JvmField @TarsField(12) val cpicInfo: ArrayList<CPicInfo>? = null,
    @JvmField @TarsField(13) val shareData: ShareData? = null,
    @JvmField @TarsField(14) val fromInstId: Long = 0,
    @JvmField @TarsField(15) val remarkOfSender: ByteArray = EMPTY_BYTE_ARRAY,
    @JvmField @TarsField(16) val fromMobile: String? = null,
    @JvmField @TarsField(17) val fromName: String? = null,
    @JvmField @TarsField(18) val nickName: ArrayList<String>? = null,
    @JvmField @TarsField(19) val c2cTmpMsgHead: TempMsgHead? = null,
): TarsBase()

@TarsClass
internal data class CPicInfo(
    @JvmField @TarsField(0) val path: ByteArray = EMPTY_BYTE_ARRAY,
    @JvmField @TarsField(1) val host: ByteArray = EMPTY_BYTE_ARRAY,
): TarsBase()

@TarsClass
internal data class TempMsgHead(
    @JvmField @TarsField(0) val c2cType: Int = 0,
    @JvmField @TarsField(1) val serviceType: Int = 0,
): TarsBase()

@TarsClass
internal data class ShareData(
    @JvmField @TarsField(0) val pkgName: String? = null,
    @JvmField @TarsField(1) val msgTail: String? = null,
    @JvmField @TarsField(2) val picUrl: String? = null,
    @JvmField @TarsField(3) val url: String? = null,
): TarsBase()

@TarsClass
internal data class UinPairMsg(
    @JvmField @TarsField(1) val lastReadTime: Long = 0,
    @JvmField @TarsField(2) val peerUin: Long = 0,
    @JvmField @TarsField(3) val msgCompleted: Long = 0,
    @JvmField @TarsField(4) val msgInfos: ArrayList<MsgInfo>? = null,
): TarsBase()
