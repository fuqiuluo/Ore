package moe.ore.highway

import moe.ore.api.IPacketServlet
import moe.ore.api.Ore
import moe.ore.core.OreBot
import moe.ore.core.helper.DataManager
import moe.ore.core.protocol.ProtocolInternal
import moe.ore.helper.longToIp
import moe.ore.helper.md5
import moe.ore.highway.data.FileMsg
import moe.ore.highway.net.TcpConnection
import moe.ore.highway.protobuf.highway.DataHighwayHead
import moe.ore.highway.protobuf.highway.ReqDataHighwayHead
import moe.ore.highway.protobuf.highway.SegHead
import moe.ore.highway.request.TryUpTroopImage
import moe.ore.util.MD5
import java.io.File
import java.io.RandomAccessFile
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

    fun tryUpTroopImage(fileMsg: FileMsg) {
        RandomAccessFile(fileMsg.file, "r").use { msf -> // 大于10mb的东西 狗都不要
            val upKey = fileMsg.upKey
            val length = msf.length()
            if (length <= 1024 * 1024 * 10) TcpConnection().also {
                fileMsg.upServer.apply { it.connect(first.longToIp(true), second) }
            }.use { conn ->
                val limit = 1048576 // by oicq[nodejs]
                var offset = 0L

                while (offset < length) {
                    val chunk = ByteArray((if (limit + offset <= length) limit else length - offset).toInt()).also { msf.read(it) }
                    conn.sendHw(ReqDataHighwayHead(
                        baseHead = DataHighwayHead(
                            version = 1,
                            uin = ore.uin.toString(),
                            command = "PicUp.DataUp",
                            seq = session.nextHwSeq(),
                            appid = protocol.appId,
                            dataFlag = 4096,
                            commandId = 2,
                            localeId = 2052
                        ),
                        segHead = SegHead(
                            fileSize = length,
                            dataOffset = offset,
                            dataLength = chunk.size,
                            serviceTicket = upKey,
                            md5 = MD5.toMD5Byte(chunk),
                            fileMd5 = fileMsg.fileMd5,
                        )
                    ), chunk)
                    offset += chunk.size
                }
            }
            // println("img up: size: $length,file_md5:${fileMsg.fileMd5.toHexString()}, upkey； ${upKey.toHexString()}")
        }


    }
}

fun Ore.bdh() = this.getServletOrPut(TAG_BDH) { BdhCore(this as OreBot) }