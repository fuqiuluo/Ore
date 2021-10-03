package moe.ore.core.protocol.wlogin.request

import moe.ore.core.net.packet.PacketType
import moe.ore.helper.newBuilder
import moe.ore.helper.toByteArray
import moe.ore.helper.writeBytes

/*
tlv 544 sign =
9 --> 810_9
10/11 --> 810_a
13 --> 810_d

810_x
分析 810是commandId, x是subCmd

8字节 qq号
2字节 guid size
16字节 guid
2字节 sdk version size (6.0.0.2475)
10字节 sdk version
4字节 sub cmd
 */

class WtLoginPassword(uin : Long) : WtRequest(uin, CMD_LOGIN, 0x810, 9, 0x87) {
    override fun packetType(): PacketType = PacketType.LoginPacket

    override fun makeTlv(seq : Int): ByteArray = newBuilder().apply {
        writeShort(26)
        writeBytes(tlv.t18())
        writeBytes(tlv.t1())
        writeBytes(tlv.t106())
        writeBytes(tlv.t116())
        writeBytes(tlv.t100())
        writeBytes(tlv.t107())
        writeBytes(tlv.t108())
        writeBytes(tlv.t144())
        writeBytes(tlv.t142())
        writeBytes(tlv.t145())
        writeBytes(tlv.t147())
        writeBytes(tlv.t154(seq))
        writeBytes(tlv.t16b())
        // 非常规协议操作，易导致环境异常
        // 该TLV作用未知
        writeBytes(tlv.t141())
        writeBytes(tlv.t8())
        writeBytes(tlv.t511())
        writeBytes(tlv.t187())

        // writeBytes(tlv.t400())
        // 无randSeed与noPicSig
        writeBytes(tlv.t188())

        writeBytes(tlv.t191())
        // t191 提交可以回退协议 使得可以在原本设备异常的环境下登录
        // t191 QQ8.7.5开始 t191不会再发送
        writeBytes(tlv.t202())
        writeBytes(tlv.t194())
        // 可有可无的TLV 对协议本身无影响
        writeBytes(tlv.t177())
        writeBytes(tlv.t516())
        writeBytes(tlv.t521())
        writeBytes(tlv.t525())
        writeBytes(tlv.t544(subCmd))
        // writeBytes(tlv.t545())
        // 不需要
    }.toByteArray()

}