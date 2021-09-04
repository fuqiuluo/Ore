package moe.ore.helper.cache

import java.io.File

// 模仿腾讯qq的磁盘缓存器 （ 确实垃圾
class DisketteCache {
    // private val cacheMap = hashMapOf<String, Cache>()
    private lateinit var cacheDir: File

    fun init(cacheDir: File): DisketteCache {
        if(!cacheDir.exists()) {
            cacheDir.mkdirs()
        }
        this.cacheDir = cacheDir
        return this
    }

    fun load(
            tag: String,
            shelfLife: Int = 60 * 60 * 24, // default； 1 day
    ): FileCache {
        checkInit()
        // return cacheMap.getOrPut(tag) { Cache(cacheDir, tag, maxSize) }
        return FileCache(shelfLife, cacheDir, tag)
    }

    private fun checkInit() {
        require(this::cacheDir.isInitialized) { "cacheDir must not null" }
    }
}