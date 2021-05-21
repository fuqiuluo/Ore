package moe.ore.protocol.helper

import moe.ore.helper.log.TLog
import moe.ore.tars.TarsStructBase
import moe.ore.tars.UniPacket

object JceHelper {
    private val logger = TLog.getLogger("JceHelper")

    @JvmStatic
    private val cpInfo = mapOf(
        "StatSvc.register" to arrayOf("PushService", "SvcReqRegister", "SvcReqRegister", "SvcRespRegister"),

    )

    @JvmStatic
    fun decodeTarsPacket(byteArray: ByteArray) : UniPacket {
        return UniPacket.decode(byteArray)
    }

    @JvmStatic
    fun <T : TarsStructBase> decodeTarsPacket(cmdName: String, byteArray: ByteArray, t : T) : T {
        val mapName = cpInfo[cmdName]!![3]
        val uni = UniPacket.decode(byteArray)
        return uni.findByClass(mapName, t)
    }

    @JvmStatic
    fun encodeTarsPacket(cmdName : String, requestId : Int, base: TarsStructBase) : ByteArray {
        val cpName = cpInfo[cmdName]
        if(cpName == null) {
            logger.warn("Unable to synthesize the Jce package, code name: " + 1)
            return byteArrayOf()
        }
        val uni = UniPacket()
        uni.requestId = requestId
        uni.funcName = cpName[1]
        uni.servantName = cpName[0]
        uni.put(cpName[2], base)
        return uni.encode()
    }

}