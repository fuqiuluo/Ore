package moe.ore.highway.net

import moe.ore.core.bot.SsoSession
import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.helper.longToIp
import moe.ore.highway.data.FileMsg
import moe.ore.highway.protobuf.highway.DataHighwayHead
import moe.ore.highway.protobuf.highway.ReqDataHighwayHead
import moe.ore.highway.protobuf.highway.RspDataHighwayHead
import moe.ore.highway.protobuf.highway.SegHead
import moe.ore.util.MD5
import java.io.RandomAccessFile

// 上传数据通过提供文件
internal fun upResourceByFile(uin: Long, session: SsoSession, appId: Int, fileMsg: FileMsg) {
    RandomAccessFile(fileMsg.file, "r").use { rf ->
        TcpConnection().also {
            fileMsg.upServer.apply { it.connect(first.longToIp(true), second) }
        }.use { conn ->
            conn.echo(uin, session.nextHwSeq(), appId) // 没什么鸟用的操作 但是我就是喜欢发

            var chunkSize = 4096
            // Length limit
            if (chunkSize in 1024 .. 1048575){
                chunkSize = 8192 // from https://github.com/KonataDev/Konata.Core
            }

            var i = 0L
            while (i < fileMsg.fileSize) {
                var chunk: Int = chunkSize
                if (fileMsg.fileSize - i < chunkSize) {
                    chunk = (fileMsg.fileSize - i).toInt()
                }

                // DataUp
                val result = conn.upBlock(
                    uin, session.nextHwSeq(), appId,

                    // file info
                    fileMsg.fileSize,
                    i,
                    fileMsg.fileMd5,
                    fileMsg.upKey,
                    ByteArray(chunk).also { rf.readFully(it) }
                ) ?: return // 服务端可能主动断开连接 或者 错误发包导致被踢下线
                if(result.first.msgSegHead?.rtcode != 0) {
                    return
                } // 上传失败

                // println(result.first.msgSegHead?.rtcode)

                i += chunk
            }

        }
    }
}

// 说句hello 非必要操作
internal fun TcpConnection.echo(uin: Long, seq: Int, appId: Int): Pair<RspDataHighwayHead, ByteArray>? {
    sendPacket(
        ReqDataHighwayHead(
        baseHead = DataHighwayHead(
            version = 1,
            uin = uin.toString(),
            command = "PicUp.Echo",
            seq = seq,
            appid = appId,
            dataFlag = 4096,
            commandId = 0,
            localeId = 2052
        ),
    ), EMPTY_BYTE_ARRAY)
    return readPacket() // await
}

internal fun TcpConnection.upBlock(
    uin: Long, seq: Int, appId: Int, // must have

    fileSize: Long,
    dataOffset: Long,
    fileMd5: ByteArray,

    ticket: ByteArray,

    block: ByteArray
): Pair<RspDataHighwayHead, ByteArray>? {
    sendPacket(
        ReqDataHighwayHead(
            baseHead = DataHighwayHead(
                version = 1,
                uin = uin.toString(),
                command = "PicUp.DataUp",
                seq = seq,
                retryTimes = 0,
                appid = appId,
                dataFlag = 4096,
                commandId = 2,
                localeId = 2052
            ),
            segHead = SegHead(
                fileSize = fileSize,
                dataOffset = dataOffset,
                dataLength = block.size,
                serviceTicket = ticket,
                md5 = MD5.toMD5Byte(block),
                fileMd5 = fileMd5,
                cacheAddress = 0,
                unknown = 0
            ),
        ),

        block
    )
    return readPacket() // await
}