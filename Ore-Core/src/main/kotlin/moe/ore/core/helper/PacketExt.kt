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

package moe.ore.core.helper

import kotlinx.io.core.discardExact
import moe.ore.helper.readByteReadPacket
import moe.ore.helper.readString
import moe.ore.helper.reader
import moe.ore.util.TeaUtil
import okhttp3.internal.closeQuietly

inline fun ByteArray.readPacket(uin: Long, block: () -> Unit) {
    val manager = DataManager.manager(uin)
    this.reader {
        val keyType = readInt()
        val packetType = readByte().toInt()



        discardExact(1)
        // 不知道是什么奇怪的玩意 skip 吧
        val uinStr = readString(readInt() - 4)
        TeaUtil.decrypt(ByteArray(remaining.toInt()).also { readAvailable(it) }, byteArrayOf()).reader {
            val headReader = readByteReadPacket(readInt() - 4)
            val bodyReader = readByteReadPacket(readInt() - 4)
            if (bodyReader.remaining > 0) {
                val msfSSoSeq = headReader.readInt()


            } else {
                /**
                 * Body 为空的包不需要处理 因为不需要
                 *
                 * 是否需要释放呢?
                 *
                 * Answer ： 会被GC掉
                 */
                this.closeQuietly()
                headReader.closeQuietly()
                bodyReader.closeQuietly()
            }
        }
    }
}

fun buildPacket() {

}