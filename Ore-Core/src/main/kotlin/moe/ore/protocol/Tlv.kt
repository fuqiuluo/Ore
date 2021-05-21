package moe.ore.protocol

import moe.ore.ToolQ
import moe.ore.helper.Initializer
import moe.ore.helper.bytes.ByteBuilder
import moe.ore.helper.bytes.hex2ByteArray
import moe.ore.helper.toolq.KeyType
import moe.ore.protocol.pb.DeviceReport
import moe.ore.util.BytesUtil
import moe.ore.util.MD5
import moe.ore.util.TeaUtil
import moe.ore.virtual.Android
import java.lang.Exception
import java.security.SecureRandom
import kotlin.math.abs
import kotlin.random.Random

class Tlv(toolQ: ToolQ) {
    private val record = toolQ.msfRecorder
    private val android = Android.android
    private val account = toolQ.account
    private val protocol = toolQ.protocolInfo

    fun t1() = createTlv(0x1) {
        writeShort(protocol.ipVersion())
        writeInt(0)
        writeULong(account.uin)
        writeULong(Initializer.initTime / 1000)
        writeULong(0)
        writeShort(0)
    }

    fun t8() = createTlv(0x8) {
        writeShort(0)
        writeInt(protocol.localeId())
        writeShort(0)
    }

    fun t18() = createTlv(0x18) {
        writeShort(protocol.pingVersion())
        writeInt(protocol.ssoVersion())
        writeInt(protocol.subAppId())
        writeInt(0)
        writeULong(account.uin)
        writeInt(record.rollbackTime)
        // 回滚重定向次数 默认开始是0
    }

    fun t100() = createTlv(0x100) {
        writeShort(protocol.dbVersion())
        writeInt(protocol.msfSsoVersion())
        writeInt(protocol.subAppId())
        writeInt(protocol.msfAppId())
        writeInt(0)
        writeInt(0x21410e0)
    }

    fun t104() = createTlv(0x104) {
        writeBytes(record.getKey(KeyType.Tlv104)!!)
    }

    fun t106(loginType : Int) = createTlv(0x106) {
        val builder = ByteBuilder()
        builder.writeShort(protocol.tgtgVersion())
        builder.writeInt(Random.nextInt(0, 828922931))
        builder.writeInt(protocol.msfSsoVersion())
        builder.writeInt(protocol.subAppId())
        builder.writeInt(0)
        builder.writeInt(0)
        builder.writeULong(account.uin)
        builder.writeULong(Initializer.initTime / 1000)
        builder.writeInt(0)
        // IP Address
        builder.writeByte(1)
        builder.writeBytes(account.md5Password())
        builder.writeBytes(android.getTgtgKey())
        builder.writeInt(0)
        builder.writeBoolean(true)
        builder.writeBytes(android.getGuid())
        builder.writeInt(protocol.msfAppId())
        builder.writeInt(loginType)
        builder.writeStringWithShortSize(account.uinString())
        builder.writeShort(0)
        writeBytes(TeaUtil.encrypt(builder.toByteArray(), account.md5UinAndPassword()))
        builder.close()
    }

    fun t107() = createTlv(0x107) {
        writeShort(0)
        writeByte(0)
        writeShort(0)
        writeByte(1)
    }

    fun t108() = createTlv(0x108) {
        writeBytes(android.ksid)
    }

    fun t109() = createTlv(0x109) {
        writeBytes(MD5.toMD5Byte(android.imei.ifEmpty { android.androidId }))
    }

    fun t116() = createTlv(0x116) {
        writeByte(0)
        writeInt(184024956)
        writeInt(66560)
        val appIds = intArrayOf(0x5f5e10e2)
        writeByte(appIds.size)
        appIds.forEach {
            writeInt(it)
        }
    }

    fun t116V2() = createTlv(0x116) {
        writeByte(0)
        writeInt(0x08F7FF7C)
        writeInt(66560)
        val appIds = intArrayOf(0x5f5e10e2)
        writeByte(appIds.size)
        appIds.forEach {
            writeInt(it)
        }
    }

    fun t124() = createTlv(0x124) {
        writeStringWithShortSize(android.osType)
        writeStringWithShortSize(android.androidVersion)
        writeShort(when(android.netType) {
            "5g", "wap", "net", "4gnet", "3gwap", "cncc", "cmcc" -> 1
            "wifi" -> 2
            else -> 0
        })
        writeStringWithShortSize(android.apn)
        writeStringWithSize(android.netType)
    }

    fun t128() = createTlv(0x128) {
        writeShort(0)
        writeBoolean(false)
        writeBoolean(true)
        writeBoolean(false)
        writeInt(0x01000000)
        writeStringWithShortSize(android.machineName)
        writeBytesWithShortSize(android.getGuid())
        writeStringWithShortSize(android.machineManufacturer)
    }

    fun t141() = createTlv(0x141) {
        writeShort(1)
        writeStringWithShortSize(android.apn)
        writeShort(when(android.netType) {
            "5g", "wap", "net", "4gnet", "3gwap", "cncc", "cmcc" -> 1
            "wifi" -> 2
            else -> 0
        })
        writeStringWithShortSize(android.apn)
    }

    fun t142() = createTlv(0x142) {
        writeShort(0)
        writeStringWithShortSize(protocol.packageName())
    }

    fun t144() = createTlv(0x144) {
        val builder = ByteBuilder()
        builder.writeShort(5)
        builder.writeBytes(t109())
        builder.writeBytes(t52d())
        builder.writeBytes(t124())
        builder.writeBytes(t128())
        builder.writeBytes(t16e())
        writeBytes(TeaUtil.encrypt(builder.toByteArray(), android.getTgtgKey()))
        builder.close()
    }

    fun t145() = createTlv(0x145) {
        writeBytes(android.getGuid())
    }

   fun t147() = createTlv(0x147) {
       writeInt(protocol.subAppId())
       writeStringWithShortSize(protocol.packageVersion())
       writeBytesWithShortSize(protocol.sdkMd5())
   }

    fun t154(seq : Int) = createTlv(0x154) {
        writeInt(seq)
    }

    fun t16a() = createTlv(0x16a) {
        writeBytes(record.getKey(KeyType.NoPicSig).let {
            it ?: "20 B5 33 79 18 79 9C AB E4 4A 8E F8 0D 66 84 B81F 8C 15 24 AD 46 D6 D7 7A AF 24 6A 09 16 0A 59AF 22 ED 5B 14 A8 B4 78 36 F2 AC 9A 34 61 15 3A".hex2ByteArray()
        })
    }

    fun t16b() = createTlv(0x16b) {
        val domains = arrayOf(
            "tenpay.com",
            "qzone.qq.com",
            "qun.qq.com",
            "mail.qq.com",
            "openmobile.qq.com",
            "qzone.com",
            "game.qq.com",
            "vip.qq.com"
        )
        writeShort(domains.size)
        for (s in domains) {
            writeStringWithShortSize(s)
        }
    }

    fun t16e() = createTlv(0x16e) {
        writeString(android.machineName)
    }

   fun t174() = createTlv(0x174) {
       writeBytes(record.getKey(KeyType.Tlv174)!!)
   }

    fun t177() = createTlv(0x177) {
        writeBoolean(true)
        writeInt(protocol.buildTime())
        writeStringWithShortSize(protocol.buildVersion())
    }

    fun t17a() = createTlv(0x17a) {
        writeInt(9)
    }

    fun t17c(code: String) = createTlv(0x17c) {
        writeStringWithShortSize(code)
    }

    fun t187() = createTlv(0x187) {
        writeBytes(MD5.toMD5Byte(android.macAddress))
    }

    fun t188() = createTlv(0x188) {
        writeBytes(MD5.toMD5Byte(android.imei.ifEmpty { android.androidId }))
    }

    fun t191() = createTlv(0x191) {
        writeByte(0x82)
    }

    fun t194() = createTlv(0x194) {
        writeBytes(MD5.toMD5Byte(android.imsi))
    }

    fun t197() = createTlv(0x197) {
        writeByte(0)
    }

    fun t198() = createTlv(0x198) {
        writeByte(0)
    }

    fun t202() = createTlv(0x202) {
        writeBytesWithShortSize(MD5.toMD5Byte(android.wifiBSsid))
        writeStringWithShortSize("\"${android.wifiSsid}\"")
    }

    fun t400() = createTlv(0x400) {
        writeHex("D1387BC477015873D624BB495618F37A3096BCB21757E66741E1E5E090E6DD293C402D0003B169879C5B95BB5A21028062CD406335AFE249A508144C26A18A42B3FF12D1A1EB95E8")
    }

    fun t401() = createTlv(0x401) {
        val builder = ByteBuilder()
        builder.writeBytes(android.getGuid())
        builder.writeString(fun() : String {
            try {
                val seed = SecureRandom.getSeed(16)
                val ret = StringBuffer()
                repeat(16) {
                    ret.append( ((if(Random.nextBoolean()) 97 else 65) + abs(seed[it] % 26)).toChar() )
                }
            } catch (e : Exception) {
                e.printStackTrace()
            }
            return "1234567890123456"
        } ())
        builder.writeBytes(record.getKey(KeyType.Tlv402)!!)
        writeBytes(MD5.toMD5Byte(builder.toByteArray()))
        builder.close()
    }

    fun t402() = createTlv(0x402) {
        writeBytes(record.getKey(KeyType.Tlv402)!!)
    }

    fun t403() = createTlv(0x403) {
        writeBytes(record.getKey(KeyType.Tlv403)!!)
    }

    fun t511() = createTlv(0x511) {
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
            var b = 1
            if (start == 0 || end > 0) {
                val i = domain.substring(start + 1, end).toInt()
                b = if (1048576 and i > 0) { 1 } else { 0 }
                if (i and 0x08000000 > 0) {
                    b = (b or 2)
                }
            }
            writeByte(b)
            writeStringWithShortSize(domain)
        }
    }

    fun t516() = createTlv(0x516) {
        writeInt(0)
    }

    fun t521() = createTlv(0x521) {
        writeInt(0)
        writeShort(0)
    }

    fun t525() = createTlv(0x525) {
        writeShort(1)
        writeBytes(t536())
    }

    fun t536() = createTlv(0x536) {
        writeHex("0103000000006FE2BD 2E 04 DF 68 5C 58 5F8A F7 FE 20 02 F8 A1 00 00 00 00 99 68 42 96 04 DF 68 5C 58 5F 8A FD 41 20 02 F8 A1 00 00 00 00 62 5A BF 50 04 DF 68 5C 58 5F 8A FD 42 20 02 F8 A1")
    }

    fun t52d() = createTlv(0x52d) {
        val report = DeviceReport()
        report.bytes_bootloader.set("bytes_bootloader")
        report.bytes_android_id.set(android.androidId)
        report.bytes_version.set("Linux version 4.19.113-perf-gb3dd08fa2aaa (builder@c5-miui-ota-bd143.bj) (clang version 8.0.12 for Android NDK) #1 SMP PREEMPT Thu Feb 4 04:37:10 CST 2021;")
        report.bytes_codename.set("REL")
        report.bytes_incremental.set("20.8.13")
        report.bytes_inner_ver.set("21.2.4")
        report.bytes_fingerprint.set("Xiaomi/vangogh/vangogh:11/RKQ1.200826.002/21.2.4:user/release-keys")
        report.bytes_boot_id.set("")
        report.bytes_baseband.set("")
        writeBytes(report.toByteArray())
    }

    fun t542() = createTlv(0x542) {
        writeByte(0)
        writeByte(0)
    }

    fun t544() = createTlv(0x544) {
        // val data = "00 00 07 D9 00 00 00 00 \n00 2E 00 20 15 97 BF B2 \n50 07 9C 86 AF 7A FB 53 \n64 4F 39 97 E9 0A 15 91 \n83 AD F1 20 CC 89 F8 75 \n28 63 5C 3E 00 08 00 00 \n00 00 00 00 50 C9 00 03 \n01 00 00 00 04 00 00 00 \n03 00 00 00 01 75 37 33 \nF4 38 29 75 6F 47 67 32 \n76 48 33 70 00 14 63 6F \n6D 2E 74 65 6E 63 65 6E \n74 2E 6D 6F 62 69 6C 65 \n71 71 41 36 42 37 34 35 \n42 46 32 34 41 32 43 32 \n37 37 35 32 37 37 31 36 \n46 36 46 33 36 45 42 36 \n38 44 05 E7 AD 8C 00 00 \n00 00 00 00 02 00 00 01 \n00 10 D8 44 E7 DC BB E2 \n17 CB 9C 77 7F B0 FF B7 \nB7 42".hex2ByteArray()
        writeInt(2009)
        writeInt(0)
        writeShort(2 + 32 + 8)
        writeShort(32)
        writeBytes(BytesUtil.randomKey(32))
        writeShort(8)
        writeInt(0)
        writeInt(2020)
        writeShort(3)

        writeBoolean(true)
        writeInt(4)
        writeShort(0)

        writeByte(0)
        writeByte(0)
        writeByte(0)
        writeBytes(BytesUtil.randomKey(16).apply {
        this[0] = 1
        })

        writeStringWithShortSize(protocol.packageName())
        writeString("A6B745BF24A2C277527716F6F36EB68D")

        writeBytes(Random.nextBytes(4))

        writeInt(0)

        // writeBytes(data)
    }

    companion object {
        /**
         * 创建Tlv包
         * @param ver Int
         * @param block [@kotlin.ExtensionFunctionType] Function1<ByteBuilder, Unit>
         * @return ByteArray
         */
        @JvmStatic
        private fun createTlv(ver : Int, block : ByteBuilder.() -> Unit) : ByteArray {
            val blockBuilder = ByteBuilder()
            blockBuilder.block()
            val blockBytes = blockBuilder.toByteArray()
            blockBuilder.close()
            val out = ByteBuilder()
            out.writeShort(ver)
            out.writeShort(blockBytes.size)
            out.writeBytes(blockBytes)
            val outBytes = out.toByteArray()
            out.close()
            return outBytes
        }
    }
}