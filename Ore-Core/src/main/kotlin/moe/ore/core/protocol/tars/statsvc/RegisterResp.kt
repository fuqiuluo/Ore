package moe.ore.core.protocol.tars.statsvc

import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField
import moe.ore.tars.TarsStructBase

@TarsClass(
    requireRead = true,
    respName = "SvcRespRegister"
)
class RegisterResp : TarsStructBase() {
    @TarsField(id = 9)
    var bCrashFlag: Byte = 0

    @TarsField(id = 14)
    var bLargeSeqUpdate: Byte = 0

    @TarsField(id = 5)
    var bLogQQ: Byte = 0

    @TarsField(id = 6)
    var bNeedKik: Byte = 0

    @TarsField(id = 7)
    var bUpdateFlag: Byte = 0

    @TarsField(id = 15)
    var bytes_0x769_rspbody: ByteArray? = null

    @TarsField(id = 2)
    var cReplyCode: Byte = 0

    @TarsField(id = 11)
    var iClientPort = 0

    @TarsField(id = 12)
    var iHelloInterval = 300

    @TarsField(id = 13)
    var iLargeSeq: Long = 0

    @TarsField(id = 16)
    var iStatus = 0

    @TarsField(id = 1)
    var lBid: Long = 0

    @TarsField(id = 4)
    var lServerTime: Long = 0

    @TarsField(id = 0)
    var lUin: Long = 0

    @TarsField(id = 10)
    var strClientIP = ""

    @TarsField(id = 3)
    var strResult = ""

    @TarsField(id = 8)
    var timeStamp: Long = 0

    @TarsField(id = 19)
    var uClientAutoStatusInterval: Long = 600

    @TarsField(id = 18)
    var uClientBatteryGetInterval: Long = 86400

    @TarsField(id = 17)
    var uExtOnlineStatus: Long = 0
}