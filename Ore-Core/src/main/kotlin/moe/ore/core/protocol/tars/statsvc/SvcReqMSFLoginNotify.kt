package moe.ore.core.protocol.tars.statsvc

import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsStructBase

class SvcReqMSFLoginNotify : TarsStructBase() {
    companion object {
        private val cache_vecInstanceList = ArrayList<InstanceInfo>().apply { add(InstanceInfo()) }
    }

    var cStatus : Byte = 0
    var cTablet : Byte = 0
    var iAppId : Long = 0
    var iClientType : Long = 0
    var iPlatform : Long = 0
    var iProductType : Long = 0
    var strInfo = ""
    var strTitle = ""
    var vecInstanceList : ArrayList<InstanceInfo>?  = null

    override fun readFrom(input : TarsInputStream) {
        this.iAppId = input.read(this.iAppId, 0, true)
        this.cStatus = input.read(this.cStatus, 1, true)
        this.cTablet = input.read(this.cTablet, 2, false)
        this.iPlatform = input.read(this.iPlatform, 3, false)
        this.strTitle = input.readString(4, false)
        this.strInfo = input.readString(5, false)
        this.iProductType = input.read(this.iProductType, 6, false)
        this.iClientType = input.read(this.iClientType, 7, false)
        this.vecInstanceList = input.read(cache_vecInstanceList, 8, false) as ArrayList<InstanceInfo>?
    }
}

