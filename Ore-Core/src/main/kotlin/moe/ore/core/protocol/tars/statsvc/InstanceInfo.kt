package moe.ore.core.protocol.tars.statsvc

import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField
import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsStructBase

@TarsClass(requireRead = true)
class InstanceInfo : TarsStructBase() {
    @TarsField(id = 1)
    var cTablet: Byte = 0

    @TarsField(id = 0)
    var iAppId = 0

    @TarsField(id = 4)
    var iClientType: Long = 0

    @TarsField(id = 2)
    var iPlatform: Long = 0

    @TarsField(id = 3)
    var iProductType: Long = 0
}