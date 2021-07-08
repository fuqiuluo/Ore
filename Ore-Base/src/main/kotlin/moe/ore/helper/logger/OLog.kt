package moe.ore.helper.logger

import java.text.SimpleDateFormat
import java.util.*


class OLog(
    private val tag : String,
    private val dataPath : String
) {
    init {

    }

    fun log(level: Level, msg : Any?) {

    }

    fun log(level: Level, msg : Any?, e : Throwable?) {

    }

    companion object {
        @JvmStatic
        private val threadLocal = ThreadLocal<SimpleDateFormat>()

        @JvmStatic
        private fun getTime() : String {
            val sdf: SimpleDateFormat = threadLocal.get()
            return sdf.format(Date())
        }
    }
}

fun main() {

}