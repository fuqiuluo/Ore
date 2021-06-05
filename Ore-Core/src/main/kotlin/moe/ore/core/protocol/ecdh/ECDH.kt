package moe.ore.core.protocol

import moe.ore.core.protocol.util.md5
import java.security.*
import java.security.spec.ECGenParameterSpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.KeyAgreement

fun String.chunkedHexToBytes(): ByteArray =
    this.asSequence().chunked(2).map { (it[0].toString() + it[1]).toUByte(16).toByte() }.toList().toByteArray()

typealias ECDHPrivateKey = PrivateKey
typealias ECDHPublicKey = PublicKey

class ECDHKeyPairImpl(private val delegate: KeyPair) : ECDHKeyPair {
    override val privateKey: ECDHPrivateKey get() = delegate.private
    override val publicKey: ECDHPublicKey get() = delegate.public

    override val initialShareKey: ByteArray by lazy { ECDH.calculateShareKey(privateKey, initialPublicKey) }
}

interface ECDHKeyPair {
    val privateKey: ECDHPrivateKey
    val publicKey: ECDHPublicKey

    /**
     * 私匙和固定公匙([initialPublicKey]) 计算得到的 shareKey
     */
    val initialShareKey: ByteArray

    object DefaultStub : ECDHKeyPair {
        val defaultPublicKey = "020b03cf3d99541f29ffec281bebbd4ea211292ac1f53d7128".chunkedHexToBytes()
        val defaultShareKey = "4da0f614fc9f29c2054c77048a6566d7".chunkedHexToBytes()

        override val privateKey: Nothing get() = error("stub!")
        override val publicKey: Nothing get() = error("stub!")
        override val initialShareKey: ByteArray get() = defaultShareKey
    }
}

internal fun ECDH() = ECDH(ECDH.generateKeyPair())

class ECDH constructor(val keyPair: ECDHKeyPair) {
    companion object {
        private const val curveName = "secp192k1" // p-256

        val isECDHAvailable: Boolean

        init {
            isECDHAvailable = kotlin.runCatching {
                fun testECDH() {
                    ECDHKeyPairImpl(KeyPairGenerator.getInstance("ECDH").also { it.initialize(ECGenParameterSpec(curveName)) }.genKeyPair()).let {
                        calculateShareKey(it.privateKey, it.publicKey)
                    }
                }

                if (kotlin.runCatching { testECDH() }.isSuccess) {
                    return@runCatching
                }

                TODO("BouncyCastleProvider")
//                if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) != null) {
//                    Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
//                }
//                Security.addProvider(BouncyCastleProvider())
                testECDH()
            }.onFailure {
                it.printStackTrace()
            }.isSuccess
        }

        fun generateKeyPair(): ECDHKeyPair {
            if (!isECDHAvailable) {
                return ECDHKeyPair.DefaultStub
            }
            return ECDHKeyPairImpl(KeyPairGenerator.getInstance("ECDH").also { it.initialize(ECGenParameterSpec(curveName)) }.genKeyPair())
        }

        fun calculateShareKey(privateKey: ECDHPrivateKey, publicKey: ECDHPublicKey): ByteArray {
            val instance = KeyAgreement.getInstance("ECDH", "BC")
            instance.init(privateKey)
            instance.doPhase(publicKey, true)
            return instance.generateSecret().md5()
        }

        fun constructPublicKey(key: ByteArray): ECDHPublicKey {
            return KeyFactory.getInstance("EC", "BC").generatePublic(X509EncodedKeySpec(key))
        }
    }

    fun calculateShareKeyByPeerPublicKey(peerPublicKey: ECDHPublicKey): ByteArray {
        return calculateShareKey(keyPair.privateKey, peerPublicKey)
    }

    override fun toString(): String {
        return "ECDH(keyPair=$keyPair)"
    }
}

// this is for old curve
internal val initialPublicKey
    get() = ECDH.constructPublicKey("3046301006072A8648CE3D020106052B8104001F03320004928D8850673088B343264E0C6BACB8496D697799F37211DEB25BB73906CB089FEA9639B4E0260498B51A992D50813DA8".chunkedHexToBytes())

private val head1 = "302E301006072A8648CE3D020106052B8104001F031A00".chunkedHexToBytes()
private val head2 = "3046301006072A8648CE3D020106052B8104001F03320004".chunkedHexToBytes()
internal fun ByteArray.adjustToPublicKey(): ECDHPublicKey {
    val head = if (this.size < 30) head1 else head2

    return ECDH.constructPublicKey(head + this)
}