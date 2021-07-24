package moe.ore.core.protocol.tars.service

import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField
import moe.ore.tars.TarsBase

@TarsClass(requireRead = true)
class RequestPushForceOffline : TarsBase() {
    @TarsField(id = 3)
    var sameDevice: Byte = 0

    @TarsField(id = 0)
    var uin: Long = 0

    @TarsField(id = 2)
    var tips = ""

    @TarsField(id = 1)
    var title = ""
}