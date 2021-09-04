package moe.ore.helper

import moe.ore.util.BytesUtil
import moe.ore.util.IpUtil

fun Short.toByteArray(): ByteArray = BytesUtil.int16ToBuf(this.toInt())

fun Int.toByteArray(): ByteArray = BytesUtil.int32ToBuf(this)

fun Long.toByteArray(): ByteArray = BytesUtil.int64ToBuf(this)

fun Int.toHexString(): String = Integer.toHexString(this)

fun Int.intToIp(reverse: Boolean = false): String = if(reverse) IpUtil.int_to_ip(this.toLong()) else IpUtil.getNumConvertIp(this.toLong())

fun Long.longToIp(reverse: Boolean = false): String = if(reverse) IpUtil.int_to_ip(this) else IpUtil.getNumConvertIp(this)