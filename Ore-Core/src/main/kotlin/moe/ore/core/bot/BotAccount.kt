package moe.ore.core.bot

import moe.ore.util.MD5

data class BotAccount(
    val uin : ULong,
    val password : String
) {
    /**
     * 获取密码的MD5
     * @return String
     */
    fun md5Password() = MD5.toMD5(password.toByteArray())!!


}
