package moe.ore.core.bot

import moe.ore.helper.hex2ByteArray
import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsOutputStream
import moe.ore.tars.TarsStructBase
import moe.ore.util.BytesUtil
import moe.ore.util.MD5
import java.util.*
import kotlin.random.Random

class DeviceInfo : TarsStructBase() {
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

    var imei: String = " 867109044454073"

    // 53f156a0b5b89966【真的】  53f256a0aaff9966【假的】
    var androidId: String = "53f156a0b5b89966"
    var imsi: String = "460023785098616"
    var model: String = "M2002J9E"
    var osType: String = "android"
    var brand: String = "Xiaomi"
    var androidVersion: String = "11"
    var androidSdkVersion: Int = 30
    var wifiSsid: String = "<unknown ssid>"
    var wifiBSsid = "02:00:00:00:00:00"
    var macAddress = "02:00:00:00:00:00"
    var netType: NetworkType = NetworkType.WIFI

    var apn = if (netType == NetworkType.WIFI) {
        "wifi"
    } else {
        "cmnet"
    }

    var apnName = "中国移动"
    var ksid: ByteArray = "31008c9064e89b48f20765fd739edd1f".hex2ByteArray()

    // %4;7t>;28<fc.5*6
    var guid: ByteArray = MD5.toMD5Byte(androidId + macAddress)

    // 实际上在逆向8.7.5时 并没有出现md5(macAddr) 而是随机了一个16字节的东西
    // 不保存 懒加载在线合成
    var tgtgt: ByteArray = MD5.toMD5Byte(Random.nextBytes(16) + guid)

    // expamel 1, 0, 0, 127 是倒过来的哦！
    var clientIp = byteArrayOf(0, 0, 0, 0)

    override fun writeTo(output: TarsOutputStream) {
        output.write(imei, 1)
        output.write(androidId, 2)
        output.write(imsi, 3)
        output.write(model, 4)
        output.write(osType, 5)
        output.write(brand, 6)
        output.write(androidVersion, 7)
        output.write(androidSdkVersion, 8)
        output.write(wifiSsid, 9)
        output.write(wifiBSsid, 10)
        output.write(macAddress, 11)
        output.write(netType.name, 12)
        output.write(apn, 13)
        output.write(apnName, 14)
        output.write(ksid, 15)
        output.write(clientIp, 18)
    }

    override fun readFrom(input: TarsInputStream) {
        imei = input.read(imei, 1, false)
        androidId = input.read(androidId, 2, false)
        imsi = input.read(imsi, 3, false)
        model = input.read(model, 4, false)
        osType = input.read(osType, 5, false)
        brand = input.read(brand, 6, false)
        androidVersion = input.read(androidVersion, 7, false)
        androidSdkVersion = input.read(androidSdkVersion, 8, false)
        wifiSsid = input.read(wifiSsid, 9, false)
        wifiBSsid = input.read(wifiBSsid, 10, false)
        macAddress = input.read(macAddress, 11, false)
        netType = NetworkType.valueOf(input.read("netType.name", 12, false))
        apn = input.read(apn, 13, false)
        apnName = input.read(apnName, 14, false)
        ksid = input.read(ksid, 15, false)
        clientIp = input.read(clientIp, 18, false)
    }

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