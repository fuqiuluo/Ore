package moe.ore.util

import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL


object HttpUtils {
    fun post(url: String, data: ByteArray): ByteArray? {
        try {
            val conn = URL(url).openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            conn.doInput = true
            conn.connectTimeout = 3000
            conn.readTimeout = 3000
            val out = DataOutputStream(conn.outputStream)
            out.write(data)
            out.flush()
            out.close()
            return if (200 == conn.responseCode) {
                conn.inputStream.readBytes()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}