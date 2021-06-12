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

package moe.ore.helper.bytes

import kotlinx.io.core.*
import kotlinx.io.streams.outputStream
import moe.ore.util.BytesUtil
import moe.ore.util.MD5
import moe.ore.util.TeaUtil
import java.io.DataOutputStream
import kotlin.text.toByteArray

fun createBuilder() = BytePacketBuilder()

/**
 * 转字节组
 * @receiver BytePacketBuilder
 * @return ByteArray
 */
fun BytePacketBuilder.toByteArray(): ByteArray {
    val reader = this.build()
    val array = ByteArray(reader.remaining.toInt())
    reader.readAvailable(array)
    return array
}

/**
 * 补充功能代码
 * @receiver BytePacketBuilder
 * @param packet BytePacketBuilder
 */
fun BytePacketBuilder.writePacket(packet: BytePacketBuilder) = this.writePacket(packet.build())

/**
 * 写布尔型
 * @receiver BytePacketBuilder
 * @param z Boolean
 */
fun BytePacketBuilder.writeBoolean(z: Boolean) = this.writeByte(if (z) 1 else 0)

/**
 * 自动转换类型
 * @receiver BytePacketBuilder
 * @param i Int
 */
fun BytePacketBuilder.writeShort(i: Int) = this.writeShort(i.toShort())

fun BytePacketBuilder.writeBytes(bytes: ByteArray) = this.writeFully(bytes)

fun BytePacketBuilder.writeLongToBuf32(v: Long) {
    this.writeBytes(BytesUtil.int64ToBuf32(v))
}

fun BytePacketBuilder.writeBytesWithShortSize(body: ByteArray, add: Int) {
    writeShort(body.size + add)
    writeBytes(body)
}

fun BytePacketBuilder.writeBytesWithShortSize(body: ByteArray) {
    writeBytesWithShortSize(body, 0)
}

fun BytePacketBuilder.writeBytesWithSize(body: ByteArray, add: Int) {
    writeInt(body.size + add)
    writeBytes(body)
}

fun BytePacketBuilder.writeBytesWithSize(body: ByteArray) {
    writeBytesWithSize(body, 0)
}

fun BytePacketBuilder.writeStringWithSize(str: String) {
    writeBytesWithSize(str.toByteArray())
}

fun BytePacketBuilder.writeStringWithSize(str: String, add: Int) {
    writeBytesWithSize(str.toByteArray(), add)
}

fun BytePacketBuilder.writeStringWithShortSize(str: String) {
    writeBytesWithShortSize(str.toByteArray())
}

fun BytePacketBuilder.writeStringWithShortSize(str: String, add: Int) {
    writeBytesWithShortSize(str.toByteArray(), add)
}

fun BytePacketBuilder.md5(): ByteArray {
    return MD5.toMD5Byte(toByteArray())
}

fun BytePacketBuilder.writeTeaEncrypt(key: ByteArray, block: BytePacketBuilder.() -> Unit) {
    val body = createBuilder()
    body.block()
    this.writeBytes(TeaUtil.encrypt(body.toByteArray(), key))
}

fun BytePacketBuilder.writeString(str: String) {
    this.writeStringUtf8(str)
}

fun BytePacketBuilder.writeLimitedByteArray(array: ByteArray, maxLength: Int) {
    this.writeFully(array, 0, maxLength)
}

fun BytePacketBuilder.writeLimitedString(str: String, maxLength: Int) =
    writeLimitedByteArray(str.toByteArray(), maxLength)

internal fun BytePacketBuilder.writeShortLVByteArray(byteArray: ByteArray): Int {
    this.writeShort(byteArray.size.toShort())
    this.writeFully(byteArray)
    return byteArray.size
}

internal fun BytePacketBuilder.writeShortLVString(str: String) = writeShortLVByteArray(str.toByteArray())

fun BytePacketBuilder.writeHex(uHex: String) {
    uHex.split(" ").forEach {
        if (it.isNotBlank()) {
            writeUByte(it.toUByte(16))
        }
    }
}
