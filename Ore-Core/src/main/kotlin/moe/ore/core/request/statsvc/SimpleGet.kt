package moe.ore.core.request.statsvc

import moe.ore.core.bot.ContractJcePacketFactory
import moe.ore.tars.TarsBase

object SimpleGet: ContractJcePacketFactory<TarsBase>("StatSvc.SimpleGet", false) {
    override fun handle(data: ByteArray, seq: Int) = error("simple get not need resp")

    override fun request(uin: Long, args: Array<out Any>): TarsBase? = null
}