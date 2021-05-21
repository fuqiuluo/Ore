package moe.ore.helper.toolq

enum class ToolQStatus {
    /**
     * 等待登录状态（初始状态）
     */
    NotLogin,

    /**
     * 已登录，没有上线
     */
    Login,

    /**
     * 在线状态
     */
    Online
}