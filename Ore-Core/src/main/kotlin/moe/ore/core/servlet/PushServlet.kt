package moe.ore.core.servlet

import moe.ore.core.net.packet.FromService
import moe.ore.core.transfile.dns.InnerDns

class PushServlet: MSFServlet(arrayOf(
    "ConfigPushSvc.PushDomain",
)) {
    override fun onReceive(from: FromService) {
        when(from.commandName) {
            "ConfigPushSvc.PushDomain" -> InnerDns.default.updateDomainServerList(from.body)



        }
    }
}