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

import kotlinx.io.core.*
import kotlinx.serialization.ExperimentalSerializationApi
import moe.ore.core.bot.LoginExtraData
import moe.ore.core.bot.writeLoginExtraData
import moe.ore.core.helper.DataManager
import moe.ore.helper.bytes.*
import moe.ore.util.MD5
import kotlin.random.Random
import kotlin.experimental.or

@ExperimentalSerializationApi
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
        return out.toByteArray()
    }

    fun t1() = buildTlv(0x1) {
        writeShort(protocolInfo.ipVersion)
        writeInt(Random.nextInt())
        writeLongToBuf32(uin)
        writeInt(currentTimeSeconds())
        writeFully(deviceInfo.clientIp)
        writeShort(0)
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
        writeInt(protocolInfo.appId)
        writeInt(0)
        writeInt(protocolInfo.mainSigMap)
        // mainSigMap
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
        writeInt(protocolInfo.subSigMap)
        val appidArray = intArrayOf(0x5f5e10e2)
        writeByte(appidArray.size.toByte())
        for (appid in appidArray) {
            writeInt(appid)
        }
    }

    fun t116V2() = buildTlv(0x116) {
        writeByte(0.toByte())
        writeInt(0x08F7FF7C)
        writeInt(66560)
        val appidArray = intArrayOf(0x5f5e10e2)
        writeByte(appidArray.size.toByte())
        for (appid in appidArray) {
            writeInt(appid)
        }
    }

    fun t106() = buildTlv(0x106) {
        writeTeaEncrypt(dataManager.botAccount.md5PasswordWithQQ()) {
            writeShort(protocolInfo.tgtgVersion)
            writeInt(Random.nextInt())
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
            writeInt(protocolInfo.appId)
            writeInt(protocolInfo.loginType)
            writeStringWithShortSize(uin.toString())
            writeShort(0)
        }
    }

    private fun t124() = buildTlv(0x124) {
        writeBytesWithShortSize(deviceInfo.osType.toByteArray())
        writeStringWithShortSize(deviceInfo.androidVersion)
        writeShort(deviceInfo.netType.value)
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
        writeShort(deviceInfo.netType.value)
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
        /**
         * 20 B5 33 79 18 79 9C AB E4 4A 8E F8 0D 66 84 B81F 8C 15 24 AD 46 D6 D7 7A AF 24 6A 09 16 0A 59AF 22 ED 5B 14 A8 B4 78 36 F2 AC 9A 34 61 15 3A
         */
        writeFully(noPicSig)
    }

    /**
     * 登录过 设备所显示名称
     */
    fun t16b() = buildTlv(0x16b) {
        val domains = arrayOf("tenpay.com", "qzone.qq.com", "qun.qq.com", "mail.qq.com", "openmobile.qq.com", "qzone.com", "game.qq.com", "vip.qq.com")
        writeShort(domains.size)
        for (s in domains) {
            writeStringWithShortSize(s)
        }
    }

    private fun t16e() = buildTlv(0x16e) {
        writeString(deviceInfo.model)
    }

    fun t174(dt174: ByteArray) = buildTlv(0x174) {
        writeBytes(dt174)
    }

    fun t177() = buildTlv(0x177) {
        writeBoolean(true)
        writeInt(protocolInfo.buildTime)
        writeStringWithShortSize(protocolInfo.buildVersion)
    }


    fun t17a() = buildTlv(0x17a) {
        writeInt(9)
    }

    fun t17c(code: String) = buildTlv(0x17c) {
        writeShort(code.length)
        writeString(fun(): String {
            val newCode = code.replace(" ", "")
            if (newCode.length == 6) {
                return newCode
            }
            error("验证码不合法")
        }())
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
        writeLimitedString(deviceInfo.wifiBSsid, 16)
        writeLimitedString(deviceInfo.wifiBSsid, 32)
    }

    // TODO: 2021/6/9 Emp用的
    // sigInfo2 = (client.device.guid + client.dpwd + tlvMap.getOrFail(0x402)).md5()
    fun t400(sigInfo2: ByteArray) = buildTlv(0x400) {
        writeTeaEncrypt(sigInfo2) {
            writeByte(1) // version
            writeLong(uin)
            writeFully(deviceInfo.guid)
            writeFully(dataManager.wLoginSigInfo.dpwd)
            writeInt(protocolInfo.appId)
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
        val domains = arrayOf(
            "office.qq.com",
            "qun.qq.com",
            "gamecenter.qq.com",
            "docs.qq.com",
            "mail.qq.com",
            "ti.qq.com",
            "vip.qq.com",
            "tenpay.qq.com",
            "qqqweb.qq.com",
            "qzone.qq.com",
            "mma.qq.com",
            "game.qq.com",
            "openmobile.qq.com",
            "conect.qq.com"
            // "y.qq.com",
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
            writeByte(b)
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

    // 1334
    fun t536(loginExtraData: Collection<LoginExtraData>) = buildTlv(0x536) {
        writeShortLVPacket {
            writeByte(1)
            writeByte(loginExtraData.size.toByte())
            for (extraData in loginExtraData) {
                writeLoginExtraData(extraData)
            }
        }
    }

    fun t525(loginExtraData: Collection<LoginExtraData>) = buildTlv(0x525) {
        writeShort(1)
        t536(loginExtraData)
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
