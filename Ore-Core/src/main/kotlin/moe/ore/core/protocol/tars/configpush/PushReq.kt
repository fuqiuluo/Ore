package moe.ore.core.protocol.tars.configpush

import moe.ore.tars.TarsBase
import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField

@TarsClass(requireRead = true)
internal class PushReq : TarsBase() {
    @TarsField(id = 1) var type = 0

    @TarsField(id = 2) var data : ByteArray? = null

    @TarsField(id = 3) var seq = 0L
}