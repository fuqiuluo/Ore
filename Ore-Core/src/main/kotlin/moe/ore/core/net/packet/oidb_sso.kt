package moe.ore.core.net.packet

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

class oidb_sso private constructor() {

    @Serializable
    class OIDBSSOPkg {
        @JvmField
        @ProtoNumber(number = 34 ushr 3)
        var bytes_bodybuffer = ByteArray(0)

        @JvmField
        @ProtoNumber(number = 50 ushr 3)
        var str_client_version = "android 8.7.8"

        @ProtoNumber(number = 42 ushr 3)
        var str_error_msg = ""

        @JvmField
        @ProtoNumber(number = 8 ushr 3)
        var uint32_command = 0

        @JvmField
        @ProtoNumber(number = 24 ushr 3)
        var uint32_result = 0

        @JvmField
        @ProtoNumber(number = 16 ushr 3)
        var uint32_service_type = 0

        companion object {
            const val BYTES_BODYBUFFER_FIELD_NUMBER = 4
            const val STR_CLIENT_VERSION_FIELD_NUMBER = 6
            const val STR_ERROR_MSG_FIELD_NUMBER = 5
            const val UINT32_COMMAND_FIELD_NUMBER = 1
            const val UINT32_RESULT_FIELD_NUMBER = 3
            const val UINT32_SERVICE_TYPE_FIELD_NUMBER = 2
        }

        override fun toString(): String {
            return "OIDBSSOPkg(bytes_bodybuffer=${bytes_bodybuffer.contentToString()}, str_client_version='$str_client_version', str_error_msg='$str_error_msg', uint32_command=$uint32_command, uint32_result=$uint32_result, uint32_service_type=$uint32_service_type)"
        }

    }
}