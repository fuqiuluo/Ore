package moe.ore.helper.netty

data class NettyResponse(
    val body : ByteArray,
    var charsetName : String = "UTF-8"
)
