package moe.ore.pay

import moe.ore.pay.data.QPayBalance

interface IQPay {
    /**
     * 获取钱包余额
     */
    fun getWalletBalance(): Double

    /**
     * 发送群聊普通红包
     */
    fun sendTrCommonHb(groupId: Long, wishing: String, totalAmount: Int, totalNum: Int): QPayBalance?

    /**
     * 发送群聊拼手气红包
     */
    fun sendTrLuckyHb(groupId: Long, wishing: String, totalAmount: Int, totalNum: Int): QPayBalance?

    /**
     * 群聊专享红包
     */
    fun sendTrExclusiveHb(
        groupId: Long,
        wishing: String,
        totalAmount: Int,
        totalNum: Int,
        grabUinList: Array<Long>
    ): QPayBalance?

    /**
     * 群口令红包
     */
    fun sendTrPasswordHb(groupId: Long, word: String, totalAmount: Int, totalNum: Int): QPayBalance?

    /**
     * 语音红包
     */
    fun sendTrVoiceHb(groupId: Long, word: String, totalAmount: Int, totalNum: Int): QPayBalance?

    /**
     * 发送生僻子字红包
     */
    fun sendTrRareHb(groupId: Long, word: String, totalAmount: Int, totalNum: Int): QPayBalance?
}