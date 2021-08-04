@file:Suppress("PLUGIN_IS_NOT_ENABLED", "EXPERIMENTAL_API_USAGE", "ArrayInDataClass")

package moe.ore.msg.protocol.protobuf

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.protobuf.Protobuf

@Serializable
data class PbPushMsg(
    @ProtoNumber(1) @JvmField var msg: Msg? = null,
    @ProtoNumber(2) @JvmField var svrip: Int = 0,
    @ProtoNumber(3) @JvmField var bytes_push_token: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var ping_flag: UInt = 0u,
    @ProtoNumber(9) @JvmField var uint32_general_flag: UInt = 0u,
    @ProtoNumber(10) @JvmField var uint64_bind_uin: ULong = 0u,
): Protobuf<PbPushMsg>
