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

package moe.ore.util

import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsStructBase
import moe.ore.tars.UniPacket
import java.nio.charset.Charset

object TarsUtil {
    @JvmStatic
    fun <T : TarsStructBase> decodeWup(t : T, bytes : ByteArray) : T {
        val input = TarsInputStream(bytes)
        input.setServerEncoding(Charset.defaultCharset())
        t.readFrom(input)
        return t
    }

    @JvmStatic
    fun <T : TarsStructBase> decodeWup(clazz : Class<T>, bytes : ByteArray) : T? {
        return try {
            val t = clazz.newInstance()
            val input = TarsInputStream(bytes)
            input.setServerEncoding(Charset.defaultCharset())
            t.readFrom(input)
            t
        } catch (e : Exception) {
            e.printStackTrace()
            null
        }
    }

    @JvmStatic
    fun getUni(version: Int) : UniPacket {
        val uni = UniPacket()
        uni.version = version
        return uni
    }

    @JvmStatic
    fun encodeRequest(
        requestId : Int,
        base: TarsStructBase,
        version : Int = 3
    ) : ByteArray {
        val uni = getUni(version)
        uni.requestId = requestId
        uni.version = version
        uni.put(base)
        return uni.encode()
    }

    @JvmStatic
    fun decodeRequest(data: ByteArray) : UniPacket {
        return UniPacket.decode(data)
    }
}