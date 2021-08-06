package moe.ore.core.bot

import moe.ore.api.Ore
import moe.ore.core.helper.DataManager
import moe.ore.core.helper.sendJcePacket
import moe.ore.core.net.packet.PacketSender.Companion.sync
import moe.ore.protobuf.Protobuf
import moe.ore.protobuf.decodeProtobuf
import moe.ore.tars.TarsBase
import moe.ore.util.TarsUtil

abstract class ContractJcePacketFactory<T>( // this is resp
    private val commandName: String
) {
    private var timeout = 5 * 1000L

    abstract fun handle(data: ByteArray, seq: Int): Any

    abstract fun request(uin: Long, args: Array<out Any>): TarsBase // send request

    operator fun invoke(ore: Ore, vararg args: Any): Result<T> {
        val sender = ore.sendJcePacket(commandName, request(ore.uin, args), DataManager.manager(ore.uin).session.nextRequestId())
        val from = (sender sync timeout) ?: return Result.failure(RuntimeException("wait packet timeout..."))
        val ret = handle(from.body, from.seq)
        return if(ret is Throwable) {
            Result.failure(ret)
        } else {
            Result.success(ret as T)
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