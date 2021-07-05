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

package moe.ore.core.util

import moe.ore.core.net.SsoServerInfoReq
import moe.ore.core.net.SsoServerInfoResp
import moe.ore.helper.*
import moe.ore.tars.UniPacket
import moe.ore.util.OkhttpUtil
import moe.ore.util.TeaUtil
import okhttp3.RequestBody.Companion.toRequestBody

object QQUtil {
    @JvmStatic
    private val ConfigSvrKey = "F0441F5FF42DA58FDCF7949ABA62D411".hex2ByteArray()

    @JvmStatic
    fun groupCode2GroupUin(groupcode: Long): Long {
        var calc = groupcode / 1000000L
        loop@ while (true) calc += when (calc) {
            in 0..10 -> {
                (202 - 0).toLong()
            }
            in 11..19 -> {
                (480 - 11).toLong()
            }
            in 20..66 -> {
                (2100 - 20).toLong()
            }
            in 67..156 -> {
                (2010 - 67).toLong()
            }
            in 157..209 -> {
                (2147 - 157).toLong()
            }
            in 210..309 -> {
                (4100 - 210).toLong()
            }
            in 310..499 -> {
                (3800 - 310).toLong()
            }
            else -> {
                break@loop
            }
        }
        return calc * 1000000L + groupcode % 1000000L
    }

    @JvmStatic
    fun groupUin2GroupCode(groupuin: Long): Long {
        var calc = groupuin / 1000000L
        loop@ while (true) calc -= when {
            calc >= 0 + 202 && calc + 202 <= 10 -> {
                (202 - 0).toLong()
            }
            calc >= 11 + 480 && calc <= 19 + 480 -> {
                (480 - 11).toLong()
            }
            calc >= 20 + 2100 && calc <= 66 + 2100 -> {
                (2100 - 20).toLong()
            }
            calc >= 67 + 2010 && calc <= 156 + 2010 -> {
                (2010 - 67).toLong()
            }
            calc >= 157 + 2147 && calc <= 209 + 2147 -> {
                (2147 - 157).toLong()
            }
            calc >= 210 + 4100 && calc <= 309 + 4100 -> {
                (4100 - 210).toLong()
            }
            calc >= 310 + 3800 && calc <= 499 + 3800 -> {
                (3800 - 310).toLong()
            }
            else -> {
                break@loop
            }
        }
        return calc * 1000000L + groupuin % 1000000L
    }

    @JvmStatic
    fun getOicqServer(appId : Long = 0x200302d5) : Pair<String, Int>? {
        val isUseDebugSo = true
        try {

            val uniPacket = UniPacket()
            uniPacket.servantName = "ConfigHttp"
            uniPacket.funcName = "HttpServerListReq"
            uniPacket.version = 3
            uniPacket.requestId = 0
            uniPacket.put("HttpServerListReq", SsoServerInfoReq().apply {
                this.a = 0 // uin string 转数字失败即可设置为0
                this.c = 1
                this.d = "460023785098616" // subscriberId(imsi)
                this.f = appId
                this.b = 0 // xxx / 1000
                this.g = "867109044454073" // imei
                this.h = 0 // gsm cid
                this.e = 100
                this.k = 1
                this.l = 0
                this.m = 0
            })
            val encrypt = TeaUtil.encrypt(newBuilder().apply {
                writeBlockWithIntLen({ it + 4 }) {
                    writeBytes(uniPacket.encode())
                }
            }.toByteArray(), ConfigSvrKey)
            val url = if (isUseDebugSo) "https://configsvr.sparta.html5.qq.com/configsvr/serverlist.jsp?mType=getssolist" else "https://configsvr.msf.3g.qq.com/configsvr/serverlist.jsp?mType=getssolist"
            val resp = OkhttpUtil().also { it.defaultUserAgent() }.post(url, encrypt.toRequestBody())
            val code = resp?.code
            val result = resp?.body?.bytes()
            resp?.close()
            val decrypt = TeaUtil.decrypt(result!!, ConfigSvrKey)
            val decode = UniPacket.decode(decrypt, 4)
            val findByClass = decode.findByClass("HttpServerListRes", SsoServerInfoResp())
            val info = findByClass.b!![0]
            val ret = info.ip to info.port
            println("get sso server by configsvr : $ret")
            return ret
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return null
    }

    @JvmStatic
    fun checkAccount(uin: Long): Long {
        check((uin >= 10000L) or (uin <= 4000000000L)) { "QQ号格式错误" }
        return uin
    }

    @JvmStatic
    fun hash(seq: Int, commandName: String): Int {
        var result = seq
        result = 31 * result + commandName.hashCode()
        return result
    }
}