package moe.ore.util

import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL


object HttpUtils {
    @JvmOverloads
    fun post(url: String, data: ByteArray, connectTimeout: Int = 3000, readTimeout: Int = 3000): ByteArray? {
        runCatching<ByteArray?> {
            val conn = URL(url).openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            conn.doInput = true
            conn.connectTimeout = connectTimeout
            conn.readTimeout = readTimeout
            val out = DataOutputStream(conn.outputStream)
            out.write(data)
            out.flush()
            out.close()
            return if (200 == conn.responseCode) {
                conn.inputStream.readBytes()
            } else {
                null
            }
        }.onSuccess { return it }.onFailure { it.printStackTrace() }
        return null
    }
}