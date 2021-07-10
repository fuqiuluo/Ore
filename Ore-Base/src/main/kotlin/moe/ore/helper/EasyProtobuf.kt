package moe.ore.helper

import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.protobuf.ProtoBuf

fun <T> SerializationStrategy<T>.encode(value : T) : ByteArray {
    return ProtoBuf.encodeToByteArray(this, value)
}