package moe.ore.msg.code

internal val MSG_CODE_PARSERS = mapOf(
    MSG_TEXT to MsgCodeParser { Text(it["src"]!!) },
    MSG_AT to MsgCodeParser { At( if(it.containsKey("all") && it["all"].toBoolean()) 0 else (it["code"] ?: it["uin"])!!.toLong() ) },
    MSG_FACE to MsgCodeParser { Face(it["id"]!!.toInt()) },

)

class Face(
    val id: Int
): BaseCode(MSG_FACE, mapOf("id" to id.toString()))

class At(
    val code: Long
): BaseCode(MSG_AT, mapOf("code" to code.toString())) {
    val all: Boolean = code == 0L
}

class Text(
    val src: String
): BaseCode(MSG_TEXT, mapOf("src" to src)) {
    override fun toString() = OreCode.encodeText(src)
}

/**
 * a parser interface
 */
fun interface MsgCodeParser {
    operator fun invoke(kv: Map<String, String>): BaseCode
}