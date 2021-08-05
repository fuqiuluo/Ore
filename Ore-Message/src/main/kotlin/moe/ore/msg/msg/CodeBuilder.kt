package moe.ore.msg.msg

import java.util.*

internal class CodeBuilder {
    private val codes: ArrayList<Code> = arrayListOf()

    fun get() = codes

    fun add(code: Code) {
        codes.add(code)
    }

    fun add(msg: Msg) {
        codes.add(msg.toCode())
    }

    fun add(key: String, values: Map<String, String>) {
        codes.add(Code(key, values))
    }

    fun add(msg: String) {
        val vs = splitCode(msg)
        vs.forEach {
            if(it.startsWith("[") && it.endsWith("]")) {
                val kv = getKV(it)
                val key = kv.first()
                val valueMap = hashMapOf<String, String>()
                kv.forEachIndexed { index, s ->
                    if (index != 0) parseKV(s).apply { valueMap[first] = second }
                }
                add(key, valueMap)
            } else {
                add(Text(src = OreMsg.decodeText(it)))
            }
        }
    }

    /**
    fun add(key: String, values: String) {
    val valueMap = hashMapOf<String, String>()
    }**/

    fun size() = codes.size

    fun merge(builder: CodeBuilder) {
        codes.addAll(builder.codes)
    }

    fun clear() {
        codes.clear()
    }

    override fun toString(): String = codes.joinToString("") { code ->
        if (code.key != MSG_TEXT) code.toString() else OreMsg.encodeText( Text().also { it.from(code) }.src )
    }

    companion object {
        @JvmStatic
        private fun parseKV(kv: String): Pair<String, String> {
            val kvs = kv.split(CODE_KV)
            if(kvs.size == 2) {
                return kvs[0] to OreMsg.decodeParams(kvs[1])
            }
            error("wrong code, because kvs")
        }

        @JvmStatic
        private fun getKV(codeString: String): List<String> {
            val list = arrayListOf<String>()
            val tokenizer = StringTokenizer(codeString.substring(1 + CODE_HEAD.length + 1, codeString.length - 1), CODE_PS)
            while (tokenizer.hasMoreTokens())
                list.add(tokenizer.nextToken())
            return list
        }

        @JvmStatic
        private fun splitCode(msg: String): List<String> {
            val list = arrayListOf<String>()
            var isInCode = false
            var builder = StringBuilder()
            for (c in msg.toCharArray()) {
                when (c) {
                    '[' -> {
                        if(isInCode) {
                            error("wrong ore code.")
                        } else {
                            list.add(builder.toString())
                            builder = StringBuilder()
                            isInCode = true
                            builder.append(c)
                        }
                    }
                    ']' -> {
                        if(isInCode) {
                            isInCode = false
                            builder.append(c)
                            list.add(builder.toString())
                            builder = StringBuilder()
                        } else {
                            error("wrong ore code.")
                        }
                    }
                    else -> {
                        builder.append(c)
                    }
                }
            }
            if(isInCode) error("wrong ore code.") else list.add(builder.toString())
            return list.filter { it.isNotEmpty() }
        }
    }
}
