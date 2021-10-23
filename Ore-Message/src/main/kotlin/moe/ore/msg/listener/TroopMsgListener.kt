package moe.ore.msg.listener

interface TroopMsgListener {
    fun onTroopMsg(
        fromTroop: Long,
        fromUin: Long,
        troopName: String,
        uinName: String,
        // troopCard: String,
        // troopLevel: Int,
        msgTime: Long,
        msgId: Long,
        msg: String
    )

}