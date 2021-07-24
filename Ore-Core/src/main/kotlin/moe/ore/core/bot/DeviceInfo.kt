package moe.ore.core.bot

import kotlinx.io.core.toByteArray
import moe.ore.core.protocol.pb.DeviceReport
import moe.ore.helper.hex2ByteArray
import moe.ore.tars.*
import moe.ore.util.BytesUtil
import moe.ore.util.MD5
import java.util.*
import kotlin.random.Random

@TarsClass(requireWrite = true, requireRead = true)
class DeviceInfo : TarsBase() {
    enum class NetworkType(val value: Int) {
        /**
         * 移动网络
         */
        MOBILE(1),

        /**
         * Wifi
         */
        WIFI(2),

        /**
         * 其他任何类型
         */
        OTHER(0),
    }

    @TarsField(id = 1)
    var imei: String = " 867109044454073"

    // 53f156a0b5b89966【真的】  53f256a0aaff9966【假的】
    @TarsField(id = 2)
    var androidId: String = "53f156a0b5b89966"
    @TarsField(id = 3)
    var imsi: String = "460023785098616"
    @TarsField(id = 4)
    var model: String = "M2002J9E"
    @TarsField(id = 5)
    var osType: String = "android"
    @TarsField(id = 6)
    var brand: String = "Xiaomi"
    @TarsField(id = 7)
    var androidVersion: String = "11"
    @TarsField(id = 8)
    var androidSdkVersion: Int = 30
    @TarsField(id = 9)
    var wifiSsid: String = "<unknown ssid>"
    @TarsField(id = 10)
    var wifiBSsid = "02:00:00:00:00:00"
    @TarsField(id = 11)
    var macAddress = "02:00:00:00:00:00"

    @TarsField(id = 12, isEnum = true)
    var netType: NetworkType = NetworkType.WIFI

    @TarsField(id = 13)
    var apn = if (netType == NetworkType.WIFI) {
        "wifi"
    } else {
        "cmnet"
    }

    @TarsField(id = 14)
    var apnName = "中国移动"
    @TarsField(id = 15)
    var ksid: ByteArray = "31008c9064e89b48f20765fd739edd1f".hex2ByteArray()

    // %4;7t>;28<fc.5*6
    var guid: ByteArray = MD5.toMD5Byte(androidId + macAddress)

    // 实际上在逆向8.7.5时 并没有出现md5(macAddr) 而是随机了一个16字节的东西
    // 不保存 懒加载在线合成
    var tgtgt: ByteArray = MD5.toMD5Byte(Random.nextBytes(16) + guid)

    // expamel 1, 0, 0, 127 是倒过来的哦！
    @TarsField(id = 18)
    var clientIp = byteArrayOf(0, 0, 0, 0)
    
    var deviceReport = DeviceReport(bootloader = "unknown".toByteArray(), version = "Linux version 4.19.113-perf-gb3dd08fa2aaa (builder@c5-miui-ota-bd143.bj) (clang version 8.0.12 for Android NDK) #1 SMP PREEMPT Thu Feb 4 04:37:10 CST 2021;".toByteArray(), codename = "REL".toByteArray(), incremental = "20.8.13".toByteArray(), fingerprint = "Xiaomi/vangogh/vangogh:11/RKQ1.200826.002/21.2.4:user/release-keys".toByteArray(), bootId = "".toByteArray(), androidId = androidId.toByteArray(), baseband = "".toByteArray(), innerVer = "21.2.4".toByteArray())

    companion object {
        @JvmStatic
        private fun getImei15(imei: String): String {
            val imeiChar = imei.toCharArray()
            var resultInt = 0
            var i = 0
            while (i < imeiChar.size) {
                val a = imeiChar[i].toString().toInt()
                i++
                val temp = imeiChar[i].toString().toInt() * 2
                val b = if (temp < 10) temp else temp - 9
                resultInt += a + b
                i++
            }
            resultInt %= 10
            resultInt = if (resultInt == 0) 0 else 10 - resultInt
            return imei + resultInt
        }

        @JvmStatic
        private fun getRandomAndroidId(): String {
            return UUID.randomUUID().toString().replace("-", "").substring(0, 16)
        }

        @JvmStatic
        private fun getRandomMacAddress(): String {
            val randomKey = BytesUtil.randomKey(6)
            val sb = StringBuilder()
            val length: Int = randomKey.size
            for (i in 0 until length) {
                sb.append(String.format("%02X:", java.lang.Byte.valueOf(randomKey[i])))
            }
            if (sb.isNotEmpty()) {
                sb.deleteCharAt(sb.length - 1)
            }
            return sb.toString()
        }
    }
}