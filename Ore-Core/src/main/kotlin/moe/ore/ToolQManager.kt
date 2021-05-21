package moe.ore

import moe.ore.helper.toolq.QQAccount

object ToolQManager {
    @JvmStatic
    var botMaps = hashMapOf<Long, ToolQ>()

    @JvmStatic
    fun getToolQ(uin : Long) = botMaps[uin]

    @JvmStatic
    fun addToolQ(uin: Long, password : String, isHd : Boolean = false) : ToolQ {
        val toolQ = ToolQ(
            QQAccount(uin, password),
            isHd = isHd
        )
        botMaps[uin] = toolQ
        return toolQ
    }

    @JvmStatic
    fun removeToolQ(uin: Long) {
        botMaps.remove(uin)
    }
}