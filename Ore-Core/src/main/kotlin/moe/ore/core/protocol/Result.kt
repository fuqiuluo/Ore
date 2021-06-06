package moe.ore.core.protocol

/**
 *@author 飞翔的企鹅
 *create 2021-06-06 10:43
 */
class Result<T>(var status: Int, var message: String, data: T) {

    enum class Status(private val status: Int) {
        NETWORK_ERROR(-1),
        RESULT_TIMEOUT(-2),
        UNKNOWN_ERROR(-3);

        fun value(): Int {
            return status
        }
    }

}