package moe.ore.protobuf

import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import moe.ore.helper.EMPTY_BYTE_ARRAY

/**
 * 实现一个接口而已啦
 */
interface Protobuf<T : Protobuf<T>> {
    fun toByteArray() : ByteArray {
        return EMPTY_BYTE_ARRAY
    }

    /**
    fun from(data : ByteArray) : T {
        return this as T
    }**/
}

inline fun <reified T: Protobuf<T>> encodeProtobuf(value: T): ByteArray {
    return ProtoBuf.encodeToByteArray(value)
}

inline fun <reified T: Protobuf<T>> decodeProtobuf(data: ByteArray): T {
    return ProtoBuf.decodeFromByteArray(data)
}