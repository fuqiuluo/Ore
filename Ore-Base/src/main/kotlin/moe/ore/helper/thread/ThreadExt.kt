package moe.ore.helper.thread

fun Thread.sleepQuietly(time: Long) = kotlin.runCatching {
    Thread.sleep(time)
}