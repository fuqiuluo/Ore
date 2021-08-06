package moe.ore.msg.code

import java.util.*

open class BaseCode(
    val name: String,
    val values: Map<String, String>
) {
    override fun toString() = StringJoiner(CODE_PS, CODE_START, CODE_END).apply {
        add("$CODE_HEAD:$name")
        values.forEach { (t, u) -> add(t + CODE_KV + OreCode.encodeParams(u)) }
    }.toString()
}