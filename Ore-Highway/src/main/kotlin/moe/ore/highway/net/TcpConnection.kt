package moe.ore.highway.net

import moe.ore.helper.closeQuietly
import moe.ore.highway.protobuf.highway.ReqDataHighwayHead
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
        client.sendBufferSize = 524288
        writeChannel = DataOutputStream(client.getOutputStream())
        readChannel = DataInputStream(client.getInputStream())
    }

    fun sendHw(head: ReqDataHighwayHead, body: ByteArray) {
        if(this::writeChannel.isInitialized) {
            writeChannel.writeBytes("(")
            val headBody = head.toByteArray()
            writeChannel.writeInt(headBody.size)
            // println("head:" + headBody.toHexString())
            writeChannel.writeInt(body.size)
            writeChannel.write(headBody)
            writeChannel.write(body)
            writeChannel.writeBytes(")")
            writeChannel.flush()
        }
    }

    fun readHw() {
        if(this::readChannel.isInitialized) {
            if(readChannel.readChar() == '(') {
                val headSize = readChannel.readInt()
                val dataSize = readChannel.readInt()

                val head = ByteArray(headSize).also { readChannel.readFully(it) }
                val data = ByteArray(dataSize).also { readChannel.readFully(it) }

                if(readChannel.readChar() == ')') {

                }
            }
        }
    }

    override fun close() {
        writeChannel.closeQuietly()
        readChannel.closeQuietly()
        client.closeQuietly()
    }
}
