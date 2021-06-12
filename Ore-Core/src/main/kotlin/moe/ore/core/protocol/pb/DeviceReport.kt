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

package moe.ore.core.protocol.pb

import kotlinx.serialization.Serializable

@Serializable
data class DeviceReport(
    var bootloader: ByteArray,
    var version: ByteArray,
    var codename: ByteArray,
    var incremental: ByteArray,
    var fingerprint: ByteArray,
    var bootId: ByteArray,
    var androidId: ByteArray,
    var baseband: ByteArray,
    var innerVer: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DeviceReport

        if (!bootloader.contentEquals(other.bootloader)) return false
        if (!version.contentEquals(other.version)) return false
        if (!codename.contentEquals(other.codename)) return false
        if (!incremental.contentEquals(other.incremental)) return false
        if (!fingerprint.contentEquals(other.fingerprint)) return false
        if (!bootId.contentEquals(other.bootId)) return false
        if (!androidId.contentEquals(other.androidId)) return false
        if (!baseband.contentEquals(other.baseband)) return false
        if (!innerVer.contentEquals(other.innerVer)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bootloader.contentHashCode()
        result = 31 * result + version.contentHashCode()
        result = 31 * result + codename.contentHashCode()
        result = 31 * result + incremental.contentHashCode()
        result = 31 * result + fingerprint.contentHashCode()
        result = 31 * result + bootId.contentHashCode()
        result = 31 * result + androidId.contentHashCode()
        result = 31 * result + baseband.contentHashCode()
        result = 31 * result + innerVer.contentHashCode()
        return result
    }
}