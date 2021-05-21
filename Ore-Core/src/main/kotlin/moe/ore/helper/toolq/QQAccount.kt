package moe.ore.helper.toolq

import moe.ore.helper.bytes.ByteBuilder
import moe.ore.util.MD5

data class QQAccount (
    val uin : Long,
    val password : String
) {
    fun md5Password() = MD5.toMD5Byte(password)!!

    fun uinString() = uin.toString()

    fun md5UinAndPassword() : ByteArray {
        val builder = ByteBuilder(md5Password())
        builder.writeLong(uin)
        val out = MD5.toMD5Byte(builder.toByteArray())
        builder.close()
        return out
    }
}
