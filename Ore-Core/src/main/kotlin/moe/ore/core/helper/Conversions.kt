package moe.ore.core.helper

import moe.ore.util.BytesUtil

fun Short.toByteArray(): ByteArray = BytesUtil.int16ToBuf(this.toInt())

fun Int.toByteArray(): ByteArray = BytesUtil.int32ToBuf(this)

fun Long.toByteArray(): ByteArray = BytesUtil.int64ToBuf(this)

fun ByteArray.toInt() = BytesUtil.bufToInt32(this, 0)

fun ByteArray.toShort(): Short = BytesUtil.bufToInt16(this, 0).toShort()
