package moe.ore.helper.cache

import moe.ore.helper.currentTimeSeconds
import java.io.File
import java.io.RandomAccessFile

class FileCache(
        var shelfLife: Int,
        cacheDir: File,
        tag: String,
) {
    private val cacheFile = File(cacheDir.absolutePath + File.separator + tag + ".c")
    private val reader = RandomAccessFile(cacheFile, "rw")

    private var datas: ByteArray? = null

    fun get(): ByteArray? {
        if (datas != null) {
            return datas
        }
        reader.seek(0)
        if (reader.readInt() + reader.readInt() <= currentTimeSeconds()) {
//            不用做删除和关闭 因为没有读取到的时候 一会请求完成后一般会复写这个文件
//                this.isExpired = true
//                reader.close()
//                cacheFile.delete() // 过期 -> 删除
            return null
        } else {
            reader.seek(8)
            return ByteArray(reader.readInt()).apply { reader.readFully(this) }.also { datas = it }
        }
    }

//    var isExpired: Boolean = false

    init {
        if (!cacheFile.exists() || !cacheFile.canRead() || !cacheFile.canWrite()) error("without permission")
        //            initValues()
    }

//    private fun initValues() {
//        reader.seek(0)
//        if (reader.readInt() + reader.readInt() <= currentTimeSeconds()) {
//            this.isExpired = true
//            reader.close()
//            cacheFile.delete() // 过期 -> 删除
//        }
//    }

//    fun get(): ByteArray {
//        reader.seek(8)
//        val data = ByteArray(reader.readInt()).apply { reader.readFully(this) }
//        return data
//    }

    fun put(data: ByteArray): FileCache {
        reader.setLength(0)
        reader.seek(0)
        reader.writeInt(currentTimeSeconds())
        reader.writeInt(shelfLife)
        reader.writeInt(data.size)
        reader.write(data)
        return this
    }

    fun close() {
        reader.close()
    }
}