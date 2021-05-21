package moe.ore.helper.luoapi

import moe.ore.util.JsonUtil
import moe.ore.util.OkhttpUtil

object LuoApi {
    @JvmStatic
    fun tencentServer() : TencentServer {
        var ret : TencentServer
        try {
            val okhttp = OkhttpUtil(ssl = true, proxy = false)
            okhttp.defaultUserAgent()
            val response = okhttp.get("https://www.luololi.cn/?s=Main.tencentServer")
            ret = if (response != null  && response.code == 200) {
                val str = response.body!!.string()
                val data = JsonUtil.fromObject(str)["data"].asJsonObject
                TencentServer(
                    host = data["ip"].asString,
                    port = data["port"].asInt
                )
            } else {
                TencentServer()
            }
            response?.close()
        } catch (e : Exception) {
            ret = TencentServer()
            e.printStackTrace()
        }
        return ret
    }

}

data class TencentServer (
    val host : String = "msfwifi.3g.qq.com",
    val port: Int = 8080
)
