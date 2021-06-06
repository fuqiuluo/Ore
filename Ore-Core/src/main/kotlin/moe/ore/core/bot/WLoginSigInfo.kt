package moe.ore.core.bot

import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsOutputStream
import moe.ore.tars.TarsStructBase

class WLoginSigInfo : TarsStructBase() {

    var d2Key: ByteArray = byteArrayOf()

    override fun writeTo(output: TarsOutputStream) {
        output.write(d2Key, 1)
    }

    override fun readFrom(input: TarsInputStream) {
        d2Key = input.read(d2Key, 1, true)
    }
}