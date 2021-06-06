package moe.ore.core

import moe.ore.core.bot.BotRecorder
import moe.ore.core.bot.WLoginSigInfo

class DataManager(val uin: ULong) {
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
        // 清空所有map


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
        fun manager(uin : ULong) : DataManager {
            if(!managerMap.containsKey(uin)) {
                managerMap[uin] = DataManager(uin)
            }
            return managerMap[uin]!!
        }

        /**
         * 销毁释放
         *
         * @param uin ULong
         */
        @JvmStatic
        fun destroy(uin: ULong) {
            if(managerMap.containsKey(uin)) {
                managerMap.remove(uin)?.destroy()
            }
        }
    }
}