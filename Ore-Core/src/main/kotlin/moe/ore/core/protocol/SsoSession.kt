package moe.ore.core.protocol

interface SsoSession {
    var outgoingPacketSessionId: ByteArray

    /**
     * always 0 for now.
     */
    var loginState: Int
    val ecdh: ECDH

    // also present in AccountSecrets
    var wLoginSigInfo: WLoginSigInfo
    val randomKey: ByteArray
}