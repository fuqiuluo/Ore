package moe.ore.helper.logger

import java.text.SimpleDateFormat
import java.util.*

// 日志器嘛，咕咕咕 2021.9.5
class OLog(
        private val tag: String,
        private val dataPath: String
) {
    init {

    }


    companion object {
        @JvmStatic
        var levelE = Level.WARING

        @JvmStatic
        private val threadLocal = ThreadLocal<SimpleDateFormat>()

        @JvmStatic
        private fun getTime(): String {
            val sdf: SimpleDateFormat = threadLocal.get()
            return sdf.format(Date())
        }

        fun log(level: Level = Level.INFO, msg: Any?) {
            if (level >= levelE) {
                println("OLog:${level.name}:$msg")
            }
        }

        fun log(level: Level = Level.ERROR, msg: Any?, e: Throwable?) {
            if (level >= levelE) {
                println("OLog:${level.name}:$msg ${e?.printStackTrace()}")
            }
        }
    }
}

fun main() {
    OLog.log(Level.INFO,"dd")
}
