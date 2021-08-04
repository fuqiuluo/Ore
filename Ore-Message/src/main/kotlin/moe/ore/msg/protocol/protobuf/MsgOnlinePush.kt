@file:Suppress("PLUGIN_IS_NOT_ENABLED", "EXPERIMENTAL_API_USAGE", "ArrayInDataClass")

package moe.ore.msg.protocol.protobuf

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.protobuf.Protobuf

@Serializable
internal data class PbPushMsg(
    @ProtoNumber(1) @JvmField var msg: Msg,
    @ProtoNumber(2) @JvmField var svrip: Int = 0,
    @ProtoNumber(3) @JvmField var pushToken: ByteArray = EMPTY_BYTE_ARRAY,
    @ProtoNumber(4) @JvmField var pingFlag: UInt = 0u,
    @ProtoNumber(9) @JvmField var generalFlag: UInt = 0u,
    @ProtoNumber(10) @JvmField var bindUin: ULong = 0u, // 在关联账号消息的时候，这里是关联账号
): Protobuf<PbPushMsg>
