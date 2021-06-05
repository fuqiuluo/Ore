package moe.ore.core.protocol

import kotlinx.io.core.BytePacketBuilder
import kotlinx.io.core.ByteReadPacket
import kotlinx.io.core.buildPacket
import kotlinx.io.core.writeFully
import moe.ore.core.protocol.util.encryptAndWrite
import moe.ore.core.protocol.util.writeShortLVByteArray

interface EncryptMethod {
    val id: Int
    fun buildBody(body: BytePacketBuilder.() -> Unit): ByteReadPacket
}

internal interface EncryptMethodSessionKey : EncryptMethod {
    override val id: Int get() = 69
    val currentLoginState: Int
    val sessionKey: ByteArray

    override fun buildBody(body: BytePacketBuilder.() -> Unit): ByteReadPacket =
        buildPacket {
            require(currentLoginState == 2 || currentLoginState == 3) { "currentLoginState must be either 2 or 3" }
            writeByte(1) // const
            writeByte(if (currentLoginState == 2) 3 else 2)
            writeFully(sessionKey)
            writeShort(258) // const
            writeShort(0) // const, length of publicKey
            encryptAndWrite(sessionKey, body)
        }
}

internal class EncryptMethodSessionKeyNew(
    private val wtSessionTicket: ByteArray, // t133
    private val wtSessionTicketKey: ByteArray, // t134
) : EncryptMethod {
    override val id: Int get() = 69

    override fun buildBody( body: BytePacketBuilder.() -> Unit): ByteReadPacket =
        buildPacket {
            writeShortLVByteArray(wtSessionTicket)
            encryptAndWrite(wtSessionTicketKey, body)
        }
}

internal class EncryptMethodSessionKeyLoginState2(override val sessionKey: ByteArray) :
    EncryptMethodSessionKey {
    override val currentLoginState: Int get() = 2
}

internal class EncryptMethodSessionKeyLoginState3(override val sessionKey: ByteArray) :
    EncryptMethodSessionKey {
    override val currentLoginState: Int get() = 3
}

internal class EncryptMethodECDH135(override val ecdh: ECDH) :
    EncryptMethodECDH {
    override val id: Int get() = 135
}

internal class EncryptMethodECDH7(override val ecdh: ECDH) :
    EncryptMethodECDH {
    override val id: Int get() = 7 // 135
}

internal interface EncryptMethodECDH : EncryptMethod {
    companion object {
        operator fun invoke(ecdh: ECDH): EncryptMethodECDH {
            return if (ecdh.keyPair === ECDHKeyPair.DefaultStub) {
                EncryptMethodECDH135(ecdh)
            } else EncryptMethodECDH7(ecdh)
        }
    }

    val ecdh: ECDH

    override fun buildBody(body: BytePacketBuilder.() -> Unit): ByteReadPacket = buildPacket {
        /* //new curve p-256
        writeByte(2) // const
        writeByte(1) // const
        writeFully(client.randomKey)
        writeShort(0x0131) // const
        writeShort(0x0001)
         */

        writeByte(1) // version
        writeByte(1) // const
        writeFully(TODO("randomKey"))//client.randomKey
        writeShort(0x0102)

        if (ecdh.keyPair === ECDHKeyPair.DefaultStub) {
            writeShortLVByteArray(ECDHKeyPair.DefaultStub.defaultPublicKey)
            encryptAndWrite(ECDHKeyPair.DefaultStub.defaultShareKey, body)
        } else {
            // for p-256, drop(26). // but not really sure.
            writeShortLVByteArray(ecdh.keyPair.publicKey.getEncoded().drop(23).toByteArray().also {
                check(it[0].toInt() == 0x04) { "Bad publicKey generated. Expected first element=0x04, got${it[0]}" }
            })

            encryptAndWrite(ecdh.keyPair.initialShareKey, body)
        }
    }
}