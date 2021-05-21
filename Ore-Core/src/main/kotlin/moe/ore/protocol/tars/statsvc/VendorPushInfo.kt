package moe.ore.protocol.tars.statsvc

import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsOutputStream
import moe.ore.tars.TarsStructBase

class VendorPushInfo : TarsStructBase() {
    var uVendorType: Long = 0

    override fun writeTo(output: TarsOutputStream) {
        output.write(uVendorType, 0)
    }

    override fun readFrom(input: TarsInputStream) {
        uVendorType = input.read(uVendorType, 0, false)
    }

}