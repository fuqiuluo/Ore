package moe.ore.core.protocol.wlogin

enum class EncryptMethod {
    EM_ST,
    EM_ECDH,

    /**
     * 特殊模式
     * 二维码登录
     */
    SPECIAL_QR
}