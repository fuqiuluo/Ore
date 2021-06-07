package moe.ore.core

import moe.ore.core.bot.BotRecorder
import moe.ore.core.bot.WLoginSigInfo
import moe.ore.core.protocol.ProtocolInternal
import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsOutputStream
import moe.ore.tars.TarsStructBase
import moe.ore.util.BytesUtil
import moe.ore.util.FileUtil
import moe.ore.util.MD5
import moe.ore.util.bytes.hex2ByteArray
import java.io.File
import java.security.SecureRandom
import java.util.*
import kotlin.math.abs

class DataManager private constructor(uin: ULong) : TarsStructBase() {

    private var path = ""

    private constructor(uin: ULong, path: String) : this(uin) {
        this.path = path
    }

    /**
     * 数据保存目录
     */
    @JvmField
    var dataPath: String = File(path + File.separator + "$uin.ore").absolutePath

    /**
     * 管理器
     */
    @JvmField
    val recorder = BotRecorder()

    /**
     * 保存各种Token
     */
    @JvmField
    var sigInfo = WLoginSigInfo()

    /**
     * 模拟的安卓信息
     */
    var deviceInfo = DeviceInfo()
    var protocol: ProtocolInternal.ProtocolType = ProtocolInternal.ProtocolType.ANDROID_PHONE

    init {
        if (path.isBlank()) {
            throw RuntimeException("错误：${uin}，请先调用${OreBot::class.java.simpleName}.setDataPath()完成初始化")
        }
        if (FileUtil.has(dataPath)) {
            println(dataPath)
            readFrom(TarsInputStream(FileUtil.readFile(dataPath)))
        }
    }

    /**
     * 销毁
     */
    fun destroy() {
        FileUtil.saveFile(dataPath, toByteArray())
        println("destroy")
        // TODO: 2021/6/6 销毁之前序列化到本地文件
        // 清空自身类里面的map或存在引用关系的事务
    }

    fun flush() {
        FileUtil.saveFile(dataPath, toByteArray())
    }

    class DeviceInfo : TarsStructBase() {
        class NetworkType(val value: Int) {
            companion object {
                /**
                 * 移动网络
                 */
                val MOBILE = NetworkType(1)

                /**
                 * Wifi
                 */
                val WIFI = NetworkType(2)

                /**
                 * 其他任何类型
                 */
                val OTHER = NetworkType(0)
            }
        }

        var imei: String = getImei15(("86" + System.currentTimeMillis()).substring(0, 14))
        var androidId: String = getRandomAndroidId()
        var imsi: String = ("46002" + System.currentTimeMillis()).substring(0, 15)
        var machineName: String = "M2002J9E"
        var osType: String = "android"
        var machineManufacturer: String = "Xiaomi"
        var androidVersion: String = "11"
        var androidSdkVersion: Int = 30
        var wifiSsid: String = "<unknown ssid>"
        var wifiBSsid = "02:00:00:00:00:00"
        var macAddress = "02:00:00:00:00:00"
        var netType = NetworkType.WIFI.value
        var apn = if (netType == NetworkType.WIFI.value) {
            "wifi"
        } else {
            "cmnet"
        }
        var apnName = "中国移动"
        var ksid: ByteArray = "14751d8e7d633d9b06a392c357c675e5".hex2ByteArray() //t108
        var randKey: ByteArray = BytesUtil.randomKey(16)
        var guid: ByteArray = MD5.toMD5Byte((imei.ifEmpty { androidId }) + macAddress)

        var tgtgKey: ByteArray = MD5.toMD5Byte(BytesUtil.byteMerger(MD5.toMD5Byte(macAddress), guid))

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
            output.write(apnName, 14)
            output.write(ksid, 15)
            output.write(randKey, 16)
            output.write(guid, 17)
            output.write(tgtgKey, 18)
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
            apnName = input.read(apn, 14, true)
            ksid = input.read(ksid, 15, true)
            randKey = input.read(randKey, 16, true)
            guid = input.read(guid, 17, true)
            tgtgKey = input.read(tgtgKey, 18, true)
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

    companion object {
        private val managerMap = hashMapOf<ULong, DataManager>()

        /**
         * 获取管理器
         *
         * @param uin ULong
         * @return DataManger
         */
        @JvmStatic
        fun manager(uin: ULong): DataManager {
            return managerMap.getOrElse(uin) { throw RuntimeException("错误：${uin}，请先调用${OreBot::class.java.simpleName}.setDataPath()完成初始化") }
        }

        @JvmStatic
        fun init(uin: ULong, path: String): DataManager {
            return managerMap.getOrPut(uin) { DataManager(uin, path) }
        }

        @JvmStatic
        fun flush(uin: ULong) {
            managerMap.remove(uin)?.flush()
        }

        /**
         * 销毁释放
         *
         * @param uin ULong
         */
        @JvmStatic
        fun destroy(uin: ULong) {
            managerMap.remove(uin)?.destroy()
        }
    }

    @Override
    override fun writeTo(output: TarsOutputStream) {
        output.write(deviceInfo, 1)
        output.write(sigInfo, 2)
        output.write(protocol.name, 3)
    }

    @Override
    override fun readFrom(input: TarsInputStream) {
        deviceInfo = input.read(deviceInfo, 1, true)
        sigInfo = input.read(sigInfo, 2, true)
        protocol = ProtocolInternal.ProtocolType.valueOf(input.readString(3, true))
    }

    fun get_mpasswd(): String {
        return try {
            val str = StringBuilder()
            for (b in SecureRandom.getSeed(16)) {
                val abs = abs(b % 26) + if (Random().nextBoolean()) 97 else 65
                str.append(abs.toChar())
            }
            str.toString()
        } catch (unused: Throwable) {
            "1234567890123456"
        }
    }
}

