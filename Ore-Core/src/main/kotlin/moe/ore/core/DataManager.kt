package moe.ore.core

import moe.ore.core.bot.BotRecorder
import moe.ore.core.bot.WLoginSigInfo

class DataManager private constructor(uin: ULong) {
    /**
     * 管理器
     */
    val recorder = BotRecorder()

    /**
     * 保存各种Token
     */
    val sigInfo = WLoginSigInfo()

    /**
     * 销毁
     */
    fun destroy() {
        println("destroy")
        // TODO: 2021/6/6 销毁之前序列化到本地文件
        // 清空自身类里面的map或存在引用关系的事务

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
            return managerMap.getOrPut(uin) { DataManager(uin) }
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
}