package moe.ore.core.protocol.tars.configpush

import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.tars.TarsBase
import moe.ore.tars.TarsClass
import moe.ore.tars.TarsField

@TarsClass(requireRead = true)
internal class FileStoragePushFSSvcList : TarsBase() {
    @TarsField(id = 0) var uploadList = arrayListOf<FileStorageServerListInfo>()

    @TarsField(id = 1) var picDownloadList = arrayListOf<FileStorageServerListInfo>()

    @TarsField(id = 2) var gPicDownloadList = arrayListOf<FileStorageServerListInfo>() // 腾讯群聊图片下载服务器

    @TarsField(id = 3) var qzoneProxyServiceList = arrayListOf<FileStorageServerListInfo>()

    @TarsField(id = 4) var urlEncodeServiceList = arrayListOf<FileStorageServerListInfo>()

    @TarsField(id = 5) var bigDataChannel : BigDataChannel? = null

    @TarsField(id = 6) var vipEmotionList = arrayListOf<FileStorageServerListInfo>()

    @TarsField(id = 7) var c2cPicDownloadList = arrayListOf<FileStorageServerListInfo>() // 腾讯c2c图片下载服务器

    @TarsField(id = 8) var fmtIPInfo : FmtIPInfo? = null

    @TarsField(id = 9) var domainIpChannel : DomainIpChannel? = null

    @TarsField(id = 10) var pttList = EMPTY_BYTE_ARRAY
}

@TarsClass(requireRead = true)
internal class FmtIPInfo: TarsBase() {
    @TarsField(id = 0) var gateIp = ""

    @TarsField(id = 1) var gateIpOper = 0L
}

@TarsClass(requireRead = true)
class FileStorageServerListInfo : TarsBase() {
    @TarsField(id = 1) var ip = ""

    @TarsField(id = 2) var port = 80
}
