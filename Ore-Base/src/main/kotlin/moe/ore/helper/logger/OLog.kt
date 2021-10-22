package moe.ore.helper.logger

import java.text.SimpleDateFormat
import java.util.*

// 日志器嘛，咕咕咕 2021.9.5
object OLog {
    @JvmStatic
    var minLogLevel = Level.WARING

    @JvmStatic
    private val threadLocal = ThreadLocal<SimpleDateFormat>()

    @JvmStatic
    private fun getTime(): String {
        val sdf: SimpleDateFormat = threadLocal.get()
        return sdf.format(Date())
    }

    @JvmOverloads
    fun log(level: Level = Level.INFO, msg: Any?) {
        if (minLogLevel.num == Level.ALL.num || minLogLevel.num <= level.num) {
            println("OLog:${level.name}:$msg")
        }
    }

    @JvmOverloads
    fun error(level: Level = Level.ERROR, msg: Any? = null, e: Throwable? = null) {
        if (minLogLevel.num == Level.ALL.num || minLogLevel.num <= level.num) {
            println("OLog:${level.name}:${msg ?: ""} ${e?.printStackTrace()}")
        }
    }
}
