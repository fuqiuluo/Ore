@file:Suppress("EXPERIMENTAL_API_USAGE", "PLUGIN_IS_NOT_ENABLED")
package moe.ore.msg.protocol.protobuf

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import moe.ore.protobuf.Protobuf

@Serializable
internal data class InstCtrl(
    @ProtoNumber(1) @JvmField var rpt_msg_send_to_inst: ArrayList<InstInfo>? = null,
    @ProtoNumber(2) @JvmField var rpt_msg_exclude_inst: ArrayList<InstInfo>? = null,
    @ProtoNumber(3) @JvmField var msg_from_inst: InstInfo? = null,
): Protobuf<InstCtrl>

@Serializable
internal data class InstInfo(
    @ProtoNumber(1) @JvmField var uint32_apppid: UInt = 0u,
    @ProtoNumber(2) @JvmField var uint32_instid: UInt = 0u,
    @ProtoNumber(3) @JvmField var uint32_platform: UInt = 0u,
    @ProtoNumber(10) @JvmField var enum_device_type: Int = 0,
): Protobuf<InstInfo>
