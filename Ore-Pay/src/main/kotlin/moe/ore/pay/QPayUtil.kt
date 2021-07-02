package moe.ore.pay

import moe.ore.helper.hex2ByteArray
import moe.ore.helper.toHexString
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

object QPayUtil {
    @JvmStatic
    fun getTime(): String {
        val data = Date()
        val sd = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return sd.format(data).replace("-".toRegex(), "").replace(":".toRegex(), "").replace(" ".toRegex(), "")
    }

    @JvmStatic
    fun String.formatToJson() : String {
        return String(toByteArray().toHexString().replace("00".toRegex(), "").hex2ByteArray())
    }

    fun Map<String, String>.toRequestString() : String {
        val buffer = StringBuffer()
        var isFirst = true
        this.forEach { key, value ->
            if(isFirst) {
                buffer.append(key)
                isFirst = false
            } else {
                buffer.append("&$key")
            }
            buffer.append("=")
            buffer.append(URLEncoder.encode(value, "UTF-8"))
        }
        return buffer.toString()
    }
}