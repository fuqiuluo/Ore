package moe.ore.core.protocol.tars.statsvc

import moe.ore.tars.TarsOutputStream
import moe.ore.tars.TarsStructBase

class RegisterReq(
    var bIsOnline: Byte = 0,
    var bIsSetStatus: Byte = 0,
    var bIsShowOnline: Byte = 0,
    var bKikPC: Byte = 0,
    var bKikWeak: Byte = 0,
    var bOnlinePush: Byte = 0,
    var bOpenPush: Byte = 1,
    var bRegType: Byte = 0,
    var bSetMute: Byte = 0,
    var bSilentPush: Byte = 0,
    var bytes0x769ReqBody: ByteArray? = null,
    var cConnType: Byte = 0,
    var cNetType: Byte = 0,
    var cNotifySwitch: Byte = 0,
    var iBatteryStatus : Int = 0,
    var iLargeSeq: Long = 0,
    var iLastWatchStartTime: Long = 0,
    var iLocaleID : Int = 2052,
    var iOSVersion: Int = 0,
    var iStatus : Int = 11,
    var lBid: Long = 0,
    var lCpId: Long = 0,
    var lUin: Long = 0,
    var sBuildVer : String = "",
    var sChannelNo : String = "",
    var sOther : String = "",
    var stVendorPushInfo: VendorPushInfo? = null,
    var strDevName : String = "",
    var strDevType : String = "",
    var strIOSIdfa : String = "",
    var strOSVer : String = "",
    var strVendorName : String = "",
    var strVendorOSName : String = "",
    var timeStamp: Long = 0,
    var uExtOnlineStatus: Long = 0,
    var uNewSSOIp: Long = 0,
    var uOldSSOIp: Long = 0,
    var vecDevParam: ByteArray? = null,
    var vecGuid: ByteArray? = null,
    var vecServerBuf: ByteArray? = null
) : TarsStructBase() {
    override fun funcName(): String = "SvcReqRegister"

    override fun servantName(): String = "PushService"

    override fun reqName(): String = "SvcReqRegister"

    override fun writeTo(output: TarsOutputStream) {
        output.write(lUin, 0)
        output.write(lBid, 1)
        output.write(cConnType, 2)
        output.write(sOther, 3)
        output.write(iStatus, 4)
        output.write(bOnlinePush, 5)
        output.write(bIsOnline, 6)
        output.write(bIsShowOnline, 7)
        output.write(bKikPC, 8)
        output.write(bKikWeak, 9)
        output.write(timeStamp, 10)
        output.write(iOSVersion, 11)
        output.write(cNetType, 12)
        output.write(sBuildVer, 13)
        output.write(bRegType, 14)
        vecDevParam?.let { output.write(it, 15) }
        vecGuid?.let { output.write(it, 16) }
        output.write(iLocaleID, 17)
        output.write(bSilentPush, 18)
        output.write(strDevName, 19)
        output.write(strDevType, 20)
        output.write(strOSVer, 21)
        output.write(bOpenPush, 22)
        output.write(iLargeSeq, 23)
        output.write(iLastWatchStartTime, 24)
        /*
        val arrayList = vecBindUin
        if (arrayList != null) {
            stream.write(arrayList, 25)
        }
        没什么鸟用的东西，咕咕咕吧
         */
        output.write(uOldSSOIp, 26)
        output.write(uNewSSOIp, 27)
        output.write(sChannelNo, 28)
        output.write(lCpId, 29)
        output.write(strVendorName, 30)
        output.write(strVendorOSName, 31)
        output.write(strIOSIdfa, 32)
        bytes0x769ReqBody?.let { output.write(it, 33) }
        output.write(bIsSetStatus, 34)
        vecServerBuf?.let { output.write(it, 35) }
        output.write(bSetMute, 36)
        output.write(cNotifySwitch, 37)
        output.write(uExtOnlineStatus, 38)
        output.write(iBatteryStatus, 39)
        stVendorPushInfo?.let { output.write(it, 42) }
    }
}