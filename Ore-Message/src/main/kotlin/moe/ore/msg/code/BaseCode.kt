package moe.ore.msg.code

import java.util.*

abstract class BaseCode(
    val type: String,
    val values: Map<String, String>
) {
    // internal abstract fun toElemMsg(): Elem

    internal open fun isActionMsg() = false

    override fun toString() = StringJoiner(CODE_PS, CODE_START, CODE_END).apply {
        add("$CODE_HEAD:$type")
        values.forEach { (t, u) ->
            if(u.isNotEmpty()) add(t + CODE_KV + OreCode.encodeParams(u))
        }
    }.toString()
}