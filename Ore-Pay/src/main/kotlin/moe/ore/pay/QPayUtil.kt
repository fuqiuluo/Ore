package moe.ore.pay

import moe.ore.helper.hex2ByteArray
import moe.ore.helper.toHexString
import moe.ore.util.BytesUtil
import moe.ore.util.DesECBUtil
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

internal object QPayUtil {
    @JvmStatic
    private val desKeys = arrayOf(
        "9973e345",
        "5dac6cf7",
        "f5c88847",
        "f02c91bd",
        "3c0c3ea1",
        "8b00b67f",
        "c28931b2",
        "c8510256",
        "c42bfdef",
        "890fe53c",
        "0d181064",
        "0ef940b7",
        "10d75d6d",
        "c5d8e9f6",
        "66c3987e",
        "c48cebe3"
    )

    @JvmStatic
    fun decryptToJsonStr(string: String, keyIndex: Int): String {
        val bytes = DesECBUtil.decryptDES(string.hex2ByteArray(), desKeys[keyIndex])
        return String(bytes).formatToJson()
    }

    @JvmStatic
    fun encryptToReqText(map: Map<String, Any>, keyIndex: Int): String {
        val sourceBody = map.toRequestString().toByteArray()
        // println(sourceBody.toHexString())
        return DesECBUtil.encryptDES(sourceBody, desKeys[keyIndex]).toHexString()
    }

    @JvmStatic
    fun getTime(): String {
        val data = Date()
        val sd = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return sd.format(data).replace("-".toRegex(), "").replace(":".toRegex(), "").replace(" ".toRegex(), "")
    }

    @JvmStatic
    fun getTimes(): String {
        val data = Date()
        val sd = SimpleDateFormat("yyyy-MM-dd HH")
        return sd.format(data).replace("-".toRegex(), "").replace(":".toRegex(), "").replace(" ".toRegex(), "")
    }

    @JvmStatic
    fun String.formatToJson(): String {
        return String(toByteArray().toHexString().replace("00".toRegex(), "").hex2ByteArray())
    }

    @JvmStatic
    fun getMsgno(uin: Long, seq: Int): String {
        return "$uin${getTime()}${BytesUtil.int16ToBuf(seq).toHexString()}"
    }

    @JvmStatic
    fun Map<String, Any>.toRequestString(): String {
        // println(this)
        val buffer = StringBuffer()
        var isFirst = true
        this.forEach { (key, value) ->
            if (isFirst) {
                buffer.append(key)
                isFirst = false
            } else {
                buffer.append("&$key")
            }
            buffer.append("=")
            buffer.append(URLEncoder.encode(value.toString(), "UTF-8"))
        }
        return buffer.toString()
    }

    fun tryToDecrypt(data: String) {
        var id = 0
        desKeys.forEach {
            DesECBUtil.decryptDES(data.hex2ByteArray(), it).let { bytes ->
                val str = String(bytes)
                if (str.startsWith("p") || str.startsWith("u") || str.startsWith("s")) {
                    println("KeyId : $id")
                    println(bytes.toHexString())
                    println(str)
                }
            }
            id++
        }
    }
}