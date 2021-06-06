/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 *  此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 *  Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 *  https://github.com/mamoe/mirai/blob/master/LICENSE
 */

package moe.ore.core.protocol

import kotlinx.io.core.*
import moe.ore.core.protocol.util.*
import moe.ore.util.TeaUtil


class PacketParser {

    /**
     * 这里进来 然后分类给 parseSsoFrame 或者parseOicqResponse 然后给Handler处理(解码后的实体byte)
     * */
    fun decodeRaw(client: SsoSession, input: ByteReadPacket): RawIncomingPacket = input.run {
        // login
        val flag1 = readInt()

//        PacketLogger.verbose { "开始处理一个包" }

        val flag2 = readByte().toInt()
        val flag3 = readByte().toInt()
        check(flag3 == 0) {
            "Illegal flag3. Expected 0, whereas got $flag3. flag1=$flag1, flag2=$flag2. " + "Remaining=${this.readBytes().toUHexString()}"
        }

        readString(readInt() - 4)// uinAccount

        ByteArrayPool.useInstance(this.remaining.toInt()) { buffer ->
            val size = this.readAvailable(buffer)
            when (flag2) {
                2 -> TeaUtil.decrypt(buffer, DECRYPTER_16_ZERO, size)
                1 -> TeaUtil.decrypt(buffer, client.wLoginSigInfo.d2Key, size)
                0 -> buffer
                else -> error("Unknown flag2=$flag2")
            }.let { decryptedData ->
                when (flag1) {
                    0x0A -> parseSsoFrame(client, decryptedData)
                    0x0B -> parseSsoFrame(client, decryptedData) // 这里可能是 uni?? 但测试时候发现结构跟 sso 一样.
                    else -> error("unknown flag1: ${flag1.toByte().toUHexString()}")
                }
            }.let { raw ->
                when (flag2) {
                    0, 1 -> RawIncomingPacket(raw.commandName, raw.sequenceId, raw.body.readBytes())
                    2 -> RawIncomingPacket(raw.commandName, raw.sequenceId, raw.body.withUse {
                        try {
                            parseOicqResponse(client)
                        } catch (e: Throwable) {
                            throw RuntimeException(e)
                        }
                    })
                    else -> error("Unknown flag2=$flag2")
                }
            }
        }
    }

    internal class DecodeResult constructor(
        val commandName: String,
        val sequenceId: Int,
        /**
         * Can be passed to [PacketFactory]
         */
        val body: ByteReadPacket,
    )

    private fun parseSsoFrame(client: SsoSession, bytes: ByteArray): DecodeResult = bytes.toReadPacket().let { input ->
        val commandName: String
        val ssoSequenceId: Int
        val dataCompressed: Int
        input.readPacketExact(input.readInt() - 4).withUse {
            ssoSequenceId = readInt()
////                PacketLogger.verbose { "sequenceId = $ssoSequenceId" }
//
//                val returnCode = readInt()
//                check(returnCode == 0) {
//                    if (returnCode <= -10000) {
//                        // https://github.com/mamoe/mirai/issues/470
//                        error("returnCode = $returnCode")
//                    } else "returnCode = $returnCode"
//                }
//
//                if (PacketLogger.isEnabled) {
//                    val extraData = readBytes(readInt() - 4)
//                    PacketLogger.verbose { "(sso/inner)extraData = ${extraData.toUHexString()}" }
//                } else {
//                    discardExact(readInt() - 4)
//                }
//
            commandName = readString(readInt() - 4)
//                client.outgoingPacketSessionId = readBytes(readInt() - 4)
//
            dataCompressed = readInt()
        }

        val packet = when (dataCompressed) {
            0 -> {
//                    val size = input.readInt().toLong() and 0xffffffff
//                    if (size == input.remaining || size == input.remaining + 4) {
//                        input
//                    } else {
//                        buildPacket {
//                            writeInt(size.toInt())
//                            writePacket(input)
//                        }
//                    }
                "toByteArray".toByteArray().toReadPacket()
            }
            1 -> {
//                    input.discardExact(4)
//                    input.useBytes { data, length ->
//                        data.unzip(0, length).let {
//                            val size = it.toInt()
//                            if (size == it.size || size == it.size + 4) {
//                                it.toReadPacket(offset = 4)
//                            } else {
//                                it.toReadPacket()
//                            }
//                        }
//                    }
                "toByteArray".toByteArray().toReadPacket()
            }
            8 -> input
            else -> error("unknown dataCompressed flag: $dataCompressed")
        }

        // body

        return DecodeResult(commandName, ssoSequenceId, packet)
    }

    private fun ByteReadPacket.parseOicqResponse(
        client: SsoSession,
    ): ByteArray {
        readByte().toInt().let {
            check(it == 2) { "$it" }
        }
        this.discardExact(2)
        this.discardExact(2)
        this.readUShort()
        this.readShort()
        this.readUInt().toLong()
        val encryptionMethod = this.readUShort().toInt()

        this.discardExact(1)
        return when (encryptionMethod) {
            4 -> {
//                val data =
//                    TEA.decrypt(
//                        this.readBytes(),
//                        client.ecdh.keyPair.initialShareKey,
//                        length = (this.remaining - 1).toInt()
//                    )
//
//                val peerShareKey =
//                    client.ecdh.calculateShareKeyByPeerPublicKey(readUShortLVByteArray().adjustToPublicKey())
//                TEA.decrypt(data, peerShareKey)
                return "ByteArray()".toByteArray()
            }
            3 -> {
                // session
//                TEA.decrypt(
//                    this.readBytes(),
//                    client.wLoginSigInfo.wtSessionTicketKey,
//                    length = (this.remaining - 1).toInt()
//                )
                return "ByteArray()".toByteArray()
            }
            0 -> {
//                if (client.loginState == 0) {
//                    val size = (this.remaining - 1).toInt()
//                    val byteArrayBuffer = this.readBytes(size)
//
//                    runCatching {
//                        TEA.decrypt(byteArrayBuffer, client.ecdh.keyPair.initialShareKey, length = size)
//                    }.getOrElse {
//                        TEA.decrypt(byteArrayBuffer, client.randomKey, length = size)
//                    }
//                } else {
//                    TEA.decrypt(this.readBytes(), client.randomKey, length = (this.remaining - 1).toInt())
//                }
                return "ByteArray()".toByteArray()
            }
            else -> error("Illegal encryption method. expected 0 or 4, got $encryptionMethod")
        }
    }
}

class RawIncomingPacket constructor(
    val commandName: String,
    val sequenceId: Int,
    /**
     * Can be passed to [PacketFactory]
     */
    val body: ByteArray,
)