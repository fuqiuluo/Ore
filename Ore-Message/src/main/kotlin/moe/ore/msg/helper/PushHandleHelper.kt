@file:Suppress("EXPERIMENTAL_API_USAGE_FUTURE_ERROR")

package moe.ore.msg.helper

import kotlinx.io.core.ByteReadPacket
import kotlinx.io.core.discardExact
import kotlinx.io.core.readBytes
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import moe.ore.api.Ore
import moe.ore.helper.logger.Level
import moe.ore.helper.logger.OLog
import moe.ore.helper.readBuf32ToInt64
import moe.ore.helper.reader
import moe.ore.helper.toHexString
import moe.ore.msg.CoreConfig
import moe.ore.msg.event.TroopEvent
import moe.ore.msg.protocol.protobuf.TroopTips0x857

internal class PushHandleHelper(val ore: Ore, val config: CoreConfig) {
    fun push732(troopEvent: TroopEvent, vMsg: ByteArray) {
        vMsg.reader {
            val groupId = readBuf32ToInt64()
            when(val type = readByte().toInt()) {
                12 -> push732x12(troopEvent, groupId) // 禁言
                14 -> push732x14(troopEvent, groupId)
                16 -> push732x16(troopEvent)
                else -> OLog.log(Level.WARING, "unknown push732 type: $type, ${vMsg.toHexString()}")
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
            else {
                /*
                if (uin == ore.uin) {
                    config.troopShut[groupCode] = shutTime + time
                }*/
                troopEvent.onTroopMemberMute(groupCode, operator, uin, time, shutTime)
            }
        }
    }

    private fun ByteReadPacket.push732x14(troopEvent: TroopEvent, groupId: Long) {
        discardExact(1)
        val operator = readBuf32ToInt64()
        troopEvent.onTroopAnonymousStatusChanged(groupId, operator, when(readInt()) {
            -1 -> false
            0 -> true
            else -> true
        })
    }

    private fun ByteReadPacket.push732x16(troopEvent: TroopEvent) {
        if(!hasBytes(7)) return
        val bytes = readBytes(readShort().toInt())
        val notifyMsgBody = ProtoBuf.decodeFromByteArray<TroopTips0x857.NotifyMsgBody>(bytes)

        val troopCode = notifyMsgBody.groupCode.toLong()
        val operateTime = notifyMsgBody.msgTime.toLong()

        when(val type = notifyMsgBody.enumType) {
            1 -> notifyMsgBody.aioGrayTipsInfo?.let { troopEvent.onTroopTips(troopCode, operateTime, String(it.content)) }
            24 -> notifyMsgBody.groupInfoChange?.let { groupInfoChange ->
                val operator = notifyMsgBody.senderUin.toLong()

                if (groupInfoChange.groupFlagExt3 != null) { // and 33554432 群荣誉
                    troopEvent.onTroopWriteTogetherStatusChanged( // 一起写
                        troopCode, operator, operateTime,
                        isOpen = TroopInfoUtil.isTroopWriteTogether(groupInfoChange.groupFlagExt3!!)
                    )
                } else if (groupInfoChange.groupFlagExt4 != null) {
                    troopEvent.onTroopLuckyCharStatusChanged( // 幸运字符
                        troopCode, operator, operateTime, TroopInfoUtil.isTroopLuckyChar(groupInfoChange.groupFlagExt4!!)
                    )
                }
                else {
                    OLog.log(Level.WARING, "unknown push732x16-24 type: ${bytes.toHexString()}")
                }
            }
            else -> OLog.log(Level.WARING, "unknown push732x16 type: $type")
        }
    }
}

