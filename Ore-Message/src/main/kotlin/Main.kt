import moe.ore.api.LoginResult
import moe.ore.api.OreStatus
import moe.ore.api.listener.CaptchaChannel
import moe.ore.api.listener.OreListener
import moe.ore.api.listener.SmsHelper
import moe.ore.core.OreManager
import moe.ore.msg.event.TroopMsgEvent
import moe.ore.msg.messageCenter
import java.io.File
import java.util.*

fun main() {
    val ore = OreManager.addBot(3042628723, "911586ABCD", File("C:\\Users\\13723\\Desktop\\Ore").absolutePath)

    ore.oreListener = object : OreListener {
        override fun onStatusChanged(status: OreStatus) {
            println("机器人状态改变为：$status")
        }

        override fun onLoginStart() {
            println("登录开始了，呼呼呼！！！")
        }

        override fun onLoginFinish(result: LoginResult) {
            println("登录结果：$result")

            val msgCenter = ore.messageCenter()
            msgCenter.setTroopMsgEvent(object : TroopMsgEvent() {
                override fun onTroopMsg(
                    fromTroop: Long,
                    fromUin: Long,
                    troopName: String,
                    uinName: String,
                    msgTime: Long,
                    msgId: Int,
                    msg: String
                ) {
                    println(msg)
                }
            })

        }

        override fun onLoginAnother(platform: Long, tittle: String, info: String) {
            println("[notice]($platform) $tittle --> $info")
        }

        override fun onKicked(sameDevice: Byte, tittle: String, tips: String) {
            println("[kicked]($sameDevice) $tittle --> $tips")
        }

        override fun onKickedAndClearToken(sameDevice: Byte, tittle: String, tips: String) {
            println("[offline]($sameDevice) $tittle --> $tips")
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


