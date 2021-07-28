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

import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.tars.TarsBase
import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField

@TarsClass(requireRead = true, requireWrite = true)
class UserSigInfo : TarsBase() {
    // 保质期 28 days
    @TarsField(id = 2)
    lateinit var tgtKey: BytesTicket

    // A2
    @TarsField(id = 1)
    lateinit var tgt: BytesTicket

    /**
     * 没有名字 QQ逆向里面它叫做G from 8.7.5
     */
    @TarsField(id = 5)
    var G = EMPTY_BYTE_ARRAY

    @TarsField(id = 6)
    var payToken = EMPTY_BYTE_ARRAY

    // 保质期：下个月 1 日
    @TarsField(id = 7)
    lateinit var d2: BytesTicket
    @TarsField(id = 8)
    lateinit var d2Key: BytesTicket

    @TarsField(id = 9)
    var extraDataList = arrayListOf<LoginExtraData>()

    // from t16a
    @TarsField(id = 10)
    lateinit var noPicSig: BytesTicket

    @TarsField(id = 11)
    lateinit var superKey: StringTicket

    // 用t106和t10c计算得到 from QQ 8.6.0
    // 保质期 30 days
    @TarsField(id = 12)
    lateinit var encryptA1: BytesTicket

    // from 10c
    @TarsField(id = 13)
    lateinit var gtKey: BytesTicket

    // 保质期：2 hours
    @TarsField(id = 14)
    lateinit var webSig: BytesTicket

    @TarsField(id = 15)
    var da2: BytesTicket = BytesTicket()

    @TarsField(id = 16)
    lateinit var st: BytesTicket
    @TarsField(id = 17)
    lateinit var stKey: BytesTicket

    @TarsField(id = 18)
    var deviceToken: BytesTicket = BytesTicket()

    @TarsField(id = 20)
    lateinit var downloadStKey: BytesTicket
    @TarsField(id = 19)
    lateinit var downloadSt: BytesTicket

    @TarsField(id = 21)
    lateinit var wtSessionTicket: BytesTicket
    @TarsField(id = 22)
    lateinit var wtSessionTicketKey: BytesTicket

    @TarsField(id = 23)
    var t528: ByteArray = EMPTY_BYTE_ARRAY
}

open class BytesTicket(value: ByteArray, createTime: Long, shelfLife: Long = 0) : Ticket(value, createTime, shelfLife) {
    constructor() : this(EMPTY_BYTE_ARRAY, 0, 0)

    fun ticket(): ByteArray = value()
}

open class StringTicket(value: ByteArray, createTime: Long, shelfLife: Long = 0) : Ticket(value, createTime, shelfLife) {
    constructor() : this(EMPTY_BYTE_ARRAY, 0, 0)

    fun ticket() = String(value())

    override fun toString(): String {
        return ticket()
    }
}

@TarsClass(requireRead = true, requireWrite = true)
open class Ticket() : TarsBase() {
    @TarsField(id = 1)
    var value = EMPTY_BYTE_ARRAY
    @TarsField(id = 2)
    var createTime = 0L
    @TarsField(id = 3)
    var shelfLife = 0L

    constructor(
            value: ByteArray,
            createTime: Long,
            /**
             * 过期时间可修改 from <h>T138</h>
             */
            shelfLife: Long) : this() {
        this.value = value
        this.createTime = createTime
        this.shelfLife = shelfLife
    }

    fun value(): ByteArray {
        return value
    }

    fun isExpired(): Boolean {
        return System.currentTimeMillis() > (createTime + shelfLife)
    }
}

@TarsClass(requireWrite = true, requireRead = true)
class LoginExtraData(

) : TarsBase() {
    @TarsField(id = 1)
    val uin: Long = 0

    @TarsField(id = 2)
    val ip: ByteArray = EMPTY_BYTE_ARRAY

    @TarsField(id = 3)
    val time: Int = 0

    @TarsField(id = 4)
    val appId: Int = 0
}