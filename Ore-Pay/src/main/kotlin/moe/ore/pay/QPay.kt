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
import moe.ore.helper.hex2ByteArray
import moe.ore.helper.toHexString
import moe.ore.pay.QPayUtil.formatToJson
import moe.ore.pay.QPayUtil.getTime
import moe.ore.pay.QPayUtil.toRequestString
import moe.ore.pay.data.QPayWallet
import moe.ore.util.DesECBUtil
import moe.ore.util.MD5
import moe.ore.util.OkhttpUtil
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

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

    fun getWalletBalanceV2() : Double {
        try {
            val skey = userStInfo.sKey.ticket()
            val pskey = userStInfo.superKey.ticket()
            val text = mapOf(
                // "pskey" to pskey,
                // "pskey_scene" to "client",
                "skey" to skey,
                // "skey_type" to "2",
                // "app_info" to "appid#0|bargainor_id#0|channel#wallet",
                "uin" to uin.toString(),
                // "need_suggest" to "1",
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
            )
            val sourceBody = (text.toRequestString().toByteArray().toHexString() + "0000").hex2ByteArray()
            // println(sourceBody.toHexString())
            val keyIndex = 8
            val reqText =  DesECBUtil.encryptDES(sourceBody, desKeys[keyIndex]).toHexString()
            val okhttp = OkhttpUtil()
            val result = okhttp.post(
                HbWallet, mapOf(
                    "req_text" to reqText,
                    "skey_type" to "2", // 0 为vkey 2 为skey
                    "random" to "$keyIndex",
                    // 密钥的标识
                    "msgno" to "$uin${getTime()}0001",
                    "skey" to skey
                )
            )
            if(result?.code == 200) {
                val bytes = DesECBUtil.decryptDES(result.body!!.string().hex2ByteArray(), desKeys[keyIndex])
                val data = String(bytes).formatToJson()
                println(data)
                val hbWallet = Gson().fromJson(data, QPayWallet::class.java)
                result.close()
                return hbWallet.balance.toInt() * 0.01
            }
        } catch (e : Exception) {
            e.printStackTrace()
        }
        return 0.00
    }

    private val paySeq = AtomicInteger(Random().nextInt(1))

    private fun nextSeqId(): Int {
        val id = paySeq.incrementAndGet()
        if (id > 1000000) {
            paySeq.set(1)
        }
        return id
    }

    companion object {
        private val desKeys = arrayOf(
            "9973e345",
            "5dac6cf7",
            "f5c88847",
            "f02c91bd",
            "3c0c3ea1",
            "8b00b67f",
            "c28931b2",
            "c8510256",
            "c42bfdef",
            "890fe53c",
            "0d181064",
            "0ef940b7",
            "10d75d6d",
            "c5d8e9f6",
            "66c3987e",
            "c48cebe3"
        )

        private const val HbPackUrl = "https://mqq.tenpay.com/cgi-bin/hongbao/qpay_hb_pack.cgi?ver=2.0&chv=3"
        private const val HbGateUrl = "https://myun.tenpay.com/cgi-bin/clientv1.0/qpay_gate.cgi?ver=2.0&chv=3"

        /**
         * 查询余额
         */
        private const val HbWallet = "https://myun.tenpay.com/cgi-bin/clientv1.0/qwallet.cgi?ver=2.0&chv=3"
        private const val HbBalanceUrl = "https://myun.tenpay.com/cgi-bin/clientv1.0/qpay_balance.cgi?ver=2.0&chv=3"

        fun tryToDecrypt(data : String) {
            var id = 0
            desKeys.forEach {
                DesECBUtil.decryptDES(data.hex2ByteArray(), it).let { bytes ->
                    val str = String(bytes)
                    if(str.startsWith("p") || str.startsWith("u") || str.startsWith("s")) {
                        println("KeyId : $id")
                        println(bytes.toHexString())
                        println(str)
                    }
                }
                id++
            }
        }
    }
}

fun Ore.getPay(payWord: String) : QPay {
    check(payWord.length == 6) { "支付密码不合法" }
    return QPay(this.uin, payWord)
}

fun main() {
    // val ore = OreManager.addBot(203411690, "911586ABc", "C:\\")

    // QPay.tryToDecrypt("ACA855071E810C9C7A3AF0099BA3364A1C9D8B1F2376CE83579216F047D2E223548AB7B7031D56039D03348FBEA1BDA594B2C51D524B6FBFDA1F363FDB1B8CF17B0791B094B7677D71BDA4850BFCED8949A9F813B33FC0AAC1DA96E77BB2C39D24392B8FCAC58E178113802B8CA99EA2A8C29B07822CC443304BA6BE2CBE5B0B82B5AA12BE77D51AEE0393C841A8CBF9CE4ED1B93F7AE9635C72B57D0C6F07595FEC507C4477467F71EE6C7D043A40AD3A2010A66DCB96BB2F78A8A5AAD5C189B3B61631DC5D04D4B96E24790566B9F851F464EE5372526A5CBB6643826C417CF6665B3F13978796A44E683EC93C89F4BD8319CFFD26BB2DDB3CC2D02D80C1B84C2180390A9BF739CEAE1B7C6139E258E52C51AF0FEE39187A393AB957C0564EB0EC165E7F72B6525673D998655031F132886BA9469DE74925D4B3D7E6021753CA1236BE2EAE4F280E172F3BBAE9FE986F1614A3BB685E1FE720114F97B549388E03D183CCD7585318BC91B88A8587682825C992C24F9DE86E195A0BFD9D2BB667342F6FDA954EA21B7ECBD2FD2D756C8F4CD82E86560663D5FF80F2AFFFD4C22471DFA4ED8390AE9778E8FB81C8403FB11EE1C48E41A654E16FCE05B042575A3D8DD02E12AC700E")

    val ore = OreManager.addBot(3042628723, "911586abcd", "C:\\")

    ore.oreListener = object : OreListener {
        override fun onLoginStart() {
            println("登录开始了，呼呼呼！！！")
        }

        override fun onLoginFinish(result: LoginResult) {
            println("登录结果：$result")


            val balance = ore.getPay("662899").getWalletBalanceV2()
            println("钱包余额：$balance")
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

