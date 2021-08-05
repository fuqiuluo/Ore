@file:JvmName("OreCode")
package moe.ore.msg.msg

import kotlinx.io.core.readUInt
import moe.ore.helper.ifNotNull
import moe.ore.helper.toByteReadPacket
import moe.ore.msg.protocol.protobuf.RichText

class Text(
    var src: String = ""
): Msg() {
    override fun from(code: Code) {
        if(code.key != MSG_TEXT) error("the code is not text")
        this.src = code.values["src"]!!
    }

    override fun toCode(): Code = Code(MSG_TEXT, mapOf(
        "src" to src
    ))
}

class At(
    var uin: Long = 0
): Msg() {
    override fun from(code: Code) {
        if(code.key != MSG_AT) error("the code is not text")
        this.uin = (code.values["uin"] ?: code.values["code"])!!.toLong()
    }

    override fun toCode(): Code = Code(
        MSG_AT, mapOf(
        "uin" to uin.toString()
    ))
}

abstract class Msg {
    abstract fun from(code: Code)

    internal abstract fun toCode(): Code

    override fun toString() = toCode().toString()
}

internal fun RichText.toMsg(): String {
    val builder = CodeBuilder()
    if(ptt != null) {
        TODO("voice message not support")
    }
    for (elem in this.elems!!) {
        elem.text.ifNotNull {
            if(it.attr6Buf.isNotEmpty()) {
                val pat = it.attr6Buf.toByteReadPacket()
                val startPos = pat.readShort()
                val strLen = pat.readInt()
                val flag = pat.readByte()
                val uin = pat.readUInt().toLong()
                builder.add(At(uin))
            } else {
                builder.add(Text(it.str))
            }
        }


    }
    return builder.toString()
}
