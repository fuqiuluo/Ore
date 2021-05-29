package moe.ore.util

import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsStructBase
import java.nio.charset.Charset

object TarsUtil {
    @JvmStatic
    fun <T : TarsStructBase> decodeWup(t : T, bytes : ByteArray) : T {
        val input = TarsInputStream(bytes)
        input.setServerEncoding(Charset.defaultCharset())
        t.readFrom(input)
        return t
    }

    @JvmStatic
    fun <T : TarsStructBase> decodeWup(clazz : Class<T>, bytes : ByteArray) : T? {
        return try {
            val t = clazz.newInstance()
            val input = TarsInputStream(bytes)
            input.setServerEncoding(Charset.defaultCharset())
            t.readFrom(input)
            t
        } catch (e : Exception) {
            e.printStackTrace()
            null
        }
    }

    @JvmStatic
    fun encodeRequest(base: TarsStructBase) : ByteArray {
        TODO("下次一定写！！！")
    }
}