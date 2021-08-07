package moe.ore.msg.code

import moe.ore.msg.code.util.CodeParser

open class OreCode: ArrayList<BaseCode>() {
    fun sface(id: Int, name: String): OreCode {
        add(SuperFace(id, name))
        return this
    }

    fun face(id: Int): OreCode {
        add(Face(id))
        return this
    }

    fun at(uin: Long): OreCode {
        add(At(uin))
        return this
    }

    fun text(msg: String): OreCode {
        add(Text(msg))
        return this
    }

    override fun toString(): String = joinToString("") { it.toString() }

    companion object {
        @JvmStatic
        fun parse(msg: String): OreCode {
            return CodeParser(msg).toCodes()
        }

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
}