package moe.ore.helper.thread

interface IResultThread<T> {
    @Throws(Throwable::class)
    fun on(): T
}