package moe.ore.core

import moe.ore.api.Ore

/**
 * Ore管理器
 *
 * 批量管理已登录QQ
 */
object OreManager {
    private val botMap = hashMapOf<Long, Ore>()

    /**
     * 获取Bot
     *
     * @param uin Long
     * @return Ore?
     */
    fun getBot(uin : Long) = botMap[uin]

    /**
     * 添加Bot
     *
     * @param uin Long
     * @param password String
     * @return Ore
     */
    fun addBot(uin: Long, password : String) : Ore {
        TODO()
    }

}