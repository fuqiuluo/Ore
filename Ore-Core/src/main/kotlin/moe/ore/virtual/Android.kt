package moe.ore.virtual

import moe.ore.helper.Initializer
import moe.ore.helper.bytes.hex2ByteArray
import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsOutputStream
import moe.ore.tars.TarsStructBase
import moe.ore.util.BytesUtil
import moe.ore.util.FileUtil
import moe.ore.util.MD5
import moe.ore.util.TarsUtil

object Android {
    @JvmStatic
    private val path = "${Initializer.dataDirectory.absolutePath}/androidConfig"
    @JvmStatic
    var android : AndroidConfig = if(FileUtil.has(path))
        TarsUtil.decodeWup(AndroidConfig(), FileUtil.readFileBytes(path))
    else {
        AndroidConfig().apply {
            this.flush()
        }
    }

    class AndroidConfig : TarsStructBase() {
        var imei : String = ""
        var androidId : String = "53f156a0b5b89966"
        var imsi : String = "460023785098616"
        var machineName : String = "M2002J9E"
        var osType : String = "android"
        var machineManufacturer : String = "Xiaomi"
        var androidVersion : String = "11"
        var androidSdkVersion : Int = 30
        var wifiSsid : String = "705"
        var wifiBSsid = "D8:9E:61:9C:3D:E0"
        var macAddress = "20:F4:78:6B:80:AA"

        var netType = "wifi"
        var apn = "中国移动"
        var ksid : ByteArray = "14751d8e7d633d9b06a392c357c675e5".hex2ByteArray()

        var randKey : ByteArray = BytesUtil.randomKey(16)

        fun getTgtgKey() = MD5.toMD5Byte(BytesUtil.byteMerger(MD5.toMD5Byte(macAddress), getGuid()))!!

        fun getGuid() = MD5.toMD5Byte( (imei.ifEmpty { androidId }) + macAddress)!!

        fun flush() {
            FileUtil.saveFile(path, toByteArray())
        }

        override fun writeTo(output: TarsOutputStream) {
            output.write(imei, 1)
            output.write(androidId, 2)
            output.write(imsi, 3)
            output.write(machineName, 4)
            output.write(osType, 5)
            output.write(machineManufacturer, 6)
            output.write(androidVersion, 7)
            output.write(androidSdkVersion, 8)
            output.write(wifiSsid, 9)
            output.write(wifiBSsid, 10)
            output.write(macAddress, 11)
            output.write(netType, 12)
            output.write(apn, 13)
            output.write(ksid, 14)
            output.write(randKey, 15)
        }

        override fun readFrom(input: TarsInputStream) {
            imei = input.read(imei, 1, true)
            androidId = input.read(androidId, 2, true)
            imsi = input.read(imsi, 3, true)
            machineName = input.read(machineName, 4, true)
            osType = input.read(osType, 5, true)
            machineManufacturer = input.read(machineManufacturer, 6, true)
            androidVersion = input.read(androidVersion, 7, true)
            androidSdkVersion = input.read(androidSdkVersion, 8, true)
            wifiSsid = input.read(wifiSsid, 9, true)
            wifiBSsid = input.read(wifiBSsid, 10, true)
            macAddress = input.read(macAddress, 11, true)
            netType = input.read(netType, 12, true)
            apn = input.read(apn, 13, true)
            ksid = input.read(ksid, 14, true)
            randKey = input.read(randKey, 15, true)
        }
    }
}

