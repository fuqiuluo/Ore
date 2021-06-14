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

import kotlinx.io.core.ByteReadPacket
import kotlinx.io.core.discardExact
import kotlinx.io.core.readBytes
import moe.ore.api.LoginResult
import moe.ore.api.listener.OreListener
import moe.ore.core.helper.DataManager
import moe.ore.core.net.BotClient
import moe.ore.core.net.packet.SingleHandler
import moe.ore.core.protocol.ECDH_SHARE_KEY
import moe.ore.helper.toByteReadPacket
import moe.ore.util.TeaUtil

/**
 * 登录器
 * @property uin Long
 * @property listener OreListener?
 * @constructor
 */
class LoginHelper(private val uin: Long, private val client: BotClient, private val listener: OreListener?) :
    Thread() {

    private val manager = DataManager.manager(uin)
    private val userStInfo = manager.wLoginSigInfo
    private val device = manager.deviceInfo

    override fun run() {
        invoke()
    }

    private fun invoke() {
        handle(WtLoginV1(uin).sendTo(client))
    }

    private fun handle(seq: Int) {
        val handler = client.registerCommonHandler(SingleHandler(seq, WtLogin.LOGIN))
        if (handler.wait()) {
            handler.fromService!!.body.readLoginPacket(device.randKey) { result, tlvMap ->
                when (result) {
                    1 -> listener?.onLoginFinish(LoginResult.PasswordWrong)
                    2 -> TODO("需要滑块啊！傻卵！！")
                    204 -> TODO("伟大的设备锁验证")
                    else -> error("unknown login result : $result")
                }
            }
        } else {
            // 等待返回包超时
            listener?.onLoginFinish(LoginResult.ServerTimeout)
        }
    }

    companion object {
        @JvmStatic
        private inline fun ByteArray.readLoginPacket(randKey: ByteArray, block: (Int, Map<Int, ByteArray>) -> Unit) {
            val reader = this.toByteReadPacket()
            reader.discardExact(1 + 2 + 2 + 2 + 2)
            // 02 (dis) xx xx (dis) 1f 41 (dis) 08 01 (dis) 00 01 (dis)
            val uin = reader.readInt().toLong()
            reader.discardExact(2)
            // 00 00 (dis)
            val result = reader.readByte().toInt() and 0xff
            // 235 协议版本过低
            val teaKey = if (result == 180) randKey else ECDH_SHARE_KEY
            val tlvBody = TeaUtil.decrypt(reader.readBytes(reader.remaining.toInt() - 1), teaKey).toByteReadPacket()
                .also { it.discardExact(3) }
            block.invoke(result, parseTlv(tlvBody))
        }

        @JvmStatic
        private fun parseTlv(bs: ByteReadPacket): Map<Int, ByteArray> {
            val size = bs.readShort().toInt()
            val map = hashMapOf<Int, ByteArray>()
            repeat(size) {
                val ver = bs.readShort().toInt()
                val tSize = bs.readShort().toInt()
                val content = bs.readBytes(tSize)
                map[ver] = content
            }
            return map
        }
    }
}