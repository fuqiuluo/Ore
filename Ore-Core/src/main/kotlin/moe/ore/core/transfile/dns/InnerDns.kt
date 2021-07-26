package moe.ore.core.transfile.dns

import moe.ore.core.net.BotClient
import moe.ore.core.net.packet.FromService
import moe.ore.core.net.packet.LongHandler
import moe.ore.core.protocol.pb.configpush.DomainIp

class InnerDns private constructor() {
    private val domainDns = hashMapOf<String, DomainData>()
    private val domainDnsIpv6 = hashMapOf<String, DomainData>()
    private val handler = object : LongHandler("ConfigPushSvc.PushDomain") {
        override fun handle(from: FromService) {
            updateDomainServerList(from.body)
        }
    }

    fun init(client: BotClient) = client.registerSpecialHandler(handler)

    @JvmOverloads
    operator fun get(domain : String, isIpv6 : Boolean = false) : DomainData? {
        if (isIpv6) {
            return domainDnsIpv6[domain]
        }
        return domainDns[domain]
    }

    private fun updateDomainServerList(data: ByteArray) {
        DomainIp.NameRspBody().from(data).subCmdNameRsp?.let { rsp ->
            rsp.ipListInfo.forEach { info ->
                if(info.result == 0) {
                    val domain = DomainData(info.domainName)
                    info.serverList.forEach { server ->
                        domain.ipList.add(IpData(server.ip, server.port))
                    }
                    domainDns[info.domainName] = domain
                }
            }
            rsp.ipListInfoIpv6.forEach { info ->
                if(info.result == 0) {
                    val domain = DomainData(info.domainName)
                    info.serverList.forEach { server ->
                        domain.ipList.add(IpData(server.ip, server.port))
                    }
                    domainDnsIpv6[info.domainName] = domain
                }
            }
        }
    }

    companion object {
        @JvmStatic val default = InnerDns()
    }
}