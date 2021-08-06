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
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

abstract class LongHandler(commandName: String) : Handler(0, commandName) {
    abstract fun handle(from: FromService)

    override fun check(from: FromService): Boolean {
        handle(from)
        return false
    }
}

open class SingleHandler(seq: Int, commandName: String) : Handler(seq, commandName) {
    /**
     * 堵塞器
     */
//    private val queue = ArrayBlockingQueue<Boolean>(1)
    private val countDownLatch = CountDownLatch(1)

    //    private var isSuccess = false
    private var isOver = false
    var fromService: FromService? = null

    /**
     * 等待返回包
     *
     * !!!等包间隔起步10秒
     * @return Boolean
     */
    fun wait(timeout: Long = 20 * 1000): Boolean {
        if (isOver || countDownLatch.count == 0L) {
            // 已经结束了，没必要再继续同步接包
            return false
        }
//        Timer().apply {
//            schedule(object : TimerTask() {
//                override fun run() {
//                    queue.add(false)
//                    this@apply.cancel()
//                }
//            }, timeout)
//        }
        val ret = countDownLatch.await(timeout, TimeUnit.MILLISECONDS)//queue.take().also { this.isSuccess = it }
//        this.isSuccess=true
        this.isOver = true
        return ret
    }

    override fun check(from: FromService): Boolean {
        // 已经Over了，但是因为线程安全注销删掉halder，所以说这里是个安全验证
        if (isOver) return true
        val ret = ((from.seq == seq) and (from.commandName == commandName)).also {
            if (it) this.fromService = from
        }
        // println("拿到了：$ret")
//        queue.add(ret)
        countDownLatch.countDown()
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