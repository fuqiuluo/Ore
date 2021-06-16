package moe.ore.core.net.packet

import kotlinx.io.core.*
import moe.ore.core.helper.ByteArrayPool.Companion.EMPTY_BYTE_ARRAY
import moe.ore.core.helper.DataManager
import moe.ore.core.helper.writeIntLVPacket
import moe.ore.core.protocol.ECDH_PUBLIC_KEY
import moe.ore.core.protocol.ECDH_SHARE_KEY
import moe.ore.core.protocol.ECDH_VERSION
import moe.ore.helper.*
import moe.ore.util.TeaUtil

// TODO: 2021/6/16 学习分步构建包装 以原版名称命名 待优化
internal inline fun buildUniPacket(manager: DataManager, bodyType: Byte = 1, // 1: PB?
    commandName: String, key: ByteArray = manager.wLoginSigInfo.d2Key, extraData: ByteReadPacket = BRP_STUB, sequenceId: Int = manager.recorder.nextSeq(), body: BytePacketBuilder.(sequenceId: Int) -> Unit): ByteReadPacket {

    return buildPacket {
        writeIntLVPacket(lengthOffset = { it + 4 }) {
            writeInt(0x0B)
            writeByte(bodyType)
            writeInt(sequenceId)
            writeByte(0)
            manager.botAccount.uin.toString().let {
                writeInt(it.length + 4)
                writeStringUtf8(it)
            }
            writeTeaEncrypt(key) {
                writeUniPacket(commandName, manager.wLoginSigInfo.packetSessionId, extraData) {
                    body(sequenceId)
                }
            }
        }
    }
}


internal inline fun buildResponseUniPacket(dataManager: DataManager, bodyType: Byte = 1, // 1: PB?
    commandName: String, key: ByteArray = dataManager.wLoginSigInfo.d2Key, extraData: ByteReadPacket = BRP_STUB, sequenceId: Int = dataManager.recorder.nextSeq(), body: BytePacketBuilder.(sequenceId: Int) -> Unit): ByteReadPacket {
    @Suppress("DuplicatedCode") return buildPacket {
        writeIntLVPacket(lengthOffset = { it + 4 }) {
            writeInt(0x0B)
            writeByte(bodyType)
            writeInt(sequenceId)
            writeByte(0)
            dataManager.botAccount.uin.toString().let {
                writeInt(it.length + 4)
                writeStringUtf8(it)
            }
            writeTeaEncrypt(key) {
                writeUniPacket(commandName, dataManager.wLoginSigInfo.packetSessionId, extraData) {
                    body(sequenceId)
                }
            }
        }
    }
}


private inline fun BytePacketBuilder.writeUniPacket(commandName: String, unknownData: ByteArray, extraData: ByteReadPacket = BRP_STUB, body: BytePacketBuilder.() -> Unit) {
    writeIntLVPacket(lengthOffset = { it + 4 }) {
        commandName.let {
            writeInt(it.length + 4)
            writeStringUtf8(it)
        }

        writeInt(4 + 4)
        writeFully(unknownData) //  02 B0 5B 8B

        if (extraData === BRP_STUB) {
            writeInt(0x04)
        } else {
            writeInt((extraData.remaining + 4).toInt())
            writePacket(extraData)
        }
    }

    // body
    writeIntLVPacket(lengthOffset = { it + 4 }, builder = body)
}

internal val NO_ENCRYPT: ByteArray = ByteArray(0)

/**
 * com.tencent.qphone.base.util.CodecWarpper#encodeRequest(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, byte[], int, int, java.lang.String, byte, byte, byte, byte[], byte[], boolean)
 */
internal inline fun buildLoginPacket(dataManager: DataManager, bodyType: Byte, extraData: ByteArray = EMPTY_BYTE_ARRAY, commandName: String, key: ByteArray = ByteArray(16), body: BytePacketBuilder.(sequenceId: Int) -> Unit): ByteReadPacket {
    val sequenceId: Int = dataManager.recorder.nextSeq()

    return buildPacket {
        writeIntLVPacket(lengthOffset = { it + 4 }) {
            writeInt(0x00_00_00_0A)
            writeByte(bodyType)
            extraData.let {
                writeInt(it.size + 4)
                writeFully(it)
            }
            writeByte(0x00)

            dataManager.botAccount.uin.toString().let {
                writeInt(it.length + 4)
                writeStringUtf8(it)
            }

            if (key === NO_ENCRYPT) {
                body(sequenceId)
            } else {
                writeTeaEncrypt(key) { body(sequenceId) }
            }
        }
    }
}

private inline val BRP_STUB get() = ByteReadPacket.Empty


internal inline fun BytePacketBuilder.writeSsoPacket(dataManager: DataManager, subAppId: Long, commandName: String, extraData: ByteReadPacket = BRP_STUB, unknownHex: String = "01 00 00 00 00 00 00 00 00 00 01 00", sequenceId: Int, body: BytePacketBuilder.() -> Unit) {

    /* send
     * 00 00 00 78
     * 00 00 94 90
     * 20 02 ED BD
     * 20 02 ED BD
     * 01 00 00 00 00 00 00 00 00 00 01 00
     * 00 00 00 04
     * 00 00 00 13 48 65 61 72 74 62 65 61 74 2E 41 6C 69 76 65
     * 00 00 00 08 59 E7 DF 4F
     * 00 00 00 13 38 36 35 31 36 36 30 32 36 34 34 36 39 32 35
     * 00 00 00 04
     * 00 22 7C 34 36 30 30 30 31 39 31 39 38 37 36 30 32 36 7C 41 38 2E 32 2E 30 2E 32 37 66 36 65 61 39 36
     * 00 00 00 04
     *
     * 00 00 00 04
     */
    writeIntLVPacket(lengthOffset = { it + 4 }) {
        writeInt(sequenceId)
        writeInt(subAppId.toInt())
        writeInt(subAppId.toInt())
        writeHex(unknownHex)
        if (extraData === BRP_STUB || extraData.remaining == 0L) {
            // fast-path
            writeInt(0x04)
        } else {
            writeInt((extraData.remaining + 4).toInt())
            writePacket(extraData)
        }
        commandName.let {
            writeInt(it.length + 4)
            writeStringUtf8(it)
        }

        writeInt(4 + 4)
        writeFully(dataManager.wLoginSigInfo.packetSessionId) //  02 B0 5B 8B

        dataManager.deviceInfo.imei.let {
            writeInt(it.length + 4)
            writeStringUtf8(it)
        }

        writeInt(4)

        dataManager.deviceInfo.ksid.let {
            writeShort((it.size + 2).toShort())
            writeFully(it)
        }

        writeInt(4)
    }

    // body
    writeIntLVPacket(lengthOffset = { it + 4 }, builder = body)
}

internal fun BytePacketBuilder.writeOicqRequestPacket(dataManager: DataManager, encryptType: Int, commandId: Int, key: ByteArray = ECDH_SHARE_KEY, bodyBlock: BytePacketBuilder.() -> Unit) {
    writeIntLVPacket { makeBody(dataManager.botAccount.uin, BytePacketBuilder().apply(bodyBlock).build().readBytes(), commandId, encryptType, dataManager.deviceInfo.randKey, key) }
}

private fun makeBody(uin: Long, byteArray: ByteArray, commandId: Int, encryptType: Int, randomKey: ByteArray, key: ByteArray = ECDH_SHARE_KEY): ByteArray {
    val builder = createBuilder()
    builder.writePacket(createBuilder().apply {
        val tlvBody = TeaUtil.encrypt(byteArray, key)

        writeByte(0x2)
        writeShort(tlvBody.size + 4 + 49 + ECDH_PUBLIC_KEY.size)
        writeShort(0x1f41)
        writeShort(commandId)
        writeShort(1)
        writeLongToBuf32(uin)

        writeByte(3)
        writeByte(encryptType.toByte())
        writeInt(0)
        writeByte(2)
        writeInt(0)
        writeInt(0)
        writeByte(2)
        writeByte(1)
        // 03 87 00 00 00 00 02 00 00 00 00 00 00 00 00 02 01

        writeBytes(randomKey)
        writeShort(305)
        writeShort(ECDH_VERSION.toShort())
        writeBytesWithShortSize(ECDH_PUBLIC_KEY)
        writeBytes(tlvBody)

        writeByte(0x3)
    })
    return builder.toByteArray()
}