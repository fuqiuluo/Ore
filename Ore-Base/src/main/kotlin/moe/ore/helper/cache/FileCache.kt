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
    var isExpired: Boolean = false

    init {
        if(cacheFile.exists() && cacheFile.canRead() && cacheFile.canWrite()) {
            initValues()
        } else error("without permission")
    }

    private fun initValues() {
        if(cacheFile.exists()) {
            val reader = RandomAccessFile(cacheFile, "r")
            if(reader.readInt() + reader.readInt() <= currentTimeSeconds()) {
                this.isExpired = true
                reader.close()
                cacheFile.delete() // 过期 -> 删除
            }
        }
    }

    fun get(): ByteArray {
        val reader = RandomAccessFile(cacheFile, "r")
        reader.skipBytes(8)
        val data = ByteArray(reader.readInt()).apply { reader.readFully(this) }
        reader.close()
        return data
    }

    fun edit(data: ByteArray) {
        val writer = RandomAccessFile(cacheFile, "rws")
        writer.cl
        writer.writeInt(currentTimeSeconds())
        writer.writeInt(shelfLife)
        writer.writeInt(data.size)
        writer.write(data)
        writer.close()
    }
}