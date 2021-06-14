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

package moe.ore.core.protocol

import moe.ore.helper.hex2ByteArray

/**
 * ECDH固定 非常规协议操作 跳过ECDH
 * 所以可以固定ECDH
 */

const val ECDH_VERSION = 2.toByte()

val ECDH_PUBLIC_KEY =
    "04803E9940F3FD8E6474C3CC6994A2F972B1DA6BFDE8DDB4E775E36AB4E439DB8EA7A0E6CAFC722089F4921DFEAEFBA0F56932F3E6AA3ECF81154FD230AF32B18F".hex2ByteArray()

val ECDH_SHARE_KEY = "3F539B2549AB1F71421F2C3A66298D05".hex2ByteArray()