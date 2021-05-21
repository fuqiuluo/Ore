package moe.ore.helper.bytes

fun String.hex2ByteArray(): ByteArray {
    val s = this.replace(" ", "").replace("\n", "").replace("\t", "").replace("\r", "")
    val bs = ByteArray(s.length / 2)
    for (i in 0 until s.length / 2){
        bs[i] = s.substring(i * 2, i * 2 + 2).toInt(16).toByte()
    }
    return bs
}