package moe.ore.protobuf

import moe.ore.helper.runtimeError

/**
 * 实现一个接口而已啦
 */
interface Protobuf<T : Protobuf<T>> {
    fun toByteArray() : ByteArray {
        runtimeError("No 'toByteArray' method is generated")
    }


}