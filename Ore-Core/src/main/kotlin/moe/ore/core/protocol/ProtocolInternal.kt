package moe.ore.core.protocol

import moe.ore.util.bytes.hex2ByteArray
import java.util.*

class ProtocolInternal(
    @JvmField internal var openAppId: Long,
    @JvmField internal var appId: Long,
    @JvmField internal var subAppId: Int,
    @JvmField internal var pingVersion: Int,
    @JvmField internal var ipVersion: Int,
    @JvmField internal var ssoVersion: Int,
    @JvmField internal var msfSsoVersion: Int,
    @JvmField internal var tgtgVersion: Int,
    @JvmField internal var mainSigMap: Int,
    @JvmField internal var miscBitmap: Int,
    @JvmField internal var subSigMap: Int,
    @JvmField internal var dbVersion: Int,
    @JvmField internal var buildTime: Int,
    @JvmField internal var localId: Int,
    @JvmField internal var protocolVersion: Int,
    @JvmField internal var loginType: Int,
    @JvmField internal var isGuidAvailable: Boolean,
    @JvmField internal var isGuidFromFileNull: Boolean,
    @JvmField internal var isGuidChange: Boolean,
    @JvmField internal var buildVersion: String,
    @JvmField internal var agreementVersion: String,
    @JvmField internal var packageName: String,
    @JvmField internal var packageVersion: String,
    @JvmField internal var tencentSdkMd5: ByteArray
) {

    // TODO: 2021/6/6  待优化 这里静态有问题

    companion object {
        @JvmStatic
        private val protocols = EnumMap<ProtocolType, ProtocolInternal>(ProtocolType::class.java)

        @JvmStatic
        operator fun get(protocolType: ProtocolType): ProtocolInternal =
            protocols[protocolType] ?: error("Internal Error: Missing protocol $protocolType")

        init {
            protocols[ProtocolType.ANDROID_PHONE] = ProtocolInternal(
                openAppId = 715019303, //8.8.0
                appId = 0x2002f77f,
                subAppId = 16,
                pingVersion = 1,
                ipVersion = 0x1,
                ssoVersion = 1536,
                msfSsoVersion = 12,
                tgtgVersion = 4,
                mainSigMap = 16724722,//8.8.0
                miscBitmap = 0x0af7ff7c,
                subSigMap = 66560,
                dbVersion = 1,
                buildTime = 0x5f433810,
                localId = 2052,
                protocolVersion = 8001,
                loginType = 1,
                isGuidAvailable = true,
                isGuidFromFileNull = false,
                isGuidChange = false,
                buildVersion = "6.0.0.2436",
                agreementVersion = "|454001228437590|A8.4.8.94cf45ad",
                packageName = "com.tencent.mobileqq",
                packageVersion = "8.4.8",
                tencentSdkMd5 = "A6B745BF24A2C277527716F6F36EB68D".hex2ByteArray(),
            )
        }

    }

    enum class ProtocolType {
        /**
         * Android
         */
        ANDROID_PHONE, IOS_PHONE,

        /**
         * Android 平板
         */
        ANDROID_PAD, IOS_IPAD,
    }
}

fun ProtocolInternal.ProtocolType.info() : ProtocolInternal = ProtocolInternal[this]