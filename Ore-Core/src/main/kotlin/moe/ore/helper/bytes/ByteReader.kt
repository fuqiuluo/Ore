package moe.ore.helper.bytes

import java.io.ByteArrayInputStream
import java.io.DataInputStream

class ByteReader(val source : ByteArray) {
    val size = source.size
    private val inputStream = ByteArrayInputStream(source)
    private val dataInputStream = DataInputStream(inputStream)

    fun readString(len: Int) = String(readBytes(len))

    fun readRestBytes() = readBytes(available())

    fun readReader(len: Int) = ByteReader(readBytes(len))

    fun readBytes(length : Int) : ByteArray {
        val out = ByteArray(length)
        dataInputStream.readFully(out)
        return out
    }

    fun readByte() : Byte {
        return dataInputStream.readByte()
    }

    fun readBoolean() : Boolean {
        return dataInputStream.readBoolean()
    }

    fun readShort() : Int {
        return this.dataInputStream.readUnsignedShort()
    }

    fun readUInt(): Int {
        val ch1 = this.dataInputStream.readByte().toInt()
        val ch2 = this.dataInputStream.readByte().toInt()
        val ch3 = this.dataInputStream.readByte().toInt()
        val ch4 = this.dataInputStream.readByte().toInt()
        return ((ch1 shl 24) and -16777216) +
                ((ch2 shl 16) and 16711680) +
                ((ch3 shl 8) and 65280) +
                (ch4 and 255)
    }

    fun readInt() : Int {
        return this.dataInputStream.readInt()
    }

    fun readLong() : Long {
        return dataInputStream.readLong()
    }

    fun readULong() : Long {
        return dataInputStream.readInt().toLong()
    }

    fun dis(len : Int) {
        dataInputStream.skipBytes(len)
    }

    fun available() = dataInputStream.available()

    fun close() {
        this.dataInputStream.close()
    }
}