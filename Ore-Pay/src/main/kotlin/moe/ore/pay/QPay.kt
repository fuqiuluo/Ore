package moe.ore.pay

import moe.ore.api.LoginResult
import moe.ore.api.Ore
import moe.ore.api.listener.CaptchaChannel
import moe.ore.api.listener.OreListener
import moe.ore.api.listener.SmsHelper
import moe.ore.core.OreManager
import moe.ore.core.helper.DataManager
import java.util.*

class QPay(ore: Ore) {
    private val dataManager = DataManager.manager(ore.uin)
    private val userStInfo = dataManager.userSigInfo
    private val session = dataManager.session

    /**
     * 获取钱包余额
     */
    fun getWalletBalance() {
        val skey = userStInfo.sKey
        val pskey = session.pSKeyMap


    }

}

fun Ore.getPay() : QPay {
    return QPay(this)
}

fun main() {
    // val ore = OreManager.addBot(203411690, "911586ABc", "C:\\")

    val ore = OreManager.addBot(3042628723, "911586abcd", "C:\\")

    ore.oreListener = object : OreListener {
        override fun onLoginStart() {
            println("登录开始了，呼呼呼！！！")
        }

        override fun onLoginFinish(result: LoginResult) {
            println("登录结果：$result")


            ore.getPay().getWalletBalance()

        }

        override fun onCaptcha(captchaChan: CaptchaChannel) {

            println(captchaChan.url)

            print("请输入Ticket：")
            val ticket = Scanner(System.`in`).nextLine()
            captchaChan.submitTicket(ticket)


        }

        override fun onSms(sms: SmsHelper) {
            println(sms)
            println(sms.sendSms())
            print("请输入SMSCode：")
            val code = Scanner(System.`in`).nextLine()
            sms.submitSms(code)
        }

    }
    ore.login()


}
