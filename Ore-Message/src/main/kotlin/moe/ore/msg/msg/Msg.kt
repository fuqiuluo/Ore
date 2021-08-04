@file:JvmName("OreCode")
package moe.ore.msg.msg

import moe.ore.msg.protocol.protobuf.RichText

class Text(
    var src: String
): Msg() {
    override fun from(code: Code) {
        if(code.key != MSG_TEXT) error("the code is not text")
        this.src = code.values["src"]!!
    }

    override fun toCode(): Code = Code(MSG_TEXT, mapOf(
        "src" to src
    ))
}

abstract class Msg {
    abstract fun from(code: Code)

    abstract fun toCode(): Code

    override fun toString() = toCode().toString()
}

internal fun RichText.toCode(): String {
    val builder = StringBuilder()
    this.ptt?.let {

    }
    return builder.toString()
}

fun main() {
    println(Text("你好").toString())
}
