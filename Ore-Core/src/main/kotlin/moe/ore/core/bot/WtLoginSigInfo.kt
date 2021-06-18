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

    var t104: ByteArray? = null

    var t174: ByteArray? = null

//    // from t172
//    var rollbackSig : ByteArray? = null

    /**
     * 没有名字 QQ逆向里面它叫做G from 8.7.5
     */
    var G = byteArrayOf()

    var d2: ByteArray? = null
    lateinit var d2Key: ByteArray

//    // form T403
//    var randSeed: ByteArray? = null

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
//
//package moe.ore.core.bot
//
//import moe.ore.core.helper.toByteArray
//import moe.ore.helper.currentTimeSeconds
//import java.security.SecureRandom
//import java.util.*
//import kotlin.math.abs
//
//class WtLoginSigInfo {
//    var packetSessionId: ByteArray = 0x02B05B8B.toByteArray()
//
//    //        lateinit var wtSessionTicket: ByteArray
////    lateinit var wtSessionTicketKey: ByteArray
//    lateinit var tgtKey: ByteArray
//    lateinit var tgt: ByteArray
//    lateinit var superKey: ByteArray
//    lateinit var t104: ByteArray
//
////    var sigInfo2 = ByteArray(0)//todo sigInfo[2]
//
//    var d2: ByteArray? = null
//    lateinit var d2Key: ByteArray
//
//    // form T403
////    var randSeed: ByteArray? = null
//
//    /**
//     * 每次初始化都随机
//     */
//    val dpwd: ByteArray = try {
//        val str = StringBuilder()
//        for (b in SecureRandom.getSeed(16)) {
//            val abs = abs(b % 26) + if (Random().nextBoolean()) 97 else 65
//            str.append(abs.toChar())
//        }
//        str.toString()
//    } catch (unused: Throwable) {
//        "1234567890123456"
//    }.toByteArray()
//
//    // from t16a
////    lateinit var noPicSig: ByteArray
//
//    // 用t106和t10c计算得到
////    lateinit var enA1: ByteArray
//
//    open class KeyWithExpiry(val data: ByteArray, val dataKey: ByteArray = ByteArray(0), val creationTime: Long = currentTimeSeconds().toLong(), val expireTime: Long = 2592000) {
//        override fun toString(): String {
//            return "KeyWithExpiry(data=${data.contentToString()}, dataKey=${dataKey.contentToString()}, creationTime=$creationTime, expireTime=$expireTime)"
//        }
//
//        fun isTicketExpired(): Boolean {
//            return currentTimeSeconds() > creationTime + expireTime
//        }
//    }
//    //    tlv_t(405)
//    var _LHSig = ByteArray(0)
//    //    tlv_t(791)
//    var _QRPUSHSig = ByteArray(0)
//
//    lateinit var _TGT: KeyWithExpiry
////    lateinit var _TGTKey: KeyWithExpiry
////    var _A2_create_time: Long = 0
////    var _A2_expire_time: Long = 0
//
//    lateinit var _D2: KeyWithExpiry
////    lateinit var _D2Key: KeyWithExpiry
////    var _D2_create_time: Long = 0
////    var _D2_expire_time: Long = 0
//
//    lateinit var _DA2: ByteArray
//    lateinit var _G: ByteArray
//
//    lateinit var _access_token: KeyWithExpiry
////    var _access_token_create_time: Long = 0
//
//    var _app_pri: Long = 0
//
//    lateinit var _aqSig: KeyWithExpiry
////    var _aqSig_create_time: Long = 0
//
//    var _create_time: Long = 0
//
//    lateinit var _device_token: ByteArray
//    lateinit var _dpwd: ByteArray
//    lateinit var _en_A1: ByteArray
//    var _login_bitmap = 0
//
//    lateinit var _lsKey: KeyWithExpiry
////    var _lsKey_create_time: Long = 0
////    var _lsKey_expire_time: Long = 0
//
//    lateinit var _noPicSig: ByteArray
//
//    lateinit var _openid: KeyWithExpiry
////    lateinit var _openkey: ByteArray
////    var _openkey_create_time: Long = 0
//
//    lateinit var _pay_token: ByteArray
//    lateinit var _pf: ByteArray
//    lateinit var _pfKey: ByteArray
//
//    lateinit var _psKey: KeyWithExpiry
////    var _psKey_create_time: Long = 0
//
//    lateinit var _pt4Token: ByteArray
//    lateinit var _randseed: ByteArray
//
//    lateinit var _sKey: KeyWithExpiry
////    var _sKey_create_time: Long = 0
////    var _sKey_expire_time: Long = 0
//
//    lateinit var _sid: KeyWithExpiry
////    var _sid_create_time: Long = 0
////    var _sid_expire_time: Long = 0
//
//    lateinit var _superKey: ByteArray
//
//    lateinit var _userA5: KeyWithExpiry
////    var _userA5_create_time: Long = 0
//
//    lateinit var _userA8: KeyWithExpiry
////    var _userA8_create_time: Long = 0
////    var _userA8_expire_time: Long = 0
//
//    lateinit var _userSig64: KeyWithExpiry
////    var _userSig64_create_time: Long = 0
//
//    lateinit var _userStSig: KeyWithExpiry
////    var _userStSig_create_time: Long = 0
//
//    lateinit var _userStWebSig: KeyWithExpiry
////    var _userStWebSig_create_time: Long = 0
////    var _userStWebSig_expire_time: Long = 0
//
//    lateinit var _userSt_Key: KeyWithExpiry
////    var _vKey_expire_time: Long = 0
//
//    lateinit var _vkey: KeyWithExpiry
////    var _vkey_create_time: Long = 0
//
//    var mainSigMap = 0
//    lateinit var wtSessionTicket: KeyWithExpiry
////    var wtSessionTicketCreatTime: Long = 0
////    lateinit var wtSessionTicketKey: ByteArray
//
////    @Transient
////    var cacheTickets: List<Ticket>? = null
//
//    //    @Transient
////    var cacheUpdateStamp: Long = 0
//}
//
///**
// * 有时间限制的TOKEN
// *
// * 所有的Key有1个月的保质期，但是随时间迁移key的权力会越来越小 ————> 需要刷新(emp)
// */
//class TokenWithTimeLimit {
//
//}
//class LoginExtraData(
//    val uin: Long,
//    val ip: ByteArray,
//    val time: Int,
//    val version: Int
//) {
//    override fun toString(): String {
//        return "LoginExtraData(uin=$uin, ip=${ip.contentToString()}, time=$time, version=$version)"
//    }
//}