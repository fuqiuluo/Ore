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
import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsOutputStream
import moe.ore.tars.TarsStructBase
import kotlin.properties.Delegates

class UserSigInfo : TarsStructBase() {
    // 保质期 28 days
    lateinit var tgtKey: BytesTicket

    // A2
    lateinit var tgt: BytesTicket

    /**
     * 没有名字 QQ逆向里面它叫做G from 8.7.5
     */
    var G = EMPTY_BYTE_ARRAY

    var payToken = EMPTY_BYTE_ARRAY

    // 保质期：下个月 1 日
    lateinit var d2: BytesTicket
    lateinit var d2Key: BytesTicket

    var extraDataList = arrayListOf<LoginExtraData>()

    // from t16a
    lateinit var noPicSig: BytesTicket

    lateinit var superKey: StringTicket

    // 用t106和t10c计算得到 from QQ 8.6.0
    // 保质期 30 days
    lateinit var encryptA1: BytesTicket

    // from 10c
    lateinit var gtKey: BytesTicket

    // 保质期：2 hours
    lateinit var webSig: BytesTicket

    lateinit var da2: BytesTicket

    lateinit var st: BytesTicket
    lateinit var stKey: BytesTicket

    lateinit var deviceToken: BytesTicket

    lateinit var downloadStKey: BytesTicket
    lateinit var downloadSt: BytesTicket

    lateinit var wtSessionTicket: BytesTicket
    lateinit var wtSessionTicketKey: BytesTicket

     var t528: ByteArray = EMPTY_BYTE_ARRAY

    override fun writeTo(output: TarsOutputStream) {
        output.write(tgt, 1)
        output.write(tgtKey, 2)
        output.write(G, 5)
        output.write(payToken, 6)
        output.write(d2, 7)
        output.write(d2Key, 8)
        output.write(extraDataList, 9)
        output.write(noPicSig, 10)
        output.write(superKey, 11)
        output.write(encryptA1, 12)
        output.write(gtKey, 13)
        output.write(webSig, 14)
        output.write(da2, 15)
        output.write(st, 16)
        output.write(stKey, 17)
        output.write(deviceToken, 18)
        output.write(downloadSt, 19)
        output.write(downloadStKey, 20)
        output.write(wtSessionTicket, 21)
        output.write(wtSessionTicketKey, 22)
        output.write(t528, 23)
    }

    override fun readFrom(input: TarsInputStream) {
        val ticketType = BytesTicket()
        val strTicketType = StringTicket()
        tgt = input.read(ticketType, 1, false)
        tgtKey = input.read(ticketType, 2, false)
        G = input.read(G, 5, false)
        payToken = input.read(payToken, 6, false)
        d2 = input.read(ticketType, 7, false)
        d2Key = input.read(ticketType, 8, false)
        extraDataList = input.read(extraDataList, 9, false) as ArrayList<LoginExtraData>
        noPicSig = input.read(ticketType, 10, false)
        superKey = input.read(strTicketType, 11, false)
        encryptA1 = input.read(ticketType, 12, false)
        gtKey = input.read(ticketType, 13, false)
        webSig = input.read(ticketType, 14, false)
        da2 = input.read(ticketType, 15, false)
        st = input.read(ticketType, 16, false)
        stKey = input.read(ticketType, 17, false)
        deviceToken = input.read(ticketType, 18, false)
        downloadSt = input.read(ticketType, 19, false)
        downloadStKey = input.read(ticketType, 20, false)
        wtSessionTicket = input.read(ticketType, 21, false)
        wtSessionTicketKey = input.read(ticketType, 22, false)
        t528 = input.read(t528, 23, false)
    }
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


open class Ticket() : TarsStructBase() {
    var value = EMPTY_BYTE_ARRAY
    var createTime = 0L
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

    override fun writeTo(output: TarsOutputStream) {
        output.write(value, 1)
        output.write(createTime, 2)
        output.write(shelfLife, 3)
    }

    override fun readFrom(input: TarsInputStream) {
        value=  input.read(value, 1, false)
        createTime= input.read(createTime, 2, false)
        shelfLife=input.read(shelfLife, 3, false)
    }
}

data class LoginExtraData(
        val uin: Long,
        val ip: ByteArray,
        val time: Int,
        val appId: Int
) : TarsStructBase() {
    override fun writeTo(output: TarsOutputStream) {
        output.write(uin, 1)
        output.write(ip, 2)
        output.write(time, 3)
        output.write(appId, 4)
    }

    override fun readFrom(input: TarsInputStream) {
        input.read(uin, 1, false)
        input.read(ip, 1, false)
        input.read(time, 1, false)
        input.read(appId, 1, false)
    }
}