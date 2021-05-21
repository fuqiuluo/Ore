package moe.ore.virtual

interface ProtocolInfo {
    fun msfAppId() : Int

    fun subAppId() : Int

    fun pingVersion() : Int

    fun ipVersion() : Int

    fun ssoVersion() : Int

    fun msfSsoVersion() : Int

    fun tgtgVersion() : Int

    fun buildTime() : Int

    fun localeId() : Int

    fun dbVersion() : Int

    fun buildVersion() : String

    fun protocolDetail() : String

    fun packageVersion() : String

    fun sdkMd5() : ByteArray

    fun packageName() : String
}