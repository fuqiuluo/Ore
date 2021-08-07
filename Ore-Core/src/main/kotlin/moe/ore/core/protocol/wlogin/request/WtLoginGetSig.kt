package moe.ore.core.protocol.wlogin.request

import moe.ore.core.net.packet.PacketType
import moe.ore.core.protocol.buildTlv
import moe.ore.core.protocol.wlogin.EncryptMethod
import moe.ore.helper.newBuilder
import moe.ore.helper.toByteArray
import moe.ore.helper.writeBytes

class WtLoginGetSig(uin: Long) : WtRequest(uin, CMD_EXCHANGE_EMP, 0x810, 15, 0x45, EncryptMethod.EM_ST)  {
    override fun makeTlv(seq: Int): ByteArray = newBuilder().apply {
        writeShort(23)
        writeBytes(tlv.t18())
        writeBytes(tlv.t1())
        writeBytes(buildTlv(0x106) { writeBytes(userStSig.encryptA1.ticket()) })
        writeBytes(tlv.t116())
        writeBytes(tlv.t100(
            appId = 2,
            mainSigMap = 0x21010e0
        ))
        // 逆向分析这里的appid不是2/1
        writeBytes(tlv.t107())
        // writeBytes(tlv.t108())
        writeBytes(tlv.t144(userStSig.gtKey.ticket()))
        writeBytes(tlv.t142())
        // writeBytes(tlv.t112())
        writeBytes(tlv.t145())
        // writeBytes(tlv.t166())

        writeBytes(tlv.t16a(userStSig.noPicSig.ticket()))

        // writeBytes(tlv.t154(seq))
        writeBytes(tlv.t141())
        writeBytes(tlv.t8())
        writeBytes(tlv.t511())
        writeBytes(tlv.t147())
        // writeBytes(tlv.t172())
        // 传递回滚参数 无需发送
        writeBytes(tlv.t177())
        writeBytes(tlv.t400(userStSig.G))

        // writeBytes(tlv.t401())
        writeBytes(tlv.t187())
        writeBytes(tlv.t188())
        writeBytes(tlv.t194())
        // writeBytes(tlv.t201())
        // 发不出来 参数待分析
        writeBytes(tlv.t202())
        writeBytes(tlv.t516())
        writeBytes(tlv.t521())
        writeBytes(tlv.t525(userStSig.extraDataList))
    }.toByteArray()

    override fun packetType(): PacketType = PacketType.ExChangeEmpA1
}