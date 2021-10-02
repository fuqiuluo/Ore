package moe.ore.core.protocol.tars.configpush

import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.tars.TarsBase
import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField

@TarsClass(requireRead = true)
internal class BigDataChannel: TarsBase() {
    @TarsField(id = 0) var bigDataIpList = arrayListOf<BigDataIpList>()

    @TarsField(id = 1) var bigDataSigSession = EMPTY_BYTE_ARRAY // 上传的upkey

    @TarsField(id = 2) var bigDataSigKeySession = EMPTY_BYTE_ARRAY

    @TarsField(id = 3) var sigUin = 0L

    @TarsField(id = 4) var connectFlag = 0

    @TarsField(id = 5) var bigDataPbBuf = EMPTY_BYTE_ARRAY
}

@TarsClass(requireRead = true)
internal class BigDataIpList: TarsBase() {
    @TarsField(id = 0) var serviceType = 0L

    @TarsField(id = 1) var ipList: ArrayList<BigDataIpInfo>? = null

    @TarsField(id = 2) var netSegConfs: ArrayList<NetSegConf>? = null

    @TarsField(id = 3) var fragmentSize = 0L
}

// 逆向的时候长得和个Pb一样，腾讯程序员写错了吧
@TarsClass(requireRead = true)
internal class NetSegConf: TarsBase() {
    @TarsField(id = 0) var type = 0L

    @TarsField(id = 1) var segSize = 0L

    @TarsField(id = 2) var segNum = 0L

    @TarsField(id = 3) var curConnNum = 0L
}

@TarsClass(requireRead = true)
internal class BigDataIpInfo: TarsBase() {
    @TarsField(id = 0) var type = 0L

    @TarsField(id = 1) var ip = ""

    @TarsField(id = 2) var port = 0L
}