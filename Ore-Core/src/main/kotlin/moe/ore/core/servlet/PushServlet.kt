package moe.ore.core.servlet

import moe.ore.api.OreStatus
import moe.ore.core.OreManager
import moe.ore.core.helper.DataManager
import moe.ore.core.net.packet.FromService
import moe.ore.core.protocol.tars.configpush.FileStoragePushFSSvcList
import moe.ore.core.protocol.tars.configpush.PushReq
import moe.ore.core.protocol.tars.configpush.SsoListPush
import moe.ore.core.transfile.dns.InnerDns
import moe.ore.tars.TarsInputStream

internal class PushServlet(val uin: Long): MSFServlet(arrayOf(
    "ConfigPushSvc.PushDomain",
    "ConfigPushSvc.PushReq"
)) {
    private val manager = DataManager.manager(uin)

    override fun onReceive(from: FromService) {
        when(from.commandName) {
            "ConfigPushSvc.PushDomain" -> InnerDns.default.updateDomainServerList(from.body)
            "ConfigPushSvc.PushReq" -> {
                val rsp = decodePacket(from.body, "PushReq", PushReq())
                when(rsp.type) {
                    1 -> {
                        // receive ssoList push
                        // have http api, so not to parse
                        if(rsp.data != null && rsp.data!!.isNotEmpty()) {
                            val ssoListPush = SsoListPush().apply { readFrom(TarsInputStream(rsp.data)) }
                            if(ssoListPush.needReconnect == 1) {
                                OreManager.changeStatus(uin, OreStatus.Reconnecting)
                                val info = ssoListPush.mobileSSOServerList?.first()
                                if(info == null) {
                                    client.connect()
                                } else {
                                    client.connect(info.ip, info.port)
                                }
                            }
                        }
                    }
                    2 -> {
                        // RECEIVED PUSH: FMT SERVER ADDR LIST
                        if(rsp.data != null && rsp.data!!.isNotEmpty()) {
                            val fsSvcList = FileStoragePushFSSvcList().apply { readFrom(TarsInputStream(rsp.data)) }
                            manager.uploadServerList.addAll(fsSvcList.uploadList)
                            manager.troopPicServerList.addAll(fsSvcList.gPicDownloadList)
                            fsSvcList.bigDataChannel?.bigDataSigSession?.let {
                                manager.bigDataUpKey = it
                            }
                        }
                    }
                    3 -> {
                    }
                    else -> {
                        println("unknown push type: ${rsp.type}")
                    }
                }
            }


        }
    }
}