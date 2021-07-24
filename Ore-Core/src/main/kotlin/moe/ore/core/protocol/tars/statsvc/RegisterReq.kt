package moe.ore.core.protocol.tars.statsvc

import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField
import moe.ore.tars.TarsBase

@TarsClass(
    requireWrite = true,
    servantName = "PushService",
    funcName = "SvcReqRegister",
    reqName = "SvcReqRegister"
)
class RegisterReq(
    @TarsField(id = 6)
    var bIsOnline: Byte = 0,

    @TarsField(id = 34)
    var bIsSetStatus: Byte = 0,

    @TarsField(id = 7)
    var bIsShowOnline: Byte = 0,

    @TarsField(id = 8)
    var bKikPC: Byte = 0,

    @TarsField(id = 9)
    var bKikWeak: Byte = 0,

    @TarsField(id = 5)
    var bOnlinePush: Byte = 0,

    @TarsField(id = 22)
    var bOpenPush: Byte = 1,

    @TarsField(id = 14)
    var bRegType: Byte = 0,

    @TarsField(id = 36)
    var bSetMute: Byte = 0,

    @TarsField(id = 18)
    var bSilentPush: Byte = 0,

    @TarsField(id = 33)
    var bytes0x769ReqBody: ByteArray? = null,

    @TarsField(id = 2)
    var cConnType: Byte = 0,

    @TarsField(id = 12)
    var cNetType: Byte = 0,

    @TarsField(id = 37)
    var cNotifySwitch: Byte = 0,

    @TarsField(id = 39)
    var iBatteryStatus : Int = 0,

    @TarsField(id = 23)
    var iLargeSeq: Long = 0,

    @TarsField(id = 24)
    var iLastWatchStartTime: Long = 0,

    @TarsField(id = 17)
    var iLocaleID : Int = 2052,

    @TarsField(id = 11)
    var iOSVersion: Int = 0,

    @TarsField(id = 4)
    var iStatus : Int = 11,

    @TarsField(id = 1)
    var lBid: Long = 0,

    @TarsField(id = 29)
    var lCpId: Long = 0,

    @TarsField(id = 0)
    var lUin: Long = 0,

    @TarsField(id = 13)
    var sBuildVer : String = "",

    @TarsField(id = 28)
    var sChannelNo : String = "",

    @TarsField(id = 3)
    var sOther : String = "",

    @TarsField(id = 42)
    var stVendorPushInfo: VendorPushInfo? = null,

    @TarsField(id = 19)
    var strDevName : String = "",

    @TarsField(id = 20)
    var strDevType : String = "",

    @TarsField(id = 32)
    var strIOSIdfa : String = "",

    @TarsField(id = 21)
    var strOSVer : String = "",

    @TarsField(id = 30)
    var strVendorName : String = "",

    @TarsField(id = 31)
    var strVendorOSName : String = "",

    @TarsField(id = 10)
    var timeStamp: Long = 0,

    @TarsField(id = 38)
    var uExtOnlineStatus: Long = 0,

    @TarsField(id = 27)
    var uNewSSOIp: Long = 0,

    @TarsField(id = 26)
    var uOldSSOIp: Long = 0,

    @TarsField(id = 15)
    var vecDevParam: ByteArray? = null,

    @TarsField(id = 16)
    var vecGuid: ByteArray? = null,

    @TarsField(id = 35)
    var vecServerBuf: ByteArray? = null
) : TarsBase()