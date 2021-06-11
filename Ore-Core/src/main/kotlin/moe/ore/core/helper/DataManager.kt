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
import moe.ore.core.bot.WtLoginSigInfo
import moe.ore.core.protocol.ProtocolInternal
import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsOutputStream
import moe.ore.tars.TarsStructBase
import moe.ore.util.BytesUtil
import moe.ore.util.FileUtil
import moe.ore.util.MD5
import moe.ore.helper.bytes.hex2ByteArray
import moe.ore.helper.bytes.xor
import java.io.File
import java.util.*

@ExperimentalSerializationApi
class DataManager private constructor(uin: Long, path: String, private val safePwd: String) : TarsStructBase() {

    /**
     * 数据保存目录
     */
    @JvmField
    @Transient
    var dataPath: String = fun(): String {
        return File(path).absolutePath + File.pathSeparator + MD5.toMD5(uin.toString()) + ".ore"
    }()

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
    val wLoginSigInfo: WtLoginSigInfo = WtLoginSigInfo(uin)

    /**
     * 模拟的安卓信息
     */
    var deviceInfo = DeviceInfo()
    var protocolType: ProtocolInternal.ProtocolType = ProtocolInternal.ProtocolType.ANDROID_PHONE

    init {
        if (path.isBlank()) error("错误：${uin}，请先调用${OreBot::class.java.simpleName}.setDataPath()完成初始化")
        if (FileUtil.has(dataPath)) {
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

    @Override
    override fun writeTo(output: TarsOutputStream) {
        output.write(xor(ProtoBuf.encodeToByteArray(deviceInfo)), 1)
        //  output.write(xor(ProtoBuf.encodeToByteArray(wLoginSigInfo)), 2)
        output.write(protocolType.name, 3)
    }

    private fun xor(value: ByteArray) = if (safePwd.isBlank()) value else value.xor(safePwd.toByteArray())

    @Override
    override fun readFrom(input: TarsInputStream) {
        // 都设为不是必须 因为考虑后面添加字段 然后初始化读取老版本保存的信息里面没有新的字段会导致报错
        deviceInfo = ProtoBuf.decodeFromByteArray(xor(input.read(byteArrayOf(), 1, false)))
        // wLoginSigInfo = ProtoBuf.decodeFromByteArray(xor(input.read(byteArrayOf(), 2, false)))
        protocolType = ProtocolInternal.ProtocolType.valueOf(input.readString(3, false))
    }

    @Serializable
    class DeviceInfo {
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
        var netType: NetworkType = NetworkType.WIFI

        var apn = if (netType == NetworkType.WIFI) {
            "wifi"
        } else {
            "cmnet"
        }

        var apnName = "中国移动"
        var ksid: ByteArray = "14751d8e7d633d9b06a392c357c675e5".hex2ByteArray() //todo t108
        var randKey: ByteArray = BytesUtil.randomKey(16)
        var guid: ByteArray = MD5.toMD5Byte((imei.ifEmpty { androidId }) + macAddress)

        var tgtgKey: ByteArray = MD5.toMD5Byte(BytesUtil.byteMerger(MD5.toMD5Byte(macAddress), guid))

        var clientIp = byteArrayOf(0, 0, 0, 0)

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
        @JvmStatic
        private val managerMap = hashMapOf<Long, DataManager>()

        @JvmStatic
        private fun checkAccount(uin: Long): Long {
            if ((uin >= 10000L) and (uin <= 4000000000L)) {
                return uin
            } else error("QQ号格式错误")
        }

        /**
         * 获取管理器
         *
         * @param uin Long
         * @return DataManger
         */
        @JvmStatic
        fun manager(uin: Long): DataManager {
            return managerMap.getOrElse(checkAccount(uin)) { error("错误：${uin}，请先调用${OreBot::class.java.simpleName}.setDataPath()完成初始化") }
        }

        @JvmStatic
        fun init(uin: Long, path: String): DataManager {
            return managerMap.getOrPut(checkAccount(uin)) { DataManager(uin, path, "") }
        }

        @JvmStatic
        fun init(uin: Long, path: String, safePwd: String): DataManager {
            return managerMap.getOrPut(checkAccount(uin)) { DataManager(uin, path, safePwd) }
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

