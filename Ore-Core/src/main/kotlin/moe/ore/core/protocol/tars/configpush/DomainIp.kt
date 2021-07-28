package moe.ore.core.protocol.tars.configpush

import moe.ore.tars.TarsBase
import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField

@TarsClass(requireRead = true)
internal class DomainIpChannel: TarsBase() {
    @TarsField(id = 0) var domainList: ArrayList<DomainIpList>? = null
}

@TarsClass(requireRead = true)
internal class DomainIpList: TarsBase() {
    @TarsField(id = 0) var domainType = 0

    @TarsField(id = 1) var ipList: ArrayList<DomainIpInfo>? = null
}

@TarsClass(requireRead = true)
internal class DomainIpInfo: TarsBase() {
    @TarsField(id = 1) var ip = 0

    @TarsField(id = 2) var port = 0
}
