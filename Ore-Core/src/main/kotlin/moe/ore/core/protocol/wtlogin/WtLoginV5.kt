package moe.ore.core.protocol.wtlogin

import moe.ore.core.helper.DataManager
import moe.ore.core.protocol.Tlv
import moe.ore.helper.*

/**
 *@author 飞翔的企鹅
 *create 2021-06-16 09:33
 */
class WtLoginV5(var uin: Long) {

    fun buildRequestTgtgtNopicsigPacket(seq: Int): ByteArray {
        val manager = DataManager.manager(uin)
        val tlv = Tlv(uin)


        writeShort(15)//subCommand
        writeShort(24)// tlv count
        writeBytes(tlv.t18())
        writeBytes(tlv.t1())
        writeShort(0x106)
        val encryptA1 = manager.wLoginSigInfo.enA1
//        writeShort(encryptA1.size)
        writeBytesAndShotLen(encryptA1)
        writeBytes(tlv.t116()) // 116 D2
        writeBytes(tlv.t100())// 100 D2
        writeBytes(tlv.t107())
        writeBytes(tlv.t108())
        writeBytes(tlv.t142())
        writeBytes(tlv.t144())
        writeBytes(tlv.t145())
        writeBytes(tlv.t16a(manager.wLoginSigInfo.noPicSig))
        writeBytes(tlv.t154(seq))
        writeBytes(tlv.t141())
        writeBytes(tlv.t8())
        writeBytes(tlv.t511())
        writeBytes(tlv.t147())
        writeBytes(tlv.t177())
        writeBytes(tlv.t400(manager.wLoginSigInfo.sigInfo2))
        writeBytes(tlv.t187())
        writeBytes(tlv.t188())
        writeBytes(tlv.t194())
        writeBytes(tlv.t202())
        writeBytes(tlv.t516())
        writeBytes(tlv.t521())
        writeBytes(tlv.t525())
        //    packet := packets.BuildUniPacket(c.Uin, seq, "wtlogin.exchange_emp", 2, c.OutGoingPacketSessionId, []byte{}, make([]byte, 16), req)

    }


    fun buildRequestChangeSigPacket(seq: Int): ByteArray {
        val tlv = Tlv(uin)

        writeShort(11)
        writeShort(17)
        writeBytes(tlv.t100())
        writeBytes(tlv.t10a())
        writeBytes(tlv.t116())
        writeBytes(tlv.t108())
        writeBytes(tlv.t144())
        writeBytes(tlv.t143())
        writeBytes(tlv.t142())
        writeBytes(tlv.t154())
        writeBytes(tlv.t18())
        writeBytes(tlv.t141())
        writeBytes(tlv.t8())
        writeBytes(tlv.t147())
        writeBytes(tlv.t177())
        writeBytes(tlv.t187())
        writeBytes(tlv.t188())
        writeBytes(tlv.t194())
        writeBytes(tlv.t511())

        //sso := packets.BuildSsoPacket(seq, c.version.AppId, c.version.SubAppId, "wtlogin.exchange_emp", SystemDeviceInfo.IMEI, c.sigInfo.tgt, c.OutGoingPacketSessionId, req, c.ksid)
        //packet := packets.BuildLoginPacket(c.Uin, 2, make([]byte, 16), sso, []byte{})
    }

}