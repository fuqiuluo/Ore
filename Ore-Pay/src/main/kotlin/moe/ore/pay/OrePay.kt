package moe.ore.pay

import moe.ore.api.LoginResult
import moe.ore.api.Ore
import moe.ore.api.OreStatus
import moe.ore.api.listener.CaptchaChannel
import moe.ore.api.listener.OreListener
import moe.ore.api.listener.SmsHelper
import moe.ore.core.OreBot
import moe.ore.core.OreManager
import moe.ore.core.protocol.tars.service.GrayUin
import java.util.*

fun Ore.getPay(payWord: String) : IQPay {
    check(payWord.length == 6) { "支付密码不合法" }
    return QPay(this.uin, payWord)
}

