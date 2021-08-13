package moe.ore.helper

import moe.ore.util.MD5
import java.io.File

fun File.md5(): ByteArray {
    return MD5.toMD5Byte(readBytes())
}