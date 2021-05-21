package moe.ore.helper.toolq.util

import moe.ore.helper.bytes.ByteBuilder
import moe.ore.helper.toolq.ToolQStatus
import moe.ore.util.BytesUtil.randomKey
import moe.ore.util.TeaUtil
import moe.ore.virtual.Android
import moe.ore.virtual.ProtocolInfo

/**
 * 魔法代码，一堆问好切勿观看！！！
 */
object PacketUtil {
    @JvmStatic
    fun makeNoLoginPacket(
        cmdName: String?,
        data: ByteArray?,
        seq: Int,
        uin: Long,
        iqq: ProtocolInfo?,
        tgt: ByteArray?,
        hasSize: Boolean
    ): ByteArray {
        val android = Android.android
        return makePacket(
            cmdName,
            data,
            ToolQStatus.NotLogin,
            null,
            ByteArray(16),
            seq,
            uin,
            iqq,
            tgt,
            android.ksid,
            !hasSize
        )
    }

    @JvmStatic
    fun makeOnlinePacket(
        cmdName: String?,
        status: ToolQStatus,
        body: ByteArray?,
        key: ByteArray?,
        seq: Int,
        uin: Long,
        needSize: Boolean
    ): ByteArray {
        return makePacket(cmdName, body, status, null, key, seq, uin, null, null, null, needSize)
    }

    @JvmStatic
    fun makePacket(
        cmdName: String?,
        data: ByteArray?,
        status: ToolQStatus,
        d2: ByteArray?,
        key: ByteArray?,
        seq: Int,
        uin: Long,
        iqq: ProtocolInfo?,
        tgt: ByteArray?,
        ksid: ByteArray?,
        needSize: Boolean
    ): ByteArray {
        var packetType = 0xA
        var encryptType = 0x1
        when (status) {
            ToolQStatus.NotLogin -> encryptType = 0x2
            ToolQStatus.Online -> packetType = 0xB
            ToolQStatus.Login -> {}
        }
        return makePacket(packetType, encryptType, cmdName, data, status, d2, key, seq, uin, iqq, tgt, ksid, needSize)
    }

    @JvmStatic
    fun makeOnlinePacket(
        packetType: Int,
        encType: Int,
        cmdName: String?,
        status: ToolQStatus?,
        body: ByteArray?,
        key: ByteArray?,
        seq: Int,
        uin: Long,
        needSize: Boolean
    ): ByteArray {
        return makePacket(packetType, encType, cmdName, body, status, null, key, seq, uin, null, null, null, needSize)
    }

    @JvmStatic
    fun makePacket(
        packetType: Int,
        encType: Int,
        cmdName: String?,
        data: ByteArray?,
        status: ToolQStatus?,
        d2: ByteArray?,
        key: ByteArray?,
        seq: Int,
        uin: Long,
        protocolInfo: ProtocolInfo?,
        tgt: ByteArray?,
        ksid: ByteArray?,
        needSize: Boolean
    ): ByteArray {
        val android = Android.android
        val builder = ByteBuilder()
        val tgtType = if (tgt != null) 256 else 0
        when (status) {
            ToolQStatus.Login -> {
                builder.writeInt(0xA)
                builder.writeByte(0x1)
                builder.writeBytesWithSize(d2!!, 4)
            }
            ToolQStatus.NotLogin -> {
                builder.writeInt(packetType)
                builder.writeByte(encType)
                builder.writeBytesWithSize(d2 ?: ByteArray(0), 4)
            }
            ToolQStatus.Online -> {
                builder.writeInt(packetType)
                builder.writeByte(encType)
                builder.writeInt(seq)
            }
        }
        builder.writeByte(0)
        builder.writeStringWithSize(uin.toString(), 4)
        val body = ByteBuilder()
        val head = ByteBuilder()
        if (protocolInfo != null) {
            head.writeInt(seq)
            head.writeInt(protocolInfo.msfAppId())
            head.writeInt(protocolInfo.msfAppId())
            head.writeInt(16777216)
            head.writeInt(0)
            head.writeInt(tgtType)
            head.writeBytesWithSize(tgt ?: ByteArray(0), 4)
            head.writeStringWithSize(cmdName!!, 4)
            head.writeBytesWithSize(randomKey(4), 4)
            head.writeStringWithSize(android.imei.ifEmpty { android.androidId }, 4)
            head.writeBytesWithSize(ksid!!, 4)
            head.writeStringWithShortSize(protocolInfo.protocolDetail(), 2)
            // head.writeInt(4);
        } else {
            head.writeStringWithSize(cmdName!!, 4)
            head.writeBytesWithSize(randomKey(4), 4)
            head.writeInt(4)
        }
        head.writeToStart {
            writeInt(size() + 4)
        }
        body.writeBytes(head.toByteArray())
        if (needSize) body.writeBytesWithSize(data!!, 4) else body.writeBytes(data!!)
        val eData = TeaUtil.encrypt(body.toByteArray(), key)
        builder.writeBytes(eData)
        builder.writeToStart {
            writeInt(this.size() + 4)
        }
        return builder.toByteArray()
    }
}