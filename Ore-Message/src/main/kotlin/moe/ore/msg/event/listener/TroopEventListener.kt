package moe.ore.msg.event.listener

interface TroopEventListener {
    /**
     * 全群禁言
     */
    fun onTroopMute(
        fromTroop: Long,
        operateUin: Long, // 操作人
        operateTime: Int, // 操作时间（秒）
        isMute: Boolean) // 是否开启全群禁言

    /**
     * 群禁言
     */
    fun onTroopMemberMute(
        fromTroop: Long,
        operateUin: Long, // 操作人
        toUin: Long, // 被禁言的人
        operateTime: Int, // 禁言开始时间
        muteTime: Int) // 禁言持续时间
}