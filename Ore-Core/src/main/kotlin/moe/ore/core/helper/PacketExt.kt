/*
 * English :
 *  The project is protected by the MPL open source agreement.
 * Open source agreement warning that prohibits deletion of project source code files.
 * The project is prohibited from acting in illegal areas.
 * All illegal activities arising from the use of this project are the responsibility of the second author, and the original author of the project is not responsible
 *
 *  中文：
 *  该项目由MPL开源协议保护。
 *  禁止删除项目源代码文件的开源协议警告内容。
 * 禁止使用该项目在非法领域行事。
 * 使用该项目产生的违法行为，由使用者或第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 *  オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 *
 */

package moe.ore.core.helper

import kotlinx.io.core.BytePacketBuilder
import kotlinx.io.core.discardExact
import kotlinx.io.core.readBytes
import moe.ore.core.net.packet.FromService
import moe.ore.core.net.packet.PacketType
import moe.ore.core.protocol.ProtocolInternal
import moe.ore.helper.*
import moe.ore.util.BytesUtil
import moe.ore.util.TeaUtil
import moe.ore.util.ZipUtil
import okhttp3.internal.closeQuietly

val DEFAULT_TEA_KEY = ByteArray(16)

inline fun ByteArray.readPacket(uin: Long, crossinline block: (String, FromService) -> Unit) {
    val manager = DataManager.manager(uin)
    this.reader {
        val packetType = readInt()
        // 貌似没有什么鸟用
        val keyType = readByte().toInt()
        discardExact(1)
        // 不知道是什么奇怪的玩意 skip 吧
        val uinStr = readString(readInt() - 4)
        ByteArrayPool.loanAndReturn(remaining.toInt()) {
            TeaUtil.decrypt(
                this.apply { readAvailable(this) }, when (keyType) {
                    1 -> manager.wLoginSigInfo.d2Key!!
                    2 -> DEFAULT_TEA_KEY
                    else -> runtimeError("unknown key type : $keyType")
                }
            ).reader {
                val headReader = readByteReadPacket(-4)
                val body = readBytes(readInt() - 4)
                if (body.isNotEmpty()) {
                    val msfSSoSeq = headReader.readInt()
                    if (headReader.readInt() != 0) {
                        headReader.discardExact(headReader.readInt() - 4)
                        // 一个奇怪的Token
                    } else {
                        headReader.discardExact(4)
                    }
                    val commandName = headReader.readString(headReader.readInt() - 4)
                    // val randomSeed =
                    headReader.discardExact(headReader.readInt() - 4)
                    when (headReader.readInt()) {
                        0, 4 -> body
                        1 -> ZipUtil.unCompress(body)
                        else -> runtimeError("unknown encode type.")
                    }.let {
                        // msfSSoSeq , commandName, uinStr, it (body)
                        val from = FromService(msfSSoSeq, commandName, body)
                        block(uinStr, from)
                    }
                }
                this.closeQuietly()
                headReader.closeQuietly()
            }
        }
    }
}

/**
 * 构建第一层，最外面那层
 */
fun buildFirstLayer(uin: Long, packetType: PacketType, body: BytePacketBuilder): ByteArray {
    return createBuilder().apply {
        writeBodyWithSize {
            writeInt(packetType.flag1)
            writeByte(packetType.flag2)
            when (packetType) {
                PacketType.LoginPacket -> {
                    writeBytesWithSize(ByteArrayPool.EMPTY_BYTE_ARRAY, 0 + 4)
                }

            }
            writeByte(0)
            val uinStr = uin.toString()
            writeStringWithSize(uinStr, uinStr.length + 4)
            writePacket(body)
        }
    }.toByteArray()
}

fun buildSecondLayer(
    uin: Long,
    commandName: String,
    body: ByteArray,
    packetType: PacketType,
    seq: Int
): BytePacketBuilder {
    val builder = createBuilder()
    val manager = DataManager.manager(uin)
    val protocolInfo = ProtocolInternal[manager.protocolType]
    val deviceInfo = manager.deviceInfo
    when (packetType) {
        PacketType.LoginPacket -> {
            builder.writeBodyWithSize {
                writeInt(seq)
                writeInt(protocolInfo.appId)
                writeInt(protocolInfo.appId)
                writeInt(16777216)
                writeInt(0)
                writeInt(0) // Token Type 如果有Token就是256
                writeInt(0 + 4) // Token Size
                commandName.let {
                    writeStringWithSize(it, it.length + 4)
                }
                BytesUtil.randomKey(4).let {
                    writeBytesWithSize(it, it.size + 4)
                }
                deviceInfo.androidId.let {
                    writeStringWithSize(it, it.length + 4)
                }
                deviceInfo.ksid.let {
                    writeBytesWithSize(it, it.size + 4)
                }
                protocolInfo.protocolDetail.let {
                    writeStringWithShortSize(it, it.length + 2)
                }
                // 非常规组包，跳过部分异常
                // writeInt(4)
            }
        }
    }
    builder.writeInt(body.size + 4)
    builder.writeBytes(body)
    return builder
}

private inline fun BytePacketBuilder.writeBodyWithSize(block: BytePacketBuilder.() -> Unit) {
    val builder = createBuilder().apply { this.block() }
    this.writeInt(builder.size + 4)
    this.writePacket(builder)
}
