package moe.ore.core.net.packet

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.protobuf.Protobuf

const val BYTES_BODY_BUFFER_FIELD_NUMBER = 4
const val STR_CLIENT_VERSION_FIELD_NUMBER = 6
const val STR_ERROR_MSG_FIELD_NUMBER = 5
const val UINT32_COMMAND_FIELD_NUMBER = 1
const val UINT32_RESULT_FIELD_NUMBER = 3
const val UINT32_SERVICE_TYPE_FIELD_NUMBER = 2

@Serializable
data class OidbSSOPkg(
    @JvmField @ProtoNumber(number = 4) var bodyBuffer: ByteArray = EMPTY_BYTE_ARRAY,
    @JvmField @ProtoNumber(number = 6) var clientVersion: String = "",
    @JvmField @ProtoNumber(number = 5) var errMsg: String = "",
    @JvmField @ProtoNumber(number = 1) var command: Int = 0,
    @JvmField @ProtoNumber(number = 3) var result: Int = 0,
    @JvmField @ProtoNumber(number = 2) var serviceType: Int = 0
) : Protobuf<OidbSSOPkg> {
    companion object {
        @JvmStatic
        fun compose(
            command: Int,
            service: Int,
            buffer: ByteArray
        ): OidbSSOPkg = OidbSSOPkg(
            bodyBuffer = buffer,
            command = command,
            serviceType = service
        )
    }
}