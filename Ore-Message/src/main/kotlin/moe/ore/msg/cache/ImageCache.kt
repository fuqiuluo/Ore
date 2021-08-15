@file:Suppress("HttpUrlsUsage")
package moe.ore.msg.cache

import moe.ore.core.helper.DataManager
import moe.ore.util.FileUtil
import moe.ore.util.OkhttpUtil
import java.io.File

internal object ImageCache {
    private var dataPaths = hashMapOf<Long, String>()

    fun init(uin: Long, dataPath: String) {
        this.dataPaths[uin] = File("$dataPath/image").also {
            if(!it.exists()) it.mkdirs()
        }.absolutePath
    }

    fun saveTroopImage(uin: Long, fileName: String, originUrl: String) = kotlin.runCatching {
        val manager = DataManager.manager(uin)
        val dataPath = dataPaths[uin]!!
        if(!getImage(uin, fileName).exists()) {
            val http = OkhttpUtil()
            val server = manager.troopPicServerList.random()
            http.get("http://" + server.ip + ":" + server.port + originUrl).use { res ->
                res.body?.let { FileUtil.saveFile("$dataPath/troop/$fileName", it.bytes()) }
            }
        }
        return@runCatching getImage(uin, fileName)
    }

    fun getImage(uin: Long, fileName: String): File {
        val dataPath = dataPaths[uin]!!
        return File("$dataPath/troop/$fileName")
    }

}