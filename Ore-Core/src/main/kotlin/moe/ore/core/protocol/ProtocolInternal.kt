/*
 * English :
 *  The project is protected by the MPL open source agreement.
 * Open source agreement warning that prohibits deletion of project source code files.
 * The project is prohibited from acting in illegal areas.
 * All illegal activities arising from the use of this project are the responsibility of the second author, and the original author of the project is not responsible
 *
 *  中文：
 *  该项目由MPL开源协议保护。
 *  禁止删除项目源代码文件的开源协议警告内容。
 * 禁止使用该项目在非法领域行事。
 * 使用该项目产生的违法行为，由使用者或第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 *  オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 *
 */

package moe.ore.core.protocol

import moe.ore.helper.hex2ByteArray
import java.util.*

class ProtocolInternal(
    @JvmField internal var openAppId: Int,
    @JvmField internal var appId: Int,
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
    @JvmField internal var isGuidFromFileNull: Boolean = false, // 保存到文件的 GUID 是否为 null
    @JvmField internal var isGuidAvailable: Boolean = true, // GUID 是否可用(计算/读取成功)
    @JvmField internal var isGuidChanged: Boolean = false, // GUID 是否有变动
    @JvmField internal var buildVersion: String,
    @JvmField internal var protocolDetail: String,
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
                openAppId = 715019303,
                // 8.8.0
                appId = 0x2002F789,
                subAppId = 16,
                pingVersion = 1,
                ipVersion = 0x1,
                ssoVersion = 1536,
                msfSsoVersion = 12,
                tgtgVersion = 4,
                mainSigMap = 16724722,
                miscBitmap = 0x0af7ff7c,
                subSigMap = 66560,
                dbVersion = 1,
                buildTime = 0x5f433810,
                localId = 2052,
                protocolVersion = 8001,
                loginType = 1,
                isGuidAvailable = true,
                isGuidFromFileNull = false,
                isGuidChanged = false,
                buildVersion = "6.0.0.2436",
                protocolDetail = "|454001228437590|A8.4.8.94cf45ad",
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
        ANDROID_PHONE
    }
}