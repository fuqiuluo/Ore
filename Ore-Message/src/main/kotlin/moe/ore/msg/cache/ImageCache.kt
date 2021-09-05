@file:Suppress("HttpUrlsUsage")
package moe.ore.msg.cache

import moe.ore.core.helper.DataManager
import moe.ore.helper.md5
import moe.ore.helper.toHexString
import moe.ore.highway.util.ImageType
import moe.ore.highway.util.ImageUtil
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

    fun saveTroopImage(uin: Long, file: File) = kotlin.runCatching {
        val md5 = file.md5()
        val fileName = md5.toHexString() + when(ImageUtil.getImageType(file)) {
            ImageType.PNG -> ".png"
            ImageType.JPG -> ".jpg"
            ImageType.GIF -> ".gif"
            else -> ".ore"
        }
        val dataPath = dataPaths[uin]!!
        if(!getImage(uin, fileName).exists()) {
            FileUtil.saveFile("$dataPath/$fileName", file.inputStream())
        }
        return@runCatching getImage(uin, fileName)
    }

    fun saveTroopImage(uin: Long, fileName: String, md5: ByteArray) = kotlin.runCatching {
        val manager = DataManager.manager(uin)
        val dataPath = dataPaths[uin]!!
        if(!getImage(uin, fileName).exists()) {
            val http = OkhttpUtil()
            val server = manager.troopPicServerList.random()
            // println("cache image downlo9ad url: " + "http://" + server.ip + ":" + server.port + originUrl)
            http.get("http://" + server.ip + ":" + server.port + "/gchatpic_new/0/0-0-${md5.toHexString()}/0?term=2").use { res ->
                res.body?.let {
                    FileUtil.saveFile("$dataPath/$fileName", it.byteStream())
                }
            }
        }
        return@runCatching getImage(uin, fileName)
    }

    fun getImage(uin: Long, fileName: String): File {
        val dataPath = dataPaths[uin]!!
        return File("$dataPath/$fileName")
    }

}