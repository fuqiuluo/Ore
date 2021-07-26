package moe.ore.core.servlet

import moe.ore.core.net.BotClient
import moe.ore.core.net.packet.FromService
import moe.ore.core.net.packet.LongHandler
import moe.ore.tars.TarsBase
import moe.ore.util.TarsUtil

abstract class MSFServlet(
    private val handleCmd : Array<String>
) {
    fun init(client: BotClient) {
        handleCmd.forEach {
            client.registerSpecialHandler(object : LongHandler(it) {
                override fun handle(from: FromService) {
                    onReceive(from)
                }
            })
        }
    }

    abstract fun onReceive(from: FromService)

    fun <T: TarsBase> decodePacket(data: ByteArray, req: String, base: T): T {
        return TarsUtil.decodeRequest(base, req, data)
    }
}