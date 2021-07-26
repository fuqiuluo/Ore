package moe.ore.protobuf

import moe.ore.helper.EMPTY_BYTE_ARRAY

/**
 * 实现一个接口而已啦
 */
interface Protobuf<T : Protobuf<T>> {
    fun toByteArray() : ByteArray {
        return EMPTY_BYTE_ARRAY
    }

    fun from(data : ByteArray) : T {
        return this as T
    }
}