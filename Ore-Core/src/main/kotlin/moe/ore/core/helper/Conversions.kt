package moe.ore.core.helper

/**
 * 255 -> 00 FF
 */
public fun Short.toByteArray(): ByteArray = with(toInt()) {
    byteArrayOf(
        (shr(8) and 0xFF).toByte(),
        (shr(0) and 0xFF).toByte()
    )
}

/**
 * 255 -> 00 00 00 FF
 */
public fun Int.toByteArray(): ByteArray = byteArrayOf(
    ushr(24).toByte(),
    ushr(16).toByte(),
    ushr(8).toByte(),
    ushr(0).toByte()
)

/**
 * 255 -> 00 00 00 FF
 */
public fun Long.toByteArray(): ByteArray = byteArrayOf(
    (ushr(56) and 0xFF).toByte(),
    (ushr(48) and 0xFF).toByte(),
    (ushr(40) and 0xFF).toByte(),
    (ushr(32) and 0xFF).toByte(),
    (ushr(24) and 0xFF).toByte(),
    (ushr(16) and 0xFF).toByte(),
    (ushr(8) and 0xFF).toByte(),
    (ushr(0) and 0xFF).toByte()
)

public fun ByteArray.toUInt(): UInt =
    (this[0].toUInt().and(255u) shl 24) + (this[1].toUInt().and(255u) shl 16) + (this[2].toUInt()
        .and(255u) shl 8) + (this[3].toUInt().and(
        255u
    ) shl 0)

public fun ByteArray.toUShort(): UShort =
    ((this[0].toUInt().and(255u) shl 8) + (this[1].toUInt().and(255u) shl 0)).toUShort()

public fun ByteArray.toInt(): Int =
    (this[0].toInt().and(255) shl 24) + (this[1].toInt().and(255) shl 16) + (this[2].toInt()
        .and(255) shl 8) + (this[3].toInt().and(
        255
    ) shl 0)