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

package moe.ore.helper

import moe.ore.util.BytesUtil
import java.util.*
import kotlin.experimental.xor

fun ByteArray.toInt() = BytesUtil.bufToInt32(this, 0)

fun ByteArray.toShort(): Short = BytesUtil.bufToInt16(this, 0).toShort()

fun ByteArray.toHexString(): String = this.joinToString("") {
    (it.toInt() and 0xFF).toString(16).padStart(2, '0').uppercase(Locale.getDefault())
}

fun ByteArray.xor(key: ByteArray): ByteArray {
    val result = ByteArray(this.size)
    for (i in 0 until this.size) {
        result[i] = (this[i] xor key[i % key.size] xor ((i and 0xFF).toByte()))
    }
    return result
}

fun ByteArray.sub(offset: Int, length: Int) = BytesUtil.subByte(this, offset, length)

fun ByteArray.toAsciiHexString() = joinToString("") {
    if (it in 32..127) it.toInt().toChar().toString() else "{${it.toUByte().toString(16).padStart(2, '0').uppercase(
        Locale.getDefault())}}"
}