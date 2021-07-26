package moe.ore.core.transfile.dns

import moe.ore.core.protocol.pb.configpush.DomainIp

class InnerDns private constructor() {
    private val domainDns = hashMapOf<String, DomainData>()
    private val domainDnsIpv6 = hashMapOf<String, DomainData>()

    @JvmOverloads
    operator fun get(domain : String, isIpv6 : Boolean = false) : DomainData? {
        if (isIpv6) {
            return domainDnsIpv6[domain]
        }
        return domainDns[domain]
    }

    fun updateDomainServerList(data: ByteArray) {
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