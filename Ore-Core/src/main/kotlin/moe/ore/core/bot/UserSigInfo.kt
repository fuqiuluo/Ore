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

import kotlinx.serialization.Serializable
import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsOutputStream
import moe.ore.tars.TarsStructBase

class UserSigInfo : TarsStructBase() {
    // 保质期 28 days
    lateinit var tgtKey: BytesTicket
    // A2
    lateinit var tgt: BytesTicket

    // 1 days
    lateinit var sKey: StringTicket

    var t104: ByteArray? = null

    var t174: ByteArray? = null

    /**
     * 没有名字 QQ逆向里面它叫做G from 8.7.5
     */
    var G = byteArrayOf()

    var payToken = byteArrayOf()

    // 保质期：下个月 1 日
    lateinit var d2: BytesTicket
    lateinit var d2Key: BytesTicket

    val extraDataList = arrayListOf<LoginExtraData>()

    // from t16a
    lateinit var noPicSig: BytesTicket

    lateinit var superKey : StringTicket

    // 用t106和t10c计算得到 from QQ 8.6.0
    // 保质期 30 days
    lateinit var encryptA1: BytesTicket

    // from 10c
    lateinit var gtKey : BytesTicket

    // 保质期：2 hours
    lateinit var webSig : BytesTicket

    lateinit var da2 : BytesTicket

    lateinit var st : BytesTicket
    lateinit var stKey : BytesTicket

    lateinit var deviceToken : BytesTicket

    lateinit var downloadStKey : BytesTicket
    lateinit var downloadSt : BytesTicket

    lateinit var wtSessionTicket : BytesTicket
    lateinit var wtSessionTicketKey : BytesTicket

    lateinit var t528 : ByteArray

    override fun writeTo(output: TarsOutputStream) {

    }

    override fun readFrom(input: TarsInputStream) {

    }
}

open class BytesTicket(value: ByteArray, createTime: Long, shelfLife: Long = 0) : Ticket(value, createTime, shelfLife) {
    fun ticket() : ByteArray = value()
}

open class StringTicket(value: ByteArray, createTime: Long, shelfLife: Long = 0) : Ticket(value, createTime, shelfLife) {
    fun ticket() = String(value())

    override fun toString(): String {
        return ticket()
    }
}


open class Ticket(
    private var value : ByteArray,
    var createTime : Long,
    /**
     * 过期时间可修改 from <h>T138</h>
     */
    var shelfLife : Long) : TarsStructBase() {

    fun value() : ByteArray {
        return value
    }

    fun isExpired() : Boolean {
        return System.currentTimeMillis() > (createTime + shelfLife)
    }
}

@Serializable
data class LoginExtraData(
    val uin : Long,
    val ip : ByteArray,
    val time : Int,
    val appId : Int
)