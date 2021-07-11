package moe.ore.core.protocol.tars.service

import moe.ore.core.OreBot
import moe.ore.core.helper.DataManager
import moe.ore.core.helper.sendPacket
import moe.ore.core.net.packet.PacketType
import moe.ore.tars.TarsOutputStream
import moe.ore.tars.TarsStructBase
import moe.ore.tars.UniPacket

object GrayUin {
    private const val CMD_CHECK = "GrayUinPro.Check"

    fun test(oreBot: OreBot) {
        try {
            val manager = DataManager.manager(oreBot.uin)
            val uni = UniPacket()
            uni.put(CheckReq().apply {
                appId = 537070774
                this.uin = oreBot.uin.toString()
                machineInfo = "771234164054420055"
            })
            uni.requestId = manager.session.nextRequestId()
            oreBot.sendPacket(CMD_CHECK, uni.encode(), PacketType.LoginPacket).call()
        } catch (e : Exception) {
            e.printStackTrace()
        }


    }

    // from class com.tencent.msf.service.protocol.e.c
    class CheckReq : TarsStructBase() {
        var appId : Int = 0 // 'a'
        lateinit var uin : String // 'b'
        lateinit var machineInfo : String // 'd'
        var unknown : String = ""// 'c'

        override fun servantName(): String = "KQQ.ConfigService.ConfigServantObj"

        override fun funcName(): String = "ClientReq"

        override fun reqName(): String = "req"

        override fun writeTo(output: TarsOutputStream) {
            output.write(appId, 1)
            output.write(uin, 2)
            output.write(unknown, 3)
            output.write(machineInfo, 4)
        }
    }
}