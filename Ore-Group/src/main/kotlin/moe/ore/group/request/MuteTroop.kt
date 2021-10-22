package moe.ore.group.request

import moe.ore.api.Ore
import moe.ore.core.bot.ContractPbPacketFactory
import moe.ore.core.net.packet.OidbSSOPkg
import moe.ore.group.protobuf.oidb.x89a.GroupInfo
import moe.ore.group.protobuf.oidb.x89a.ReqBody
import moe.ore.protobuf.Protobuf

object MuteTroop: ContractPbPacketFactory<OidbSSOPkg>("OidbSvc.0x89a_0") {
    override fun handle(data: ByteArray, seq: Int) = decodePbPacket<OidbSSOPkg>(data)

    override fun request(uin: Long, args: Array<out Any>): Protobuf<*> {
        val groupCode = args[0] as Long
        val isMute = args[1] as Boolean

        val x89a = ReqBody(
            groupCode = groupCode,
            groupInfo = GroupInfo(
                shutUpTime = if (isMute) 0xfffffff else 0
            )
        )

        return OidbSSOPkg.compose(0x89a, 0, x89a.toByteArray())
    }

    operator fun invoke(ore: Ore, groupCode: Long, isMute: Boolean) = super.invoke(ore, groupCode, isMute)
}