package moe.ore.pay

internal enum class HbType(
    val busType: Int,
    val channel: Int
) {
    Common(1, 1), // 普通红包
    Lucky(2, 1), // 拼手气红包
    Exclusive(1, 1024), // 专属红包
    Rare(2, 1000003), // 生僻字红包
    Password(2, 32), // 口令红包
    Voice(2, 65536) // 语音红包
}