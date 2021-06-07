package moe.ore.core.protocol

import moe.ore.core.DataManager
import moe.ore.util.bytes.ByteBuilder

class Tlv(val uin: ULong) {

    val dataManager = DataManager.manager(uin)

    /**
     * 协议信息
     */
    val protocolInfo = ProtocolInternal[dataManager.protocolInfo]

    fun t1() = buildTlv(0x1) {
        writeBoolean(true)
    }


    private fun buildTlv(tlvVer: Int, block: ByteBuilder.() -> Unit): ByteArray {
        val bodyBuilder = ByteBuilder()
        val out = ByteBuilder()
        bodyBuilder.block()
        out.writeShort(tlvVer)
        out.writeShort(bodyBuilder.size())
        out.writeBytes(bodyBuilder.toByteArray())
        return out.toByteArray()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
        }
    }
}