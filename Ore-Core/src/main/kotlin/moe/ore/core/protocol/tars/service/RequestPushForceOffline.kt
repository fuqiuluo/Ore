package moe.ore.core.protocol.tars.service

import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsStructBase

class RequestPushForceOffline : TarsStructBase() {
    var bSameDevice: Byte = 0
    var lUin: Long = 0
    var strTips = ""
    var strTitle = ""

    override fun readFrom(input: TarsInputStream) {
        lUin = input.read(lUin, 0, true)
        strTitle = input.readString(1, false)
        strTips = input.readString(2, false)
        bSameDevice = input.read(bSameDevice, 3, false)
    }
}