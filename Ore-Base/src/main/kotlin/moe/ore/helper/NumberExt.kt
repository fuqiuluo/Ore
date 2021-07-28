package moe.ore.helper

import moe.ore.util.BytesUtil
import moe.ore.util.IpUtil

fun Short.toByteArray(): ByteArray = BytesUtil.int16ToBuf(this.toInt())

fun Int.toByteArray(): ByteArray = BytesUtil.int32ToBuf(this)

fun Long.toByteArray(): ByteArray = BytesUtil.int64ToBuf(this)

fun Int.toHexString(): String = Integer.toHexString(this)

fun Int.intToIp(): String = IpUtil.int_to_ip(this.toLong())

fun Long.longToIp(): String = IpUtil.int_to_ip(this)