package moe.ore.pay

import moe.ore.api.Ore

fun Ore.getPay(payWord: String): IQPay {
    check(payWord.length == 6) { "支付密码不合法" }
    return QPay(this.uin, payWord)
}

