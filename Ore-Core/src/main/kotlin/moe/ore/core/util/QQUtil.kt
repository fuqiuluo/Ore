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
import moe.ore.helper.hex2ByteArray
import moe.ore.tars.UniPacket
import moe.ore.util.TeaUtil
import java.net.HttpURLConnection
import java.net.URL

object QQUtil {
    @JvmStatic
    private val ConfigSvrKey = "F0441F5FF42DA58FDCF7949ABA62D411".hex2ByteArray()

    @JvmStatic
    fun getOicqServer(appId : Long = 0x200302d5) : Pair<String, Int>? {
        val isUseDebugSo = false
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
            val encrypt = TeaUtil.encrypt(uniPacket.encode(), ConfigSvrKey)
            val url = if (isUseDebugSo) URL("https://configsvr.sparta.html5.qq.com/configsvr/serverlist.jsp?mType=getssolist") else URL("https://configsvr.msf.3g.qq.com/configsvr/serverlist.jsp?mType=getssolist")
            val conn = url.openConnection() as HttpURLConnection
            conn.doOutput = true
            conn.requestMethod = "POST"
            conn.connectTimeout = 20000
            conn.readTimeout = 20000
            conn.outputStream.let {
                it.write(encrypt)
                it.flush()
                it.close()
            }
            if(conn.responseCode == 200) {
                var size = 0
                val bytes = ByteArray(128)
                val tmp = arrayListOf<ByteArray>()
                val input = conn.inputStream
                while (true) {
                    val read : Int = input.read(bytes)
                    if(read == -1) {
                        break
                    }
                    val bs = ByteArray(read).also { System.arraycopy(bytes, 0, it, 0, read) }
                    tmp.add(bs)
                    size += read
                }
                val barr = if(tmp.size == 1) {
                    tmp[0]
                } else {
                    ByteArray(size).also { `in` ->
                        var i = 0
                        tmp.forEach {
                            System.arraycopy(it, 0, `in`, i, it.size)
                            i += it.size
                        }
                    }
                }
                val decrypt = TeaUtil.decrypt(barr, ConfigSvrKey)
                val decode = UniPacket.decode(decrypt)
                val findByClass = decode.findByClass("HttpServerListRes", SsoServerInfoResp())
                for (ipAddressInfo in findByClass.b!!) {
                    return ipAddressInfo.ip to ipAddressInfo.port
                }
            }
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