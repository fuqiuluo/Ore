/*
 * English :
 *  The project is protected by the MPL open source agreement.
 * Open source agreement warning that prohibits deletion of project source code files.
 * The project is prohibited from acting in illegal areas.
 * All illegal activities arising from the use of this project are the responsibility of the second author, and the original author of the project is not responsible
 *
 *  中文：
 *  该项目由MPL开源协议保护。
 *  禁止删除项目源代码文件的开源协议警告内容。
 * 禁止使用该项目在非法领域行事。
 * 使用该项目产生的违法行为，由使用者或第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 *  オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 *
 */

package moe.ore.core

import moe.ore.api.LoginResult
import moe.ore.api.Ore
import moe.ore.api.OreStatus
import moe.ore.api.listener.CaptchaChannel
import moe.ore.api.listener.OreListener
import moe.ore.api.listener.SmsHelper
import moe.ore.core.helper.DataManager
import moe.ore.core.net.BotClient
import moe.ore.core.net.listener.ClientListener
import moe.ore.core.protocol.wlogin.WloginHelper
import moe.ore.core.util.QQUtil
import moe.ore.helper.runtimeError
import moe.ore.helper.thread.ThreadManager
import java.util.*

class OreBot(val uin: Long) : Ore() {
    private val manager = DataManager.manager(uin)

    private val threadManager: ThreadManager = manager.threadManager

    val client: BotClient = BotClient(uin).apply {
        this.listener = object : ClientListener {
            override fun onConnect() {
                when (this@OreBot.status()) {
                    OreStatus.NoLogin -> {
                        // 登录
                        WloginHelper(uin, this@apply, oreListener).loginByPassword()
                    }
                    OreStatus.Online -> {
                        // 重连
                        WloginHelper(uin, this@apply).loginByToken()
                    }
                    else -> runtimeError("未知的错误操作")
                }
            }
        }
    }

    private var status = OreStatus.NoLogin

    override fun login() {
        // 登录开始传递登录开始事件
        threadManager.addTask {
            oreListener?.onLoginStart()
        }
        // 连接到服务器会自动发送登录包
        client.connect()
    }

    override fun status() = status

    override fun shut() {
        // 关闭机器人
        this.status = OreStatus.Destroy
        DataManager.destroy(uin)

    }
}

fun main() {
    // 3042628723 911586abcd
    val ore = OreManager.addBot(203411690, "911586ABc", "C:\\")

    ore.oreListener = object : OreListener {
        override fun onLoginStart() {
            println("登录开始了，呼呼呼！！！")
        }

        override fun onLoginFinish(result: LoginResult) {
            println("登录结果：$result")

        }

        override fun onCaptcha(captchaChan: CaptchaChannel) {

            println(captchaChan.url)

            print("请输入Ticket：")
            val ticket = Scanner(System.`in`).nextLine()
            captchaChan.submitTicket(ticket)


        }

        override fun onSms(sms: SmsHelper) {
            println(sms)
        }


    }
    ore.login()

}
