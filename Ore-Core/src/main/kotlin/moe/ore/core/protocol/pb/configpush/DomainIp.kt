package moe.ore.core.protocol.pb.configpush

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import moe.ore.core.transfile.dns.IpData.Companion.IPV4
import moe.ore.protobuf.Protobuf

object DomainIp {
    @Serializable
    class NameRspBody : Protobuf<NameRspBody> {
        @ProtoNumber(number = 1) @JvmField var subCmdNameRsp : SubCmdNameRsp? = null
    }

    @Serializable
    class SubCmdNameRsp : Protobuf<SubCmdNameRsp> {
        @ProtoNumber(number = 1) @JvmField var uip : Long = 0 // ip address

        @ProtoNumber(number = 2) @JvmField var uin : Long = 0

        @ProtoNumber(number = 3) @JvmField var ipListInfo : List<IpListInfo> = listOf()

        @ProtoNumber(number = 5) @JvmField var ipListInfoIpv6 : List<IpListInfo> = listOf()
    }

    @Serializable
    class IpListInfo : Protobuf<IpListInfo> {
        @ProtoNumber(number = 1) @JvmField var result : Int = 0

        @ProtoNumber(number = 2) @JvmField var domainName : String = ""

        @ProtoNumber(number = 3) @JvmField var type : Int = IPV4

        @ProtoNumber(number = 4) @JvmField var ttl : Int = 0

        @ProtoNumber(number = 5) @JvmField var serverList : List<ServerData> = listOf()
    }

    @Serializable
    class ServerData : Protobuf<ServerData> {
        @ProtoNumber(number = 1) @JvmField var ip : String = ""

        @ProtoNumber(number = 2) @JvmField var port : Int = 0
    }
}