package moe.ore.helper

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.protobuf.ProtoBuf

fun <T> SerializationStrategy<T>.encode(value : T) : ByteArray {
    return ProtoBuf.encodeToByteArray(this, value)
}

fun <T> DeserializationStrategy<T>.decode(value : ByteArray) : T {
    return ProtoBuf.decodeFromByteArray(this, value)
}