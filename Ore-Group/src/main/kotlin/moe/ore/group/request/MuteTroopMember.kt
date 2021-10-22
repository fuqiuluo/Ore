package moe.ore.group.request

import moe.ore.api.Ore
import moe.ore.core.bot.ContractPbPacketFactory
import moe.ore.core.net.packet.OidbSSOPkg
import moe.ore.protobuf.Protobuf
import java.nio.ByteBuffer

object MuteTroopMember: ContractPbPacketFactory<OidbSSOPkg>("OidbSvc.0x570_8", false) {
    override fun handle(data: ByteArray, seq: Int) = decodePbPacket<OidbSSOPkg>(data)

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun request(admin: Long, args: Array<out Any>): Protobuf<*> {
        val groupCode = args[0] as Long
        val uin = args[1] as Long
        val time = args[2] as Int

        val buffer = ByteBuffer.allocate((1 * 8) + 7)
        buffer.putInt(groupCode.toInt())
        buffer.put(32) // 禁言成功代号
        buffer.putShort(1) // 批量禁言数量

        buffer.putInt(uin.toInt())
        buffer.putInt(time)

        return OidbSSOPkg.compose(0x570, 8, buffer.array())
    }

    operator fun invoke(ore: Ore, groupCode: Long, uin: Long, time: Int) = super.invoke(ore, groupCode, uin, time)
}