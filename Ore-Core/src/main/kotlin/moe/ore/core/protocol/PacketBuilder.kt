package moe.ore.core.protocol

/**
 * @author 飞翔的企鹅
 * create 2021-06-05 19:10
 */
class PacketBuilder {
    /**
     * com.tencent.qphone.base.util.CodecWarpper#encodeRequest(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, byte[], int, int, java.lang.String, byte, byte, byte, byte[], byte[], boolean)
     */
    fun buildLoginPacket() {}
    fun readLoginPacket() {}

    fun buildSsoPacket() {}
    fun readSsoPacket() {}

    fun buildOicqPacket() {}
    fun readOicqPacket() {}


    fun buildJcePacket() {}
    fun readJcePacket() {}

    fun buildProtoBufPacket() {}
    fun readProtoBufPacket() {}

    fun buildOidb() {}
    fun readOidb() {}

    fun buildUniPacket() {}
    fun readUniPacket() {
        /**
         * 先解析为 [RequestPacket], 即 `UniRequest`, 再按版本解析 map, 再找出指定数据并反序列化
         */
        decodeUniRequestPacketAndDeserialize()
    }

    fun decodeUniRequestPacketAndDeserialize() {
//    val request = this.readJceStruct(RequestPacket.serializer())
//
//    return block(if (name == null) when (request.version?.toInt() ?: 3) {
//        2 -> request.sBuffer.loadAs(RequestDataVersion2.serializer()).map.singleValue().singleValue()
//        3 -> request.sBuffer.loadAs(RequestDataVersion3.serializer()).map.singleValue()
//        else -> error("unsupported version ${request.version}")
//    } else when (request.version?.toInt() ?: 3) {
//        2 -> request.sBuffer.loadAs(RequestDataVersion2.serializer()).map.getOrElse(name) { error("cannot find $name") }
//            .singleValue()
//        3 -> request.sBuffer.loadAs(RequestDataVersion3.serializer()).map.getOrElse(name) { error("cannot find $name") }
//        else -> error("unsupported version ${request.version}")
//    })
    }
}