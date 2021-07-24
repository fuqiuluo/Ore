package moe.ore.core.protocol.tars.statsvc

import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField
import moe.ore.tars.TarsBase

@TarsClass(requireRead = true)
class InstanceInfo : TarsBase() {
    @TarsField(id = 1)
    var tablet: Byte = 0

    @TarsField(id = 0)
    var appId = 0

    @TarsField(id = 4)
    var clientType: Long = 0

    @TarsField(id = 2)
    var platform: Long = 0

    @TarsField(id = 3)
    var productType: Long = 0
}