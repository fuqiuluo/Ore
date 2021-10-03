package moe.ore.highway

import moe.ore.api.IPacketServlet
import moe.ore.api.Ore
import moe.ore.core.OreBot
import moe.ore.core.helper.DataManager
import moe.ore.core.protocol.ProtocolInternal
import moe.ore.helper.md5
import moe.ore.highway.data.FileMsg
import moe.ore.highway.net.upResourceByFile
import moe.ore.highway.request.TryUpTroopImage
import java.io.File
import kotlin.random.Random
import kotlin.random.nextInt

const val TAG_BDH = "BDH"

class BdhCore(
    val ore: OreBot
): IPacketServlet {
    private val manager = DataManager.manager(ore.uin)
    private val session = manager.session
    private val protocol = ProtocolInternal[manager.protocolType]

    fun trySendTroopImage(troopCode: Long, file: File): FileMsg {
        val fileMd5 = file.md5()

        val fileSize = file.length()
        val fileName = file.name
        TryUpTroopImage(ore, troopCode, fileMd5, fileName, fileSize, protocol.apkBuildVersion).onSuccess {
            // println("怠速：" + it.msgTryUpImgRsp?.size)
            val rsp = it.msgTryUpImgRsp!![0]
            val index = Random.nextInt(0 until rsp.upIp!!.size)
            val upServer = rsp.upIp!![index] to rsp.upPort!![index]
            return FileMsg(file, rsp.fileExist, rsp.fileId, fileMd5, fileSize, fileName, upServer, rsp.upOffset, rsp.blockSize, rsp.ukey)
        }.onFailure {
            println("Check if the image failed to upload")
        }
        return FileMsg(
            file = file,
            exits = true,
            fileId = 0u,
            fileMd5 = fileMd5,
            fileSize = fileSize,
            fileName = fileName,
            offset = 0u,
            blockSize = 0u,
        )
    }

    fun tryUpTroopImage(fileMsg: FileMsg) = upResourceByFile(ore.uin, session, protocol.appId, fileMsg)
}

fun Ore.bdh() = this.getServletOrPut(TAG_BDH) { BdhCore(this as OreBot) }