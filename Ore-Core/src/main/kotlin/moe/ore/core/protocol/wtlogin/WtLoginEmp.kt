package moe.ore.core.protocol.wtlogin

import kotlinx.io.core.BytePacketBuilder
import moe.ore.core.net.packet.PacketType
import moe.ore.core.protocol.buildTlv
import moe.ore.helper.*

/**
 *@author 飞翔的企鹅
 *create 2021-06-16 09:33
 */
class WtLoginEmp(uin: Long) : WtLogin(uin, EXCHANGE_EMP, 0x810, 0x45) {
    init {
        packetType = PacketType.ExChangeEmp
    }

    /**
     * 附加为空
     */
    override fun buildAttach(): BytePacketBuilder = createBuilder()

    override fun publicKey() = userStSig.wtSessionTicket.ticket()

    override fun encryptKey() = userStSig.wtSessionTicketKey.ticket()

    override fun buildTlvBody(seq: Int) = createBuilder().apply {
        writeShort(15)
        writeShort(25)

        writeBytes(tlv.t18())
        writeBytes(tlv.t1())
        writeBytes(buildTlv(0x106) {
            writeBytes(userStSig.encryptA1.ticket())
        })
        writeBytes(tlv.t116())
        writeBytes(tlv.t100(
            appId = 1
        ))
        writeBytes(tlv.t107())
        writeBytes(tlv.t108())
        writeBytes(tlv.t144())
        writeBytes(tlv.t142())
        // writeBytes(tlv.t112())
        writeBytes(tlv.t145())
        // writeBytes(tlv.t166())
        writeBytes(tlv.t16a(userStSig.noPicSig.ticket()))
        writeBytes(tlv.t154(0))
        writeBytes(tlv.t141())
        writeBytes(tlv.t8())
        writeBytes(tlv.t511())
        writeBytes(tlv.t147())
        // writeBytes(tlv.t172())
        writeBytes(tlv.t177())
        writeBytes(tlv.t400(userStSig.G))
        writeBytes(tlv.t187())
        writeBytes(tlv.t188())
        // writeBytes(tlv.t194())
        // writeBytes(tlv.t201())
        // 发不出来 参数待分析
        writeBytes(tlv.t202())
        writeBytes(tlv.t516())
        writeBytes(tlv.t521())
        writeBytes(tlv.t525(userStSig.extraDataList))
        writeBytes(tlv.t544())
        // writeBytes(tlv.t545())
    }.toByteArray()
}
