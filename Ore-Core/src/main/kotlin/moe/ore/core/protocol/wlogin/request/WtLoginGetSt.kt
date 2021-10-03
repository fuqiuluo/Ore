package moe.ore.core.protocol.wlogin.request

import moe.ore.core.net.packet.PacketType
import moe.ore.helper.newBuilder
import moe.ore.helper.toByteArray
import moe.ore.helper.writeBytes
import moe.ore.util.MD5

// 810_a
class WtLoginGetSt(uin: Long) : WtRequest(uin, CMD_EXCHANGE_EMP, 0x810, 11, 0x7)  {
    override fun packetType(): PacketType = PacketType.ExChangeEmpSt

    override var secondToken: ByteArray? = userStSig.tgt.ticket()

    override fun makeTlv(seq: Int): ByteArray = newBuilder().apply {
        writeShort(18)
        writeBytes(tlv.t100())
        writeBytes(tlv.t10a(userStSig.tgt.ticket()))
        writeBytes(tlv.t116())
        writeBytes(tlv.t108())
        writeBytes(tlv.t144( MD5.toMD5Byte(userStSig.d2Key.ticket()) ))
        writeBytes(tlv.t143(userStSig.d2.ticket()))
        writeBytes(tlv.t142())
        writeBytes(tlv.t154(seq))
        writeBytes(tlv.t18())
        writeBytes(tlv.t141())
        writeBytes(tlv.t8())
        writeBytes(tlv.t147())
        writeBytes(tlv.t177())
        writeBytes(tlv.t187())
        writeBytes(tlv.t188())
        writeBytes(tlv.t194())

        val domains = arrayOf(
            "tenpay.com",
            "openmobile.qq.com",
            "docs.qq.com",
            "connect.qq.com",
            "qzone.qq.com",
            "vip.qq.com",
            "qun.qq.com",
            "game.qq.com",
            "qqweb.qq.com",
            "office.qq.com",
            "ti.qq.com",
            "mail.qq.com",
            "qzone.com",
            "mma.qq.com"
        )

        writeBytes(tlv.t511(domains))
        writeBytes(tlv.t202())
    }.toByteArray()

}