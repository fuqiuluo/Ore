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

package moe.ore.api

enum class OreStatus {
    /**
     * 最初始的状态
     */
    NoLogin,

    /**
     * 该机器人实例已销毁
     */
    Destroy,

    /**
     * 在线状态
     */
    Online,

    /**
     * 二维码登录
     */
    QRLogin,

    /**
     * 重新连接服务器中
     */
    Reconnecting,

    /**
     * 重连失败
     *
     * 接下来不会再尝试重连
     *
     * 请做好判断
     */
    ReconnectFail,

    /**
     * 重连成功
     */
    ReconnectSuccess,

    /**
     * 在别的地方登录
     *
     * 掉线了，已下线，需要手动重新连接
     */
    OffLine
}