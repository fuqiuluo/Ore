package moe.ore.core.protocol.tars.statsvc

import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField
import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsStructBase

@TarsClass(requireRead = true)
class SvcReqMSFLoginNotify : TarsStructBase() {
    @TarsField(id = 1, require = true)
    var cStatus : Byte = 0

    @TarsField(id = 2)
    var cTablet : Byte = 0

    @TarsField(id = 0, require = true)
    var iAppId : Long = 0

    @TarsField(id = 7)
    var iClientType : Long = 0

    @TarsField(id = 3)
    var iPlatform : Long = 0

    @TarsField(id = 6)
    var iProductType : Long = 0

    @TarsField(id = 5)
    var strInfo = ""

    @TarsField(id = 4)
    var strTitle = ""

    @TarsField(id = 8)
    var vecInstanceList : ArrayList<InstanceInfo>?  = null
}

