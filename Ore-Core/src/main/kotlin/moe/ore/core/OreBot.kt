package moe.ore.core

import moe.ore.api.Ore
import moe.ore.api.listener.OreListener
import moe.ore.core.bot.BotAccount

class OreBot(val account: BotAccount) : Ore() {
    /**
     * 数据管理器
     */
    val dataManager = DataManager.manager(account.uin)

    /**
     * 事件监听器
     */
    var oreListener : OreListener? = null

    override fun login() {
        // 登录开始传递登录开始事件
        oreListener?.onLoginStart()



    }

    override fun shut() {
        DataManager.destroy(account.uin)
    }
}