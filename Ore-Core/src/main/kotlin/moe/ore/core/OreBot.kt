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
import moe.ore.api.listener.OreListener
import moe.ore.core.bot.BotAccount
import moe.ore.core.helper.DataManager

class OreBot(val uin: Long) : Ore() {
    /**
     * 机器人状态
     */
    private var status = OreStatus.NoLogin

    /**
     * 事件监听器
     */
    var oreListener: OreListener? = null

    override fun login() {
        // 登录开始传递登录开始事件
        oreListener?.onLoginStart()


    }

    override fun status() = status

    override fun shut() {
        // 关闭机器人
        this.status = OreStatus.Destroy
        DataManager.destroy(uin)


    }
}