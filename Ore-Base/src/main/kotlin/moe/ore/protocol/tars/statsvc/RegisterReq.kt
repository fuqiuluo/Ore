package moe.ore.protocol.tars.statsvc

import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsOutputStream
import moe.ore.tars.TarsStructBase


class RegisterReq : TarsStructBase() {
    companion object {
        var cache_bytes_0x769_reqbody = ByteArray(1)
        var cache_vecDevParam = ByteArray(1)
        var cache_vecGuid = ByteArray(1)
        var cache_vecServerBuf = ByteArray(1)
    }

    var bIsOnline: Byte = 0
    var bIsSetStatus: Byte = 0
    var bIsShowOnline: Byte = 0
    var bKikPC: Byte = 0
    var bKikWeak: Byte = 0
    var bOnlinePush: Byte = 0
    var bOpenPush: Byte = 1
    var bRegType: Byte = 0
    var bSetMute: Byte = 0
    var bSilentPush: Byte = 0
    var bytes0x769ReqBody: ByteArray? = null
    var cConnType: Byte = 0
    var cNetType: Byte = 0
    var cNotifySwitch: Byte = 0
    var iBatteryStatus = 0
    var iLargeSeq: Long = 0
    var iLastWatchStartTime: Long = 0
    var iLocaleID = 2052
    var iOSVersion: Long = 0
    var iStatus = 11
    var lBid: Long = 0
    var lCpId: Long = 0
    var lUin: Long = 0
    var sBuildVer = ""
    var sChannelNo = ""
    var sOther = ""
    var stVendorPushInfo: VendorPushInfo? = null
    var strDevName = ""
    var strDevType = ""
    var strIOSIdfa = ""
    var strOSVer = ""
    var strVendorName = ""
    var strVendorOSName = ""
    var timeStamp: Long = 0
    var uExtOnlineStatus: Long = 0
    var uNewSSOIp: Long = 0
    var uOldSSOIp: Long = 0
    var vecDevParam: ByteArray? = null
    var vecGuid: ByteArray? = null
    var vecServerBuf: ByteArray? = null

    override fun writeTo(stream: TarsOutputStream) {
        stream.write(lUin, 0)
        stream.write(lBid, 1)
        stream.write(cConnType, 2)
        stream.write(sOther, 3)
        stream.write(iStatus, 4)
        stream.write(bOnlinePush, 5)
        stream.write(bIsOnline, 6)
        stream.write(bIsShowOnline, 7)
        stream.write(bKikPC, 8)
        stream.write(bKikWeak, 9)
        stream.write(timeStamp, 10)
        stream.write(iOSVersion, 11)
        stream.write(cNetType, 12)
        stream.write(sBuildVer, 13)
        stream.write(bRegType, 14)
        vecDevParam?.let { stream.write(it, 15) }
        vecGuid?.let { stream.write(it, 16) }
        stream.write(iLocaleID, 17)
        stream.write(bSilentPush, 18)
        stream.write(strDevName, 19)
        stream.write(strDevType, 20)
        stream.write(strOSVer, 21)
        stream.write(bOpenPush, 22)
        stream.write(iLargeSeq, 23)
        stream.write(iLastWatchStartTime, 24)
        /*
        val arrayList = vecBindUin
        if (arrayList != null) {
            stream.write(arrayList, 25)
        }
        没什么鸟用的东西，咕咕咕吧
         */
        stream.write(uOldSSOIp, 26)
        stream.write(uNewSSOIp, 27)
        stream.write(sChannelNo, 28)
        stream.write(lCpId, 29)
        stream.write(strVendorName, 30)
        stream.write(strVendorOSName, 31)
        stream.write(strIOSIdfa, 32)
        bytes0x769ReqBody?.let { stream.write(it, 33) }
        stream.write(bIsSetStatus, 34)
        vecServerBuf?.let { stream.write(it, 35) }
        stream.write(bSetMute, 36)
        stream.write(cNotifySwitch, 37)
        stream.write(uExtOnlineStatus, 38)
        stream.write(iBatteryStatus, 39)
        stVendorPushInfo?.let { stream.write(it, 42) }
    }

    override fun readFrom(stream: TarsInputStream) {
        lUin = stream.read(lUin, 0, true)
        lBid = stream.read(lBid, 1, true)
        cConnType = stream.read(cConnType, 2, true)
        sOther = stream.readString(3, true)
        iStatus = stream.read(iStatus, 4, false)
        bOnlinePush = stream.read(bOnlinePush, 5, false)
        bIsOnline = stream.read(bIsOnline, 6, false)
        bIsShowOnline = stream.read(bIsShowOnline, 7, false)
        bKikPC = stream.read(bKikPC, 8, false)
        bKikWeak = stream.read(bKikWeak, 9, false)
        timeStamp = stream.read(timeStamp, 10, false)
        iOSVersion = stream.read(iOSVersion, 11, false)
        cNetType = stream.read(cNetType, 12, false)
        sBuildVer = stream.readString(13, false)
        bRegType = stream.read(bRegType, 14, false)
        vecDevParam = stream.read(cache_vecDevParam, 15, false)
        vecGuid = stream.read(cache_vecGuid, 16, false)
        iLocaleID = stream.read(iLocaleID, 17, false)
        bSilentPush = stream.read(bSilentPush, 18, false)
        strDevName = stream.readString(19, false)
        strDevType = stream.readString(20, false)
        strOSVer = stream.readString(21, false)
        bOpenPush = stream.read(bOpenPush, 22, false)
        iLargeSeq = stream.read(iLargeSeq, 23, false)
        iLastWatchStartTime = stream.read(iLastWatchStartTime, 24, false)
        uOldSSOIp = stream.read(uOldSSOIp, 26, false)
        uNewSSOIp = stream.read(uNewSSOIp, 27, false)
        sChannelNo = stream.readString(28, false)
        lCpId = stream.read(lCpId, 29, false)
        strVendorName = stream.readString(30, false)
        strVendorOSName = stream.readString(31, false)
        strIOSIdfa = stream.readString(32, false)
        bytes0x769ReqBody = stream.read(cache_bytes_0x769_reqbody, 33, false)
        bIsSetStatus = stream.read(bIsSetStatus, 34, false)
        vecServerBuf = stream.read(cache_vecServerBuf, 35, false)
        bSetMute = stream.read(bSetMute, 36, false)
        cNotifySwitch = stream.read(cNetType, 37, false)
        uExtOnlineStatus = stream.read(uExtOnlineStatus, 38, false)
        iBatteryStatus = stream.read(iBatteryStatus, 39, false)
        stVendorPushInfo = stream.read(stVendorPushInfo, 42, false)
    }
}