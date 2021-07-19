package moe.ore.core.protocol.tars.statsvc

import moe.ore.tars.*

@TarsClass(requireWrite = true, requireRead = true)
class VendorPushInfo : TarsStructBase() {
    @TarsField(id = 0)
    var uVendorType: Long = 0
}