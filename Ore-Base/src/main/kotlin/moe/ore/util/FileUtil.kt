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

import kotlin.Throws
import java.io.*

object FileUtil {
    @JvmStatic
    fun has(string: String) = File(string).exists()

    @Throws(IOException::class)
    @JvmStatic
    fun readFile(f: File): ByteArray {
        return readFile(f.absolutePath)
    }

    @Throws(IOException::class)
    @JvmStatic
    fun readFile(filename: String) : ByteArray {
        return readFileBytes(filename)
    }

    @Throws(IOException::class)
    @JvmStatic
    fun readFileString(f: File): String {
        return String(readFile(f))
    }

    @JvmStatic
    @Throws(IOException::class)
    fun readFileString(path: String): String {
        return String(readFileBytes(path))
    }

    @Throws(IOException::class)
    @JvmStatic
    fun readFileBytes(path: String): ByteArray {
        return readFileBytes(FileInputStream(path))
    }

    @Throws(IOException::class)
    @JvmStatic
    fun readFileBytes(inputStream: InputStream): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        while (true) {
            val read = inputStream.read()
            if (read == -1) {
                inputStream.close()
                val byteArray = byteArrayOutputStream.toByteArray()
                byteArrayOutputStream.close()
                return byteArray
            }
            byteArrayOutputStream.write(read)
        }
    }

    @Throws(IOException::class)
    @JvmStatic
    fun saveFile(path: String, content: String) {
        saveFile(path, content.toByteArray())
    }

    @Throws(IOException::class)
    @JvmStatic
    fun saveFile(path: String, content: ByteArray) {
        val file = File(path)
        if (!file.exists()) {
            checkParentFile(file.parentFile)
            if (!file.createNewFile()) {
                return
            }
        }
        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.write(content)
        fileOutputStream.close()
    }

    @JvmStatic
    private fun checkParentFile(file: File) {
        if (!file.exists()) {
            file.mkdirs()
        }
    }
}