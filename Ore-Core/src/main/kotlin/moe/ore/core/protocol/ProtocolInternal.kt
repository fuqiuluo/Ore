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
    @JvmField var openAppId: Int,
    @JvmField var appId: Int,
    @JvmField var subAppId: Int,
    @JvmField var pingVersion: Int,
    @JvmField var ipVersion: Int,
    @JvmField var ssoVersion: Int,
    @JvmField var msfSsoVersion: Int,
    @JvmField var tgtgVersion: Int,
    @JvmField var mainSigMap: Int,
    @JvmField var miscBitmap: Int,
    @JvmField var subSigMap: Int,
    @JvmField var dbVersion: Int,
    @JvmField var buildTime: Int,
    @JvmField var localId: Int,
    @JvmField var protocolVersion: Int,
    @JvmField var loginType: Int,
    @JvmField var isGuidFromFileNull: Boolean = false, // 保存到文件的 GUID 是否为 null
    @JvmField var isGuidAvailable: Boolean = true, // GUID 是否可用(计算/读取成功)
    @JvmField var isGuidChanged: Boolean = false, // GUID 是否有变动
    @JvmField var buildVersion: String,
    @JvmField var protocolDetail: String,
    @JvmField var packageName: String,
    @JvmField var packageVersion: String,
    @JvmField var tencentSdkMd5: ByteArray,
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
                openAppId = 0x2a9e5427,
                // from 8.7.5
                appId = 0x200302d5,
                subAppId = 16,
                pingVersion = 1,
                ipVersion = 1,
                ssoVersion = 1536,
                msfSsoVersion = 17,
                tgtgVersion = 4,
                mainSigMap = 0x21410e0,
                miscBitmap = 184024956,
                subSigMap = 66560,
                dbVersion = 1,
                buildTime = 0x6082b52f,
                localId = 2052,
                protocolVersion = 8001,
                loginType = 1,
                isGuidAvailable = true,
                isGuidFromFileNull = false,
                isGuidChanged = false,
                buildVersion = "6.0.0.2475",
                protocolDetail = "||A8.7.5.18f5bf29",
                packageName = "com.tencent.mobileqq",
                packageVersion = "8.7.5",
                tencentSdkMd5 = "a6b745bf24a2c277527716f6f36eb68d".hex2ByteArray(),
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