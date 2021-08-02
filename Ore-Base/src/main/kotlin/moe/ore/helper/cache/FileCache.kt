package moe.ore.helper.cache

import moe.ore.helper.currentTimeSeconds
import moe.ore.tars.TarsBase
import java.io.File
import java.io.RandomAccessFile

class FileCache(
        var shelfLife: Int,
        cacheDir: File,
        tag: String,
) {
    private val cacheFile = File(cacheDir.absolutePath + File.separator + tag + ".c")
    // private val accessFile = RandomAccessFile(cacheFile, "rw")

    /**
     * cache in memory
     */
    // private lateinit var datas: ByteArray

    /**
     * is expired
     */
    var isExpired: Boolean = true

    fun get(): ByteArray {
        if(isExpired) error("cache is expired...")
        return RandomAccessFile(cacheFile, "r").use {
            it.skipBytes(8)
            ByteArray(it.readInt()).apply { it.readFully(this) }
        }
    }

    init {
        if (!cacheFile.exists() || !cacheFile.canRead() || !cacheFile.canWrite()) error("without permission")
        initValues()
    }

    private fun initValues() {
        val accessFile = RandomAccessFile(cacheFile, "r")
        if (accessFile.readInt() + accessFile.readInt() > currentTimeSeconds()) {
            isExpired = false
        }
    }

    fun put(data: ByteArray) {
        val accessFile = RandomAccessFile(cacheFile, "r")
        accessFile.setLength(0)
        accessFile.seek(0)
        accessFile.writeInt(currentTimeSeconds())
        accessFile.writeInt(shelfLife)
        accessFile.writeInt(data.size)
        accessFile.write(data)
    }

    fun put(tars: TarsBase) {
        put(tars.toByteArray())
    }
}