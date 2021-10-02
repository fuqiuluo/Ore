@file:Suppress("PLUGIN_IS_NOT_ENABLED", "EXPERIMENTAL_API_USAGE", "ArrayInDataClass")
package moe.ore.msg.protocol.protobuf

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import moe.ore.protobuf.Protobuf

@Serializable
internal class ReceiptReq(
    @ProtoNumber(1) @JvmField var command: Int = 0,
    @ProtoNumber(2) @JvmField var msg_info: MsgInfo? = null,
): Protobuf<ReceiptReq>

@Serializable
internal class MsgInfo(
    @ProtoNumber(1) @JvmField var uint64_from_uin: ULong = 0u,
    @ProtoNumber(2) @JvmField var uint64_to_uin: ULong = 0u,
    @ProtoNumber(3) @JvmField var uint32_msg_seq: UInt = 0u,
    @ProtoNumber(4) @JvmField var uint32_msg_random: UInt = 0u,
): Protobuf<MsgInfo>
