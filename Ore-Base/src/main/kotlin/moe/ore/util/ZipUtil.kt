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

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.Deflater
import java.util.zip.Inflater

object ZipUtil {
    @JvmStatic
    fun unCompress(inputByte: ByteArray?): ByteArray {
        var len: Int
        val infill = Inflater()
        infill.setInput(inputByte)
        val bos = ByteArrayOutputStream()
        val outByte = ByteArray(1024)
        try {
            while (!infill.finished()) {
                len = infill.inflate(outByte)
                if (len == 0) {
                    break
                }
                bos.write(outByte, 0, len)
            }
            infill.end()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                bos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return bos.toByteArray()
    }

    @JvmStatic
    fun compress(inputByte: ByteArray?): ByteArray {
        var len: Int
        val defile = Deflater()
        defile.setInput(inputByte)
        defile.finish()
        val bos = ByteArrayOutputStream()
        val outputByte = ByteArray(1024)
        try {
            while (!defile.finished()) {
                len = defile.deflate(outputByte)
                bos.write(outputByte, 0, len)
            }
            defile.end()
        } finally {
            try {
                bos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return bos.toByteArray()
    }
}