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

package moe.ore.api.data

/**
 *@author 飞翔的企鹅
 *create 2021-06-06 10:43
 */
data class Result<T>(var status: Int, var message: String, val data: T?) {
    companion object {
        @JvmStatic
        fun <T> success(message: String, data : T?) : Result<T> {
            val status: Int = Status.Success.value()
            return Result(status, message, data)
        }

        @JvmStatic
        fun <T> clientError(message: String, data : T?) : Result<T> {
            val status: Int = Status.ClientError.value()
            return Result(status, message, data)
        }

        @JvmStatic
        fun <T> serverError(message: String, data : T?) : Result<T> {
            val status: Int = Status.ServerError.value()
            return Result(status, message, data)
        }

        @JvmStatic
        fun <T> unknownError(message: String, data : T?) : Result<T> {
            val status: Int = Status.UnknownError.value()
            return Result(status, message, data)
        }

        @JvmStatic
        fun <T> result(status: Int, message: String, data : T?) : Result<T> {
            return Result(status, message, data)
        }
    }
}

enum class Status(private val status: Int) {
    Success(0),
    ServerError(-1),
    ClientError(-2),
    UnknownError(-3);

    fun value(): Int {
        return status
    }
} 