import com.google.crypto.tink.subtle.Base64
import moe.ore.api.LoginResult
import moe.ore.api.OreStatus
import moe.ore.api.listener.CaptchaChannel
import moe.ore.api.listener.OreListener
import moe.ore.api.listener.SmsHelper
import moe.ore.core.OreBot
import moe.ore.core.OreManager
import moe.ore.core.helper.DataManager
import moe.ore.core.net.BotClient
import moe.ore.core.net.listener.ClientListener
import moe.ore.core.net.packet.PacketSender.Companion.sync
import moe.ore.core.protocol.wlogin.request.qr.QRLoginHelper
import moe.ore.core.protocol.wlogin.request.qr.QRLoginListener
import moe.ore.core.protocol.wlogin.request.qr.WtLoginGetQR
import moe.ore.group.TroopManagerWeb
import moe.ore.helper.toHexString
import java.io.File
import java.util.*

fun main(args: Array<String>) {
    val helper = QRLoginHelper("C:\\Users\\13723\\Desktop\\Ore",  object : OreListener {
        override fun onStatusChanged(status: OreStatus) {
            println("机器人状态改变为：$status")
        }

        override fun onLoginStart() {
            println("登录开始了，呼呼呼！！！")
        }

        override fun onLoginFinish(result: LoginResult) {
            println("登录结果：$result")
        }

        override fun onLoginAnother(platform: Long, tittle: String, info: String) {
            println("[notice]($platform) $tittle --> $info")
        }

        override fun onOffLine(tittle: String, tips: String) {
            println("[offline] $tittle --> $tips")
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
    })
    helper.listener = object : QRLoginListener {
        override fun onQRExpired() {
            println("二维码过期")
        }

        override fun onGetQRSuccess(imgBuf: ByteArray) {
            println("二维码获取成功")
            println("1 --> base64")
            println("2 --> hex")

            print("请选择输出格式[序号]：")

            val id = Scanner(System.`in`).nextLine()

            if(id == "1") {
                println("输出：" + Base64.encode(imgBuf))
            } else if(id == "2") {
                println("输出：" + imgBuf.toHexString())
            }

            println("即将进入轮询模式等待时间：5秒")

            Thread.sleep(5000)
        }

        override fun onGetQRFailure() {
            println("二维码获取失败")
        }

        override fun onFailConnect() {
            println("服务器链接失败")
        }

        override fun onScanCode() {
            println("有设备扫描了二维码。等待确认登录")
        }
    }
    helper.login()
}

fun main() {
    // val ore = OreManager.addBot(203411690, "911586ABc", "C:\Users\13723\Desktop\Ore")

    val ore = OreManager.addBot(3042628723, "911586abcd", File("C:\\Users\\13723\\Desktop\\Ore").absolutePath)
    val manager = DataManager.manager(ore.uin)

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

            println(TroopManagerWeb(ore.uin).getGroupList())
        }

        override fun onLoginAnother(platform: Long, tittle: String, info: String) {
            println("[notice]($platform) $tittle --> $info")
        }

        override fun onOffLine(tittle: String, tips: String) {
            println("[offline] $tittle --> $tips")
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


    // ore.login()
    // ore.tokenLogin()


}
