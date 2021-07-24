package moe.ore.core.protocol.tars.statsvc

import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField
import moe.ore.tars.TarsBase

@TarsClass(requireRead = true)
class SvcReqMSFLoginNotify : TarsBase() {
    @TarsField(id = 1, require = true)
    var status : Byte = 0

    @TarsField(id = 2)
    var tablet : Byte = 0

    @TarsField(id = 0, require = true)
    var appId : Long = 0

    @TarsField(id = 7)
    var clientType : Long = 0

    @TarsField(id = 3)
    var platform : Long = 0

    @TarsField(id = 6)
    var productType : Long = 0

    @TarsField(id = 5)
    var info = ""

    @TarsField(id = 4)
    var title = ""

    @TarsField(id = 8)
    var instanceList : ArrayList<InstanceInfo>?  = null
}

