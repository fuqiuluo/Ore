package moe.ore.virtual

import moe.ore.helper.bytes.hex2ByteArray

class AndroidQQ(val isHd : Boolean = false) : ProtocolInfo {
    // TODO("HD登录下次一定")

    override fun msfAppId() = 537065343

    /**
     * 电脑是1，手机是10
     * @return Int
     */
    override fun subAppId() = 16

    override fun pingVersion() = 1

    override fun ipVersion() = 1

    override fun ssoVersion() = 1536

    override fun msfSsoVersion() = 12

    override fun tgtgVersion() = 4

    override fun buildTime() = 1598240784

    override fun localeId() = 2052

    override fun dbVersion() = 1

    override fun buildVersion() = "6.0.0.2436"

    override fun protocolDetail() = "|454001228437590|A8.4.8.94cf45ad"

    override fun packageVersion() = "8.4.8"

    override fun sdkMd5() = "A6B745BF24A2C277527716F6F36EB68D".hex2ByteArray()

    override fun packageName() = "com.tencent.mobileqq"
}