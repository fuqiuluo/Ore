@file:Suppress("PLUGIN_IS_NOT_ENABLED", "EXPERIMENTAL_API_USAGE", "ArrayInDataClass")
package moe.ore.highway.protobuf

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.protobuf.Protobuf

class Cmd0x388 {
    @Serializable
    data class RspBody(
        @ProtoNumber(1) @JvmField var clientIp: UInt = 0u,
        @ProtoNumber(2) @JvmField var subCmd: UInt = 0u,
        @ProtoNumber(3) @JvmField var msgTryUpImgRsp: ArrayList<TryUpImgRsp>? = null,
        @ProtoNumber(4) @JvmField var rpt_msg_getimg_url_rsp: ArrayList<GetImgUrlRsp>? = null,
        @ProtoNumber(5) @JvmField var rpt_msg_tryup_ptt_rsp: ArrayList<TryUpPttRsp>? = null,
        @ProtoNumber(6) @JvmField var rpt_msg_getptt_url_rsp: ArrayList<GetPttUrlRsp>? = null,
        @ProtoNumber(7) @JvmField var rpt_msg_del_img_rsp: ArrayList<DelImgRsp>? = null,
    ): Protobuf<RspBody>

    @Serializable
    data class DelImgRsp(
        @ProtoNumber(1) @JvmField var uint32_result: UInt = 0u,
        @ProtoNumber(2) @JvmField var bytes_fail_msg: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(3) @JvmField var bytes_file_resid: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<DelImgRsp>

    @Serializable
    data class GetPttUrlRsp(
        @ProtoNumber(1) @JvmField var uint64_fileid: ULong = 0u,
        @ProtoNumber(2) @JvmField var bytes_file_md5: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(3) @JvmField var uint32_result: UInt = 0u,
        @ProtoNumber(4) @JvmField var bytes_fail_msg: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(5) @JvmField var rpt_bytes_down_url: List<String>? = null,
        @ProtoNumber(6) @JvmField var rpt_uint32_down_ip: List<Int>? = null,
        @ProtoNumber(7) @JvmField var rpt_uint32_down_port: List<Int>? = null,
        @ProtoNumber(8) @JvmField var bytes_down_domain: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(9) @JvmField var bytes_down_para: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(10) @JvmField var uint64_file_id: ULong = 0u,
        @ProtoNumber(11) @JvmField var uint32_transfer_type: UInt = 0u,
        @ProtoNumber(12) @JvmField var uint32_allow_retry: UInt = 0u,
        @ProtoNumber(26) @JvmField var rpt_msg_down_ip6: ArrayList<IPv6Info>? = null,
        @ProtoNumber(27) @JvmField var bytes_client_ip6: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(28) @JvmField var rpt_str_domain: String = "",
    ): Protobuf<GetPttUrlRsp>

    @Serializable
    data class TryUpPttRsp(
        @ProtoNumber(1) @JvmField var uint64_file_id: ULong = 0u,
        @ProtoNumber(2) @JvmField var uint32_result: UInt = 0u,
        @ProtoNumber(3) @JvmField var bytes_fail_msg: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(4) @JvmField var bool_file_exit: Boolean = false,
        @ProtoNumber(5) @JvmField var rpt_uint32_up_ip: List<Int>? = null,
        @ProtoNumber(6) @JvmField var rpt_uint32_up_port: List<Int>? = null,
        @ProtoNumber(7) @JvmField var bytes_up_ukey: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(8) @JvmField var uint64_fileid: ULong = 0u,
        @ProtoNumber(9) @JvmField var uint64_up_offset: ULong = 0u,
        @ProtoNumber(10) @JvmField var uint64_block_size: ULong = 0u,
        @ProtoNumber(11) @JvmField var bytes_file_key: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(12) @JvmField var uint32_channel_type: UInt = 0u,
        @ProtoNumber(26) @JvmField var rpt_msg_up_ip6: ArrayList<IPv6Info>? = null,
        @ProtoNumber(27) @JvmField var bytes_client_ip6: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<TryUpPttRsp>

    @Serializable
    data class GetImgUrlRsp(
        @ProtoNumber(1) @JvmField var uint64_fileid: ULong = 0u,
        @ProtoNumber(2) @JvmField var bytes_file_md5: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(3) @JvmField var uint32_result: UInt = 0u,
        @ProtoNumber(4) @JvmField var bytes_fail_msg: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(5) @JvmField var msg_img_info: ImgInfo? = null,
        @ProtoNumber(6) @JvmField var rpt_bytes_thumb_down_url: List<String>? = null,
        @ProtoNumber(7) @JvmField var rpt_bytes_original_down_url: List<String>? = null,
        @ProtoNumber(8) @JvmField var rpt_bytes_big_down_url: List<String>? = null,
        @ProtoNumber(9) @JvmField var rpt_uint32_down_ip: List<Int>? = null,
        @ProtoNumber(10) @JvmField var rpt_uint32_down_port: List<Int>? = null,
        @ProtoNumber(11) @JvmField var bytes_down_domain: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(12) @JvmField var bytes_thumb_down_para: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(13) @JvmField var bytes_original_down_para: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(14) @JvmField var bytes_big_down_para: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(15) @JvmField var uint64_file_id: ULong = 0u,
        @ProtoNumber(16) @JvmField var uint32_auto_down_type: UInt = 0u,
        @ProtoNumber(17) @JvmField var rpt_uint32_order_down_type: List<Int>? = null,
        @ProtoNumber(19) @JvmField var bytes_big_thumb_down_para: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(20) @JvmField var uint32_https_url_flag: UInt = 0u,
        @ProtoNumber(26) @JvmField var rpt_msg_down_ip6: ArrayList<IPv6Info>? = null,
        @ProtoNumber(27) @JvmField var bytes_client_ip6: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<GetImgUrlRsp>

    @Serializable
    data class TryUpImgRsp(
        @ProtoNumber(1) @JvmField var extFileId: ULong = 0u, // 没有实际作用
        @ProtoNumber(2) @JvmField var result: UInt = 0u,
        @ProtoNumber(3) @JvmField var faiMsg: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(4) @JvmField var fileExist: Boolean = false,
        @ProtoNumber(5) @JvmField var msgImgInfo: ImgInfo? = null, // 里面只是一堆垃圾
        @ProtoNumber(6) @JvmField var upIp: ArrayList<Long>? = null,
        @ProtoNumber(7) @JvmField var upPort: ArrayList<Int>? = null,
        @ProtoNumber(8) @JvmField var ukey: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(9) @JvmField var fileId: ULong = 0u,
        @ProtoNumber(10) @JvmField var upOffset: ULong = 0u,
        @ProtoNumber(11) @JvmField var blockSize: ULong = 0u,
        @ProtoNumber(12) @JvmField var bool_new_big_chan: Boolean = false,
        @ProtoNumber(26) @JvmField var rpt_msg_up_ip6: ArrayList<IPv6Info>? = null,
        @ProtoNumber(27) @JvmField var bytes_client_ip6: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(1001) @JvmField var msg_info4busi: TryUpInfo4Busi? = null,
    ): Protobuf<TryUpImgRsp>

    @Serializable
    data class TryUpInfo4Busi(
        @ProtoNumber(1) @JvmField var bytes_down_domain: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(2) @JvmField var bytes_thumb_down_url: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(3) @JvmField var bytes_original_down_url: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(4) @JvmField var bytes_big_down_url: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(5) @JvmField var bytes_file_resid: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<TryUpInfo4Busi>

    @Serializable
    data class IPv6Info(
        @ProtoNumber(1) @JvmField var bytes_ip6: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(2) @JvmField var uint32_port: UInt = 0u,
    ): Protobuf<IPv6Info>

    @Serializable
    data class ImgInfo(
        @ProtoNumber(1) @JvmField var bytes_file_md5: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(2) @JvmField var uint32_file_type: UInt = 0u,
        @ProtoNumber(3) @JvmField var uint64_file_size: ULong = 0u,
        @ProtoNumber(4) @JvmField var uint32_file_width: UInt = 0u,
        @ProtoNumber(5) @JvmField var uint32_file_height: UInt = 0u,
    ): Protobuf<ImgInfo>

    @Serializable
    internal data class ReqBody(
        @ProtoNumber(1) @JvmField var netType: UInt = 0u,
        @ProtoNumber(2) @JvmField var subCmd: UInt = 0u,
        @ProtoNumber(3) @JvmField var msgTryUpImg: ArrayList<TryUpImgReq>? = null,
        @ProtoNumber(4) @JvmField var rpt_msg_getimg_url_req: ArrayList<GetImgUrlReq>? = null,
        @ProtoNumber(5) @JvmField var rpt_msg_tryup_ptt_req: ArrayList<TryUpPttReq>? = null,
        @ProtoNumber(6) @JvmField var rpt_msg_getptt_url_req: ArrayList<GetPttUrlReq>? = null,
        @ProtoNumber(7) @JvmField var commandId: UInt = 0u,
        @ProtoNumber(8) @JvmField var rpt_msg_del_img_req: ArrayList<DelImgReq>? = null,
        @ProtoNumber(1001) @JvmField var bytes_extension: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<ReqBody>

    @Serializable
    internal data class TryUpImgReq(
        @ProtoNumber(1) @JvmField var groupCode: Long = 0,
        @ProtoNumber(2) @JvmField var srcUin: Long = 0,
        @ProtoNumber(3) @JvmField var fileId: Long? = null,
        @ProtoNumber(4) @JvmField var fileMd5: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(5) @JvmField var fileSize: Long = 0,
        @ProtoNumber(6) @JvmField var fileName: String = "",
        @ProtoNumber(7) @JvmField var srcTerm: Int = 0,
        @ProtoNumber(8) @JvmField var platformType: Int = 0,
        @ProtoNumber(9) @JvmField var buType: Int = 0,
        @ProtoNumber(10) @JvmField var picWidth: Int = 0,
        @ProtoNumber(11) @JvmField var picHeight: Int = 0,
        @ProtoNumber(12) @JvmField var picType: Int = 0,
        @ProtoNumber(13) @JvmField var buildVer: String = "",
        @ProtoNumber(14) @JvmField var innerIp: Int = 0,
        @ProtoNumber(15) @JvmField var appPicType: Int = 0,
        @ProtoNumber(16) @JvmField var originalPic: Int = 0,
        @ProtoNumber(17) @JvmField var fileIndex: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(18) @JvmField var dstUin: Long = 0,
        @ProtoNumber(19) @JvmField var srvUpload: Int? = null,
        @ProtoNumber(20) @JvmField var transferUrl: ByteArray = EMPTY_BYTE_ARRAY,
    ): Protobuf<TryUpImgReq>

    @Serializable
    internal data class GetImgUrlReq(
        @ProtoNumber(1) @JvmField var uint64_group_code: ULong = 0u,
        @ProtoNumber(2) @JvmField var uint64_dst_uin: ULong = 0u,
        @ProtoNumber(3) @JvmField var uint64_fileid: ULong = 0u,
        @ProtoNumber(4) @JvmField var bytes_file_md5: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(5) @JvmField var uint32_url_flag: UInt = 0u,
        @ProtoNumber(6) @JvmField var uint32_url_type: UInt = 0u,
        @ProtoNumber(7) @JvmField var uint32_req_term: UInt = 0u,
        @ProtoNumber(8) @JvmField var uint32_req_platform_type: UInt = 0u,
        @ProtoNumber(9) @JvmField var uint32_inner_ip: UInt = 0u,
        @ProtoNumber(10) @JvmField var uint32_bu_type: UInt = 0u,
        @ProtoNumber(11) @JvmField var bytes_build_ver: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(12) @JvmField var uint64_file_id: ULong = 0u,
        @ProtoNumber(13) @JvmField var uint64_file_size: ULong = 0u,
        @ProtoNumber(14) @JvmField var uint32_original_pic: UInt = 0u,
        @ProtoNumber(15) @JvmField var uint32_retry_req: UInt = 0u,
        @ProtoNumber(16) @JvmField var uint32_file_height: UInt = 0u,
        @ProtoNumber(17) @JvmField var uint32_file_width: UInt = 0u,
        @ProtoNumber(18) @JvmField var uint32_pic_type: UInt = 0u,
        @ProtoNumber(19) @JvmField var uint32_pic_up_timestamp: UInt = 0u,
        @ProtoNumber(20) @JvmField var uint32_req_transfer_type: UInt = 0u,
    ): Protobuf<GetImgUrlReq>

    @Serializable
    internal data class TryUpPttReq(
        @ProtoNumber(1) @JvmField var uint64_group_code: ULong = 0u,
        @ProtoNumber(2) @JvmField var uint64_src_uin: ULong = 0u,
        @ProtoNumber(3) @JvmField var uint64_file_id: ULong = 0u,
        @ProtoNumber(4) @JvmField var bytes_file_md5: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(5) @JvmField var uint64_file_size: ULong = 0u,
        @ProtoNumber(6) @JvmField var bytes_file_name: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(7) @JvmField var uint32_src_term: UInt = 0u,
        @ProtoNumber(8) @JvmField var uint32_platform_type: UInt = 0u,
        @ProtoNumber(9) @JvmField var uint32_bu_type: UInt = 0u,
        @ProtoNumber(10) @JvmField var bytes_build_ver: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(11) @JvmField var uint32_inner_ip: UInt = 0u,
        @ProtoNumber(12) @JvmField var uint32_voice_length: UInt = 0u,
        @ProtoNumber(13) @JvmField var bool_new_up_chan: Boolean = false,
        @ProtoNumber(14) @JvmField var uint32_codec: UInt = 0u,
        @ProtoNumber(15) @JvmField var uint32_voice_type: UInt = 0u,
        @ProtoNumber(16) @JvmField var uint32_bu_id: UInt = 0u,
    ): Protobuf<TryUpPttReq>

    @Serializable
    internal data class GetPttUrlReq(
        @ProtoNumber(1) @JvmField var uint64_group_code: ULong = 0u,
        @ProtoNumber(2) @JvmField var uint64_dst_uin: ULong = 0u,
        @ProtoNumber(3) @JvmField var uint64_fileid: ULong = 0u,
        @ProtoNumber(4) @JvmField var bytes_file_md5: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(5) @JvmField var uint32_req_term: UInt = 0u,
        @ProtoNumber(6) @JvmField var uint32_req_platform_type: UInt = 0u,
        @ProtoNumber(7) @JvmField var uint32_inner_ip: UInt = 0u,
        @ProtoNumber(8) @JvmField var uint32_bu_type: UInt = 0u,
        @ProtoNumber(9) @JvmField var bytes_build_ver: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(10) @JvmField var uint64_file_id: ULong = 0u,
        @ProtoNumber(11) @JvmField var bytes_file_key: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(12) @JvmField var uint32_codec: UInt = 0u,
        @ProtoNumber(13) @JvmField var uint32_bu_id: UInt = 0u,
        @ProtoNumber(14) @JvmField var uint32_req_transfer_type: UInt = 0u,
        @ProtoNumber(15) @JvmField var uint32_is_auto: UInt = 0u,
    ): Protobuf<GetPttUrlReq>

    @Serializable
    internal data class DelImgReq(
        @ProtoNumber(1) @JvmField var uint64_src_uin: ULong = 0u,
        @ProtoNumber(2) @JvmField var uint64_dst_uin: ULong = 0u,
        @ProtoNumber(3) @JvmField var uint32_req_term: UInt = 0u,
        @ProtoNumber(4) @JvmField var uint32_req_platform_type: UInt = 0u,
        @ProtoNumber(5) @JvmField var uint32_bu_type: UInt = 0u,
        @ProtoNumber(6) @JvmField var bytes_build_ver: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(7) @JvmField var bytes_file_resid: ByteArray = EMPTY_BYTE_ARRAY,
        @ProtoNumber(8) @JvmField var uint32_pic_width: UInt = 0u,
        @ProtoNumber(9) @JvmField var uint32_pic_height: UInt = 0u,
    ): Protobuf<DelImgReq>
}
