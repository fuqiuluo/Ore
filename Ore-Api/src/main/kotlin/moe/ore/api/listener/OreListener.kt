package moe.ore.api.listener

import moe.ore.api.Ore

/**
 * 机器人事件监听器
 */
interface OreListener {

    /**
     * 开始登录
     * @param ore Ore
     */
    fun loginStart(ore : Ore)



}