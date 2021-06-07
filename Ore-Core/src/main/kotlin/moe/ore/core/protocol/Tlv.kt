package moe.ore.core.protocol

import moe.ore.core.DataManager
import moe.ore.util.bytes.ByteBuilder

class Tlv(uin : ULong) {
    val dataManager = DataManager.manager(uin)

    /**
     * 协议信息
     */
    val protocolInfo = dataManager.protocolInfo

    fun t1() = buildTlv(0x1) {
        writeBoolean(true)
    }


    private fun buildTlv(ver : Int, block : ByteBuilder.() -> Unit) : ByteArray {
        val builder = TlvBuilder(ver)
        builder.write(block)
        return builder.toByteArray()
    }

    class TlvBuilder(private val ver : Int) {
        private val bodyBuilder = ByteBuilder()

        fun write(block: ByteBuilder.() -> Unit) {
            this.bodyBuilder.block()
        }

        fun toByteArray() : ByteArray {
            val out = bodyBuilder.toByteArray()
            // bodyBuilder.close()
            // 基于内存的流，无需释放
            val builder = ByteBuilder()
            builder.writeShort(ver)
            builder.writeShort(out.size)
            builder.writeBytes(out)
            return builder.toByteArray()
        }

    }

    companion object {
        @JvmStatic
        fun main(args : Array<String>) {
        }
    }
}