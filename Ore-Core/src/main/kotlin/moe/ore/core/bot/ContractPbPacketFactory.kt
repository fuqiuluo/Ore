package moe.ore.core.bot

import moe.ore.api.Ore
import moe.ore.core.helper.sendOidbPacket
import moe.ore.core.helper.sendPacket
import moe.ore.core.net.packet.OidbSSOPkg
import moe.ore.core.net.packet.PacketSender.Companion.sync
import moe.ore.protobuf.Protobuf
import moe.ore.protobuf.decodeProtobuf
import moe.ore.tars.TarsBase
import moe.ore.util.TarsUtil

abstract class ContractPbPacketFactory<T>( // this is resp
    private val commandName: String,
    private val needResp: Boolean = true
    // private val isOidb: Boolean = false 出现特殊的oidb包，无法完全兼容
) {
    private var timeout = 5 * 1000L

    abstract fun handle(data: ByteArray, seq: Int): Any

    abstract fun request(uin: Long, vararg args: Any): Protobuf<*> // send request

    operator fun invoke(ore: Ore, vararg args: Any): Result<T> {
        val sender = request(ore.uin, *args).let {
            if (it is OidbSSOPkg) {
                ore.sendOidbPacket(commandName, it)
            } else ore.sendPacket(commandName, it.toByteArray())
        }
        if (needResp) {
            val from = (sender sync timeout) ?: return Result.failure(RuntimeException("wait packet timeout..."))
            val ret = handle(from.body, from.seq)
            return if(ret is Throwable) {
                Result.failure(ret)
            } else {
                Result.success(ret as T)
            }
        } else {
            sender.call(null, timeout)
            return Result.failure(RuntimeException("the packet not need resp"))
        }
    }

    fun config(timeout: Long) {
        this.timeout = timeout
    }

    fun <R: TarsBase> decodeUniPacket(data: ByteArray, resp: R): R {
        return TarsUtil.decodeRequest(resp, data)
    }

    inline fun <reified R: Protobuf<*>> decodePbPacket(data: ByteArray): R {
        return decodeProtobuf(data)
    }
}