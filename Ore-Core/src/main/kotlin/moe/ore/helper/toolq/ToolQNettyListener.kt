package moe.ore.helper.toolq

import moe.ore.ToolQ
import moe.ore.helper.bytes.ByteBuilder
import moe.ore.helper.bytes.ByteReader
import moe.ore.helper.bytes.toHexString
import moe.ore.helper.netty.NettyListener
import moe.ore.helper.toolq.packet.Packet
import moe.ore.helper.toolq.packet.Waiter
import moe.ore.helper.vector.SafeVector
import moe.ore.util.TeaUtil
import moe.ore.util.ZipUtil
import moe.ore.virtual.Android

class ToolQNettyListener(private val toolQ: ToolQ) : NettyListener {
    private val waiterList = SafeVector<Waiter>()

    override fun receive(body: ByteArray) {
        try {
            receiveEvent(body)
        } catch (e : Exception) {
            e.printStackTrace()
        }
    }

    private fun receiveEvent(body: ByteArray) {
        // println("Receive: " + body.toHexString())
        var reader = ByteReader(body)
        val teaKey = when(toolQ.status) {
            ToolQStatus.Login, ToolQStatus.Online -> toolQ.msfRecorder.getKey(KeyType.D2Key)
            ToolQStatus.NotLogin -> ByteArray(16)
        }
        val keyType = reader.readUInt()
        val packetType = reader.readByte()
        if(packetType.toInt() == 0) {
            /**
             * 心跳包专属Type
             * */
            reader.close()
            return
        }
        reader.dis(1)
        val uinStr = reader.readString(reader.readUInt() - 4)
        if(uinStr != toolQ.account.uinString()) {
            reader.close()
            return
        }
        val restBytes = reader.readRestBytes()
        reader.close()
        reader = ByteReader(TeaUtil.decrypt(restBytes, teaKey))
        val headReader = reader.readReader(reader.readUInt() - 4)
        val bodyReader = reader.readReader(reader.readUInt() - 4)
        reader.close()
        if(bodyReader.size > 0) {
            val msfSsoSeq = headReader.readUInt()
            if(headReader.readUInt() != 0) {
                headReader.dis(headReader.readUInt() - 4)
                // val token = headReader.readBytesByInt(4)
            } else {
                headReader.dis(4)
            }
            val cmd = headReader.readString(headReader.readUInt() - 4)
            val randSession = headReader.readBytes(headReader.readUInt() - 4)
            val packet = when(headReader.readUInt()) {
                0, 4 -> bodyReader.readRestBytes()
                1 -> ZipUtil.uncompress(bodyReader.readRestBytes())
                else -> throw RuntimeException("Error compile type")
            }
            headReader.close()
            bodyReader.close()

            println("Receive a packet called $cmd, seq = $msfSsoSeq")

            handlePacket(Packet(
                cmd = cmd,
                seq = msfSsoSeq,
                body = packet
            ))
        }
    }

    private fun handlePacket(packet: Packet) {
        var i = 0
        val size = waiterList.size
        while (i < size) {
            val waiter = waiterList[i]
            if(waiter.isUsed) {
                removeWaiter(waiter)
            } else if(!waiter.isOnce && waiter.cmd == packet.cmd) {
                waiter.check(packet)
            } else if(waiter.check(packet)) {
                waiter.isUsed = true
                waiter.packet = packet
                waiter.push()
                removeWaiter(waiter)
            }
            i++
        }
    }

    fun addWaiter(cmd : String, seq : Int) : Waiter {
        val waiter = Waiter(
            cmd = cmd,
            seq = seq,
            isOnce = true
        )
        return addWaiter(waiter)
    }

    fun addWaiter(waiter: Waiter) : Waiter {
        waiterList.add(waiter)
        return waiter
    }

    fun removeWaiter(waiter: Waiter) = waiterList.remove(waiter)

    override fun heartbeat(): ByteArray {
        toolQ.threadManager.addTask { toolQ.listener?.heartbeatEvent() }

        val android = Android.android

        val builder = ByteBuilder()
        builder.writeInt(0xa)
        builder.writeByte(0)
        builder.writeStringWithSize("", 4)
        builder.writeByte(0)
        if(toolQ.status == ToolQStatus.Online) {
            builder.writeStringWithSize(toolQ.account.uinString(), 4)
        } else {
            builder.writeStringWithSize("", 4)
        }

        val headBuilder = ByteBuilder()
        headBuilder.writeInt(toolQ.msfRecorder.nextSsoSeq())
        headBuilder.writeInt(toolQ.protocolInfo.msfAppId())
        headBuilder.writeInt(toolQ.protocolInfo.msfAppId())
        headBuilder.writeInt(16777216)
        headBuilder.writeInt(0)
        headBuilder.writeInt(256)
        headBuilder.writeBytesWithSize(byteArrayOf(), 4)
        headBuilder.writeStringWithSize("Heartbeat.Alive", 4)
        headBuilder.writeBytesWithSize(byteArrayOf(), 4)
        headBuilder.writeStringWithSize(android.imei.ifEmpty { android.androidId }, 4)
        headBuilder.writeBytesWithSize(android.ksid, 4)
        headBuilder.writeStringWithShortSize(toolQ.protocolInfo.protocolDetail(), 2)
        headBuilder.writeStringWithSize("", 4)

        builder.writeBytesWithSize(headBuilder.toByteArray(), 4)

        builder.writeInt(4)

        builder.writeToStart {
            writeInt(4 + size())
        }

        return builder.toByteArray()
    }

    override fun channelActive() {
        /**
         * 这里是在nio的线程里面哦
         */
        toolQ.listener?.serverConnectionSuccessEvent()
    }

    override fun channelInactive() {
        toolQ.listener?.serverDisconnectionEvent()
    }
}