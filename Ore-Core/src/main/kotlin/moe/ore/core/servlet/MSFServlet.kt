package moe.ore.core.servlet

import moe.ore.api.IPacketServlet
import moe.ore.core.net.BotClient
import moe.ore.core.net.packet.FromService
import moe.ore.core.net.packet.LongHandler
import moe.ore.tars.TarsBase
import moe.ore.util.TarsUtil

abstract class MSFServlet(
    private val handleCmd : Array<String>
): IPacketServlet {
    lateinit var client: BotClient

    fun init(client: BotClient) {
        this.client = client
        handleCmd.forEach {
            client.registerSpecialHandler(object : LongHandler(it) {
                override fun handle(from: FromService) {
                    onReceive(from)
                }
            })
        }
    }

    abstract fun onReceive(from: FromService)

    protected fun <T: TarsBase> decodePacket(data: ByteArray, req: String, base: T): T {
        return TarsUtil.decodeRequest(base, req, data)
    }
}