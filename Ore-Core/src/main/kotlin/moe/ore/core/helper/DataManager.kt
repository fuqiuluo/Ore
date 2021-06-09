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

package moe.ore.core.helper

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import moe.ore.core.OreBot
import moe.ore.core.bot.BotAccount
import moe.ore.core.bot.BotRecorder
import moe.ore.core.bot.WLoginSigInfo
import moe.ore.core.protocol.ProtocolInternal
import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsOutputStream
import moe.ore.tars.TarsStructBase
import moe.ore.util.BytesUtil
import moe.ore.util.FileUtil
import moe.ore.util.MD5
import moe.ore.helper.bytes.hex2ByteArray
import java.io.File
import java.util.*

class DataManager private constructor(uin: Long, path: String) : TarsStructBase() {

    /**
     * 数据保存目录
     */
    @JvmField
    @Transient
    var dataPath: String = File(path + File.separator + "$uin.ore").absolutePath

    /**
     * 管理器
     */
    @JvmField
    @Transient
    val recorder = BotRecorder()

    lateinit var botAccount: BotAccount
    /**
     * 保存各种Token
     */
//    @JvmField
    lateinit var wLoginSigInfo: WLoginSigInfo

    /**
     * 模拟的安卓信息
     */
    var deviceInfo = DeviceInfo()
    var protocolType: ProtocolInternal.ProtocolType = ProtocolInternal.ProtocolType.ANDROID_PHONE

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
        flush()
        println("destroy")
        // TODO: 2021/6/6 销毁之前序列化到本地文件
        // 清空自身类里面的map或存在引用关系的事务
    }

    fun flush() {
        FileUtil.saveFile(dataPath, toByteArray())
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Override
    override fun writeTo(output: TarsOutputStream) {
        output.write(ProtoBuf.encodeToByteArray(deviceInfo), 1)
        output.write(ProtoBuf.encodeToByteArray(wLoginSigInfo), 2)
        output.write(protocolType.name, 3)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Override
    override fun readFrom(input: TarsInputStream) {
//        都设为不是必须 因为考虑后面添加字段 然后初始化读取老版本保存的信息里面没有新的字段会导致报错
        deviceInfo = ProtoBuf.decodeFromByteArray(input.read(ByteArray(0), 1, false))
        wLoginSigInfo = ProtoBuf.decodeFromByteArray(input.read(ByteArray(0), 2, false))
        protocolType = ProtocolInternal.ProtocolType.valueOf(input.readString(3, false))
    }

    @Serializable
    class DeviceInfo {
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
        var model: String = "M2002J9E"
        var osType: String = "android"
        var brand: String = "Xiaomi"
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
        var ksid: ByteArray = "14751d8e7d633d9b06a392c357c675e5".hex2ByteArray() //todo t108
        var randKey: ByteArray = BytesUtil.randomKey(16)
        var guid: ByteArray = MD5.toMD5Byte((imei.ifEmpty { androidId }) + macAddress)

        var tgtgKey: ByteArray = MD5.toMD5Byte(BytesUtil.byteMerger(MD5.toMD5Byte(macAddress), guid))

        var clientIp = byteArrayOf(192.toByte(), 168.toByte(), 1, 123)


//        "--begin--":    "该设备文件由账号作为seed自动生成，每个账号生成的文件相同。",
//        "product":      "MRS4S",
//        "device":       "HIM188MOE",
//        "board":        "MIRAI-YYDS",
//        "brand":        "OICQX",
//        "model":        "Konata 2020",
//        "wifi_ssid":    "TP-LINK-${uin.toString(16)}",
//        "bootloader":   "U-boot",
//        "android_id":   "OICQX.${hash.readUInt16BE()}${hash[2]}.${hash[3]}${String(uin)[0]}",
//        "boot_id":      "${uuid}",
//        "proc_version": "Linux version 4.19.71-${hash.readUInt16BE(4)} (konata@takayama.github.com)",
//        "mac_address":  "00:50:${hash[6].toString(16).toUpperCase()}:${hash[7].toString(16).toUpperCase()}:${hash[8].toString(16).toUpperCase()}:${hash[9].toString(16).toUpperCase()}",
//        "ip_address":   "10.0.${hash[10]}.${hash[11]}",
//        "imei":         "${_genIMEI(uin)}",
//        "incremental":  "${hash.readUInt32BE(12)}",
//        "--end--":      "修改后可能需要重新验证设备。"
//
//        override fun writeTo(output: TarsOutputStream) {
//            output.write(imei, 1)
//            output.write(androidId, 2)
//            output.write(imsi, 3)
//            output.write(machineName, 4)
//            output.write(osType, 5)
//            output.write(machineManufacturer, 6)
//            output.write(androidVersion, 7)
//            output.write(androidSdkVersion, 8)
//            output.write(wifiSsid, 9)
//            output.write(wifiBSsid, 10)
//            output.write(macAddress, 11)
//            output.write(netType, 12)
//            output.write(apn, 13)
//            output.write(apnName, 14)
//            output.write(ksid, 15)
//            output.write(randKey, 16)
//            output.write(guid, 17)
//            output.write(tgtgKey, 18)
//        }
//
//        override fun readFrom(input: TarsInputStream) {
//            imei = input.read(imei, 1, true)
//            androidId = input.read(androidId, 2, true)
//            imsi = input.read(imsi, 3, true)
//            machineName = input.read(machineName, 4, true)
//            osType = input.read(osType, 5, true)
//            machineManufacturer = input.read(machineManufacturer, 6, true)
//            androidVersion = input.read(androidVersion, 7, true)
//            androidSdkVersion = input.read(androidSdkVersion, 8, true)
//            wifiSsid = input.read(wifiSsid, 9, true)
//            wifiBSsid = input.read(wifiBSsid, 10, true)
//            macAddress = input.read(macAddress, 11, true)
//            netType = input.read(netType, 12, true)
//            apn = input.read(apn, 13, true)
//            apnName = input.read(apn, 14, true)
//            ksid = input.read(ksid, 15, true)
//            randKey = input.read(randKey, 16, true)
//            guid = input.read(guid, 17, true)
//            tgtgKey = input.read(tgtgKey, 18, true)
//        }

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
        private val managerMap = hashMapOf<Long, DataManager>()
        private fun checkAccount(uin: Long): Long {
            if ((uin >= 10000L) and (uin <= 4000000000L)) {
                return uin
            }
            throw RuntimeException("老实点 自己uin都能写错吗")
        }

        /**
         * 获取管理器
         *
         * @param uin Long
         * @return DataManger
         */
        @JvmStatic
        fun manager(uin: Long): DataManager {
            return managerMap.getOrElse(uin) { throw RuntimeException("错误：${uin}，请先调用${OreBot::class.java.simpleName}.setDataPath()完成初始化") }
        }

        @JvmStatic
        fun init(uin: Long, path: String): DataManager {
            return managerMap.getOrPut(uin) { DataManager(uin, path) }
        }

        @JvmStatic
        fun flush(uin: Long) {
            managerMap[uin]?.flush()
        }

        /**
         * 销毁释放
         *
         * @param uin Long
         */
        @JvmStatic
        fun destroy(uin: Long) {
            managerMap.remove(uin)?.destroy()
        }
    }
}

