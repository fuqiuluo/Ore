import moe.ore.api.LoginResult
import moe.ore.api.OreStatus
import moe.ore.api.listener.CaptchaChannel
import moe.ore.api.listener.OreListener
import moe.ore.api.listener.SmsHelper
import moe.ore.core.OreBot
import moe.ore.core.OreManager
import moe.ore.core.helper.DataManager
import moe.ore.core.helper.sendPacket
import moe.ore.core.net.packet.Handler
import moe.ore.core.net.packet.PacketType
import moe.ore.core.net.packet.SingleHandler
import moe.ore.core.protocol.ProtocolInternal
import moe.ore.core.protocol.tars.service.GrayUin
import moe.ore.group.TroopManager
import moe.ore.tars.UniPacket
import java.io.File
import java.util.*

fun main() {
    // val ore = OreManager.addBot(203411690, "911586ABc", "C:\Users\13723\Desktop\Ore")

    val ore = OreManager.addBot(3042628723, "911586abcd", File("").absolutePath)

    DataManager.manager(3042628723).protocolType = ProtocolInternal.ProtocolType.IOS_IPAD
//    ore.sendPacket("cmd", ByteArray(0), PacketType.ServicePacket, firstToken = ByteArray(0), secondToken = ByteArray(0))
//    val oreBot1 = ore as OreBot
//    val singleHandler = SingleHandler(1, "")
//    oreBot1.client.registerCommonHandler(singleHandler)
//    if (singleHandler.wait(1000 * 3)) {
//        val decode = UniPacket.decode(singleHandler.fromService?.body)
//    }
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
