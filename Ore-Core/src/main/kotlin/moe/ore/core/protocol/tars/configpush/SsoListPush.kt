package moe.ore.core.protocol.tars.configpush

import moe.ore.tars.TarsBase
import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField

@TarsClass(requireRead = true)
internal class SsoListPush: TarsBase() {
    @TarsField(1) var mobileSSOServerList: List<ServerInfo>? = null

    @TarsField(3) var wifiSSOServerList: List<ServerInfo>? = null

    @TarsField(4) var needReconnect: Int = 0

    @TarsField(8) var mobileHttpServerList: List<ServerInfo>? = null

    @TarsField(9) var wifiHttpServerList: List<ServerInfo>? = null

    @TarsField(10) var quicServerList: List<ServerInfo>? = null

    @TarsField(11) var ssoServerListIpv6: List<ServerInfo>? = null

    @TarsField(12) var httpServerListIpv6: List<ServerInfo>? = null

    @TarsField(13) var quicServerListIpv6: List<ServerInfo>? = null

    @TarsField(16) var desc: String? = null
}

@TarsClass(requireRead = true)
class ServerInfo: TarsBase() {
    @TarsField(id = 1) var ip = ""

    @TarsField(id = 2) var port = 0

    @TarsField(id = 8) var location = "" // sz or sh or tj 地址

    @TarsField(id = 9) var net = "" // cm 运营商
}