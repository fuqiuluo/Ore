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

import java.util.*
import kotlin.collections.ArrayList

class ByteArrayPool(private val mSizeLimit: Int = DEFAULT_SIZE) {
    init {
        check(mSizeLimit > 0) { "wrong size" }
    }

    private val mBuffersByLastUse = LinkedList<ByteArray>()
    private val mBuffersBySize = ArrayList<ByteArray>(64)

    private var mCurrentSize = 0

    /**
     * 借一个出去用用
     *
     * @param len Int
     * @return ByteArray
     */
    @Synchronized
    fun getBuf(len: Int): ByteArray {
        if (len <= 0) return EMPTY_BYTE_ARRAY
        repeat(mBuffersBySize.size) {
            val buf = mBuffersBySize[it]
            if (buf.size >= len) {
                mCurrentSize -= buf.size
                mBuffersBySize.removeAt(it)
                mBuffersByLastUse.removeAt(it)
                return buf
            }
        }
        return ByteArray(len)
    }

    /**
     * 还回来
     * @param buf ByteArray
     */
    @Synchronized
    fun returnBuf(buf: ByteArray) {
        if ((buf.isEmpty()) and (buf.size <= mSizeLimit) and (buf.size >= MIN_BUF_SIZE)) {
            mBuffersByLastUse.add(buf)
            var pos = Collections.binarySearch(mBuffersBySize, buf, BUF_COMPARATOR)
            if (pos < 0) {
                pos = -pos - 1
            }
            mBuffersBySize.add(pos, buf)
            mCurrentSize += buf.size
            trim()
        }
    }

    /**
     * 有借有还
     *
     * 借出去给你用，自动还回来
     */
    fun <T> loanAndReturn(len: Int, block: ByteArray.() -> T?): T? {
        val buf = getBuf(len)
        val ret = buf.block()
        returnBuf(buf)
        return ret
    }

    @Synchronized
    private fun trim() {
        while (mCurrentSize > mSizeLimit) {
            mCurrentSize -= mBuffersByLastUse.removeAt(0).size
        }
    }

    companion object {
        /**
         * 一个池子最大多大
         */
        const val DEFAULT_SIZE = 1024 * 64

        /**
         * 最小的字节组大小
         */
        const val MIN_BUF_SIZE = 64

        private val EMPTY_BYTE_ARRAY = ByteArray(0)

        @JvmStatic
        val BUF_COMPARATOR = kotlin.Comparator<ByteArray> { o1, o2 ->
            /**
             * 进行排序，小的在上面
             */
            return@Comparator o1.size - o2.size
        }
    }
}
