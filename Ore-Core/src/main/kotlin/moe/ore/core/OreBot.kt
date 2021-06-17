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
import moe.ore.api.listener.OreListener
import moe.ore.core.helper.DataManager
import moe.ore.core.net.BotClient
import moe.ore.core.net.listener.ClientListener
import moe.ore.core.protocol.wtlogin.LoginHelper
import moe.ore.helper.runtimeError
import moe.ore.helper.thread.ThreadManager
import java.util.*

class OreBot(val uin: Long) : Ore() {
    /**
     * 机器人产生的线程全放这里
     */
    val threadManager: ThreadManager = ThreadManager.getInstance(uin)

    val client: BotClient = BotClient(uin).apply {
        this.listener = object : ClientListener {
            override fun onConnect() {
                when (this@OreBot.status()) {
                    OreStatus.NoLogin -> {
                        // 登录
                        threadManager.addTask(LoginHelper(uin, this@apply, oreListener))
                    }
                    OreStatus.Online -> {
                        // 重连


                    }
                    else -> runtimeError("未知的错误操作")
                }
            }
        }
    }

    /**
     * 机器人状态
     */
    private var status = OreStatus.NoLogin

    override fun login() {
        // 登录开始传递登录开始事件
        oreListener?.onLoginStart()
        client.connect()
    }

    override fun status() = status

    override fun shut() {
        // 关闭机器人
        this.status = OreStatus.Destroy
        threadManager.shutdown()
        DataManager.destroy(uin)

    }
}

fun main() {
    // 3042628723
    val ore = OreManager.addBot(3042623, "911586abcd", "C:\\")
    ore.oreListener = object : OreListener {
        override fun onLoginStart() {
            println("登录开始了，呼呼呼！！！")
        }

        override fun onLoginFinish(result: LoginResult) {
            println("登录结果：$result")
        }

        override fun onCaptcha(url: String): String? {
            println("伟大的滑块开始了：$url")
            return null
        }

    }
    ore.login()


}