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

@Serializable
internal data class MsgElemInfoServiceType3(
    @ProtoNumber(1) @JvmField var flashTroopPic: CustomFace? = null,
    @ProtoNumber(2) @JvmField var flashC2cPic: NotOnlineImage? = null,
): Protobuf<MsgElemInfoServiceType3>

@Serializable
internal data class MsgElemInfoServiceType14(
    @ProtoNumber(1) @JvmField var id: Int = 0,
    @ProtoNumber(2) @JvmField var reserveInfo: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<MsgElemInfoServiceType14>