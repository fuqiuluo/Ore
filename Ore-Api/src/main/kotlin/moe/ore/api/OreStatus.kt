package moe.ore.api

enum class OreStatus {
    /**
     * 最初始的状态
     */
    NoLogin,

    /**
     * 该机器人实例已销毁
     */
    Destroy,

    /**
     * 在线状态
     */
    Online
}