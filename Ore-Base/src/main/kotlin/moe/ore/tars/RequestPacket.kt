package moe.ore.tars

class RequestPacket : TarsStructBase() {
    companion object {
        @JvmStatic
        val cacheContext = mapOf("" to "")
        @JvmStatic
        val cacheBuffer = ByteArray(0)
    }

    var packetType : Byte = 0
    var messageType = 0
    var requestId = 0
    var timeout = 0
    var version = 0
    lateinit var buffer : ByteArray
    lateinit var funcName : String
    lateinit var servantName : String
    lateinit var status : Map<String, String>
    lateinit var context : Map<String, String>

    override fun writeTo(output: TarsOutputStream) {
        output.write(this.version, 1)
        output.write(this.packetType, 2)
        output.write(this.messageType, 3)
        output.write(this.requestId, 4)
        output.write(this.servantName, 5)
        output.write(this.funcName, 6)
        output.write(this.buffer, 7)
        output.write(this.timeout, 8)
        output.write(this.context, 9)
        output.write(this.status, 10)
    }

    override fun readFrom(input: TarsInputStream) {
        try {
            this.version = input.read(this.version, 1, true)
            this.packetType = input.read(this.packetType, 2, true)
            this.messageType = input.read(this.messageType, 3, true)
            this.requestId = input.read(this.requestId, 4, true)
            this.servantName = input.readString(5, true)
            this.funcName = input.readString(6, true)
            this.buffer = input.read(cacheBuffer, 7, true)
            this.timeout = input.read(this.timeout, 8, true)
            this.context = input.read(cacheContext, 9, true) as Map<String, String>
            this.status = input.read(cacheContext, 10, true) as Map<String, String>
        } catch (e : Exception)  {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }
}