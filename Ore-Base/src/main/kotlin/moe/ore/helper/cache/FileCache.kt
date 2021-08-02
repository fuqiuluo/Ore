package moe.ore.helper.cache

import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.helper.currentTimeSeconds
import moe.ore.util.FileUtil
import java.io.File
import java.io.RandomAccessFile

class FileCache(
    var shelfLife: Int,
    cacheDir: File,
    tag: String,
) {
    private val cacheFile = File(cacheDir.absolutePath + "/$tag.c")
    var isExpired: Boolean = true

    init {
        if(cacheFile.exists() && cacheFile.canRead() && cacheFile.canWrite() && cacheFile.length() >= 8) {
            initValues()
        } else {
            FileUtil.saveFile(cacheFile.absolutePath, EMPTY_BYTE_ARRAY)
            // cacheDir.writeBytes(EMPTY_BYTE_ARRAY)
        }
    }

    private fun initValues() {
        if(cacheFile.exists()) {
            val reader = RandomAccessFile(cacheFile, "r")
            if(reader.readInt() + reader.readInt() <= currentTimeSeconds()) {
                reader.close()
                cacheFile.delete() // 过期 -> 删除
            } else {
                this.isExpired = false
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
        this.isExpired = false
        val writer = RandomAccessFile(cacheFile, "rws")
        writer.writeInt(currentTimeSeconds())
        writer.writeInt(shelfLife)
        writer.writeInt(data.size)
        writer.setLength(3 * 4)
        writer.write(data)
        writer.close()
    }
}