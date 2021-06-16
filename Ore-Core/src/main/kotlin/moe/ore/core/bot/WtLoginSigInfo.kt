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

package moe.ore.core.bot

import moe.ore.core.helper.toByteArray
import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.*
import kotlin.math.abs

class WtLoginSigInfo {
    lateinit var wtSessionTicketKey: ByteArray

    lateinit var wtSessionTicket: ByteArray
    lateinit var tgtKey: ByteArray
    lateinit var tgt: ByteArray
    lateinit var superKey: ByteArray
    lateinit var t104: ByteArray

    var sigInfo2 = ByteArray(0)
    //todo sigInfo[2]

    var d2: ByteArray? = null
    lateinit var d2Key: ByteArray

    // form T403
    var randSeed: ByteArray? = null

    /**
     * 每次初始化都随机
     */
    val dpwd: ByteArray = try {
        val str = StringBuilder()
        for (b in SecureRandom.getSeed(16)) {
            val abs = abs(b % 26) + if (Random().nextBoolean()) 97 else 65
            str.append(abs.toChar())
        }
        str.toString()
    } catch (unused: Throwable) {
        "1234567890123456"
    }.toByteArray()

    // from t16a
    lateinit var noPicSig: ByteArray

    // 用t106和t10c计算得到
    lateinit var enA1: ByteArray


}

/**
 * 有时间限制的TOKEN
 *
 * 所有的Key有1个月的保质期，但是随时间迁移key的权力会越来越小 ————> 需要刷新(emp)
 */
class TokenWithTimeLimit {

}