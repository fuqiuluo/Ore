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
 * 使用该项目产生的违法行为，由第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 *  オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 *
 */

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
 * 使用该项目产生的违法行为，由第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 *  オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 *
 */

package moe.ore.core.helper

import kotlinx.io.pool.DefaultPool

/**
 * 缓存 [ByteArray] 实例的 [ObjectPool]
 */
object ByteArrayPool : DefaultPool<ByteArray>(256) {
    /**
     * 每一个 [ByteArray] 的大小
     */
    const val BUFFER_SIZE: Int = 8192 * 8

    override fun produceInstance(): ByteArray = ByteArray(BUFFER_SIZE)

    override fun clearInstance(instance: ByteArray): ByteArray = instance

    fun checkBufferSize(size: Int) {
        require(size <= BUFFER_SIZE) { "sizePerPacket is too large. Maximum buffer size=$BUFFER_SIZE" }
    }

    fun checkBufferSize(size: Long) {
        require(size <= BUFFER_SIZE) { "sizePerPacket is too large. Maximum buffer size=$BUFFER_SIZE" }
    }

    /**
     * 请求一个大小至少为 [requestedSize] 的 [ByteArray] 实例.
     */ // 不要写为扩展函数. 它需要优先于 kotlinx.io 的扩展函数 resolve
    inline fun <R> useInstance(requestedSize: Int = 0, block: (ByteArray) -> R): R {
        if (requestedSize > BUFFER_SIZE) {
            return ByteArray(requestedSize).run(block)
        }
        val instance = borrow()
        try {
            return block(instance)
        } finally {
            recycle(instance)
        }
    }
}