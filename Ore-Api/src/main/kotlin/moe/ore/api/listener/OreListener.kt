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

package moe.ore.api.listener

import moe.ore.api.LoginResult
import moe.ore.api.OreStatus

/**
 * 机器人事件监听器
 */
interface OreListener {
    /**
     * 机器人状态码改变
     */
    fun onStatusChanged(status: OreStatus)

    /**
     * 开始登录
     */
    fun onLoginStart()

    /**
     * 登录结束
     */
    fun onLoginFinish(result: LoginResult)

    /**
     * 在别的设备登录
     */
    fun onLoginAnother(platform : Long, tittle : String, info : String)

    /**
     * 被踢下线
     */
    fun onKicked(sameDevice : Byte, tittle: String, tips : String)

    /**
     * 被踢下线 且 所有token报废
     */
    fun onKickedAndClearToken(sameDevice : Byte, tittle: String, tips : String)

    /**
     * 遇到滑块 返回ticket
     */
    fun onCaptcha(captchaChan : CaptchaChannel)

    /**
     * 遇到短信验证
     */
    fun onSms(sms : SmsHelper)
}

abstract class CaptchaChannel(val url : String) {
    /**
     * 为了兼容java 被迫使用
     */
    abstract fun submitTicket(ticket : String)
}

abstract class SmsHelper {
    abstract fun noticeStr() : String

    abstract fun phoneNum() : String

    abstract fun otherWayUrl() : String

    abstract fun sendSms() : moe.ore.api.data.Result<Boolean>

    abstract fun submitSms(code : String)

    override fun toString(): String {
        return "notice = %s,phone = %s, other = %s".format(noticeStr(), phoneNum(), otherWayUrl())
    }
}