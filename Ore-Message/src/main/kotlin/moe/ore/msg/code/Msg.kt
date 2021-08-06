package moe.ore.msg.code

internal val MSG_CODE_PARSERS = mapOf(
    MSG_TEXT to MsgCodeParser { Text(it["src"]!!) },
    MSG_AT to MsgCodeParser { At( (it["code"] ?: it["uin"])!!.toLong() ) },

)

class At(
    val code: Long
): BaseCode(MSG_AT, mapOf("code" to code.toString()))

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