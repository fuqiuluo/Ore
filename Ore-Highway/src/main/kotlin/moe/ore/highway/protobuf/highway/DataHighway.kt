@file:Suppress("PLUGIN_IS_NOT_ENABLED", "EXPERIMENTAL_API_USAGE", "ArrayInDataClass")
package moe.ore.highway.protobuf.highway

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.protobuf.Protobuf

@Serializable
internal data class DataHighwayHead(
	@ProtoNumber(1) @JvmField var version: Int = 0,
	@ProtoNumber(2) @JvmField var uin: String = "",
	@ProtoNumber(3) @JvmField var command: String = "",
	@ProtoNumber(4) @JvmField var seq: Int = 0,
	@ProtoNumber(5) @JvmField var retryTimes: Int? = null,
	@ProtoNumber(6) @JvmField var appid: Int? = null,
	@ProtoNumber(7) @JvmField var dataFlag: Int = 0,
	@ProtoNumber(8) @JvmField var commandId: Int = 0,
	@ProtoNumber(9) @JvmField var buildVer: String = "",
	@ProtoNumber(10) @JvmField var localeId: Int = 0,
	@ProtoNumber(11) @JvmField var envId: Int = 0,
): Protobuf<DataHighwayHead>

@Serializable
internal data class RspDataHighwayHead(
	@ProtoNumber(1) @JvmField var baseHead: DataHighwayHead,
	@ProtoNumber(2) @JvmField var msgSegHead: SegHead? = null,
	@ProtoNumber(3) @JvmField var errorCode: UInt = 0u,
	@ProtoNumber(4) @JvmField var allowRetry: UInt = 0u,
	@ProtoNumber(5) @JvmField var cacheCost: UInt = 0u,
	@ProtoNumber(6) @JvmField var htcost: UInt = 0u,
	@ProtoNumber(7) @JvmField var extendInfo: ByteArray = EMPTY_BYTE_ARRAY,
	@ProtoNumber(8) @JvmField var timestamp: ULong = 0u,
	@ProtoNumber(9) @JvmField var range: ULong = 0u,
	@ProtoNumber(10) @JvmField var iseset: UInt = 0u,
): Protobuf<RspDataHighwayHead>

@Serializable
internal data class SegHead(
	@ProtoNumber(1) @JvmField var serviceId: Int = 0,
	@ProtoNumber(2) @JvmField var fileSize: Long = 0,
	@ProtoNumber(3) @JvmField var dataOffset: Long? = null,
	@ProtoNumber(4) @JvmField var dataLength: Int = 0,
	@ProtoNumber(5) @JvmField var rtcode: Int = 0,
	@ProtoNumber(6) @JvmField var serviceTicket: ByteArray = EMPTY_BYTE_ARRAY,
	@ProtoNumber(7) @JvmField var flag: Int = 0,
	@ProtoNumber(8) @JvmField var md5: ByteArray = EMPTY_BYTE_ARRAY, // block md5
	@ProtoNumber(9) @JvmField var fileMd5: ByteArray = EMPTY_BYTE_ARRAY,
	@ProtoNumber(10) @JvmField var cacheAddress: Int? = null,
	@ProtoNumber(11) @JvmField var queryTimes: Int = 0,
	@ProtoNumber(12) @JvmField var updateCacheIp: Int = 0,
): Protobuf<SegHead>

@Serializable
internal data class ReqDataHighwayHead(
	@ProtoNumber(1) @JvmField var baseHead: DataHighwayHead,
	@ProtoNumber(2) @JvmField var segHead: SegHead? = null,
	@ProtoNumber(3) @JvmField var extendinfo: ByteArray? = null,
	@ProtoNumber(4) @JvmField var timestamp: ULong = 0u,
	@ProtoNumber(5) @JvmField var loginSigHead: LoginSigHead? = null,
): Protobuf<ReqDataHighwayHead>

@Serializable
internal data class LoginSigHead(
	@ProtoNumber(1) @JvmField var uint32_loginsig_type: UInt = 0u,
	@ProtoNumber(2) @JvmField var bytes_loginsig: ByteArray = EMPTY_BYTE_ARRAY,
): Protobuf<LoginSigHead>