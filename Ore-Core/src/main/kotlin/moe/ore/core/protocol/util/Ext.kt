package moe.ore.core.protocol.util

import kotlinx.io.charsets.Charset
import kotlinx.io.core.*
import moe.ore.util.TeaUtil
import java.io.Closeable
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.Inet4Address
import java.security.MessageDigest
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.io.use
import kotlin.text.toByteArray

/**
 *@author 飞翔的企鹅
 *create 2021-06-05 19:39
 */
public fun InputStream.md5(): ByteArray {
    return digest("md5")
}

public fun InputStream.digest(algorithm: String): ByteArray {
    val digest = MessageDigest.getInstance(algorithm)
    digest.reset()
    use { input ->
        object : OutputStream() {
            override fun write(b: Int) {
                digest.update(b.toByte())
            }

            override fun write(b: ByteArray, off: Int, len: Int) {
                digest.update(b, off, len)
            }
        }.use { output ->
            input.copyTo(output)
        }
    }
    return digest.digest()
}

public fun InputStream.sha1(): ByteArray {
    return digest("SHA-1")
}

/**
 * Localhost 解析
 */
public fun localIpAddress(): String = runCatching {
    Inet4Address.getLocalHost().hostAddress
}.getOrElse { "192.168.1.123" }

public fun String.md5(): ByteArray = toByteArray().md5()

@JvmOverloads
public fun ByteArray.md5(offset: Int = 0, length: Int = size - offset): ByteArray {
    checkOffsetAndLength(offset, length)
    return MessageDigest.getInstance("MD5").apply { update(this@md5, offset, length) }.digest()
}

public fun String.sha1(): ByteArray = toByteArray().sha1()

@JvmOverloads
public fun ByteArray.sha1(offset: Int = 0, length: Int = size - offset): ByteArray {
    checkOffsetAndLength(offset, length)
    return MessageDigest.getInstance("SHA-1").apply { update(this@sha1, offset, length) }.digest()
}

public fun ByteArray.checkOffsetAndLength(offset: Int, length: Int) {
    require(offset >= 0) { "offset shouldn't be negative: $offset" }
    require(length >= 0) { "length shouldn't be negative: $length" }
    require(offset + length <= this.size) { "offset ($offset) + length ($length) > array.size (${this.size})" }
}


/**
 * 要求 [this] 最小为 [min].
 */
@PublishedApi
internal fun Int.coerceAtLeastOrFail(min: Int): Int {
    require(this >= min)
    return this
}

/**
 * 要求 [this] 最小为 [min].
 */
@PublishedApi
internal fun Long.coerceAtLeastOrFail(min: Long): Long {
    require(this >= min)
    return this
}

/**
 * 要求 [this] 最大为 [max].
 */
@PublishedApi
internal fun Int.coerceAtMostOrFail(max: Int): Int =
    if (this >= max) error("value $this is greater than its expected maximum value $max")
    else this

@PublishedApi
internal fun Long.coerceAtMostOrFail(max: Long): Long =
    if (this >= max) error("value $this is greater than its expected maximum value $max")
    else this


val EMPTY_BYTE_ARRAY: ByteArray = ByteArray(0)
val DECRYPTER_16_ZERO: ByteArray = ByteArray(16)
val KEY_16_ZEROS: ByteArray = ByteArray(16)

internal inline fun BytePacketBuilder.writeShortLVByteArray(byteArray: ByteArray): Int {
    this.writeShort(byteArray.size.toShort())
    this.writeFully(byteArray)
    return byteArray.size
}

public inline fun ByteArray.toReadPacket(offset: Int = 0, length: Int = this.size - offset): ByteReadPacket =
    ByteReadPacket(this, offset = offset, length = length)


@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
public inline fun <R> ByteReadPacket.useBytes(n: Int = remaining.toInt(),//not that safe but adequate
    block: (data: ByteArray, length: Int) -> R): R = ByteArrayPool.useInstance(n) {
    this.readFully(it, 0, n)
    block(it, n)
}

//not that safe but adequate
fun ByteReadPacket.readPacketExact(n: Int = remaining.toInt()
 ): ByteReadPacket =this.readBytes(n).toReadPacket()


internal inline fun BytePacketBuilder.encryptAndWrite(key: ByteArray, encoder: BytePacketBuilder.() -> Unit) =
    writeFully(TeaUtil.encrypt(BytePacketBuilder().apply(encoder).build().readBytes(), key))

@OptIn(ExperimentalContracts::class)
public inline fun <C : Closeable, R> C.withUse(block: C.() -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return use(block)
}

public inline fun Input.readString(length: Int, charset: Charset = Charsets.UTF_8): String =
    String(this.readBytes(length), charset = charset)

public inline fun Input.readString(length: Long, charset: Charset = Charsets.UTF_8): String =
    String(this.readBytes(length.toInt()), charset = charset)

public inline fun Input.readString(length: Short, charset: Charset = Charsets.UTF_8): String =
    String(this.readBytes(length.toInt()), charset = charset)

@JvmSynthetic
public inline fun Input.readString(length: UShort, charset: Charset = Charsets.UTF_8): String =
    String(this.readBytes(length.toInt()), charset = charset)

public inline fun Input.readString(length: Byte, charset: Charset = Charsets.UTF_8): String =
    String(this.readBytes(length.toInt()), charset = charset)






@OptIn(ExperimentalUnsignedTypes::class)
@JvmOverloads
@Suppress("DuplicatedCode") // false positive. foreach is not common to UByteArray and ByteArray
public fun ByteArray.toUHexString(
    separator: String = " ",
    offset: Int = 0,
    length: Int = this.size - offset
): String {
    this.checkOffsetAndLength(offset, length)
    if (length == 0) {
        return ""
    }
    val lastIndex = offset + length
    return buildString(length * 2) {
        this@toUHexString.forEachIndexed { index, it ->
            if (index in offset until lastIndex) {
                var ret = it.toUByte().toString(16).toUpperCase()
                if (ret.length == 1) ret = "0$ret"
                append(ret)
                if (index < lastIndex - 1) append(separator)
            }
        }
    }
}

/**
 * 转无符号十六进制表示, 并补充首位 `0`.
 */
public fun UByte.fixToUHex(): String =
    if (this.toInt() in 0..15) "0${this.toString(16).toUpperCase()}" else this.toString(16).toUpperCase()


/**
 * 转无符号十六进制表示, 并补充首位 `0`.
 * 转换结果示例: `FF`, `0E`
 */
public fun Byte.toUHexString(): String = this.toUByte().fixToUHex()

fun ByteArray.toAsciiHexString() = joinToString("") {
    if (it in 32..127) it.toInt().toChar().toString() else "{${it.toUByte().toString(16).padStart(2, '0').uppercase()}}"
}

fun String.chunkedHexToBytes(): ByteArray =
    this.asSequence().chunked(2).map { (it[0].toString() + it[1]).toUByte(16).toByte() }.toList().toByteArray()



@OptIn(ExperimentalContracts::class)
public inline fun <R> ByteArray.read(t: ByteReadPacket.() -> R): R {
    contract {
        callsInPlace(t, InvocationKind.EXACTLY_ONCE)
    }
    return this.toReadPacket().withUse(t)
}
public fun Input.readUShortLVString(): String = String(this.readUShortLVByteArray())
public fun Input.readUShortLVByteArray(): ByteArray = this.readBytes(this.readUShort().toInt())

public fun File.createFileIfNotExists() {
    if (!this.exists()) {
        this.parentFile.mkdirs()
        this.createNewFile()
    }
}

public fun File.resolveCreateFile(relative: String): File = this.resolve(relative).apply { createFileIfNotExists() }
public fun File.resolveCreateFile(relative: File): File = this.resolve(relative).apply { createFileIfNotExists() }

public fun File.resolveMkdir(relative: String): File = this.resolve(relative).apply { mkdirs() }
public fun File.resolveMkdir(relative: File): File = this.resolve(relative).apply { mkdirs() }