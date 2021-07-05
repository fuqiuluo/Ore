package moe.ore.core.protocol.pb.safe

import kotlinx.serialization.Serializable
import moe.ore.core.bot.DeviceInfo
import moe.ore.core.helper.encodeProtobuf
import moe.ore.core.protocol.ProtocolInternal
import moe.ore.helper.*

/**
 * 上传安全日志
 */
@Serializable
object SafeReport {
    @JvmStatic
    fun encode(device: DeviceInfo, protocol: ProtocolInternal): ByteArray {
        val safe = SafeReportReq()
        safe.logItemReportData.add(
            LogItem(
                rptId = 12,
                rptData = newBuilder().apply {
                    arrayOf(
                        device.guid.toHexString(),
                        device.androidVersion,
                        protocol.packageVersion,
                        "2.3.0",
                        "2.3.3",
                        "armeabi-v7a",
                        device.model,
                        device.brand,
                        0,
                        null,
                        null,
                        null,
                        20,
                        null,
                        null,
                        null,
                        null,
                        null
                    ).forEachIndexed { index, o ->
                        writeString(if (index == 0) o.toString() else ("," + o.toString()))
                    }
                    writeString(",")
                }.toByteArray()
            )
        )
        return encodeProtobuf(safe)
    }
}

@Serializable
class SafeReportReq {
    val logItemReportData : ArrayList<LogItem> = arrayListOf()
}

@Serializable
data class LogItem (
    var rptId : Int = 0,
    var rptData : ByteArray = EMPTY_BYTE_ARRAY
) {
    /**
     * rptId
     * 12 上报设备信息
     */

    /**
     * rptData 合成分析
     *
     * GUID,androidVersion,QQVersion,unknownVer,unknownVer,cpu,model,brand,0,null,null,null,20,null,null,null,null,null
     */

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LogItem) return false

        if (rptId != other.rptId) return false
        if (!rptData.contentEquals(other.rptData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rptId
        result = 31 * result + rptData.contentHashCode()
        return result
    }
}
