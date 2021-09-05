package moe.ore.msg.code

internal val MSG_CODE_PARSERS = mapOf(
    MSG_TEXT to MsgCodeParser { Text(it["src"]!!) },
    MSG_AT to MsgCodeParser { At( if(it.containsKey("all") && it["all"].toBoolean()) 0 else (it["qq"] ?: it["code"])!!.toLong() ) },
    MSG_FACE to MsgCodeParser { Face(it["id"]!!.toInt()) },
    MSG_SUPER_FACE to MsgCodeParser { SuperFace(it["id"]!!.toInt(), it.getOrDefault("name", "")) },
    MSG_IMAGE to MsgCodeParser { Image(it["file"]!!) },
    MSG_FLASH_IMAGE to MsgCodeParser { FlashImage(it["file"]!!) }
)

class FlashImage(
    val file: String
): BaseCode(MSG_FLASH_IMAGE, mapOf("file" to file))

class Image(
    val file: String
): BaseCode(MSG_IMAGE, mapOf("file" to file))

class Face(
    val id: Int
): BaseCode(MSG_FACE, mapOf("id" to id.toString()))

class SuperFace(
    val id: Int,
    val name: String
): BaseCode(MSG_SUPER_FACE, mapOf(
    "id" to id.toString(),
    "name" to name
))

class At(
    val qq: Long
): BaseCode(MSG_AT, mapOf("qq" to qq.toString())) {
    val all: Boolean = qq == 0L
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