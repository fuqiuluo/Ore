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

import kotlinx.io.core.BytePacketBuilder
import kotlinx.io.core.writeFully
import moe.ore.core.bot.BotAccount
import moe.ore.core.bot.DeviceInfo
import moe.ore.core.bot.LoginExtraData
import moe.ore.core.bot.SsoSession
import moe.ore.helper.*
import moe.ore.util.MD5
import kotlin.experimental.or
import kotlin.random.Random

class Tlv(
    private val account: BotAccount,
    private val deviceInfo : DeviceInfo,
    private val session: SsoSession,
    protocolType: ProtocolInternal.ProtocolType
) {
    /**
     * 协议信息
     */
    private val protocolInfo = ProtocolInternal[protocolType]

    fun t1() = buildTlv(0x1) {
        writeShort(protocolInfo.ipVersion)
        writeInt(Random.nextInt())
        writeLongToBuf32(account.uin)
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
        writeLongToBuf32(account.uin)
        writeInt(session.rollBackCount)
        // 默认开始是0，回滚（rollback）一次就+1
    }

    fun t10a(tgt : ByteArray) = buildTlv(0x10a) {
        writeBytes(tgt)
    }

    fun t100(appId : Int = protocolInfo.appId, mainSigMap : Int = protocolInfo.mainSigMap) = buildTlv(0x100) {
        writeShort(protocolInfo.dbVersion)
        writeInt(protocolInfo.msfSsoVersion)
        writeInt(protocolInfo.subAppId)
        writeInt(appId)
        writeInt(0)
        writeInt(mainSigMap)
        // mainSigMap
    }

    fun t104(dt104: ByteArray?) = buildTlv(0x104) {
        writeBytes(dt104 ?: byteArrayOf())
    }

    fun t106() = buildTlv(0x106) {
        writeTeaEncrypt(account.bytesMd5PasswordWithQQ) {
            writeShort(protocolInfo.tgtgVersion)
            writeInt(Random.nextInt())
            writeInt(protocolInfo.msfSsoVersion)
            writeInt(protocolInfo.subAppId)
            writeInt(0)
            writeInt(0)
            writeLongToBuf32(account.uin)
            writeInt(currentTimeSeconds())
            writeFully(deviceInfo.clientIp)
            writeByte(1.toByte())
            writeBytes(account.bytesMd5Password)
            writeBytes(deviceInfo.tgtgt)
            writeInt(0)
            writeBoolean(protocolInfo.isGuidAvailable)
            writeBytes(deviceInfo.guid)
            writeInt(protocolInfo.appId)
            writeInt(protocolInfo.loginType)
            writeStringWithShortLen(account.uin.toString())
            writeShort(0)
        }
    }

    fun t107() = buildTlv(0x107) {
        writeShort(0) // picType
        writeByte(0.toByte())
        writeShort(0)
        writeByte(1.toByte())
    }

    fun t108() = buildTlv(0x108) {
        writeBytes(deviceInfo.ksid)
    }

    private fun t109() = buildTlv(0x109) {
        writeBytes(MD5.toMD5Byte(deviceInfo.androidId.ifEmpty { deviceInfo.imei }))
    }

    fun t112() = buildTlv(0x112) {
        writeString(account.uin.toString())
    }

    fun t116(appidArray : IntArray = intArrayOf(0x5f5e10e2)) = buildTlv(0x116) {
        writeByte(0.toByte())
        // ver
        writeInt(protocolInfo.miscBitmap)
        writeInt(protocolInfo.subSigMap)
        writeByte(appidArray.size.toByte())
        for (appid in appidArray) {
            writeInt(appid)
        }
    }

    private fun t124() = buildTlv(0x124) {
        writeStringWithShortLen(deviceInfo.osType)
        writeStringWithShortLen(deviceInfo.androidVersion)
        writeShort(deviceInfo.netType.value)
        writeStringWithShortLen(deviceInfo.apnName)
        writeStringWithIntLen(deviceInfo.apn)
    }

    private fun t128() = buildTlv(0x128) {
        writeShort(0)
        writeBoolean(protocolInfo.isGuidFromFileNull)
        writeBoolean(protocolInfo.isGuidAvailable)
        writeBoolean(protocolInfo.isGuidChanged)
        writeInt(0x01000000)
        writeStringWithShortLen(deviceInfo.model)
        writeBytesWithShortLen(deviceInfo.guid)
        writeStringWithShortLen(deviceInfo.brand)
    }

    fun t141() = buildTlv(0x141) {
        writeShort(1)
        // version
        writeStringWithShortLen(deviceInfo.apnName)
        writeShort(deviceInfo.netType.value)
        writeStringWithShortLen(deviceInfo.apn)
    }

    fun t142() = buildTlv(0x142) {
        writeShort(0)
        writeStringWithShortLen(protocolInfo.packageName)
    }

    fun t143(d2 : ByteArray) = buildTlv(0x143) {
        writeBytes(d2)
        // writeBytesWithShortLen(d2)
    }

    fun t144(key : ByteArray = deviceInfo.tgtgt) = buildTlv(0x144) {
        writeTeaEncrypt(key) {
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
        writeStringWithShortLen(protocolInfo.packageVersion)
        writeBytesWithShortLen(protocolInfo.tencentSdkMd5)
    }

    fun t154(seq: Int) = buildTlv(0x154) {
        writeInt(seq)
    }

    fun t166() = buildTlv(0x166) {
        writeByte(session.imgType.toByte())
        // imgType
    }

    fun t16a(noPicSig: ByteArray) = buildTlv(0x16a) {
        /**
         * 20 B5 33 79 18 79 9C AB E4 4A 8E F8 0D 66 84 B81F 8C 15 24 AD 46 D6 D7 7A AF 24 6A 09 16 0A 59AF 22 ED 5B 14 A8 B4 78 36 F2 AC 9A 34 61 15 3A
         */
        writeBytes(noPicSig)
    }

    /**
     * 登录过 设备所显示名称
     */
    fun t16b() = buildTlv(0x16b) {
        val domains = arrayOf("tenpay.com", "qzone.qq.com", "qun.qq.com", "mail.qq.com", "openmobile.qq.com", "qzone.com", "game.qq.com", "vip.qq.com")
        writeShort(domains.size)
        for (s in domains) {
            writeStringWithShortLen(s)
        }
    }

    private fun t16e() = buildTlv(0x16e) {
        writeString(deviceInfo.model)
    }

    fun t172() = buildTlv(0x172) {
        writeBytes(session.rollbackSig ?: byteArrayOf())
    }

    fun t174(dt174: ByteArray?) = buildTlv(0x174) {
        writeBytes(dt174 ?: byteArrayOf())
    }

    fun t177() = buildTlv(0x177) {
        writeBoolean(true)
        writeInt(protocolInfo.buildTime)
        writeStringWithShortLen(protocolInfo.oicqBuildVersion)
    }

    fun t17a() = buildTlv(0x17a) {
        // 这个9 是smsAppid
        writeInt(9)
    }

    fun t17c(code: String) = buildTlv(0x17c) {
        writeShort(code.length)
        writeString(fun(): String {
            val newCode = code.replace(" ", "")
            if (newCode.length == 6) {
                return newCode
            }
            runtimeError("验证码不合法")
        }())
    }

    fun t187() = buildTlv(0x187) {
        writeBytes(MD5.toMD5Byte(deviceInfo.macAddress))
    }

    fun t188() = buildTlv(0x188) {
        writeBytes(MD5.toMD5Byte(deviceInfo.androidId.ifEmpty { deviceInfo.imei }))
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

    fun t201(payToken : ByteArray) = buildTlv(0x201) {
        writeBytesWithShortLen(payToken)
        writeStringWithShortLen("")
        writeStringWithShortLen("qq")
        writeStringWithShortLen("") // channelId
    }

    fun t202() = buildTlv(0x202) {
        writeBytesWithShortLen(MD5.toMD5Byte(deviceInfo.wifiBSsid))
        writeStringWithShortLen(deviceInfo.wifiSsid)
    }

    fun t400(g: ByteArray) = buildTlv(0x400) {
        writeTeaEncrypt(g) {
            writeByte(1) // version
            writeLong(account.uin)
            writeFully(deviceInfo.guid)
            writeFully(session.pwd)
            writeInt(protocolInfo.appId)
            writeInt(protocolInfo.subAppId)
            writeInt(currentTimeSeconds())
            writeFully(session.randSeed)
        }
    }

    fun t318(t318 : ByteArray) = buildTlv(0x318) {
        writeBytes(t318)
    }

    fun t401(g: ByteArray) = buildTlv(0x401) {
        /**
        builder.writeBytes(deviceInfo.guid)
        builder.writeBytes(dataManager.wLoginSigInfo.dpwd)
        builder.writeBytes(dt402)
         **/
        writeBytes(g)
    }

    fun t402(dt402: ByteArray) = buildTlv(0x402) {
        writeBytes(dt402)
    }

    fun t403(dt403: ByteArray) = buildTlv(0x403) {
        writeBytes(dt403)
    }

    fun t511(domains : Array<String> = arrayOf(
        "accounts.qq.com",
        "aq.qq.com",
        "buluo.qq.com",
        "connect.qq.com",
        "docs.qq.com",
        "game.qq.com",
        "gamecenter.qq.com",
        "graph.qq.com",
        "id.qq.com",
        "imgcache.qq.com",
        "mail.qq.com",
        "openmobile.qq.com",
        "qun.qq.com",
        "qzone.com",
        "tenpay.com",
        "ti.qq.com",
        "v.qq.com",
        "vip.qq.com",
        "y.qq.com",
        "office.qq.com"
    )) = buildTlv(0x511) {
        // 备选  "haoma.qq.com","mma.qq.com","om.qq.com","kg.qq.com",
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
            writeStringWithShortLen(domain)
        }
    }

    fun t516() = buildTlv(0x516) {
        writeInt(0)
    }

    fun t521() = buildTlv(0x521) {
        writeInt(0)
        writeShort(0)
    }

    private fun t536(loginExtraData: Collection<LoginExtraData> = listOf()) = buildTlv(0x536) {
        writeByte(1)
        writeByte(loginExtraData.size.toByte())
        for (extraData in loginExtraData) {
            writeLong(extraData.uin)
            writeByte(extraData.ip.size.toByte())
            writeBytes(extraData.ip)
            writeInt(extraData.time)
            writeInt(extraData.appId)
        }
    }

    fun t525(loginExtraData: Collection<LoginExtraData> = listOf()) = buildTlv(0x525) {
        writeShort(1)
        writeBytes(t536(loginExtraData))
    }

    private fun t52d() = buildTlv(0x52d) {
        writeBytes(deviceInfo.deviceReport.toByteArray())
    }

    fun t542() = buildTlv(0x542) {
        writeByte(0.toByte())
        writeByte(0.toByte())
    }

    fun t544() = buildTlv(0x544) {
        writeHex("68656861000000010100000000000000010000030800000000e4e359180000000100000096000100080000017a08916e720002000a3647446268424863434d00030004010000010005000401000001000400040000000000060004010000040007000401000002000800040100000300090020da19f01ef1921f8e5934a4315c5530724269608f163986384080b91fe202d8f0000a00107af1515ceec23e30d9df87e3b4815266000b0010b68e356ed5e54e1525124f89e9aa2471")
    }

    fun t545() = buildTlv(0x545) {
        // qimei
        writeHex("613664343731333466346264656366613138653866303266313030303165353135333131")
    }

    fun t547(byteArray: ByteArray) = buildTlv(0x547) {
        writeFully(byteArray)
    }

    fun t193(ticket: String) = buildTlv(0x193) {
        writeString(ticket)
    }
}

internal inline fun buildTlv(tlvVer: Int, block: BytePacketBuilder.() -> Unit): ByteArray {
    val bodyBuilder = BytePacketBuilder()
    val out = BytePacketBuilder()
    bodyBuilder.block()
    out.writeShort(tlvVer.toShort())
    out.writeShort(bodyBuilder.size.toShort())
    out.writePacket(bodyBuilder)
    return out.toByteArray()
}