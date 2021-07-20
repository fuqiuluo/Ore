package moe.ore.group

import moe.ore.api.Ore
import moe.ore.core.OreBot

class TroopManager(private val ore: Ore) {




}

fun Ore.troopManager() : TroopManager {
    return TroopManager(this as OreBot)
}