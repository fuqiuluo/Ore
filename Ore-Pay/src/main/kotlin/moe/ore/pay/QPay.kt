package moe.ore.pay

import com.google.gson.Gson
import moe.ore.api.LoginResult
import moe.ore.api.Ore
import moe.ore.api.listener.CaptchaChannel
import moe.ore.api.listener.OreListener
import moe.ore.api.listener.SmsHelper
import moe.ore.core.OreManager
import moe.ore.core.helper.DataManager
import moe.ore.core.protocol.ProtocolInternal
import moe.ore.helper.*
import moe.ore.pay.QPayUtil.decryptToJsonStr
import moe.ore.pay.QPayUtil.encryptToReqText
import moe.ore.pay.QPayUtil.getMsgno
import moe.ore.pay.QPayUtil.getTime
import moe.ore.pay.QPayUtil.getTimes
import moe.ore.pay.data.QPayBalance
import moe.ore.pay.data.QPayHbGate
import moe.ore.pay.data.QPayHbPack
import moe.ore.pay.data.QPayWallet
import moe.ore.pay.util.HbRSA
import moe.ore.util.*
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

class QPay(val uin : Long, var payWord : String) {
    /**
     * 支付配置文件
     */

    var hasRoot : Boolean = true

    private val dataManager = DataManager.manager(uin)
    private val protocol = ProtocolInternal[dataManager.protocolType]
    private val userStInfo = dataManager.userSigInfo
    private val device = dataManager.deviceInfo
    private val session = dataManager.session

    private var defaultKeyIndex : Int = Random.nextInt(0, 16)

    /**
     * 获取钱包余额
     */
    fun getWalletBalance() : Double {
        try {
            val skey = userStInfo.sKey.ticket()
            val okhttp = OkhttpUtil()
            val result = okhttp.post(
                HbWallet, mapOf(
                    "req_text" to encryptToReqText(mapOf(
                        "skey" to skey,
                        "uin" to uin.toString(),
                        "h_net_type" to "WIFI",
                        "h_model" to "android_mqq",
                        "h_edition" to "20",
                        "h_location" to "${MD5.hexdigest(device.androidId)}||${device.model}|${device.androidVersion},sdk${device.androidSdkVersion}|${MD5.hexdigest(device.androidId + device.macAddress)}|7C9809E2D6C9B9277643C6088BCD181C|${
                            // 这个0代表支付环境是否有root
                            (if(hasRoot) 1 else 0)
                        }|",
                        "h_qq_guid" to device.guid.toHexString(),
                        "h_qq_appid" to "537070774",
                        // 写死
                        "h_exten" to ""
                    ), defaultKeyIndex),
                    "skey_type" to "2", // 0 为vkey 2 为skey
                    "random" to "$defaultKeyIndex",
                    // 密钥的标识
                    "msgno" to "$uin${getTime()}0001",
                    "skey" to skey
                )
            )
            if(result?.code == 200) {
                val data = decryptToJsonStr(result.body!!.string(), defaultKeyIndex)
                val hbWallet = Gson().fromJson(data, QPayWallet::class.java)
                result.close()
                return hbWallet.balance.toInt() * 0.01
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return 0.00
    }

    /**
     * 发送群聊红包
     */
    fun sendTrHb(
        groupId : Long,
        hbType: HbType,
        wishing: String,
        totalAmount: Int,
        totalNum: Int
    ) {
        val skey = userStInfo.sKey.ticket()
        val pskey = session.pSKeyMap["tenpay.com"]!!["pskey"]!!.ticket()
        val tokenId = getHBPack(skey, pskey, groupId, 0, hbType.busType, hbType.channel, wishing, totalAmount, totalNum, 3)
        val gate = getHBGate(skey, pskey, tokenId)
        val balance = getHBBalance(tokenId, gate!!)
        println(balance)
    }

    private fun getHBPack(
        skey: String, pskey: String, recvUin: Long,
        /**
         * 红包皮肤
         */
        skinId: Int,
        /**
         * 红包分配类型
         * 1 普通红包 2 拼手气
         */
        busType: Int,
        /**
         * 红包领取形式
         * 1普通 1024专属 65536语音 32口令
         */
        channel: Int,
        /**
         * 红包祝福
         */
        wishing: String,
        /**
         * 总金额（单位：分）
         */
        totalAmount: Int,
        /**
         * 红包个数
         */
        totalNum: Int,
        /**
         * 接收处类型
         * 1好友 3群 4非好友
         */
        recvType: Int,
        /**
         * 专享红包
         */
        grabUinList: Array<Long>? = null
    ) : String {
        /**
         * 最多发200块的红包
         */
        check(totalAmount in 1 .. 200 * 10 * 10) { "总金额不合法" }
        check(totalNum in 1 .. 100) { "红包个数不合法" }
        check((!grabUinList.isNullOrEmpty() && channel == 1024) ||
                (channel != 1024 && grabUinList == null)) { "专享红包目标不合法" }
        try {
            val okhttp = OkhttpUtil()
            val result = okhttp.post(
                HbPackUrl, mapOf(
                    "req_text" to encryptToReqText(mapOf(
                        "pskey" to pskey,
                        "subchannel" to "0",
                        "hb_from_type" to "0",
                        "skin_id" to skinId,
                        "bus_type" to busType,
                        "channel" to channel,
                        "type" to "1",
                        "wishing" to wishing,
                        "skey_type" to "2",
                        "total_amount" to totalAmount,
                        "recv_type" to recvType,
                        "total_num" to totalNum,
                        "recv_uin" to recvUin,
                        (if (channel == 1024)
                            "grab_uin_list" to fun () : String {
                                var buffer = ""
                                grabUinList!!.forEachIndexed { index , grabUin ->
                                    if(index == 0) {
                                        buffer += grabUin
                                    } else {
                                        buffer += "|$grabUin"
                                    }
                                }
                                return buffer
                            }()
                        else
                            "name" to ""),
                        "skey" to skey,
                        "uin" to uin.toString(),
                        "h_net_type" to "WIFI",
                        "h_model" to "android_mqq",
                        "h_edition" to "74",
                        "h_location" to "${MD5.hexdigest(device.androidId)}||${device.model}|${device.androidVersion},sdk${device.androidSdkVersion}|${MD5.hexdigest(device.androidId + device.macAddress)}|7C9809E2D6C9B9277643C6088BCD181C|${
                            // 这个0代表支付环境是否有root
                            (if(hasRoot) 1 else 0)
                        }|",
                        "h_qq_guid" to device.guid.toHexString(),
                        "h_qq_appid" to "537068363",
                        "h_exten" to ""
                    ), defaultKeyIndex),
                    "skey_type" to "2",
                    "random" to "$defaultKeyIndex",
                    // 密钥的标识
                    "msgno" to getMsgno(uin, nextSeqId()),
                    "skey" to skey
                )
            )
            if(result?.code == 200) {
                val data = decryptToJsonStr(result.body!!.string(), defaultKeyIndex)
                // println(data)
                val hbPack = Gson().fromJson(data, QPayHbPack::class.java)
                result.close()
                return hbPack.token_id
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return null.toString()
    }

    private fun getHBGate(skey: String, pskey: String, token_id: String) : QPayHbGate? {
        try {
            val okhttp = OkhttpUtil()
            val result = okhttp.post(
                HbGateUrl, mapOf(
                    "req_text" to encryptToReqText(mapOf(
                        "pskey" to pskey,
                        "pskey_scene" to "client",
                        "skey_type" to "2",
                        "come_from" to "2",
                        "token_id" to token_id,
                        "skey" to skey,
                        "uin" to uin.toString(),
                        "sdk_channel" to "0",
                        "soter_flag" to "2",
                        "h_net_type" to "WIFI",
                        "h_model" to "android_mqq",
                        "h_edition" to "74",
                        "h_location" to "${MD5.hexdigest(device.androidId)}||${device.model}|${device.androidVersion},sdk${device.androidSdkVersion}|${MD5.hexdigest(device.androidId + device.macAddress)}|7C9809E2D6C9B9277643C6088BCD181C|${
                            // 这个0代表支付环境是否有root
                            (if(hasRoot) 1 else 0)
                        }|",
                        "h_qq_guid" to device.guid.toHexString(),
                        "h_qq_appid" to "537068363",
                        "h_exten" to ""
                    ), defaultKeyIndex),
                    "skey_type" to "2",
                    "random" to "$defaultKeyIndex",
                    // 密钥的标识
                    "msgno" to getMsgno(uin, nextSeqId()),
                    "skey" to skey
                )
            )
            if(result?.code == 200) {
                val str = result.body!!.string()
                val data = decryptToJsonStr(str, defaultKeyIndex)
                val hbGate = Gson().fromJson(data, QPayHbGate::class.java)
                result.close()
                defaultKeyIndex = hbGate.trans_seq.toInt()
                return hbGate
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun getHBBalance(tokenId: String, gate: QPayHbGate) : QPayBalance? {
        try {
            val timeBs = getTimes().toByteArray()
            val time = timeBs.toHexString()
            val pskey = session.pSKeyMap["tenpay.com"]!!["pskey"]!!.ticket()
            val okhttp = OkhttpUtil()
            val result = okhttp.post(
                HbBalanceUrl, mapOf(
                    "req_text" to encryptToReqText(mapOf(
                        "pskey" to pskey,
                        "p" to HbRSA.RSAEncrypt(payWord),
                        "token_id" to tokenId,
                        "is_reentry" to "0",
                        "skey" to gate.skey,
                        "timestamp" to getTimes(),
                        "h_net_type" to "WIFI",
                        "h_model" to "android_mqq",
                        "h_edition" to "74",
                        "h_location" to "${MD5.hexdigest(device.androidId)}||${device.model}|${device.androidVersion},sdk${device.androidSdkVersion}|${MD5.hexdigest(device.androidId + device.macAddress)}|7C9809E2D6C9B9277643C6088BCD181C|${
                            // 这个0代表支付环境是否有root
                            (if(hasRoot) 1 else 0)
                        }|",
                        "h_qq_guid" to device.guid.toHexString(),
                        "h_qq_appid" to "537068363",
                        "h_exten" to ""
                    ), defaultKeyIndex),
                    "msgno" to "$uin${getTime()}0003",
                    "skey" to gate.skey
                )
            )
            if(result?.code == 200) {
                val data = decryptToJsonStr(result.body!!.string(), defaultKeyIndex)
                val balance = Gson().fromJson(data, QPayBalance::class.java)
                result.close()
                return balance
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return null
    }

    private val paySeq = AtomicInteger(Random.nextInt(1))

    private fun nextSeqId(): Int {
        val id = paySeq.incrementAndGet()
        if (id > 1000000) {
            paySeq.set(1)
        }
        return id
    }

    companion object {
        private const val HbPackUrl = "https://mqq.tenpay.com/cgi-bin/hongbao/qpay_hb_pack.cgi?ver=2.0&chv=3"
        private const val HbGateUrl = "https://myun.tenpay.com/cgi-bin/clientv1.0/qpay_gate.cgi?ver=2.0&chv=3"

        /**
         * 查询余额
         */
        private const val HbWallet = "https://myun.tenpay.com/cgi-bin/clientv1.0/qwallet.cgi?ver=2.0&chv=3"
        private const val HbBalanceUrl = "https://myun.tenpay.com/cgi-bin/clientv1.0/qpay_balance.cgi?ver=2.0&chv=3"
    }
}

fun Ore.getPay(payWord: String) : QPay {
    check(payWord.length == 6) { "支付密码不合法" }
    return QPay(this.uin, payWord)
}

fun main() {
    // val ore = OreManager.addBot(203411690, "911586ABc", "C:\\")

    val ore = OreManager.addBot(3042628723, "911586abcd", "C:\\")

    ore.oreListener = object : OreListener {
        override fun onLoginStart() {
            println("登录开始了，呼呼呼！！！")
        }

        override fun onLoginFinish(result: LoginResult) {
            println("登录结果：$result")

            val pay = ore.getPay("170086")
            pay.sendTrHb(1016398585, HbType.Common, "哈哈哈", 1, 1)

            //WloginHelper(ore.uin, (ore as OreBot).client).refreshSt()
            /**val HongBao = ore.getPay("170086")
            val token_id =HongBao.getHBPack("", "", 1)
            val vskey = HongBao.getHBGate(token_id)
            val data = HongBao.getHBBalance(token_id, vskey)
            print(data)
            **/
        }

        override fun onCaptcha(captchaChan: CaptchaChannel) {

            println(captchaChan.url)

            print("请输入Ticket：")
            val ticket = Scanner(System.`in`).nextLine()
            captchaChan.submitTicket(ticket)


        }

        override fun onSms(sms: SmsHelper) {
            println(sms)
            println(sms.sendSms())
            print("请输入SMSCode：")
            val code = Scanner(System.`in`).nextLine()
            sms.submitSms(code)
        }

    }
    ore.login()


}

