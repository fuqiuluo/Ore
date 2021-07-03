package moe.ore.pay

import com.google.gson.Gson
import moe.ore.api.LoginResult
import moe.ore.api.Ore
import moe.ore.api.listener.CaptchaChannel
import moe.ore.api.listener.OreListener
import moe.ore.api.listener.SmsHelper
import moe.ore.core.OreBot
import moe.ore.core.OreManager
import moe.ore.core.helper.DataManager
import moe.ore.core.helper.toByteArray
import moe.ore.core.protocol.ProtocolInternal
import moe.ore.core.protocol.wlogin.WloginHelper
import moe.ore.helper.hex2ByteArray
import moe.ore.helper.toHexString
import moe.ore.pay.QPayUtil.decryptToJsonStr
import moe.ore.pay.QPayUtil.encryptToReqText
import moe.ore.pay.QPayUtil.formatToJson
import moe.ore.pay.QPayUtil.getTime
import moe.ore.pay.QPayUtil.getTimes
import moe.ore.pay.QPayUtil.toRequestString
import moe.ore.pay.data.QPayWallet
import moe.ore.util.*
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

class QPay(val uin : Long, var payWord : String) {
    /**
     * 支付配置文件
     */

    var hasRoot : Boolean = true

    private var vSkey : String? = null

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
                this.vSkey = hbWallet.skey
                result.close()
                return hbWallet.balance.toInt() * 0.01
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return 0.00
    }

    fun getHBPack() : String {
        try {
            val skey = userStInfo.sKey.ticket()
            val pskey = session.pSKeyMap["tenpay.com"]!!["pskey"]!!.ticket()
            val okhttp = OkhttpUtil()
            val result = okhttp.post(
                HbPackUrl, mapOf(
                    "req_text" to encryptToReqText(mapOf(
                        "pskey" to pskey,
                        "subchannel" to "0",
                        "hb_from_type" to "0",
                        "skin_id" to "0", // 红包皮肤
                        "bus_type" to "1", // 红包类型  1普通 2拼手气
                        "channel" to "1", // 红包标识  1普通 1024专属 65536语音 32口令
                        "type" to "1",
                        "wishing" to "恭喜发财", // 标题 口令 语音
                        "skey_type" to "2",
                        "total_amount" to "1", // 金额 分为单位
                        "recv_type" to "3", // 发送类型  1好友 3群 4非好友
                        "total_num" to "1", // 包数
                        "recv_uin" to "1016398585", //群或者好友 uin groupid
                        //"grab_uin_list" to "1958068659|1372362033", //专属红包 用|隔开
                        "name" to "",
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
                return hbWallet.token_id
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return null.toString()
    }

    fun getHBGate(token_id: String) : String {
        try {
            val skey = userStInfo.sKey.ticket()
            val pskey = session.pSKeyMap["tenpay.com"]!!["pskey"]!!.ticket()
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
                    "skey_type" to "2", // 0 为vkey 2 为skey
                    "random" to "$defaultKeyIndex",
                    // 密钥的标识
                    "msgno" to "$uin${getTime()}0002",
                    "skey" to skey
                )
            )
            if(result?.code == 200) {
                val data = decryptToJsonStr(result.body!!.string(), defaultKeyIndex)
                val hbWallet = Gson().fromJson(data, QPayWallet::class.java)
                result.close()
                defaultKeyIndex=hbWallet.trans_seq.toInt()
                return hbWallet.skey
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return null.toString()
    }

    fun getHBBalance(token_id: String, vskey: String) : String {
        try {
            val time = HexUtil.Bin2Hex(getTimes().toByteArray()).replace(" ", "")
            val mkey = MD5.hexdigest(payWord)
            val winm = "F0D6C4CEE093903BFD05D6303A581B97E8442ABD"
            var data = BytesUtil.byteMerger(byteArrayOf(0x00.toByte(),0x05.toByte()), BytesUtil.randomKey(69))
            data = BytesUtil.byteMerger(data, byteArrayOf(0x00.toByte()))
            data = BytesUtil.byteMerger(data, getTimes().toByteArray())
            data = BytesUtil.byteMerger(data, HexUtil.Hex2Bin("FFFFFFFFFFFFFFFFFFFFFFFFFFFF"))
            data = BytesUtil.byteMerger(data, mkey.toByteArray())
            val rsa = RsaUtil.encode(data)
            val p = time + winm + rsa
            val pskey = session.pSKeyMap["tenpay.com"]!!["pskey"]!!.ticket()
            val okhttp = OkhttpUtil()
            val result = okhttp.post(
                HbBalanceUrl, mapOf(
                    "req_text" to encryptToReqText(mapOf(
                        "pskey" to pskey,
                        "p" to p,
                        "token_id" to token_id,
                        "is_reentry" to "0",
                        "skey" to vskey,
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
                    "skey" to vskey
                )
            )
            if(result?.code == 200) {
                val data = decryptToJsonStr(result.body!!.string(), defaultKeyIndex)
                val hbWallet = Gson().fromJson(data, QPayWallet::class.java)
                result.close()
                return data
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return null.toString()
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

    // QPayUtil.tryToDecrypt("1FD4ACB79EE0980D260B380F704F1246B6196B26B525A1F0ECCC8769DAA5B03F2AC7AE6250E3E94A27CF0157254A7A06A72972E6811289AA56E3A29F351CC76D42C8DE4F626E13D4B816DBBEDFC9EB964E325645A57706E2D380A73614B26DAE76E423F99D9D31660C70A827F80CF9A9A56BBD5FAC22D58B38AAB7E18D97549684E82850C6591A12525B0D07191B83EA4E220CC91AA4D3378C4B198B95D7FD362E0773228CD6FC812113BF38F21DA178A8371180F34B7A46E6BA9B6C12FEFCEA8FC4A7B12C4139C5F658256790DFA79E399EB4D6B7FF3ABD07D2CBE0D3DA124AF37F34A05D956A7C40CF596A67AA9DF52B4D821A0D70B84915B6251F8E584CD6B1B7AD73AD8F8D81FF4F0A70B9693087934DE22D21528BFD618A97A21F2DA40A2FCF4F32196E95E6FE868B47D4280CFBF1A1F14572A1C9ED4735A5CCBF08922EF42996C54A7D18028DC72638F26D07F83367AA6A2E2A84EF55782ECDA5848F0BE907036FFDB52B3A98C79D54ABA888DDCF3F877C4763ADE288AA661E91085068AEAD0E5854642EBAC690F6EA5B60661671ED82DC7C6F39D33BF1FC6DBF0DF5815322CA21625F22B1B8612FBAE34637398E283562982B64EF34339F5BE20FA76C519017B529DFC53536A59326EFFDA1D9401D99798DEB5EC63F49FB325ED837E6782A71C6FC7EE4AE19532E9300849CD1988089515AD7936302C9273B2CC96A7F4619AA69378AB298EA476199239A8EE7434C6F73AF30ADFE8822826B9A5F8609D54E1BA4EAB32EBADE16E56160959011")

    val ore = OreManager.addBot(3042628723, "911586abcd", "C:\\")

    ore.oreListener = object : OreListener {
        override fun onLoginStart() {
            println("登录开始了，呼呼呼！！！")
        }

        override fun onLoginFinish(result: LoginResult) {
            println("登录结果：$result")

            //WloginHelper(ore.uin, (ore as OreBot).client).refreshSt()
            val HongBao = ore.getPay("170086")
            val token_id =HongBao.getHBPack()
            val vskey = HongBao.getHBGate(token_id)
            val data = HongBao.getHBBalance(token_id, vskey)
            print(data)

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

