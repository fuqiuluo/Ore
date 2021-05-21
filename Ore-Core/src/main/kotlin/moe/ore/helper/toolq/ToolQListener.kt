package moe.ore.helper.toolq

import moe.ore.ToolQ

interface ToolQListener {
    /**
     * 登录事件
     * @param result LoginResult
     */
    fun loginEvent(result : ToolQ.Companion.LoginResult)

    /**
     * 上线事件
     * @param ret Int
     */
    fun onlineEvent(ret : Int)

    /**
     * 服务器断开事件
     */
    fun serverDisconnectionEvent()

    /**
     * 服务器连接成功事件
     */
    fun serverConnectionSuccessEvent()

    /**
     * 心跳事件
     */
    fun heartbeatEvent()
}