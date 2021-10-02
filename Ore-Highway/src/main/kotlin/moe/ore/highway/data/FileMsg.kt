package moe.ore.highway.data

import moe.ore.helper.EMPTY_BYTE_ARRAY
import java.io.File

data class FileMsg(
    val file: File,
    val exits: Boolean = false,
    val fileId: ULong,
    val fileMd5: ByteArray,
    val fileSize: Long,
    val fileName: String,

    // up info
    val upServer: Pair<Long, Int> = 0L to 0,
    val offset: ULong,
    val blockSize: ULong,
    val upKey: ByteArray = EMPTY_BYTE_ARRAY
)