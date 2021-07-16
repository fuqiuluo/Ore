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
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import moe.ore.api.Ore
import moe.ore.api.OreStatus
import moe.ore.core.OreBot
import moe.ore.core.net.packet.*
import moe.ore.helper.*
import moe.ore.tars.TarsStructBase
import moe.ore.tars.UniPacket
import moe.ore.util.TarsUtil
import moe.ore.util.TeaUtil
import moe.ore.util.ZipUtil
import okhttp3.internal.closeQuietly
import java.net.Inet4Address

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
            0 -> {
                println("receive heartbeat")
                // println("接到心跳")
                return
            }
            1 -> manager.userSigInfo.d2Key.ticket()
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
 * 上线之后才能用 因为用d2Key加密 否则sendPacket
 * */
@JvmOverloads
inline fun <reified T> Ore.sendPbPacket(
        servName: String,
        oidbSsoPkg: T
): PacketSender {
    val ticket = DataManager.manager(this.uin).userSigInfo.d2Key.ticket()
    return sendPacket(servName, ProtoBuf.encodeToByteArray(oidbSsoPkg), PacketType.ServicePacket, ticket, ticket)
}

@JvmOverloads
inline fun <reified T> Ore.sendPbPacket(
        servName: String,
        command: Int,
        body: T
): PacketSender {
    val oIDBSSOPkg = oidb_sso.OIDBSSOPkg()
    oIDBSSOPkg.uint32_command = command
    oIDBSSOPkg.bytes_bodybuffer = ProtoBuf.encodeToByteArray(body)
    val ticket = DataManager.manager(this.uin).userSigInfo.d2Key.ticket()
    return sendPacket(servName, ProtoBuf.encodeToByteArray(oIDBSSOPkg), PacketType.ServicePacket, ticket, ticket)
}

@JvmOverloads
fun Ore.sendJcePacket(
        servName: String,
        body: UniPacket,
): PacketSender {
    val ticket = DataManager.manager(this.uin).userSigInfo.d2Key.ticket()
    return sendPacket(servName, body.encode(), PacketType.ServicePacket, ticket, ticket)
}

@JvmOverloads
fun Ore.sendJcePacket(
        servName: String,
        body: TarsStructBase,
        requestId: Int
): PacketSender {
    val ticket = DataManager.manager(this.uin).userSigInfo.d2Key.ticket()
    return sendPacket(servName, TarsUtil.encodeRequest(requestId, body), PacketType.ServicePacket, ticket, ticket)
}

@JvmOverloads
fun Ore.sendPacket(
        cmd: String,
        body: ByteArray,

        packetType: PacketType = if (this.status() == OreStatus.Online)
            PacketType.ServicePacket
        else
            PacketType.LoginPacket,

        firstToken: ByteArray? = null,
        secondToken: ByteArray? = null
): PacketSender {
    val bot = this as OreBot
    val client = bot.client
    val manager = DataManager.manager(bot.uin)
    val session = manager.session
    val to = ToService(
            seq = session.nextSeqId(),
            commandName = cmd,
            body = body
    )
    to.packetType = packetType
    to.firstToken = firstToken
    to.secondToken = secondToken
    return to.sendTo(client)
}