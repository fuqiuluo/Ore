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

package moe.ore.core.protocol.wtlogin

import kotlinx.io.core.discardExact
import kotlinx.io.core.readBytes
import moe.ore.api.listener.OreListener
import moe.ore.core.net.BotClient
import moe.ore.core.net.packet.SingleHandler
import moe.ore.helper.toByteReadPacket
import moe.ore.helper.toHexString

/**
 * 登录器
 * @property uin Long
 * @property oreListener OreListener?
 * @constructor
 */
class LoginHelper(private val uin: Long, private val client: BotClient, private val oreListener: OreListener?) :
    Thread() {

    override fun run() {
        invoke()
    }

    private fun invoke() {
        handle(WtLoginV1(uin).sendTo(client))
    }

    private fun handle(seq: Int) {
        val handler = client.registerCommonHandler(SingleHandler(seq, WtLogin.LOGIN))
        if (handler.wait()) {
            handler.fromService!!.body.readLoginPacket {

            }


        } else {
            // 等待返回包超时

        }
    }

    companion object {
        @JvmStatic
        private inline fun ByteArray.readLoginPacket(block: () -> Unit) {
            val reader = this.toByteReadPacket()
            reader.discardExact(1 + 2 + 2 + 2 + 2)
            // 02 (dis) xx xx (dis) 1f 41 (dis) 08 01 (dis) 00 01 (dis)
            val uin = reader.readInt().toLong()
            reader.discardExact(2)
            // 00 00 (dis)
            val result = reader.readByte().toInt() and 0xff

            println("result[$uin] : $result")
        }
    }
}