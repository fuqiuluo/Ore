package moe.ore.helper

import moe.ore.util.BytesUtil

fun Short.toByteArray(): ByteArray = BytesUtil.int16ToBuf(this.toInt())

fun Int.toByteArray(): ByteArray = BytesUtil.int32ToBuf(this)

fun Long.toByteArray(): ByteArray = BytesUtil.int64ToBuf(this)

fun Int.toHexString(): String = Integer.toHexString(this)
