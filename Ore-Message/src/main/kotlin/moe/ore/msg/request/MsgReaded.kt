package moe.ore.msg.request

import moe.ore.api.Ore
import moe.ore.core.bot.ContractPbPacketFactory
import moe.ore.msg.protocol.protobuf.PbGroupReadedReportReq
import moe.ore.msg.protocol.protobuf.PbMsgReadedReportReq
import moe.ore.msg.protocol.protobuf.PbMsgReadedReportResp
import moe.ore.msg.request.MsgReaded.ReportMode.*
import moe.ore.protobuf.Protobuf

// 消息已读
internal object MsgReaded: ContractPbPacketFactory<PbMsgReadedReportResp>("PbMessageSvc.PbMsgReadedReport") {
    override fun handle(data: ByteArray, seq: Int) = decodePbPacket<PbMsgReadedReportResp>(data)

    override fun request(uin: Long, args: Array<out Any>): Protobuf<*> {
        val mode = args[0] as ReportMode
        val list = args[1] as Collection<MsgReport>

        val readedReport = PbMsgReadedReportReq()
        when(mode) {
            GRP -> readedReport.grpReadReport = list.map { PbGroupReadedReportReq(groupCode = it.fromUin, lastReadSeq = it.fromSeq) }
            DIS -> TODO()
            C2C -> TODO()
            BIND_UIN -> TODO()
        }

        return readedReport
    }

    operator fun invoke(ore: Ore, mode: ReportMode, list: Collection<MsgReport>) = super.invoke(ore, mode, list)

    data class MsgReport(
        val fromUin: ULong,
        val fromSeq: ULong = 0u,

        /* 尚未投入使用
        val syncCookie: ByteArray = EMPTY_BYTE_ARRAY,
        val lastReadTime: Long = 0L,
        var crm_sig: ByteArray = EMPTY_BYTE_ARRAY,
        var peer_type: Int = 0,
        var chat_type: UInt = 0u,
        var cpid: ULong = 0u,
        var aio_type: UInt = 0u,
        var uint64_to_tiny_id: ULong = 0u, */
    )

    enum class ReportMode {
        GRP, DIS, C2C, BIND_UIN
    }
}