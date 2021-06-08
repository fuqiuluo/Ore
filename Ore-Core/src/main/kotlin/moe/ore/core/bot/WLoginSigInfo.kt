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
 * 使用该项目产生的违法行为，由第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 *  オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 *
 */

/*******************************************************************************
 *  2021 Ore Developer Warn
 *
 * English :
 * The project is protected by the MPL open source agreement.
 * Open source agreement warning that prohibits deletion of project source code files.
 * The project is prohibited from acting in illegal areas.
 * All illegal activities arising from the use of this project are the responsibility of the second author, and the original author of the project is not responsible
 *
 * 中文：
 * 该项目由MPL开源协议保护。
 * 禁止删除项目源代码文件的开源协议警告内容。
 * 禁止使用该项目在非法领域行事。
 * 使用该项目产生的违法行为，由第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 * オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 ******************************************************************************/

package moe.ore.core.bot

import kotlinx.io.core.ByteReadPacket
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import moe.ore.core.protocol.util.read
import moe.ore.core.protocol.util.readUShortLVByteArray
import moe.ore.core.protocol.util.readUShortLVString
import moe.ore.core.protocol.util.toUHexString

internal class WFastLoginInfo(val outA1: ByteReadPacket, var adUrl: String = "", var iconUrl: String = "", var profileUrl: String = "", var userJson: String = "") {
    override fun toString(): String {
        return "WFastLoginInfo(outA1=$outA1, adUrl='$adUrl', iconUrl='$iconUrl', profileUrl='$profileUrl', userJson='$userJson')"
    }
}

@Serializable
class WLoginSimpleInfo(val uin: Long, val imgType: ByteArray, val imgFormat: ByteArray, val imgUrl: ByteArray, val mainDisplayName: ByteArray) {
    override fun toString(): String {
        return "WLoginSimpleInfo(uin=$uin, imgType=${imgType.toUHexString()}, imgFormat=${imgFormat.toUHexString()}, imgUrl=${imgUrl.toUHexString()}, mainDisplayName=${mainDisplayName.toUHexString()})"
    }
}


@Suppress("ArrayInDataClass") // for `copy`
@Serializable
data class WLoginSigInfo(
    val uin: Long,
    var encryptA1: ByteArray?, // sigInfo[0]
    /**
     * WARNING, please check [QQAndroidClient.tlv16a]
     */
    var noPicSig: ByteArray?, // sigInfo[1]

    val simpleInfo: WLoginSimpleInfo,

    var appPri: Long,
    var a2ExpiryTime: Long,
    var loginBitmap: Long,
    var tgt: ByteArray,
    var a2CreationTime: Long,
    var tgtKey: ByteArray,
    var userStSig: KeyWithCreationTime,
    /**
     * TransEmpPacket 加密使用
     */
    var userStKey: ByteArray,
    var userStWebSig: KeyWithExpiry,
    var userA5: KeyWithCreationTime,
    var userA8: KeyWithExpiry,
    var lsKey: KeyWithExpiry,
    var sKey: KeyWithExpiry,
    var userSig64: KeyWithCreationTime,
    var openId: ByteArray,
    var openKey: KeyWithCreationTime,
    var vKey: KeyWithExpiry,
    var accessToken: KeyWithCreationTime,
    var d2: KeyWithExpiry,
    var d2Key: ByteArray,
    var sid: KeyWithExpiry,
    var aqSig: KeyWithCreationTime,
    var psKeyMap: PSKeyMap,
    var pt4TokenMap: MutableMap<String, KeyWithExpiry> = mutableMapOf(), // = Pt4TokenMap  maybe compiler bug
    var superKey: ByteArray,
    var payToken: ByteArray,
    var pf: ByteArray,
    var pfKey: ByteArray,
    var da2: ByteArray,
    // val pt4Token: ByteArray,
    var wtSessionTicket: KeyWithCreationTime,
    var wtSessionTicketKey: ByteArray,
    var deviceToken: ByteArray,

    ) {
    override fun toString(): String {
        return "WLoginSigInfo(uin=$uin, encryptA1=${encryptA1?.toUHexString()}, noPicSig=${noPicSig?.toUHexString()}, simpleInfo=$simpleInfo, appPri=$appPri, a2ExpiryTime=$a2ExpiryTime, loginBitmap=$loginBitmap, tgt=${tgt.toUHexString()}, a2CreationTime=$a2CreationTime, tgtKey=${tgtKey.toUHexString()}, userStSig=$userStSig, userStKey=${userStKey.toUHexString()}, userStWebSig=$userStWebSig, userA5=$userA5, userA8=$userA8, lsKey=$lsKey, sKey=$sKey, userSig64=$userSig64, openId=${openId.toUHexString()}, openKey=$openKey, vKey=$vKey, accessToken=$accessToken, d2=$d2, d2Key=${d2Key.toUHexString()}, sid=$sid, aqSig=$aqSig, psKey=$psKeyMap, superKey=${superKey.toUHexString()}, payToken=${payToken.toUHexString()}, pf=${pf.toUHexString()}, pfKey=${pfKey.toUHexString()}, da2=${da2.toUHexString()}, wtSessionTicket=$wtSessionTicket, wtSessionTicketKey=${wtSessionTicketKey.toUHexString()}, deviceToken=${deviceToken.toUHexString()})"
    }

//    val SIG_RESERVE_LENGTH: Int = 16
//    val SIG_RESERVE_VALID_LENGTH: Int = 7
//    var _LHSig: ByteArray? = ByteArray(0)
//    var _QRPUSHSig: ByteArray? = ByteArray(0)
//    var _A2_create_time: Long = 0
//    var _A2_expire_time: Long = 0
//    var _D2: ByteArray?
//    var _D2Key: ByteArray?
//    var _D2_create_time: Long = 0
//    var _D2_expire_time: Long = 0
//    var _DA2: ByteArray?
//    var _G: ByteArray?
//    var _TGT: ByteArray?
//    var _TGTKey: ByteArray?
//    var _access_token: ByteArray?
//    var _access_token_create_time: Long = 0
//    var _app_pri: Long = 0
//    var _aqSig: ByteArray?
//    var _aqSig_create_time: Long = 0
//    var _create_time: Long = 0
//    var _device_token: ByteArray?
//    var _dpwd: ByteArray?
//    var _en_A1: ByteArray?
//    var _login_bitmap: Int = 0
//    var _lsKey: ByteArray?
//    var _lsKey_create_time: Long = 0
//    var _lsKey_expire_time: Long = 0
//    var _noPicSig: ByteArray?
//    var _openid: ByteArray?
//    var _openkey: ByteArray?
//    var _openkey_create_time: Long = 0
//    var _pay_token: ByteArray?
//    var _pf: ByteArray?
//    var _pfKey: ByteArray?
//    var _psKey: ByteArray?
//    var _psKey_create_time: Long = 0
//    var _pt4Token: ByteArray?
//    var _randseed: ByteArray?
//    var _sKey: ByteArray?
//    var _sKey_create_time: Long = 0
//    var _sKey_expire_time: Long = 0
//    var _sid: ByteArray?
//    var _sid_create_time: Long = 0
//    var _sid_expire_time: Long = 0
//    var _superKey: ByteArray?
//    var _userA5: ByteArray?
//    var _userA5_create_time: Long = 0
//    var _userA8: ByteArray?
//    var _userA8_create_time: Long = 0
//    var _userA8_expire_time: Long = 0
//    var _userSig64: ByteArray?
//    var _userSig64_create_time: Long = 0
//    var _userStSig: ByteArray?
//    var _userStSig_create_time: Long = 0
//    var _userStWebSig: ByteArray?
//    var _userStWebSig_create_time: Long = 0
//    var _userStWebSig_expire_time: Long = 0
//    var _userSt_Key: ByteArray?
//    var _vKey_expire_time: Long = 0
//    var _vkey: ByteArray?
//    var _vkey_create_time: Long = 0
//    @kotlin.jvm.Transient
//    var cacheTickets: List<Ticket>? = null
//
//    @Transient
//    var cacheUpdateStamp: Long = 0
//    var mainSigMap = 0
//    var wtSessionTicket: ByteArray
//    var wtSessionTicketCreatTime: Long = 0
//    var wtSessionTicketKey: ByteArray
}

internal typealias PSKeyMap = MutableMap<String, KeyWithExpiry>
internal typealias Pt4TokenMap = MutableMap<String, KeyWithExpiry>

internal fun parsePSKeyMapAndPt4TokenMap(data: ByteArray, creationTime: Long, expireTime: Long, outPSKeyMap: PSKeyMap, outPt4TokenMap: Pt4TokenMap) =
    data.read {
        repeat(readShort().toInt()) {
            val domain = readUShortLVString()
            val psKey = readUShortLVByteArray()
            val pt4token = readUShortLVByteArray()

            when {
                psKey.isNotEmpty() -> outPSKeyMap[domain] = KeyWithExpiry(psKey, creationTime, expireTime)
                pt4token.isNotEmpty() -> outPt4TokenMap[domain] = KeyWithExpiry(pt4token, creationTime, expireTime)
            }
        }
    }

@Serializable
open class KeyWithExpiry(@SerialName("data1") override val data: ByteArray, @SerialName("creationTime1") override val creationTime: Long, val expireTime: Long) : KeyWithCreationTime(data, creationTime) {
    override fun toString(): String {
        return "KeyWithExpiry(data=${data.toUHexString()}, creationTime=$creationTime)"
    }
}

@Serializable
open class KeyWithCreationTime(open val data: ByteArray, open val creationTime: Long) {
    override fun toString(): String {
        return "KeyWithCreationTime(data=${data.toUHexString()}, creationTime=$creationTime)"
    }
}