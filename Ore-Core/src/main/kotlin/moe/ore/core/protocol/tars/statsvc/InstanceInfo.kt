package moe.ore.core.protocol.tars.statsvc

import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsStructBase

class InstanceInfo : TarsStructBase() {
    var cTablet: Byte = 0
    var iAppId = 0
    var iClientType: Long = 0
    var iPlatform: Long = 0
    var iProductType: Long = 0

    override fun readFrom(input: TarsInputStream) {
        iAppId = input.read(iAppId, 0, false)
        cTablet = input.read(cTablet, 1, false)
        iPlatform = input.read(iPlatform, 2, false)
        iProductType = input.read(iProductType, 3, false)
        iClientType = input.read(iClientType, 4, false)
    }
}