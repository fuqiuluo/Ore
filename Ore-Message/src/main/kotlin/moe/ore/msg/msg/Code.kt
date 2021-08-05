package moe.ore.msg.msg

import java.util.*

data class Code(
    val key: String,
    val values: Map<String, String> = mapOf()
) {
    // constructor(key: String, values: Map<String, Any>) : this(key, values.mapValues { it.value.toString() })

    override fun toString() = StringJoiner(CODE_PS, CODE_START, CODE_END).apply {
        add("$CODE_HEAD:$key")
        values.forEach { (t, u) -> add(t + CODE_KV + OreMsg.encodeParams(u)) } // encode
    }.toString()
}