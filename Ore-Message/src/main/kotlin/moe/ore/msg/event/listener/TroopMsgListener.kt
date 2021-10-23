package moe.ore.msg.event.listener

interface TroopMsgListener {
    /**
     * 接到群消息
     */
    fun onTroopMsg(
        fromTroop: Long, // 群号
        fromUin: Long, // 发信息的人
        troopName: String, // 群名
        uinName: String, // 发信息的人的名字
        // troopCard: String,
        // troopLevel: Int,
        msgTime: Long, // 发信息时间
        msgId: Long, // 消息的id（qq的消息id是递增的）
        msg: String // 消息的内容，采用ore码的形式
    )
}