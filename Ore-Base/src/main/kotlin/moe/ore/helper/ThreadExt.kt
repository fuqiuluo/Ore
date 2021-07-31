package moe.ore.helper

fun Thread.sleepQuietly(time: Long) {
    kotlin.runCatching {
        Thread.sleep(time)
    }
}