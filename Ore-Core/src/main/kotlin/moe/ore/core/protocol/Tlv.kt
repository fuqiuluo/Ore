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
 * 使用该项目产生的违法行为，由第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 *  オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 *
 */

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
 * 使用该项目产生的违法行为，由第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 *  オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 *
 */

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
 * 使用该项目产生的违法行为，由第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 *  オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 *
 */

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
 * 使用该项目产生的违法行为，由第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 *  オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 *
 */

/*******************************************************************************
 *  2021 Ore Developer Warn
 *
 * English :
 * The project is protected by the MPL open source agreement.
 * Open source agreement warning that prohibits deletion of project source code files.
 * The project is prohibited from acting in illegal areas.
 * All illegal activities arising from the use of this project are the responsibility of the second author, and the original author of the project is not responsible
 *
 * 中文：
 * 该项目由MPL开源协议保护。
 * 禁止删除项目源代码文件的开源协议警告内容。
 * 禁止使用该项目在非法领域行事。
 * 使用该项目产生的违法行为，由第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 * オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 ******************************************************************************/

package moe.ore.core.protocol

import kotlinx.io.core.BytePacketBuilder
import kotlinx.io.core.toByteArray
import kotlinx.io.core.writeFully
import moe.ore.core.helper.DataManager
import moe.ore.helper.bytes.*
import moe.ore.tars.TarsOutputStream
import moe.ore.util.MD5
import kotlin.random.Random
import kotlin.random.nextUInt
import moe.ore.util.TeaUtil
import kotlin.experimental.or
import kotlin.random.asJavaRandom

@ExperimentalUnsignedTypes
class Tlv(val uin: Long) {
    private val dataManager = DataManager.manager(uin)
    private var deviceInfo = dataManager.deviceInfo

    /**
     * 协议信息
     */
    private val protocolInfo = ProtocolInternal[dataManager.protocolType]

    private fun buildTlv(tlvVer: Int, block: BytePacketBuilder.() -> Unit): ByteArray {
        val bodyBuilder = BytePacketBuilder()
        val out = BytePacketBuilder()
        bodyBuilder.block()
        out.writeShort(tlvVer.toShort())
        out.writeShort(bodyBuilder.size.toShort())
        out.writePacket(bodyBuilder)
        bodyBuilder.close()
        return out.toByteArray()
    }


//    fun main(args: Array<String>) {
//        DataManager.init(1U, "C:\\Users\\Administrator\\Desktop\\Ore")
//        val t = Tlv(1U)
//        println(t.t1().toHexString())
//
//        val out = TarsOutputStream()
//        out.write(10899, 1)
//        println(out.toByteArray().toHexString())
//    }

    fun t1() = buildTlv(0x1) {
        writeShort(protocolInfo.ipVersion)
        writeInt(Random.nextInt())
        writeLongToBuf32(uin)
        writeInt(currentTimeSeconds())
        writeFully(deviceInfo.clientIp)
        writeShort(0)
        toByteArray()
    }

    fun t8() = buildTlv(0x8) {
        writeShort(0)
        writeInt(protocolInfo.localId)
        writeShort(0)
    }

    fun t18() = buildTlv(0x18) {
        writeShort(protocolInfo.pingVersion)
        writeInt(protocolInfo.ssoVersion)
        writeInt(protocolInfo.subAppId)
        writeInt(0)
        writeLongToBuf32(uin)
        writeShort(0)
        writeShort(0)
    }


    fun t100() = buildTlv(0x100) {
        writeShort(protocolInfo.dbVersion)
        writeInt(protocolInfo.msfSsoVersion)
        writeInt(protocolInfo.subAppId)
        writeInt(protocolInfo.appId.toInt())
        writeInt(0)
        writeInt(protocolInfo.mainSigMap)//mainSigMap
    }

    fun t104(dt104: ByteArray) = buildTlv(0x104) {
        writeBytes(dt104)
    }

    fun t107() = buildTlv(0x107) {
        writeShort(0)
        writeByte(0.toByte())
        writeShort(0)
        writeByte(1.toByte())
    }

    fun t108(ksid: ByteArray?) = buildTlv(0x108) {
        writeBytes(ksid ?: "BF F3 F1 1C 63 EE 2C B1 7D 96 77 02 A3 6E 25 12".hex2ByteArray())
    }

    private fun t109() = buildTlv(0x109) {
        writeBytes(MD5.toMD5Byte(deviceInfo.androidId))
    }

    fun t116() = buildTlv(0x116) {
        writeByte(0.toByte())
        // ver
        writeInt(protocolInfo.miscBitmap)
        writeInt(protocolInfo.subSigMap).apply {
            val appidArray = intArrayOf(0x5f5e10e2)
            writeByte(appidArray.size.toByte())
            for (appid in appidArray) {
                writeInt(appid)
            }
        }
    }

    fun t1162() = buildTlv(0x116) {
        writeByte(0.toByte())
        writeInt(0x08F7FF7C)
        writeInt(66560).apply {
            val appidArray = intArrayOf(0x5f5e10e2)
            writeByte(appidArray.size.toByte())
            for (appid in appidArray) {
                writeInt(appid)
            }
        }
    }

    fun t106() = buildTlv(0x106) {
        writeTeaEncrypt(dataManager.botAccount.md5PasswordWithQQ()) {
            writeShort(protocolInfo.tgtgVersion)
            writeInt(Random.nextInt(0, 828922931))
            writeInt(protocolInfo.msfSsoVersion)
            writeInt(protocolInfo.subAppId)
            writeInt(0)
            writeInt(0)
            writeLongToBuf32(uin)
            writeInt(currentTimeSeconds())
            writeFully(deviceInfo.clientIp)
            writeByte(1.toByte())
            writeBytes(dataManager.botAccount.md5Password())
            writeBytes(deviceInfo.tgtgKey)
            writeInt(0)
            writeBoolean(protocolInfo.isGuidAvailable)
            writeBytes(deviceInfo.guid)
            writeInt(protocolInfo.appId.toInt())
            writeInt(protocolInfo.loginType)
            writeStringWithShortSize(uin.toString())
            writeShort(0)
        }
    }

    private fun t124() = buildTlv(0x124) {
        writeBytesWithShortSize(deviceInfo.osType.toByteArray())
        writeStringWithShortSize(deviceInfo.androidVersion)
        writeShort(deviceInfo.netType)
//        when (Android.apn) {
//            "5g", "wap", "net", "4gnet", "3gwap", "cncc", "cmcc" -> 1
//            "wifi" -> 2
//            else -> 0
//        }
        writeStringWithShortSize(deviceInfo.apnName)
        writeStringWithSize(deviceInfo.apn)
    }

    private fun t128() = buildTlv(0x128) {
        writeShort(0)
        writeBoolean(protocolInfo.isGuidFromFileNull)
        writeBoolean(protocolInfo.isGuidAvailable)
        writeBoolean(protocolInfo.isGuidChanged)
        writeInt(0x01000000)
        writeStringWithShortSize(deviceInfo.model, 32)
        writeBytesWithShortSize(deviceInfo.guid, 16)
        writeStringWithShortSize(deviceInfo.brand, 16)
    }

    fun t141() = buildTlv(0x141) {
        writeShort(1)
        // version
        writeStringWithShortSize(deviceInfo.apnName)
        writeShort(deviceInfo.netType)
//    writeShort (when (Android.apn) {
//        "5g", "wap", "net", "4gnet", "3gwap", "cncc", "cmcc" -> 1
//        "wifi" -> 2
//        else -> 0
//    })
        writeStringWithShortSize(deviceInfo.apn)
    }

    fun t142() = buildTlv(0x142) {
        writeShort(0)
        writeStringWithShortSize(protocolInfo.packageName)
    }

    fun t144() = buildTlv(0x144) {
        writeTeaEncrypt(deviceInfo.tgtgKey) {
            writeShort(5)
            writeBytes(t109())
            writeBytes(t52d())
            writeBytes(t124())
            writeBytes(t128())
            writeBytes(t16e())
        }
    }

    fun t145() = buildTlv(0x145) {
        writeBytes(deviceInfo.guid)
    }

    fun t147() = buildTlv(0x147) {
        writeInt(protocolInfo.subAppId)
        writeStringWithShortSize(protocolInfo.packageVersion)
        writeBytesWithShortSize(protocolInfo.tencentSdkMd5)
    }

    fun t154(seq: Int) = buildTlv(0x154) {
        writeInt(seq)
    }

    fun t16a(noPicSig: ByteArray) = buildTlv(0x16a) {
        writeFully(noPicSig)

    }

    /**
     * 登录过 设备所显示名称
     */
    fun t16b() = buildTlv(0x16b) {
        val domains = arrayOf("tenpay.com", "qzone.qq.com", "qun.qq.com", "mail.qq.com", "openmobile.qq.com", "qzone.com", "game.qq.com", "vip.qq.com")
        writeShort(domains.size)
        writeShort(domains.size)
        for (s in domains) {
            writeBytesWithShortSize(s.toByteArray())
        }
    }

    private fun t16e() = buildTlv(0x16e) {
        writeFully(deviceInfo.model.toByteArray())
    }

    fun t174(dt174: ByteArray) = buildTlv(0x174) {
        writeFully(dt174)
    }

    fun t177() = buildTlv(0x177) {
        writeBoolean(true)
        writeInt(protocolInfo.buildTime)
        writeStringWithShortSize(protocolInfo.buildVersion)
    }


    fun t17a(i: Int) = buildTlv(0x17a) {
        writeInt(i)
    }

    fun t17c(code: String) = buildTlv(0x17c) {
        writeShort(code.length)
        writeBytes(code.toByteArray())
    }

    fun t187() = buildTlv(0x187) {
        writeBytes(MD5.toMD5Byte(deviceInfo.macAddress))
    }

    fun t188() = buildTlv(0x188) {
        writeBytes(MD5.toMD5Byte(deviceInfo.androidId))
    }

    fun t191() = buildTlv(0x191) {
        writeByte(0x82.toByte())
    }

    fun t194() = buildTlv(0x194) {
        writeBytes(MD5.toMD5Byte(deviceInfo.imsi))
    }

    fun t197() = buildTlv(0x197) {
        writeByte(0.toByte())
    }

    fun t198() = buildTlv(0x198) {
        writeByte(0.toByte())
    }

    fun t202() = buildTlv(0x202) {
        writeShortLVByteArrayLimitedLength(deviceInfo.wifiBSsid.toByteArray(), 16)
        writeShortLVByteArrayLimitedLength(deviceInfo.wifiBSsid.toByteArray(), 32)
    }

    // TODO: 2021/6/9 Emp用的
// sigInfo2 = (client.device.guid + client.dpwd + tlvMap.getOrFail(0x402)).md5()
    fun t400(sigInfo2: ByteArray) = buildTlv(0x400) {
        writeTeaEncrypt(sigInfo2) {
            writeByte(1) // version
            writeLong(uin)
            writeFully(deviceInfo.guid)
            writeFully(dataManager.wLoginSigInfo.dpwd)
            writeInt(protocolInfo.appId.toInt())
            writeInt(protocolInfo.subAppId)
            writeInt(currentTimeSeconds())
            // TODO: 2021/6/9  tlvMap[0x403]?.let { bot.client.randSeed = it }
            writeFully(dataManager.wLoginSigInfo.randSeed)
        }
    }

    fun t401(dt402: ByteArray) = buildTlv(0x401) {
        val builder = BytePacketBuilder()
        builder.writeBytes(deviceInfo.guid)
        builder.writeBytes(dataManager.wLoginSigInfo.dpwd)
        builder.writeBytes(dt402)
        writeBytes(builder.md5())
    }

    fun t402(dt402: ByteArray) = buildTlv(0x402) {
        writeBytes(dt402)
    }

    fun t403(dt403: ByteArray) = buildTlv(0x403) {
        writeBytes(dt403)

    }

    fun t511() = buildTlv(0x511) {
        val domains = arrayOf("office.qq.com", "qun.qq.com", "gamecenter.qq.com", "docs.qq.com", "mail.qq.com", "ti.qq.com", "vip.qq.com", "tenpay.qq.com", "qqqweb.qq.com", "qzone.qq.com", "mma.qq.com", "game.qq.com", "openmobile.qq.com", "conect.qq.com" // "y.qq.com",
            // "v.qq.com"
        )
        writeShort(domains.size)
        for (domain in domains) {
            val start = domain.indexOf('(')
            val end = domain.indexOf(')')
            var b: Byte = 1
            if (start == 0 || end > 0) {
                val i = domain.substring(start + 1, end).toInt()
                b = if (1048576 and i > 0) {
                    1
                } else {
                    0
                }
                if (i and 0x08000000 > 0) {
                    b = (b or 2.toByte())
                }
            }
            writeByte(b)//todo toInt
            writeStringWithShortSize(domain)
        }
    }

    fun t516() = buildTlv(0x516) {
        writeInt(0)
    }

    fun t521() = buildTlv(0x521) {
        writeInt(0)
        writeShort(0)
    }

    fun t525() = buildTlv(0x525) {
        writeShort(1)
        writeBytes(t536())
    }

    private fun t536() = buildTlv(0x536) {
        writeHex("""
    01 03 00 00 00 00 6F E2
    BD 2E 04 DF 68 5C 58 5F
    8A F7 FE 20 02 F8 A1 00
    00 00 00 99 68 42 96 04
    DF 68 5C 58 5F 8A FD 41
    20 02 F8 A1 00 00 00 00
    62 5A BF 50 04 DF 68 5C
    58 5F 8A FD 42 20 02 F8
    A1
    """.trimIndent())
    }

    private fun t52d() = buildTlv(0x52d) {
        TODO("DeviceReport()")
//        writeBytes(DeviceReport())
    }

    fun t542() = buildTlv(0x542) {
        writeByte(0.toByte())
        writeByte(0.toByte())
    }

    fun t544() = buildTlv(0x544) {
        writeHex("""
    00 00 07 D9 00 00 00 00 
    00 2E 00 20 15 97 BF B2 
    50 07 9C 86 AF 7A FB 53 
    64 4F 39 97 E9 0A 15 91 
    83 AD F1 20 CC 89 F8 75 
    28 63 5C 3E 00 08 00 00 
    00 00 00 00 50 C9 00 03 
    01 00 00 00 04 00 00 00 
    03 00 00 00 01 75 37 33 
    F4 38 29 75 6F 47 67 32 
    76 48 33 70 00 14 63 6F 
    6D 2E 74 65 6E 63 65 6E 
    74 2E 6D 6F 62 69 6C 65 
    71 71 41 36 42 37 34 35 
    42 46 32 34 41 32 43 32 
    37 37 35 32 37 37 31 36 
    46 36 46 33 36 45 42 36 
    38 44 05 E7 AD 8C 00 00 
    00 00 00 00 02 00 00 01 
    00 10 D8 44 E7 DC BB E2 
    17 CB 9C 77 7F B0 FF B7 
    B7 42
    """.trimIndent())
    }

    fun t193(ticket: String) = buildTlv(0x193) {
        writeBytes(ticket.toByteArray())
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
        }
    }
}
