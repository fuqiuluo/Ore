package moe.ore.helper

import moe.ore.util.MD5
import java.io.File
import java.io.FileInputStream

fun File.md5(length: Long = this.length()): ByteArray {
    val md5: ByteArray
    FileInputStream(this).use {
        md5 = MD5().getMD5(it, length) // 还好有支持流的md5
    }
    return md5
}
