package moe.ore.core.protocol.tars.service

import moe.ore.tars.TarsBase
import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField
import moe.ore.tars.TarsOutputStream

@TarsClass(requireRead = true)
class RequestMSFForceOffline : TarsBase() {
    @TarsField(id = 2)
    var kickType: Byte = 0

    @TarsField(id = 7)
    var sameDevice: Byte = 0

    @TarsField(id = 5)
    var sigKick: Byte = 0

    @TarsField(id = 1)
    var seqno: Long = 0

    @TarsField(id = 0)
    var uin: Long = 0

    @TarsField(id = 3)
    var info = ""

    @TarsField(id = 4)
    var title = ""

    @TarsField(id = 6)
    var sigKickData: ByteArray? = null
}