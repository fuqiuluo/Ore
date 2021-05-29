package moe.ore.util.bytes

import moe.ore.util.BytesUtil
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream

class ByteBuilder(private var position: BytePosition = BytePosition.END) {
    private var outputStream = ByteArrayOutputStream()
    private var dataOutputStream = DataOutputStream(outputStream)

    constructor(bytes: ByteArray, position: BytePosition = BytePosition.END) : this(position) {
        this.dataOutputStream.write(bytes)
        this.dataOutputStream.flush()
    }

    fun writeByte(b : Int) {
        if(position == BytePosition.END) {
            dataOutputStream.write(b)
        } else {
            writeStart(byteArrayOf(b.toByte()))
        }
        dataOutputStream.flush()
    }

    fun writeBoolean(b : Boolean) {
        if(position == BytePosition.END) {
            dataOutputStream.writeBoolean(b)
        } else {
            writeStart(byteArrayOf( if(b) 1 else 0 ))
        }
        dataOutputStream.flush()
    }

    fun writeShort(i: Int) {
        if(position == BytePosition.END) {
            dataOutputStream.writeShort(i)
        } else {
            writeStart(BytesUtil.int16ToBuf(i))
        }
        dataOutputStream.flush()
    }

    fun writeInt(i: Int) {
        if(position == BytePosition.END) {
            dataOutputStream.writeInt(i)
        } else {
            writeStart(BytesUtil.int32ToBuf(i))
        }
        dataOutputStream.flush()
    }

    fun writeLong(i: Long) {
        if(position == BytePosition.END) {
            dataOutputStream.writeLong(i)
        } else {
            writeStart(BytesUtil.int64ToBuf(i))
        }
        dataOutputStream.flush()
    }

    fun writeULong(i: Long) {
        if(position == BytePosition.END) {
            dataOutputStream.writeInt(i.toInt())
        } else {
            writeStart(BytesUtil.int64ToBuf32(i))
        }
        dataOutputStream.flush()
    }

    fun writeFloat(n: Float) {
        if(position == BytePosition.END) {
            dataOutputStream.writeFloat(n)
        } else {
            writeStart(BytesUtil.float2byte(n))
        }
        dataOutputStream.flush()
    }

    fun writeDouble(n: Double) {
        if(position == BytePosition.END) {
            dataOutputStream.writeDouble(n)
        } else {
            writeStart(BytesUtil.doubleToByte(n))
        }
        dataOutputStream.flush()
    }

    fun writeBytes(bytes : ByteArray) {
        if(position == BytePosition.END) {
            dataOutputStream.write(bytes)
        } else {
            writeStart(bytes)
        }
        dataOutputStream.flush()
    }

    fun writeString(string: String) {
        writeBytes(string.toByteArray())
    }

    fun writeBytesWithShortSize(bytes: ByteArray, add : Int = 0) {
        writeShort(bytes.size + add)
        writeBytes(bytes)
    }

    fun writeBytesWithSize(bytes: ByteArray, add : Int = 0) {
        writeInt(bytes.size + add)
        writeBytes(bytes)
    }

    fun writeStringWithShortSize(string: String, add : Int = 0) {
        writeBytesWithShortSize(string.toByteArray(), add)
    }

    fun writeStringWithSize(string: String, add : Int = 0) {
        writeBytesWithSize(string.toByteArray(), add)
    }

    fun writeHex(string: String) {
        writeBytes(string.hex2ByteArray())
    }

    fun writeToStart(block: ByteBuilder.() -> Unit) {
        val oldPosition = position
        this.position = BytePosition.START
        this.block()
        this.position = oldPosition
        dataOutputStream.flush()
    }

    fun writeToEnd(block: ByteBuilder.() -> Unit) {
        val oldPosition = position
        this.position = BytePosition.END
        this.block()
        this.position = oldPosition
        dataOutputStream.flush()
    }

    /**
     * 转字节组
     * @return ByteArray
     */
    fun toByteArray() : ByteArray {
        return outputStream.toByteArray()
    }

    /**
     * 写到开头
     * @param bytes ByteArray
     */
    private fun writeStart(bytes: ByteArray) {
        val newOutputStream = ByteArrayOutputStream()
        val newDataOutputStream = DataOutputStream(newOutputStream)
        newDataOutputStream.write(bytes)
        newDataOutputStream.write(outputStream.toByteArray())
        this.dataOutputStream.close()
        this.outputStream.close()
        this.dataOutputStream = newDataOutputStream
        this.outputStream = newOutputStream
    }

    /**
     * 获取大小
     * @return Int
     */
    fun size() : Int {
        return outputStream.size()
    }

    /**
     * 关闭流释放资源
     */
    fun close() {
        dataOutputStream.close()
        outputStream.close()
    }
}