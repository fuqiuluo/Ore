package moe.ore.protocol

import moe.ore.ToolQ
import moe.ore.helper.bytes.ByteBuilder
import moe.ore.helper.bytes.ByteReader
import moe.ore.helper.toolq.KeyType
import moe.ore.helper.toolq.LoginType
import moe.ore.helper.toolq.util.PacketUtil
import moe.ore.util.TeaUtil
import moe.ore.virtual.Android

object WtLogin {
    private const val LoginCmdName = "wtlogin.login"
    @JvmStatic
    private val android = Android.android

    @JvmStatic
    fun exchangeEmp(toolQ: ToolQ, loginType: LoginType, seq : Int) : ByteArray {
        /**
         * TODO("咕咕咕吧，下次一定")
         */
        val record = toolQ.msfRecorder

        val tlv = toolQ.tlv
        val tlvBuilder = ByteBuilder()
        tlvBuilder.writeShort(15)
        tlvBuilder.writeShort(25)
        tlvBuilder.writeBytes(tlv.t18())
        tlvBuilder.writeBytes(tlv.t1())
        tlvBuilder.writeBytes(tlv.t106(when(loginType) {
            LoginType.Password -> 1
        }))
        tlvBuilder.writeBytes(tlv.t116())
        tlvBuilder.writeBytes(tlv.t100())
        tlvBuilder.writeBytes(tlv.t107())
        tlvBuilder.writeBytes(tlv.t108())
        tlvBuilder.writeBytes(tlv.t144())
        tlvBuilder.writeBytes(tlv.t142())
        tlvBuilder.writeBytes(tlv.t145())
        tlvBuilder.writeBytes(tlv.t16a())
        tlvBuilder.writeBytes(tlv.t154(seq))
        tlvBuilder.writeBytes(tlv.t141())
        tlvBuilder.writeBytes(tlv.t8())
        tlvBuilder.writeBytes(tlv.t147())
        tlvBuilder.writeBytes(tlv.t177())
        tlvBuilder.writeBytes(tlv.t187())
        tlvBuilder.writeBytes(tlv.t188())
        tlvBuilder.writeBytes(tlv.t202())
        tlvBuilder.writeBytes(tlv.t516())
        tlvBuilder.writeBytes(tlv.t521())
        tlvBuilder.writeBytes(tlv.t525())
        tlvBuilder.writeBytes(tlv.t544())
        tlvBuilder.writeBytes(tlv.t542())

        val data = TeaUtil.encrypt(tlvBuilder.toByteArray(), record.shareKey)

        tlvBuilder.close()

        val builder = ByteBuilder()

        builder.writeShort(8001)
        builder.writeShort(0x810)
        builder.writeShort(1)
        builder.writeULong(toolQ.account.uin)
        builder.writeHex("03 45 00 00 00 00 02 00 00 00 00 00 00 00 00 00 30")
        builder.writeBytes(android.randKey)
        builder.writeShort(305)
        builder.writeShort(2)
        builder.writeBytesWithShortSize(record.publicKey)
        builder.writeBytes(data)
        builder.writeByte(3)
        builder.writeToStart {
            writeShort(size() + 3)
            writeByte(2)
        }

        return PacketUtil.makePacket(0xB, 0x2, "wtlogin.exchange_emp", builder.toByteArray(), toolQ.status, null, record.getKey(KeyType.D2Key), seq, toolQ.account.uin, null, null, null, false)
    }

    @JvmStatic
    fun getStWithPassword(toolQ: ToolQ, loginType: LoginType, seq : Int) : ByteArray {
        val record = toolQ.msfRecorder

        val tlv = toolQ.tlv
        val tlvBuilder = ByteBuilder()
        tlvBuilder.writeShort(9)
        tlvBuilder.writeShort(25)
        tlvBuilder.writeBytes(tlv.t18())
        tlvBuilder.writeBytes(tlv.t1())
        tlvBuilder.writeBytes(tlv.t106(when(loginType) {
            LoginType.Password -> 1
        }))
        tlvBuilder.writeBytes(tlv.t116())
        tlvBuilder.writeBytes(tlv.t100())
        tlvBuilder.writeBytes(tlv.t107())
        tlvBuilder.writeBytes(tlv.t108())
        tlvBuilder.writeBytes(tlv.t142())
        tlvBuilder.writeBytes(tlv.t144())
        tlvBuilder.writeBytes(tlv.t145())
        tlvBuilder.writeBytes(tlv.t147())
        tlvBuilder.writeBytes(tlv.t154(seq))
        // tlvBuilder.writeBytes(tlv.t16b())
        tlvBuilder.writeBytes(tlv.t141())
        tlvBuilder.writeBytes(tlv.t8())
        tlvBuilder.writeBytes(tlv.t511())
        tlvBuilder.writeBytes(tlv.t187())
        tlvBuilder.writeBytes(tlv.t400())
        tlvBuilder.writeBytes(tlv.t188())
        tlvBuilder.writeBytes(tlv.t191())
        tlvBuilder.writeBytes(tlv.t202())
        tlvBuilder.writeBytes(tlv.t177())
        tlvBuilder.writeBytes(tlv.t516())
        tlvBuilder.writeBytes(tlv.t521())
        tlvBuilder.writeBytes(tlv.t525())
        tlvBuilder.writeBytes(tlv.t544())

        val data = TeaUtil.encrypt(tlvBuilder.toByteArray(), record.shareKey)

        tlvBuilder.close()

        val builder = ByteBuilder()

        builder.writeShort(8001)
        builder.writeShort(0x810)
        builder.writeShort(1)
        builder.writeULong(toolQ.account.uin)
        builder.writeHex("03 87 00 00 00 00 02 00 00 00 00 00 00 00 00 02 01")
        builder.writeBytes(android.randKey)
        builder.writeShort(305)
        builder.writeShort(2)
        builder.writeBytesWithShortSize(record.publicKey)
        builder.writeBytes(data)
        builder.writeByte(3)
        builder.writeToStart {
            writeShort(size() + 3)
            writeByte(2)
        }

        return PacketUtil.makeNoLoginPacket(LoginCmdName, builder.toByteArray(), seq, toolQ.account.uin, toolQ.protocolInfo, null, false)
    }

}