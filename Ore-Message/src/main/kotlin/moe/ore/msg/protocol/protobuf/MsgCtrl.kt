@file:Suppress("PLUGIN_IS_NOT_ENABLED", "EXPERIMENTAL_API_USAGE", "ArrayInDataClass")
package moe.ore.msg.protocol.protobuf

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.protobuf.Protobuf

@Serializable
internal class MsgCtrl(
    @ProtoNumber(1) @JvmField var msg_flag: UInt = 0u,
    @ProtoNumber(2) @JvmField var resv_resv_info: ResvResvInfo? = null,
): Protobuf<MsgCtrl>

@Serializable
internal class ResvResvInfo(
    @ProtoNumber(1) @JvmField var uint32_flag: UInt = 0u,
    @ProtoNumber(2) @JvmField var bytes_reserv1: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(3) @JvmField var uint64_reserv2: ULong = 0u,
    @ProtoNumber(4) @JvmField var uint64_reserv3: ULong = 0u,
    @ProtoNumber(5) @JvmField var uint32_create_time: UInt = 0u,
    @ProtoNumber(6) @JvmField var uint32_pic_height: UInt = 0u,
    @ProtoNumber(7) @JvmField var uint32_pic_width: UInt = 0u,
    @ProtoNumber(8) @JvmField var uint32_resv_flag: UInt = 0u,
): Protobuf<ResvResvInfo>
