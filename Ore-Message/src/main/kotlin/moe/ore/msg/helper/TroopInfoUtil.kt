package moe.ore.msg.helper

object TroopInfoUtil {

    /**
     * 检查群里面哪些功能是否开启 通过flag
     */

    fun isTroopWriteTogether(flagex3: Int) = flagex3 and 1073741824 == 0 // 群一起写

    fun isTroopLuckyChar(flagex4: Int) = (flagex4 and 32768) == 0 // 群幸运字

    fun isQidianPrivateTroop(flagex3: Int) = flagex3.toLong() and 32 != 0L // 未知的玩意

    fun isVisitorSpeakEnabled(flagex: Int) = flagex and 8192 == 8192

    fun isTroopIlive(flagex4: Int): Boolean {
        val z = flagex4 and 1024 != 0
        // QLog.i("IliveGroup", 1, String.format("isTroopIlive %s", z))
        return z
    }
}