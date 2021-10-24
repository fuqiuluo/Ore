package moe.ore.msg.event.listener

interface TroopEventListener {
    /**
     * 全群禁言
     */
    fun onTroopMute(
        fromTroop: Long,
        operateUin: Long, // 操作人
        operateTime: Int, // 操作时间（秒）
        isMute: Boolean) {} // 是否开启全群禁言

    /**
     * 群禁言
     */
    fun onTroopMemberMute(
        fromTroop: Long,
        operateUin: Long, // 操作人
        toUin: Long, // 被禁言的人
        operateTime: Int, // 禁言开始时间
        muteTime: Int) {} // 禁言持续时间

    /**
     * 群里一起写状态改变
     */
    fun onTroopWriteTogetherStatusChanged(
        fromTroop: Long,
        operateUin: Long,
        operateTime: Long,
        isOpen: Boolean // 功能是否开启
    ) {
        // do nothing...
    }

    /**
     * 群幸运字符状态改变
     */
    fun onTroopLuckyCharStatusChanged(
        fromTroop: Long,
        operateUin: Long,
        operateTime: Long,
        isOpen: Boolean // 功能是否开启
    ) {
        // do nothing...
    }

    /**
     * 群功能改变发生的提示消息
     */
    fun onTroopTips(
        fromTroop: Long,
        operateTime: Long,
        content: String // 提示内容
    ) {}

    /**
     * 群匿名功能状态改变
     */
    fun onTroopAnonymousStatusChanged(
        fromTroop: Long,
        operateUin: Long,
        isOpen: Boolean
    ) {}
}