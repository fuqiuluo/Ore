@file:Suppress("PLUGIN_IS_NOT_ENABLED", "EXPERIMENTAL_API_USAGE", "ArrayInDataClass")
package moe.ore.msg.protocol.protobuf

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.protobuf.Protobuf

@Serializable
internal data class MsgElemInfoServiceType33(
    @ProtoNumber(1) @JvmField var index: UInt = 0u,
    @ProtoNumber(2) @JvmField var text: String = "",
    @ProtoNumber(3) @JvmField var compat: String = "",
    @ProtoNumber(4) @JvmField var buf: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<MsgElemInfoServiceType33>
