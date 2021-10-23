@file:Suppress("EXPERIMENTAL_API_USAGE_FUTURE_ERROR")

package moe.ore.msg.helper

import kotlinx.io.core.ByteReadPacket
import kotlinx.io.core.discardExact
import moe.ore.api.Ore
import moe.ore.helper.logger.Level
import moe.ore.helper.logger.OLog
import moe.ore.helper.readBuf32ToInt64
import moe.ore.helper.reader
import moe.ore.msg.CoreConfig
import moe.ore.msg.event.TroopEvent

internal class PushHandleHelper(val ore: Ore, val config: CoreConfig) {
    fun push732(troopEvent: TroopEvent, vMsg: ByteArray) {
        // println(vMsg.toHexString())
        vMsg.reader {
            val groupId = readBuf32ToInt64()
            when(val type = readByte().toInt()) {
                12 -> push732x12(troopEvent, groupId) // 禁言

                else -> OLog.log(Level.WARING, "unknown push type: $type")
            }
        }
    }


    private fun ByteReadPacket.push732x12(troopEvent: TroopEvent, groupCode: Long) {
        discardExact(1) // 01
        val operator = readBuf32ToInt64() // 操作人
        val time = readInt() // 操作时间
        repeat(readShort().toInt()) {
            val uin = readBuf32ToInt64()
            val shutTime = readInt()
            if (uin == 0L)
                troopEvent.onTroopMute(groupCode, operator, time, shutTime > 0 || shutTime == -1)
            else
                troopEvent.onTroopMemberMute(groupCode, operator, uin, time, shutTime)
        }
    }
}

