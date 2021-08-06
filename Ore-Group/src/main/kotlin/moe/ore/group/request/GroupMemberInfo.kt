@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
package moe.ore.group.request

import moe.ore.api.Ore
import moe.ore.core.bot.ContractPbPacketFactory
import moe.ore.group.protobuf.GroupMemberInfoReqBody
import moe.ore.group.protobuf.GroupMemberInfoRspBody
import moe.ore.group.protobuf.MemberInfo
import moe.ore.protobuf.Protobuf

object GroupMemberInfo: ContractPbPacketFactory<MemberInfo>("group_member_card.get_group_member_card_info") {
    override fun handle(data: ByteArray, seq: Int) = decodePbPacket<GroupMemberInfoRspBody>(data).msgMemInfo

    override fun request(requestUin: Long, args: Array<out Any>): Protobuf<*> {
        val groupCode = args[0] as Long
        val uin = args[1] as Long
        return GroupMemberInfoReqBody(
            groupCode = groupCode.toULong(),
            uin = uin.toULong(),
            true, 1u, 1u
        )
    }

    operator fun invoke(ore: Ore, groupCode: Long, uin: Long) = super.invoke(ore, groupCode, uin)
}