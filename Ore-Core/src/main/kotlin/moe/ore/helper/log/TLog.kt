package moe.ore.helper.log

import moe.ore.helper.Initializer
import java.util.logging.ConsoleHandler
import java.util.logging.Formatter
import java.util.logging.LogRecord
import java.util.logging.Logger

class TLog(private val logger : Logger) {
    /**
     * 打印日志
     *
     * @param msg String
     */
    fun info(msg : String) {
        if(isDebug) {
            logger.info(msg)
        }
    }

    /**
     * 打印错误
     * @param th Throwable
     */
    fun warn(th : Throwable) {
        if(isDebug) {
            logger.warning(th.toString())
            th.printStackTrace()
        }
    }

    fun warn(str : String) {
        if(isDebug) {
            logger.warning(str)
        }
    }

    companion object {
        private const val isDebug = Initializer.isDebug

        @JvmStatic
        fun getLogger(name: String): TLog {
            return TLog(Logger.getLogger(name))
        }

        @JvmStatic
        fun canonicalLogger(logger: Logger) {
            val handler = ConsoleHandler()
            handler.formatter = object : Formatter() {
                override fun format(record: LogRecord): String {
                    return "[${record.loggerName}][${record.level}][${System.currentTimeMillis()}] ${record.message}"
                }
            }
            logger.useParentHandlers = false
            logger.addHandler(handler)
        }
    }
}