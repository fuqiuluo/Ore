package moe.ore.pay

import moe.ore.api.LoginResult
import moe.ore.api.Ore
import moe.ore.api.listener.CaptchaChannel
import moe.ore.api.listener.OreListener
import moe.ore.api.listener.SmsHelper
import moe.ore.core.OreBot
import moe.ore.core.OreManager
import moe.ore.core.protocol.wlogin.WloginHelper
import java.util.*

fun Ore.getPay(payWord: String) : IQPay {
    check(payWord.length == 6) { "支付密码不合法" }
    return QPay(this.uin, payWord)
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

            WloginHelper(ore.uin, (ore as OreBot).client).refreshSt()
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
