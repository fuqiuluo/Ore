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

import moe.ore.api.Ore
import moe.ore.api.OreStatus
import moe.ore.core.helper.DataManager
import moe.ore.core.helper.sendPacket
import moe.ore.core.net.BotClient
import moe.ore.core.net.listener.ClientListener
import moe.ore.core.net.packet.FromService
import moe.ore.core.net.packet.LongHandler
import moe.ore.core.protocol.tars.service.RequestMSFForceOffline
import moe.ore.core.protocol.tars.service.RequestPushForceOffline
import moe.ore.core.protocol.tars.statsvc.SvcReqMSFLoginNotify
import moe.ore.core.protocol.wlogin.WloginHelper
import moe.ore.core.servlet.PushServlet
import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.helper.runtimeError
import moe.ore.tars.UniPacket

class OreBot(uin: Long) : Ore(uin) {
    private val manager = DataManager.manager(uin)

    private val threadManager = manager.threadManager

    val client: BotClient = BotClient(uin).apply {
        this.listener = object : ClientListener {
            override fun onConnect() {
                when (this@OreBot.status()) {
                    OreStatus.NoLogin -> {
                        // 登录
                        WloginHelper(uin, this@apply, oreListener).loginByPassword()
                    }

                    /**
                     * 重连或者在线状态下被触发操作
                     */
                    OreStatus.Reconnecting, OreStatus.Online -> {
                        // 重连
                        println("ore reconnect begin")
                        WloginHelper(uin, this@apply, oreListener).loginByToken()
                    }
                    OreStatus.QRLogin -> {
                        WloginHelper(uin, this@apply, oreListener).loginByQrToken()
                    }
                    else -> runtimeError("未知的错误操作")
                }
            }

            override fun onFailConnect() {
                /**
                 * 服务器连接失败 则继续尝试重连
                 * 重连线程受线程池管理 可通过shutBot结束重连
                 */
                println("server fail to connect")
                this@apply.connect()
                Thread.sleep(5000)
            }
        }
    }

    init {
        client.registerSpecialHandler(object : LongHandler("MessageSvc.PushForceOffline") {
            override fun handle(from: FromService) {
                val forceOffline = UniPacket.decode(from.body).findByClass("req_PushForceOffline", RequestPushForceOffline())
                changeStatus(OreStatus.OffLine) // change status
                oreListener?.onKicked(forceOffline.sameDevice, forceOffline.title, forceOffline.tips)
            }
        }) // kicked from code[2013]
        client.registerSpecialHandler(object : LongHandler("StatSvc.ReqMSFOffline") {
            override fun handle(from: FromService) {
                val forceOffline = UniPacket.decode(from.body).findByClass("RequestMSFForceOffline", RequestMSFForceOffline())
                changeStatus(OreStatus.OffLine) // change status
                manager.clearToken()
                oreListener?.onKickedAndClearToken(forceOffline.sameDevice, forceOffline.title, forceOffline.info)
            }
        }) // 强迫下线 所有的sig/cookie报废
        client.registerSpecialHandler(object : LongHandler("StatSvc.SvcReqMSFLoginNotify") {
            override fun handle(from: FromService) {
                val notify = UniPacket.decode(from.body).findByClass("SvcReqMSFLoginNotify", SvcReqMSFLoginNotify())
                oreListener?.onLoginAnother(notify.platform, notify.title, notify.info)
            }
        }) // 这里的提示 腾讯一个提示会发3次 请注意！！！


        client.registerSpecialHandler(object : LongHandler("OnlinePush.SidTicketExpired") {
            override fun handle(from: FromService) {
                sendPacket(from.commandName, EMPTY_BYTE_ARRAY, seq = from.seq).call()
            }
        }) // return 0, ignore

        PushServlet(uin).init(client)
    }

    override fun login() {
        // 登录开始传递登录开始事件
        threadManager.addTask {
            oreListener?.onLoginStart()
        }
        // 连接到服务器会自动发送登录包
        client.connect()
    }

    override fun tokenLogin() {
        // 登录开始传递登录开始事件
        threadManager.addTask {
            oreListener?.onLoginStart()
        }
        this.status = OreStatus.Reconnecting
        // 连接到服务器会自动发送登录包
        client.connect()
    }

    @Deprecated("二维码登录的入口不在这里哦！")
    override fun qrLogin() {
        // 登录开始传递登录开始事件
        threadManager.addTask {
            oreListener?.onLoginStart()
        }
        this.status = OreStatus.QRLogin
        // 连接到服务器会自动发送登录包
        client.connect()
    }

    override fun checkTicketAndRefresh() {
        kotlin.runCatching {
            if (manager.userSigInfo.d2Key.isExpired()) {
                WloginHelper(uin, client).refreshSt()
            }
            // 检查是否过期 并刷新
            if (manager.session.sKey.isExpired()) {
                WloginHelper(uin, client).refreshSig()
            }
        }
    }

    override fun shut() {
        // 关闭机器人
        println("shut ore bot")
        this.status = OreStatus.Destroy
        DataManager.destroy(uin)
        this.client.close()
    }
}

