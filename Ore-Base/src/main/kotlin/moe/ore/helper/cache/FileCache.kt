package moe.ore.helper.cache

import moe.ore.helper.currentTimeSeconds
import java.io.File
import java.io.RandomAccessFile

class FileCache(
        private var shelfLife: Int,
        cacheDir: File,
        tag: String,
) {
    private val cacheFile = File(cacheDir.absolutePath + File.separator + tag + ".c")
    private val reader = RandomAccessFile(cacheFile, "rw")

    private lateinit var datas: ByteArray

    var isExpired: Boolean = true

    fun getAndClose(): ByteArray {
        return get().also { close() }
    }

    fun get(): ByteArray {
        if (this::datas.isInitialized) {
            return datas
        }
        if (!isExpired) {
            reader.seek(8)
            return ByteArray(reader.readInt()).apply { reader.readFully(this) }.also { datas = it }
        }
        error("cache isExpired")
    }

    init {
        if (!cacheFile.exists() || !cacheFile.canRead() || !cacheFile.canWrite()) error("without permission")
        initValues()
    }

    private fun initValues() {
        reader.seek(0)
        if (reader.readInt() + reader.readInt() <= currentTimeSeconds()) {
            this.isExpired = true
        } else {
            isExpired = false
        }
    }

    fun put(data: ByteArray): FileCache {
        reader.setLength(0)
        reader.seek(0)
        reader.writeInt(currentTimeSeconds())
        reader.writeInt(shelfLife)
        reader.writeInt(data.size)
        reader.write(data)
        return this
    }

    fun putAndClose(data: ByteArray) {
        put(data).also { close() }
    }

    fun close() {
        reader.close()
    }
}