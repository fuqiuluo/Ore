package moe.ore.tars

@TarsClass(requireRead = true, requireWrite = true)
class RequestPacket : TarsBase() {
    @TarsField(2) var packetType : Byte = 0

    @TarsField(3) var messageType = 0

    @TarsField(4) var requestId = 0

    @TarsField(8) var timeout = 0

    @TarsField(1) var version = 0

    @TarsField(7) lateinit var buffer : ByteArray

    @TarsField(6) lateinit var funcName : String

    @TarsField(5) lateinit var servantName : String

    @TarsField(10) lateinit var status : Map<String, String>

    @TarsField(9) lateinit var context : Map<String, String>
}