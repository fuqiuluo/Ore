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

package moe.ore.core.net

import com.google.gson.Gson
import moe.ore.helper.hex2ByteArray
import moe.ore.tars.UniPacket
import moe.ore.util.OkhttpUtil
import moe.ore.util.TeaUtil
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

object TestMain {
    @Throws(InterruptedException::class)
    @JvmStatic
    fun main(args: Array<String>) {

        var key = "F0441F5FF42DA58FDCF7949ABA62D411".hex2ByteArray()

        val uniPacket = UniPacket()
        uniPacket.servantName = "ConfigHttp"
        uniPacket.funcName = "HttpServerListReq"
        uniPacket.version = 3
        uniPacket.put("HttpServerListReq", SsoServerInfoReq())

        val encrypt = TeaUtil.encrypt(uniPacket.encode(), key)
        val resp = OkhttpUtil().post("https://configsvr.msf.3g.qq.com/configsvr/serverlist.jsp?mType=getssolist", encrypt.toRequestBody())
        val decrypt = TeaUtil.decrypt(resp?.body?.bytes(), key)
        println(decrypt.toAsciiHexString())
        val decode = UniPacket.decode(decrypt)
        val findByClass = decode.findByClass("HttpServerListRes", SsoServerInfoResp())
        println(Gson().toJson(findByClass))
        for (ipAddressInfo in findByClass.b!!) {
            println(ipAddressInfo.ip + ":" + ipAddressInfo.port)
        }
    }

    fun ByteArray.toAsciiHexString() = joinToString("") {
        if (it in 32..127) it.toInt().toChar().toString() else "{${it.toUByte().toString(16).padStart(2, '0').uppercase(Locale.getDefault())}}"
    }
}

