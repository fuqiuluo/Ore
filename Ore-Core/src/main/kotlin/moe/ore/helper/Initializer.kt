package moe.ore.helper

import moe.ore.ToolQManager
import moe.ore.helper.log.TLog
import java.io.File
import java.util.logging.*

/**
 * 系统初始化器
 */
object Initializer {
    /**
     * 是否开启调试模式
     */
    const val isDebug = true

    @JvmStatic
    val initTime = System.currentTimeMillis()

    @JvmStatic
    private var isInit = false

    /**
     * 保存数据的路径
     */
    @JvmStatic
    var dataDirectory = File("data")

    /**
     * 是否将控制台运行日志输出到文件
     */
    private const val isLogOutFile = false
    private val logOutFile = "${dataDirectory.absolutePath}/myLog.log"
    @JvmStatic
    lateinit var logger : Logger

    @JvmStatic
    fun init() {
        if (!isInit) {
            if (isDebug) {
                // 设置日志为全局日志
                logger = Logger.getLogger("all")
                logger.level = Level.ALL
                if (isLogOutFile) {
                   logger.addHandler(FileHandler(logOutFile, true))
                }
                TLog.canonicalLogger(logger)
            }

            Runtime.getRuntime().addShutdownHook(object : Thread("MustShutDownHook") {
                override fun run() {
                    ToolQManager.botMaps.forEach { (_, bot) -> bot.shut() }

                    // 打断所有正在运行的线程，释放资源
                    currentThread().interrupt()
                }
            })
            isInit = true
        }
    }


}
