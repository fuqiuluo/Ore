package moe.ore.msg.msg

object OreMsg {
    @JvmStatic
    fun decodeText(str: String): String =
        str.replace("&#91;", "[")
            .replace("&#93;", "]")
            .replace("&#09;", "\t")
            .replace("&#10;", "\r")
            .replace("&#13;", "\n")
            .replace("&amp;", "&")

    @JvmStatic
    fun decodeParams(str: String): String =
        str.replace("&#91;", "[")
            .replace("&#93;", "]")
            .replace("&#44;", ",")
            .replace("&#61;", "=")
            .replace("&#09;", "\t")
            .replace("&#10;", "\r")
            .replace("&#13;", "\n")
            .replace("&amp;", "&")

    @JvmStatic
    fun encodeText(str: String): String =
        str.replace("&", "&amp;")
            .replace("[", "&#91;")
            .replace("]", "&#93;")
            .replace("\t", "&#09;")
            .replace("\r", "&#10;")
            .replace("\n", "&#13;")

    @JvmStatic
    fun encodeParams(str: String): String =
        str.replace("&", "&amp;")
            .replace("[", "&#91;")
            .replace("]", "&#93;")
            .replace("=", "&#61;")
            .replace("\t", "&#09;")
            .replace("\r", "&#10;")
            .replace("\n", "&#13;")
}
