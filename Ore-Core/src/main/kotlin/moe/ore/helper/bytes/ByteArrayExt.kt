package moe.ore.helper.bytes

fun ByteArray.toHexString() : String = this.joinToString("") {
    (it.toInt() and 0xFF).toString(16).padStart(2, '0')
}

