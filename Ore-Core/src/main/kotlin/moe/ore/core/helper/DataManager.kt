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

import moe.ore.core.OreBot
import moe.ore.core.bot.BotAccount
import moe.ore.core.bot.DeviceInfo
import moe.ore.core.bot.SsoSession
import moe.ore.core.bot.UserSigInfo
import moe.ore.core.protocol.PiratedEcdh
import moe.ore.core.protocol.ProtocolInternal
import moe.ore.core.util.QQUtil.checkAccount
import moe.ore.helper.runtimeError
import moe.ore.helper.thread.ThreadManager
import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsOutputStream
import moe.ore.tars.TarsStructBase
import moe.ore.util.FileUtil
import moe.ore.util.MD5
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream

class DataManager private constructor(
        uin: Long,
        /**
         * 数据保存路径
         */
        path: String,
        // 暂时移除
        // private val safePwd: String
) : TarsStructBase() {

    /**
     * 线程管理器
     */
    val threadManager = ThreadManager[uin]

    /**
     * 数据保存目录
     */
    @JvmField
    @Transient
    var dataPath: String = path + "/" + MD5.toMD5(uin.toString()) + ".ore"

    /**
     * 管理器
     */
    var session = SsoSession()

    lateinit var botAccount: BotAccount

    val ecdh: PiratedEcdh by lazy { PiratedEcdh() }

    /**
     * 保存各种Token
     */
    var userSigInfo: UserSigInfo = UserSigInfo()

    /**
     * 模拟的安卓信息
     */
    var deviceInfo = DeviceInfo()
    var protocolType: ProtocolInternal.ProtocolType = ProtocolInternal.ProtocolType.ANDROID_PHONE

    init {
        if (path.isBlank()) runtimeError("错误：${uin}，请先调用${OreBot::class.java.simpleName}.setDataPath()完成初始化")
        kotlin.runCatching {
            if (FileUtil.has(dataPath)) {
                readFrom(TarsInputStream(FileUtil.readFile(dataPath)))
            }
        }
    }

    /**
     * 销毁
     */
    fun destroy() {
        // threadManager 只有由dataManager来释放
        threadManager.shutdown()
        flush()
    }

    fun flush() {
        FileUtil.saveFile(dataPath, toByteArray())
    }

    @Override
    override fun writeTo(output: TarsOutputStream) {
        output.write(userSigInfo, 3)
        output.write(deviceInfo, 4)
        output.write(protocolType.name, 5)
    }

    @Override
    override fun readFrom(input: TarsInputStream) {
        userSigInfo = input.read(userSigInfo, 3, false)
        deviceInfo = input.read(deviceInfo, 4, false)
        protocolType = ProtocolInternal.ProtocolType.valueOf(input.read("", 5, false))
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
            return managerMap.getOrPut(checkAccount(uin)) { DataManager(uin, path) }
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

        /**
         * 转移
         */
        @JvmStatic
        fun copyTo(oldUin: Long, newUin : Long) : DataManager {
            val oldManager = manager(oldUin)
            val newManager = init(newUin, oldManager.dataPath)
            newManager.protocolType = oldManager.protocolType
            newManager.deviceInfo = oldManager.deviceInfo
            newManager.userSigInfo = oldManager.userSigInfo
            oldManager.destroy()
            return newManager
        }
    }
}

