package moe.ore.core.protocol.pb.ad

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import moe.ore.core.OreBot
import moe.ore.core.helper.sendPacket
import moe.ore.core.net.packet.PacketType
import moe.ore.helper.encode

/**
 * 腾讯广告服务 获取广告封面url
 */
@Serializable
class PubArtCenter {
    companion object {
        fun getUrlByVid(oreBot: OreBot, vid: String,
                        mv: String,
                        netType: Int = 1,
                        videoType: Int = 0) {
            val result = kotlin.runCatching {
                val req = GetUrlByVidRequest()
                req.vid = vid
                req.mv = mv
                req.netType = netType
                req.videoType = videoType
                req.mobileDetailInfo = "{\"CpuFrequency\":1762,\"CpuNum\":8,\"OsInfo\":30,\"DeviceModel\":\"M2002J9E\",\"DeviceOS\":\"11\",\"ManufactureInfo\":\"Xiaomi\",\"Sdkversion\":30,\"QQVersion\":\"8.8.3\",\"TotalRam\":5694078976,\"AvailRam\":1664561152,\"TotalRom\":109609,\"AvailRom\":47449,\"platform\":1,\"CpuModel\":\"QualcommTechnologies,IncSM7250\",\"CpuProducer\":\"CPU_OTHER\",\"simCardType\":0,\"HWCodecLevel\":\"6.1\",\"Bandwith\":0}"
                val buf = GetUrlByVidRequest.serializer().encode(req)
                oreBot.sendPacket("PubAccountArticleCenter.GetUrlByVid", buf, PacketType.ServicePacket).call()
                true
            }
            println(result)
        }
    }

    @Serializable
    internal class GetUrlByVidRequest {
        @ProtoNumber(number = 1) @JvmField var vid: String = ""

        @ProtoNumber(number = 2) @JvmField var mv: String = ""

        @ProtoNumber(number = 3) @JvmField var netType: Int = 0

        @ProtoNumber(number = 5) @JvmField var videoType: Int = 0

        @ProtoNumber(number = 9) @JvmField var mobileDetailInfo: String = ""
    }

}