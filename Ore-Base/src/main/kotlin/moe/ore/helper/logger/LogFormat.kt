package moe.ore.helper.logger

interface LogFormat {
    fun format(level: Level, msg : Any?) : String
}