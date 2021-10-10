package moe.ore.msg.request

import moe.ore.api.Ore
import moe.ore.core.bot.ContractPbPacketFactory
import moe.ore.msg.protocol.protobuf.*
import moe.ore.protobuf.Protobuf
import kotlin.random.Random
import kotlin.random.nextUInt

internal object SendMsg: ContractPbPacketFactory<PbSendMsgResp>("MessageSvc.PbSendMsg") {
    override fun handle(data: ByteArray, seq: Int) =  decodePbPacket<PbSendMsgResp>(data)

    override fun request(uin: Long, args: Array<out Any>): Protobuf<*> {
        val msgId = args[0] as Long
        val routingHead = args[1] as RoutingHead
        val msgBody = args[2] as MsgBody
        return PbSendMsgReq(
            routingHead = routingHead,
            contentHead = ContentHead(1u, 0u, 0u),
            msgBody = msgBody,
            msgSeq = msgId.toULong(),
            msgRand = Random.nextUInt(),
            msgVia = 1u
        )
    }

    operator fun invoke(ore: Ore, msgSeq: Long, routingHead: RoutingHead, msgBody: MsgBody) = super.invoke(ore, msgSeq, routingHead, msgBody)
}