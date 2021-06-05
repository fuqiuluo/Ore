package moe.ore.core.net.decoder

data class PacketResponse (
    val body : ByteArray,
    val charsetName : String = "utf-8"
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PacketResponse

        if (!body.contentEquals(other.body)) return false
        if (charsetName != other.charsetName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = body.contentHashCode()
        result = 31 * result + charsetName.hashCode()
        return result
    }
}