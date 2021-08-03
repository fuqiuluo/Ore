package moe.ore.helper.cache

import moe.ore.helper.currentTimeSeconds
import moe.ore.tars.TarsBase
import moe.ore.tars.TarsInputStream
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

    fun <T: TarsBase> getTars(src: T): T {
        src.readFrom(TarsInputStream(get()))
        return src
    }

    init {
        if(cacheFile.exists()) {
            if (!cacheFile.canRead() || !cacheFile.canWrite()) {
                error("without permission")
            } else initValues()
        }
    }

    private fun initValues() = RandomAccessFile(cacheFile, "r").use {
        if (it.readInt() + it.readInt() > currentTimeSeconds()) {
            isExpired = false
        }
    }

    fun put(data: ByteArray) = RandomAccessFile(cacheFile, "rw").use {
        it.setLength(0)
        it.seek(0)
        it.writeInt(currentTimeSeconds())
        it.writeInt(shelfLife)
        it.writeInt(data.size)
        it.write(data)
    }

    fun put(tars: TarsBase) = put(tars.toByteArray())
}