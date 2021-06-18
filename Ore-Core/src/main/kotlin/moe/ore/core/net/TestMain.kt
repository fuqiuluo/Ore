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

package moe.ore.core.net

import moe.ore.helper.hex2ByteArray

object TestMain {
    @Throws(InterruptedException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val t546 = "0102010200010000008074eaadc4365db11f6b510e66c936f2aefc7db054c6943535bc70fe15b7ad4f5c2db9a4630e11824e09eeb1e4b7ea993b05aff64ad57a1f9263838fe173c61064bdccad0c0dda679152180d87c86c52729d97af0725510582cd78c5e97a226e331e4b6a81c974c4235632051df17aa6a6d385f28efe0707b7f16a317e74bc4b1a00202ce752e5efc2c156bcd0a7dade94e629568cdcadb6ba4578de6d86d53b37e24a00ac0102010200010000008074eaadc4365db11f6b510e66c936f2aefc7db054c6943535bc70fe15b7ad4f5c2db9a4630e11824e09eeb1e4b7ea993b05aff64ad57a1f9263838fe173c61064bdccad0c0dda679152180d87c86c52729d97af0725510582cd78c5e97a226e331e4b6a81c974c4235632051df17aa6a6d385f28efe0707b7f16a317e74bc4b1a00202ce752e5efc2c156bcd0a7dade94e629568cdcadb6ba4578de6d86d53b37e24a".hex2ByteArray()


    }

}

