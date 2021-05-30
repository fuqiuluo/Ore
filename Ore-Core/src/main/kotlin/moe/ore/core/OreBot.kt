package moe.ore.core

import moe.ore.api.Ore
import moe.ore.api.listener.OreListener
import moe.ore.core.protocol.WloginSigInfo

class OreBot : Ore {

    /**
     * 事件监听器
     */
    var oreListener : OreListener? = null

    /**
     * 保存各种Token
     */
    @JvmField
    val sigInfo = WloginSigInfo()

    override fun login() {
        // 登录开始传递登录开始事件
        oreListener?.loginStart(this)


    }



}