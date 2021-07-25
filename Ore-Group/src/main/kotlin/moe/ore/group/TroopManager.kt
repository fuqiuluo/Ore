package moe.ore.group

import moe.ore.api.Ore
import moe.ore.core.OreBot
import moe.ore.core.helper.sendPbPacket

class TroopManager(private val ore: Ore) {

    fun test() {
    }


}

fun Ore.troopManager() : TroopManager {
    return TroopManager(this)
}