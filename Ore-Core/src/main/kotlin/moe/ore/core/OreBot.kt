package moe.ore.core

import moe.ore.api.Ore
import moe.ore.api.OreStatus
import moe.ore.api.listener.OreListener
import moe.ore.core.bot.BotAccount

class OreBot(val account: BotAccount) : Ore() {
    /**
     * 机器人状态
     */
    private var status = OreStatus.NoLogin

    /**
     * 数据管理器
     */
    val dataManager = DataManager.manager(account.uin)

    /**
     * 事件监听器
     */
    var oreListener: OreListener? = null

    override fun login() {
        // 登录开始传递登录开始事件
        oreListener?.onLoginStart()


    }

    fun setDataPath(path: String) {
        DataManager.init(account.uin, if (path.endsWith("/")) path.substring(0, path.length - 1) else path)
    }

    override fun status() = status

    override fun shut() {
        // 关闭机器人
        this.status = OreStatus.Destroy
        DataManager.destroy(account.uin)


    }
}