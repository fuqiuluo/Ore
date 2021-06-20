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

import kotlinx.serialization.Serializable
import moe.ore.core.OreBot
import moe.ore.core.bot.BotAccount
//import moe.ore.core.bot.BotRecorder
import moe.ore.core.bot.SsoSession
import moe.ore.core.bot.WtLoginSigInfo
import moe.ore.core.protocol.ProtocolInternal
import moe.ore.core.util.QQUtil.checkAccount
import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsOutputStream
import moe.ore.tars.TarsStructBase
import moe.ore.util.BytesUtil
import moe.ore.util.FileUtil
import moe.ore.util.MD5
import moe.ore.helper.hex2ByteArray
import moe.ore.helper.runtimeError
import moe.ore.helper.xor
import java.io.File
import java.util.*

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
    val session = SsoSession()

    lateinit var botAccount: BotAccount

    /**
     * 保存各种Token
     */
    val wLoginSigInfo: WtLoginSigInfo = WtLoginSigInfo()

    /**
     * 模拟的安卓信息
     */
    var deviceInfo = DeviceInfo()
    var protocolType: ProtocolInternal.ProtocolType = ProtocolInternal.ProtocolType.ANDROID_PHONE

    init {
        if (path.isBlank()) runtimeError("错误：${uin}，请先调用${OreBot::class.java.simpleName}.setDataPath()完成初始化")
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

    }

    private fun xor(value: ByteArray) = if (safePwd.isBlank()) value else value.xor(safePwd.toByteArray())

    @Override
    override fun readFrom(input: TarsInputStream) {

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

        var imei: String = " 867109044454073"
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
        var ksid: ByteArray = "31008c9064e89b48f20765fd739edd1e".hex2ByteArray()

        // %4;7t>;28<fc.5*6
        var guid: ByteArray = MD5.toMD5Byte(androidId + macAddress)

        // 实际上在逆向8.7.5时 并没有出现md5(macAddr) 而是随机了一个16字节的东西
        var tgtgt: ByteArray = MD5.toMD5Byte(BytesUtil.byteMerger(MD5.toMD5Byte(macAddress), guid))

        // expamel 1, 0, 0, 127 是倒过来的哦！
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

        /**
         * 获取管理器
         *
         * @param uin Long
         * @return DataManger
         */
        @JvmStatic
        fun manager(uin: Long): DataManager {
            return managerMap.getOrElse(checkAccount(uin)) { runtimeError("错误：${uin}，请先调用${OreBot::class.java.simpleName}.setDataPath()完成初始化") }
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

