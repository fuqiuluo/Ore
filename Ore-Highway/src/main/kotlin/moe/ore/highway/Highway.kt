package moe.ore.highway

import moe.ore.api.Ore
import moe.ore.core.OreBot
import moe.ore.core.helper.DataManager
import moe.ore.core.protocol.ProtocolInternal
import moe.ore.helper.md5
import moe.ore.helper.toHexString
import moe.ore.highway.protobuf.oidb.Cmd0x388
import moe.ore.highway.request.TryUpTroopImage
import java.io.File

class Highway(
    val ore: OreBot
) {
    private val manager = DataManager.manager(ore.uin)
    private val buildVer = ProtocolInternal[manager.protocolType].apkBuildVersion

    fun tryUpTroopImage(troopCode: Long, file: File): Result<Cmd0x388.RspBody> {
        val fileMd5 = file.md5()
        val fileName = fileMd5.toHexString() + ".ore"
        val fileSize = file.length()
        return TryUpTroopImage(ore, troopCode, fileMd5, fileName, fileSize, buildVer)
    }


}

fun Ore.highway() = Highway(ore = this as OreBot)