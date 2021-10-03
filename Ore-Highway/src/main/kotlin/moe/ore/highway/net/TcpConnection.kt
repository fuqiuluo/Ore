package moe.ore.highway.net

import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import moe.ore.helper.*
import moe.ore.highway.protobuf.highway.ReqDataHighwayHead
import moe.ore.highway.protobuf.highway.RspDataHighwayHead
import java.io.Closeable
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

internal class TcpConnection: Closeable {
    lateinit var client: Socket
    lateinit var writeChannel: DataOutputStream
    lateinit var readChannel: DataInputStream

    fun connect(host: String, port: Int) {
        client = Socket(host, port)
        client.keepAlive = true
        client.tcpNoDelay = true
        // client.sendBufferSize = 524288

        writeChannel = DataOutputStream(client.getOutputStream())
        readChannel = DataInputStream(client.getInputStream())
    }

    fun sendPacket(head: ReqDataHighwayHead, body: ByteArray) {
        if(this::writeChannel.isInitialized && !client.isClosed) {

            val builder = newBuilder()
            builder.writeString("(")

            val headBody = head.toByteArray()
            builder.writeInt(headBody.size)
            // println("head:" + headBody.toHexString())
            builder.writeInt(body.size)
            builder.writeBytes(headBody)
            builder.writeBytes(body)
            builder.writeString(")")

            val arr = builder.toByteArray()

            writeChannel.write(arr)

            // println("send: ${arr.toHexString()}")

            writeChannel.flush()

            builder.closeQuietly()
        }
    }

    fun readPacket(): Pair<RspDataHighwayHead, ByteArray>? {
        if(this::readChannel.isInitialized && !client.isClosed) {
            if(readChannel.readByte() == 0x28.toByte()) {
                val headSize = readChannel.readInt()
                val dataSize = readChannel.readInt()
                val headBuf = ByteArray(headSize).also { readChannel.readFully(it) }
                val dataBuf = ByteArray(dataSize).also { readChannel.readFully(it) }
                readChannel.readByte() // 读取‘)’

                val head = ProtoBuf.decodeFromByteArray<RspDataHighwayHead>(headBuf)

                // println(head)
                // println(String(dataBuf))
                return head to dataBuf
            }
        }
        return null
    }

    override fun close() {
        writeChannel.closeQuietly()
        readChannel.closeQuietly()
        client.closeQuietly()
    }
}
