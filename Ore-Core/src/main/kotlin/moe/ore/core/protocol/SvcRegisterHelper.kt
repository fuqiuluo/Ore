package moe.ore.core.protocol

import moe.ore.core.OreManager
import moe.ore.core.helper.DataManager
import moe.ore.core.helper.sendPacket
import moe.ore.core.net.packet.PacketSender.Companion.sync
import moe.ore.core.net.packet.PacketType
import moe.ore.core.net.packet.ToService
import moe.ore.core.protocol.tars.statsvc.RegisterReq
import moe.ore.core.protocol.tars.statsvc.RegisterResp
import moe.ore.helper.thread.ThreadManager
import moe.ore.helper.toHexString
import moe.ore.tars.UniPacket
import moe.ore.util.TarsUtil
import kotlin.random.Random

class SvcRegisterHelper(val uin: Long) {
    companion object {
        const val RESULT_REGISTER_FAIL = -100
    }

    val ore = OreManager.getBot(uin)!!
    val manager = DataManager.manager(uin)
    val userStSig = manager.wLoginSigInfo
    val protocolInfo = ProtocolInternal[manager.protocolType]
    val session = manager.session

    fun register() : Int {
        val req = RegisterReq (
            lUin = uin,
            /**
             * 11 我在线上
             * 21 离线
             * 31 离开
             * 41 隐身
             * 51 忙碌
             */
            /**
             * 11 我在线上
             * 21 离线
             * 31 离开
             * 41 隐身
             * 51 忙碌
             */
            iStatus = 11, // 普通在线状态
            iLocaleID = protocolInfo.localId,
            // 花里胡哨在线状态 例如：今日天气等
            uExtOnlineStatus = 0,
            lBid = 1 or 2 or 4,
            cConnType = 0,
            sOther = "",
            bOnlinePush = 0,
            bIsOnline = 0,
            bIsShowOnline = 0,
            /**
             * 踢PC设备下线 上线时
             */
            /**
             * 踢PC设备下线 上线时
             */
            bKikPC = 0,
            bKikWeak = 0,
            timeStamp = Random.nextInt(0, 300).toLong(),
            iOSVersion = 0,
            cNetType = 0,
            bRegType = 1,
            sBuildVer = "",
            bOpenPush = 1
        )
        val uni = UniPacket()
        uni.put(req)
        uni.requestId = session.nextRequestId()
        val from = ore.sendPacket("StatSvc.register", uni.encode(), PacketType.SvcRegister, userStSig.d2.ticket(), userStSig.tgt.ticket()) sync 5 * 1000
        return if(from != null) {
            TarsUtil.decodeRequest(RegisterResp(), from.body).cReplyCode.toInt()
        } else {
            RESULT_REGISTER_FAIL
        }
    }

}