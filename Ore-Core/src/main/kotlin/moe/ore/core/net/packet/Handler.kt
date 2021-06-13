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

package moe.ore.core.net.packet

import moe.ore.core.util.QQUtil
import java.util.*
import java.util.concurrent.ArrayBlockingQueue

abstract class LongHandler(commandName: String) : Handler(0, commandName)

class SingleHandler(seq: Int, commandName: String) : Handler(seq, commandName) {
    /**
     * 堵塞器
     */
    private val queue = ArrayBlockingQueue<Boolean>(1)
    private var isOver = false
    var fromService: FromService? = null

    /**
     * 等待返回包
     * @return Boolean
     */
    fun wait(timeout: Long = 5 * 1000): Boolean {
        Timer().apply {
            schedule(object : TimerTask() {
                override fun run() {
                    this@apply.cancel()
                    queue.put(false)
                }
            }, timeout)
        }
        val ret = queue.take()
        this.isOver = true
        return ret
    }

    override fun check(from: FromService): Boolean {
        val ret = ((from.seq == seq) and (from.commandName == commandName)).also {
            if (it) this.fromService = from
        }
        queue.put(ret)
        return ret
    }
}

abstract class Handler(
    val seq: Int,
    val commandName: String
) {

    abstract fun check(from: FromService): Boolean

    override fun hashCode(): Int = QQUtil.hash(seq, commandName)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Handler) return false

        if (seq != other.seq) return false
        if (commandName != other.commandName) return false

        return true
    }
}