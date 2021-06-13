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
 * GetStWithPassword
 */
class WtLoginV1(uin: Long) : WtLogin(uin, LOGIN, 0x810, 0x87) {
    override fun build(seq: Int): ByteArray {
        return createBuilder().apply {
            writeShort(9)
            writeShort(24)
            writeBytes(tlv.t18())
            writeBytes(tlv.t1())
            writeBytes(tlv.t106())
            writeBytes(tlv.t116())
            writeBytes(tlv.t100())
            writeBytes(tlv.t107())
            // writeBytes(tlv.t108(device.ksid))
            // 第一次登录不需要
            writeBytes(tlv.t142())
            writeBytes(tlv.t144())
            writeBytes(tlv.t145())
            writeBytes(tlv.t147())
            writeBytes(tlv.t154(seq))
            // writeBytes(tlv.t16b())
            // 非常规协议操作，易导致环境异常
            // 该TLV作用未知
            writeBytes(tlv.t141())
            writeBytes(tlv.t8())
            writeBytes(tlv.t511())
            writeBytes(tlv.t187())
            // writeBytes(tlv.t400())
            // 无randSeed与noPicSig
            writeBytes(tlv.t188())
            writeBytes(tlv.t194())
            writeBytes(tlv.t202())
            writeBytes(tlv.t177())
            writeBytes(tlv.t516())
            writeBytes(tlv.t521())
            writeBytes(tlv.t525())
            writeBytes(tlv.t544())
            writeBytes(tlv.t545())
        }.toByteArray()
    }
}