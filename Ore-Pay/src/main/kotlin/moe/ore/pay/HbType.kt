package moe.ore.pay

enum class HbType(
    val busType : Int,
    val channel : Int
) {
    Common(1, 1), // 普通红包
    Lucky(2, 1) // 拼手气红包
}