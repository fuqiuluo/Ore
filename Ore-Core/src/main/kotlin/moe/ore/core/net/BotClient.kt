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

package moe.ore.core.net

import moe.ore.core.helper.readMsfSsoPacket
import moe.ore.core.net.decoder.PacketResponse
import moe.ore.core.net.listener.ClientListener
import moe.ore.core.net.listener.UsefulListener
import moe.ore.core.net.packet.Handler
import moe.ore.core.net.packet.SingleHandler
import java.util.concurrent.ConcurrentHashMap

/**
 * @author 飞翔的企鹅
 * create 2021-05-30 13:18
 * uin 为BotClient唯一身份标识 代表是哪个号的bot实例
 */
class BotClient(val uin: Long) {
    /**
     * 普通接包器
     *
     * 只接一次包，例如返回包这种
     */
    private val commonHandler = ConcurrentHashMap<Int, Handler>()

    /**
     * 特殊接包器
     *
     * 例如监听群消息的包，持续监听
     */
    private val specialHandler = ConcurrentHashMap<String, Handler>()

    var listener: ClientListener? = null

    private val connection: BotConnection = BotConnection(object : UsefulListener() {
        override fun onConnect() {
            listener?.onConnect()
        }

        override fun onFailConnect() {
            listener?.onFailConnect()
        }

        override fun onMassage(msg: PacketResponse) {
            msg.body.readMsfSsoPacket(uin) { uinStr, from ->
                check(uin.toString() == uinStr) { "QQ号和ClientQQ号不一致，请检查发包" }
                val hash = from.hashCode()
                println("A = $from")
                if (commonHandler.containsKey(hash)) {
                    commonHandler[hash]!!.let {
                        if (it.check(from)) {
                            // println("注销")
                            unRegisterCommonHandler(it.hashCode())
                        }
                    }
                } else if (specialHandler.containsKey(from.commandName)) {
                    specialHandler[from.commandName]!!.check(from)
                }
            }
        }
    }, uin)

    fun registerCommonHandler(handler: Handler): SingleHandler {
        commonHandler[handler.hashCode()] = handler
        return handler as SingleHandler
    }

    fun registerSpecialHandler(handler: Handler): Handler {
        specialHandler[handler.commandName] = handler
        return handler
    }

    fun unRegisterCommonHandler(hash: Int): SingleHandler? {
        return commonHandler.remove(hash) as SingleHandler?
    }

    fun unRegisterSpecialHandler(name: String): Handler? {
        return specialHandler.remove(name)
    }

    fun send(requestBody: ByteArray) {
        // println("发送包")
        connection.send(requestBody)
    }

    fun connect(host : String, port : Int): BotClient {
        connection.connect(host, port)
        return this
    }

    fun connect() {
        connection.connect()
    }

    fun close() {
        connection.close()
    }
}