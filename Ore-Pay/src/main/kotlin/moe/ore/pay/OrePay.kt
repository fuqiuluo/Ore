package moe.ore.pay

import moe.ore.api.LoginResult
import moe.ore.api.Ore
import moe.ore.api.listener.CaptchaChannel
import moe.ore.api.listener.OreListener
import moe.ore.api.listener.SmsHelper
import moe.ore.core.OreManager
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

            try {
                val pay = ore.getPay("170086")
                println(pay.sendTrVoiceHb(1016398585, "你是我的啊", 1, 1))
                println(pay.sendTrVoiceHb(1016398585, "你是我的啊", 1, 1))
                println(pay.sendTrVoiceHb(1016398585, "你是我的啊", 1, 1))
                println(pay.sendTrVoiceHb(1016398585, "你是我的啊", 1, 1))
                // QPayUtil.tryToDecrypt("4E02884BAC013FC81CD2D1560A17A68D472A28D87F23AAF5C32F489331809930678E01A7824F1ECDAE7AF9FE848F9A51699F6E88A8FAB9EE8D5E024662F6264740FADE3EF02127D260FF3BCE3043BB836772FFB61A23D6EB37BE881CE6BEAA92111CD42FDA54F9F94C3F94F086A876A223DE5EA2D150F6DAB39CBB994219310DA63D05E4CAA5F393B8175B05AE3945D5168656C065C243EBC76578906A6AF5D8AB699C27CDA5EFFE559FA6FFF33F3AA60320892A0B1B144A63201A44852EEFA0A5FB66A9459C1F5A930091F5DCB67E1DFDB4AE1C297042DE12D7EA874C4BAA5511354A8325951E734982F407CA4EED7F71A3DD77E82A19522673081A864A535B90F0192173211B4E6C999A4763403C335A8D5BDC80C00FD097AED34359223B4D873ED3313A700C6F39CE238A2F3AB6FA19FA84F3F2B730D6BC2B0F93F3DB950BC85C1BA083567BCC25523331A3C54E6576F010FC9DE0CB554B24036D1524C9FD448BE68914898A483AC7596613D4C4F3F748DD1B6A7ED5632A5F3E95DDAA75D4CC4EA11FB4C0AABF7991BE0E7F1ABBDF461C7108612B5D1D3D82D861690B43FD493A82B0E19650D3B22FD7CF6DD66B505E1A239A2ACAD6921056C7A39FB574F108C017C2DF1C155561314D98FB22D8EBE64C6EAF51E7A279D9954D2E30D7197C6B6FD641C7C0A0C2D0823CD4B92744FAF93E1CD3B0D77976E089F41193BBE5DD4E039F3A9B01F1D2DA8E2D927593650827F1FF019CA1D236A2B1165168B2C93B75FF7FC5D3E9DD7D9EFEB136B77FFBBB32B634E6B3643D0A45C2A33F0DC3E357")
            } catch (e : Exception) {
                e.printStackTrace()
            }
            //WloginHelper(ore.uin, (ore as OreBot).client).refreshSt()
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
