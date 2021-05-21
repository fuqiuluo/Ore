package moe.ore.helper.toolq.packet

import moe.ore.helper.bytes.ByteBuilder

data class Packet(
    val cmd : String,
    val seq : Int,
    val body : ByteArray
) {
    fun bodyWithSize() : ByteArray {
        val builder = ByteBuilder()
        builder.writeInt(body.size + 4)
        builder.writeBytes(body)
        val out = builder.toByteArray()
        builder.close()
        return out
    }
}
