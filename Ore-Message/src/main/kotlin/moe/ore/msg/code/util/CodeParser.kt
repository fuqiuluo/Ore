package moe.ore.msg.code.util

import moe.ore.msg.code.*
import java.util.*

class CodeParser(private val codeMsg: String) {
    private val codeBlock: List<String> by lazy { splitCode(codeMsg) }

    /**
     * 解析多个
     */
    fun toCodes(): OreCode {
        val code = OreCode()
        codeBlock.forEach { msg ->
            if(msg.startsWith(CODE_START) && msg.endsWith(CODE_END)) {
                code.add(parseCode(msg))
            } else {
                code.add(
                    Text(
                    OreCode.decodeText(msg)
                )
                )
            }
        }
        return code
    }

    /**
     * 解析单个
     */
    private fun parseCode(codeMsg: String): BaseCode {
        val kvs = getKV(codeMsg)

        val name = kvs.first()
        val kv = kvs.slice(1 until kvs.size).associate { parseKV(it) }

        return (MSG_CODE_PARSERS[name] ?: error("not support type: $name"))(kv)
    }

    companion object {
        @JvmStatic
        private fun parseKV(kv: String): Pair<String, String> {
            val kvs = kv.split(CODE_KV)
            if(kvs.size == 2) {
                return kvs[0] to OreCode.decodeParams(kvs[1])
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
            msg.forEach { c ->
                when (c) {
                    CODE_START[0] -> {
                        if(isInCode) {
                            error("found code_start, but found another code_start")
                        } else {
                            list.add(builder.toString())
                            builder = StringBuilder()
                            isInCode = true
                            builder.append(c)
                        }
                    }
                    CODE_END[0] -> {
                        if(isInCode) {
                            isInCode = false
                            builder.append(c)
                            list.add(builder.toString())
                            builder = StringBuilder()
                        } else {
                            error("not found code_start, but found code_end")
                        }
                    }
                    else -> {
                        builder.append(c)
                    }
                }
            }
            if(isInCode) error("not found code_end") else list.add(builder.toString())
            return list.filter { it.isNotEmpty() }
        }
    }
}
