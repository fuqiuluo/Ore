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
class OidbSSOPkg : Protobuf<OidbSSOPkg> {
    @JvmField @ProtoNumber(number = 4) var bodyBuffer = EMPTY_BYTE_ARRAY

    @JvmField @ProtoNumber(number = 6) var clientVersion = ""

    @JvmField @ProtoNumber(number = 5) var errMsg = ""

    @JvmField @ProtoNumber(number = 1) var command = 0

    @JvmField @ProtoNumber(number = 3) var result = 0

    @JvmField @ProtoNumber(number = 2) var serviceType = 0
}