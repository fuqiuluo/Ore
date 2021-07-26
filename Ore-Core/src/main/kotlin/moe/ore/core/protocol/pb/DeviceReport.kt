/*
 * English :
 *  The project is protected by the MPL open source agreement.
 * Open source agreement warning that prohibits deletion of project source code files.
 * The project is prohibited from acting in illegal areas.
 * All illegal activities arising from the use of this project are the responsibility of the second author, and the original author of the project is not responsible
 *
 *  中文：
 *  该项目由MPL开源协议保护。
 *  禁止删除项目源代码文件的开源协议警告内容。
 * 禁止使用该项目在非法领域行事。
 * 使用该项目产生的违法行为，由使用者或第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 *  オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 *
 */

package moe.ore.core.protocol.pb

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.protobuf.ProtoNumber
import moe.ore.helper.EMPTY_BYTE_ARRAY
import moe.ore.protobuf.Protobuf

@Serializable
 class DeviceReport() : Protobuf<DeviceReport> {
    constructor(
       bootloader: ByteArray,
       version: ByteArray,
       codename: ByteArray,
       incremental: ByteArray,
       fingerprint: ByteArray,
       bootId: ByteArray,
       androidId: ByteArray,
       baseband: ByteArray,
       innerVer: ByteArray
    ) : this() {
       this.bootloader = bootloader
       this.version = version
       this.codename = codename
       this.incremental = incremental
       this.fingerprint = fingerprint
       this.bootId = bootId
       this.androidId = androidId
       this.baseband = baseband
       this.innerVer = innerVer
    }

    @ProtoNumber(number = 1) @JvmField var bootloader: ByteArray = EMPTY_BYTE_ARRAY
    @ProtoNumber(number = 2) @JvmField var version: ByteArray = EMPTY_BYTE_ARRAY
    @ProtoNumber(number = 3) @JvmField var codename: ByteArray = EMPTY_BYTE_ARRAY
    @ProtoNumber(number = 4) @JvmField var incremental: ByteArray = EMPTY_BYTE_ARRAY
    @ProtoNumber(number = 5) @JvmField var fingerprint: ByteArray = EMPTY_BYTE_ARRAY
    @ProtoNumber(number = 6) @JvmField var bootId: ByteArray = EMPTY_BYTE_ARRAY
    @ProtoNumber(number = 7) @JvmField var androidId: ByteArray = EMPTY_BYTE_ARRAY
    @ProtoNumber(number = 8) @JvmField var baseband: ByteArray = EMPTY_BYTE_ARRAY
    @ProtoNumber(number = 9) @JvmField var innerVer: ByteArray = EMPTY_BYTE_ARRAY
 }