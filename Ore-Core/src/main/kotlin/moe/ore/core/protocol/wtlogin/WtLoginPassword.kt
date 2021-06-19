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

import moe.ore.helper.createBuilder
import moe.ore.helper.toByteArray
import moe.ore.helper.writeBytes

/**
 * password
 * GetStWithPassword
 */
class WtLoginPassword(uin: Long) : WtLogin(uin, LOGIN, 0x810, 0x87) {
    override fun build(seq: Int): ByteArray {
        return createBuilder().apply {
            writeShort(9)
            writeShort(27)
            writeBytes(tlv.t18())
            writeBytes(tlv.t1())
            writeBytes(tlv.t106())
            writeBytes(tlv.t116())
            writeBytes(tlv.t100())
            writeBytes(tlv.t107())
            writeBytes(tlv.t108())
            writeBytes(tlv.t142())
            writeBytes(tlv.t144())
            writeBytes(tlv.t145())
            writeBytes(tlv.t147())
            writeBytes(tlv.t154(seq))
            writeBytes(tlv.t16b())
            // 非常规协议操作，易导致环境异常
            // 该TLV作用未知
            writeBytes(tlv.t141())
            writeBytes(tlv.t8())
            writeBytes(tlv.t511())
            writeBytes(tlv.t187())

            // writeHex("04000048D1387BC477015873D624BB495618F37A3096BCB21757E66741E1E5E090E6DD293C402D0003B169879C5B95BB5A21028062CD406335AFE249A508144C26A18A42B3FF12D1A1EB95E8")
            // 加上一个不知道真假的TLV400可以过部分假锁

            // writeBytes(tlv.t400())
            // 无randSeed与noPicSig
            writeBytes(tlv.t188())

            writeBytes(tlv.t191())
            // t191 提交可以回退协议 使得可以在原本设备异常的环境下登录
            // t191 QQ8.7.5开始 t191不会再发送
            writeBytes(tlv.t202())
            writeBytes(tlv.t194())
            // 可有可无的TLV 对协议本身无影响
            writeBytes(tlv.t177())
            writeBytes(tlv.t516())
            writeBytes(tlv.t521())
            writeBytes(tlv.t525())
            writeBytes(tlv.t544())
            writeBytes(tlv.t545())


        }.toByteArray()
    }
}