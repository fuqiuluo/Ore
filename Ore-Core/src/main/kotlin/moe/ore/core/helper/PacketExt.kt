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

import kotlinx.io.core.*
import moe.ore.core.net.packet.FromService
import moe.ore.core.net.packet.PacketType
import moe.ore.core.protocol.ProtocolInternal
import moe.ore.helper.*
import moe.ore.util.TeaUtil
import moe.ore.util.ZipUtil
import okhttp3.internal.closeQuietly

val DEFAULT_TEA_KEY = ByteArray(16)

inline fun ByteArray.readMsfSsoPacket(uin: Long, crossinline block: (String, FromService) -> Unit) {
    val manager = DataManager.manager(uin)
    this.reader {
        val packetType = readInt()
        // 貌似没有什么鸟用
        val keyType = readByte().toInt()
        discardExact(1)
        //
        val uinStr = readString(readInt() - 4)

        // println(remaining) 剩余字节数
        // println(size) 总字节数

        // println("type : %s, packet : %s".format(keyType, packetType))

        TeaUtil.decrypt(ByteArray(remaining.toInt()).apply { readAvailable(this) }, when (keyType) {
            1 -> manager.wLoginSigInfo.d2Key.ticket()
            2 -> DEFAULT_TEA_KEY
            else -> runtimeError("unknown key type : $keyType")
        }).reader {
            val headReader = readByteReadPacket(readInt() - 4)
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
                val sessionId = headReader.readBytes(headReader.readInt() - 4)
                // println("packetSessionId:${manager.wLoginSigInfo.lastSessionId.toInt()}")
                when (headReader.readInt()) {
                    0, 4 -> body
                    1 -> ZipUtil.unCompress(body)
                    else -> runtimeError("unknown encode type.")
                }.let {
                    // msfSSoSeq , commandName, uinStr, it (body)
                    val from = FromService(msfSSoSeq, commandName, body)
                    from.msgCookie = sessionId
                    block(uinStr, from)
                }
            }
            this.closeQuietly()
            headReader.closeQuietly()
        }
    }
}

/**
 * 构建第一层，最外面那层
 */
internal fun buildFirstLayer(uin: Long, key: ByteArray, packetType: PacketType, seq: Int, body: ByteArray): ByteArray {
    return createBuilder().apply {
        writeBodyWithSize {
            writeInt(packetType.flag1)
            writeByte(packetType.flag2)
            when (packetType) {
                PacketType.LoginPacket -> {
                    writeInt(0 + 4)
                }
                PacketType.ExChangeEmp -> {
                    writeInt(seq)
                }


            }
            writeByte(0)
            uin.toString().let {
                writeInt(it.length + 4)
                writeString(it)
            }
            writeBytes(TeaUtil.encrypt(body, key))
        }
    }.toByteArray()
}

internal fun buildSecondLayer(uin: Long, commandName: String, body: ByteArray, packetType: PacketType, seq: Int): ByteArray {
    val builder = createBuilder()
    val manager = DataManager.manager(uin)
    val protocolInfo = ProtocolInternal[manager.protocolType]
    val session = manager.session
    val deviceInfo = manager.deviceInfo

    builder.writeBodyWithSize {
        when (packetType) {
            PacketType.LoginPacket -> {
                writeInt(seq)
                writeInt(protocolInfo.appId)
                writeInt(protocolInfo.appId)
                writeInt(16777216)
                writeInt(0)
                writeInt(0) // Token Type 如果有Token就是256
                writeInt(0 + 4) // Token Size
                commandName.let {
                    writeInt(it.length + 4)
                    writeString(it)
                }
                session.msgCookie.let {
                    writeInt(it.size + 4)
                    writeBytes(it)
                }
                deviceInfo.androidId.let {
                    writeInt(it.length + 4)
                    writeString(it)
                }
                deviceInfo.ksid.let {
                    writeInt(it.size + 4)
                    writeBytes(it)
                }
                protocolInfo.protocolDetail.let {
                    writeShort(it.length + 2)
                    writeString(it)
                }
                // 非常规组包，跳过部分异常
                // writeInt(4)
            }
            PacketType.ExChangeEmp -> {
                commandName.let {
                    writeInt(it.length + 4)
                    writeString(it)
                }
                session.msgCookie.let {
                    writeInt(it.size + 4)
                    writeBytes(it)
                }
                writeInt(4)
            }
        }
    }

    builder.writeInt(body.size + 4)
    builder.writeBytes(body)
    return builder.toByteArray()
}

private inline fun BytePacketBuilder.writeBodyWithSize(block: BytePacketBuilder.() -> Unit) {
    val builder = createBuilder().apply { this.block() }
    this.writeInt(builder.size + 4)
    this.writePacket(builder)
}

