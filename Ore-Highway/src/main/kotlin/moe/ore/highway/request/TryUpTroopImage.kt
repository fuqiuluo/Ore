package moe.ore.highway.request

import moe.ore.api.Ore
import moe.ore.core.bot.ContractPbPacketFactory
import moe.ore.highway.protobuf.Cmd0x388

internal object TryUpTroopImage: ContractPbPacketFactory<Cmd0x388.RspBody>("ImgStore.GroupPicUp") {
    override fun handle(data: ByteArray, seq: Int): Cmd0x388.RspBody {
        return decodePbPacket(data)
    }

    override fun request(uin: Long, args: Array<out Any>): Cmd0x388.ReqBody {
        val troopCode = args[0] as Long
        val fileMd5 = args[1] as ByteArray
        val fileName = args[2] as String
        val fileSize = args[3] as Long
        val apkBuildVer = args[4] as String

        return Cmd0x388.ReqBody(
            /**
             * nettype有加密
             *
             * wifi 3
             * wap 5
             * 未知 6
             */
            netType = 3u,
            subCmd = 1u,
            msgTryUpImg = arrayListOf(Cmd0x388.TryUpImgReq(
                groupCode = troopCode,
                srcUin = uin,
                fileId = 0,
                fileMd5 = fileMd5,
                fileSize = fileSize,
                fileName = fileName,
                srcTerm = 5,
                platformType = 9,
                buType = 1,
                1920, 890,
                picType = 1000,
                buildVer = apkBuildVer, // can use 'unkownVersion'
                appPicType = 1052,
                srvUpload = 0,
                originalPic = 1,
            ))
        )
    }

    operator fun invoke(ore: Ore, troopCode: Long, fileMd5: ByteArray, fileName: String, fileSize: Long, apkBuildVer: String) = super.invoke(ore, troopCode, fileMd5, fileName, fileSize, apkBuildVer)
}