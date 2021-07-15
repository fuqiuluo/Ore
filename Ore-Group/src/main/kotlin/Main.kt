import moe.ore.api.LoginResult
import moe.ore.api.OreStatus
import moe.ore.api.listener.CaptchaChannel
import moe.ore.api.listener.OreListener
import moe.ore.api.listener.SmsHelper
import moe.ore.core.OreBot
import moe.ore.core.OreManager
import moe.ore.core.helper.DataManager
import moe.ore.core.protocol.ProtocolInternal
import moe.ore.group.TroopManager
import java.io.File
import java.util.*

fun main() {
    // val ore = OreManager.addBot(203411690, "911586ABc", "C:\Users\13723\Desktop\Ore")

    val ore = OreManager.addBot(3042628723, "911586abcd", File("C:\\Users\\13723\\Desktop\\Ore").absolutePath)
    val manager = DataManager.manager(ore.uin)
    manager.protocolType = ProtocolInternal.ProtocolType.IOS_IPAD

    ore.oreListener = object : OreListener {
        override fun onStatusChanged(status: OreStatus) {
            println("机器人状态改变为：$status")
        }

        override fun onLoginStart() {
            println("登录开始了，呼呼呼！！！")
        }

        override fun onLoginFinish(result: LoginResult) {
            println("登录结果：$result")

            val oreBot = (ore as OreBot)
            if (result == LoginResult.Success) {
//                保存数据
                DataManager.flush(ore.uin)
            }

            println(TroopManager(ore.uin).getGroupList())
        }

        override fun onLoginAnother(platform: Long, tittle: String, info: String) {
            println("[notice]($platform) $tittle --> $info")
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
    // ore.tokenLogin()


}
