package moe.ore.core.protocol.tars.statsvc

import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsStructBase

class RegisterResp : TarsStructBase() {
    companion object {
        val cache_bytes_0x769_rspbody = ByteArray(1)
    }

    var bCrashFlag: Byte = 0
    var bLargeSeqUpdate: Byte = 0
    var bLogQQ: Byte = 0
    var bNeedKik: Byte = 0
    var bUpdateFlag: Byte = 0
    var bytes_0x769_rspbody: ByteArray? = null
    var cReplyCode: Byte = 0
    var iClientPort = 0
    var iHelloInterval = 300
    var iLargeSeq: Long = 0
    var iStatus = 0
    var lBid: Long = 0
    var lServerTime: Long = 0
    var lUin: Long = 0
    var strClientIP = ""
    var strResult = ""
    var timeStamp: Long = 0
    var uClientAutoStatusInterval: Long = 600
    var uClientBatteryGetInterval: Long = 86400
    var uExtOnlineStatus: Long = 0

    override fun readFrom(input: TarsInputStream) {
        this.lUin = input.read(this.lUin, 0, true);
        this.lBid = input.read(this.lBid, 1, true);
        this.cReplyCode = input.read(this.cReplyCode, 2, true);
        this.strResult = input.readString(3, true);
        this.lServerTime = input.read(this.lServerTime, 4, false);
        this.bLogQQ = input.read(this.bLogQQ, 5, false);
        this.bNeedKik = input.read(this.bNeedKik, 6, false);
        this.bUpdateFlag = input.read(this.bUpdateFlag, 7, false);
        this.timeStamp = input.read(this.timeStamp, 8, false);
        this.bCrashFlag = input.read(this.bCrashFlag, 9, false);
        this.strClientIP = input.readString(10, false);
        this.iClientPort = input.read(this.iClientPort, 11, false);
        this.iHelloInterval = input.read(this.iHelloInterval, 12, false);
        this.iLargeSeq = input.read(this.iLargeSeq, 13, false);
        this.bLargeSeqUpdate = input.read(this.bLargeSeqUpdate, 14, false);
        this.bytes_0x769_rspbody = input.read(cache_bytes_0x769_rspbody, 15, false);
        this.iStatus = input.read(this.iStatus, 16, false);
        this.uExtOnlineStatus = input.read(this.uExtOnlineStatus, 17, false);
        this.uClientBatteryGetInterval = input.read(this.uClientBatteryGetInterval, 18, false);
        this.uClientAutoStatusInterval = input.read(this.uClientAutoStatusInterval, 19, false);
    }
}