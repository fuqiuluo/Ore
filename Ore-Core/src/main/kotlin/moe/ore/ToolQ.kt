package moe.ore

import moe.ore.helper.Initializer
import moe.ore.helper.bytes.ByteReader
import moe.ore.helper.bytes.hex2ByteArray
import moe.ore.helper.log.TLog
import moe.ore.helper.luoapi.LuoApi
import moe.ore.helper.netty.NettyClient
import moe.ore.helper.thread.ThreadManager
import moe.ore.helper.toolq.*
import moe.ore.helper.toolq.packet.Waiter
import moe.ore.helper.toolq.util.PacketUtil
import moe.ore.protocol.Tlv
import moe.ore.protocol.WtLogin
import moe.ore.protocol.helper.JceHelper
import moe.ore.protocol.tars.statsvc.RegisterReq
import moe.ore.protocol.tars.statsvc.RegisterResp
import moe.ore.util.TeaUtil
import moe.ore.virtual.Android
import moe.ore.virtual.AndroidQQ
import moe.ore.virtual.ProtocolInfo
import kotlin.random.Random

class ToolQ(val account : QQAccount, val isHd : Boolean = false) {
    init {
        Initializer.init()
    }

    /**
     * ToolQ状态
     */
    var status = ToolQStatus.NotLogin

    /**
     * ToolQ行为事件器
     */
    var listener : ToolQListener? = null

    /**
     * 记录器
     */
    val msfRecorder = MsfRecorder()

    val threadManager = ThreadManager.getInstance(account.uin)
    val protocolInfo : ProtocolInfo = AndroidQQ(isHd = isHd)
    val tlv = Tlv(this)

    val nettyListener = ToolQNettyListener(this)

    /**
     * 与服务端的连接器
     */
    lateinit var client : NettyClient

    /**
     * 登录进服务器
     * 拿到各种key
     */
    fun login() {
        val successBlock = fun (result : LoginResult) {
            // 登录成功修改一下状态啦
            if(result == LoginResult.Success) this.status = ToolQStatus.Login
            threadManager.addTask { this.listener?.loginEvent(result) }
        }
        threadManager.addTask(object : Thread("${toString()}-ConnectThread") {
            /**
             * 服务器繁忙
             */
            val ServerBusy = 164

            /**
             * 登陸成功
             */
            val Success = 0

            override fun run() {
                try {
                    val server = LuoApi.tencentServer()
                    this@ToolQ.client = NettyClient(server.host, server.port, true)
                    this@ToolQ.client.listener = this@ToolQ.nettyListener
                    this@ToolQ.client.connect(object : Thread("${toString()}-LoginThread") {
                        init { isDaemon = true }
                        override fun run() {
                            val seq = this@ToolQ.msfRecorder.nextSsoSeq()
                            val waiter = this@ToolQ.nettyListener.addWaiter("wtlogin.login", seq)
                            /**
                             * 服务器连接成功后的代码体
                             */
                            val data = WtLogin.getStWithPassword(this@ToolQ, LoginType.Password, seq)
                            this@ToolQ.client.send(data)
                            handlePacket(waiter)
                        }
                    })
                } catch (e : Exception) {
                    e.printStackTrace()
                    successBlock(LoginResult.Fail)
                }
            }

            fun handleBody(body : ByteArray) : LoginResult {
                var reader = ByteReader(body)
                reader.dis(1)
                reader = ByteReader(reader.readBytes(reader.readShort() - 4))
                // 去掉 02 ShortLength
                if(reader.readShort()== 8001 && reader.readShort() == 0x810 && reader.readShort() == 1 && reader.readULong() == account.uin) {
                    reader.dis(2)
                    val loginResult = reader.readByte().toInt() and 0xff
                    println("Type: $loginResult")
                    val teaKey = if(loginResult == 180) Android.android.randKey else msfRecorder.shareKey
                    val tlvMap = getTlv(TeaUtil.decrypt(reader.readRestBytes(), teaKey), true)
                    return when(loginResult) {
                        ServerBusy -> LoginResult.ServerBusy
                        Success -> {
                            parseTlv(tlvMap)
                            LoginResult.Success
                        }
                        else -> LoginResult.Fail
                    }
                }
                reader.close()
                return LoginResult.Fail
            }

            fun parseTlv(tlvMap: Map<Int, ByteArray>) {
                tlvMap.forEach { (ver, content) ->
                    when(ver) {
                        0x103 -> msfRecorder.setKey(KeyType.StWebSig, content)
                        0x108 -> Android.android.apply {
                            this.ksid = content
                            this.flush()
                        }
                        0x10a -> msfRecorder.setKey(KeyType.Tgt, content)
                        0x10d -> msfRecorder.setKey(KeyType.TgtKey, content)
                        0x10e -> msfRecorder.setKey(KeyType.StSigKey, content)
                        0x114 -> msfRecorder.setKey(KeyType.StSig, content)
                        0x119 -> parseTlv(getTlv(TeaUtil.decrypt(content, Android.android.getTgtgKey()), false))
                        0x11a -> {
                            val reader = ByteReader(content)
                            msfRecorder.face = reader.readShort()
                            msfRecorder.age = java.lang.Byte.toUnsignedInt(reader.readByte())
                            msfRecorder.gender = java.lang.Byte.toUnsignedInt(reader.readByte())
                            msfRecorder.nick = reader.readString(java.lang.Byte.toUnsignedInt(reader.readByte()))
                            reader.close()
                        }
                        0x120 -> msfRecorder.setKey(KeyType.SKey, content)
                        0x143 -> msfRecorder.setKey(KeyType.D2, content)
                        0x16d -> msfRecorder.setKey(KeyType.PSKey, content)
                        0x161 -> msfRecorder.setKey(KeyType.Tlv161, content)
                        0x16a -> msfRecorder.setKey(KeyType.NoPicSig, content)
                        0x203 -> msfRecorder.setKey(KeyType.Da2, content)
                        0x305 -> msfRecorder.setKey(KeyType.D2Key, content)
                        0x512 -> {
                            val reader = ByteReader(content)
                            val size = reader.readShort()
                            repeat(size) {
                                val domain = reader.readString(reader.readShort())
                                val psKey = reader.readString(reader.readShort())
                                msfRecorder.domainPsKey[domain] = psKey
                                // println("Domain: $domain, key : $psKey, token : $p4Token")
                            }
                        }
                        // 没有使用到的Tlv暂时不保存
                        // else -> println(Integer.toHexString(ver))
                    }
                }
            }

            fun getTlv(data : ByteArray, wt : Boolean) : Map<Int, ByteArray> {
                val tlvMap = hashMapOf<Int, ByteArray>()
                val reader = ByteReader(data)
                if(wt) {
                    reader.readShort()
                    reader.dis(1)
                }
                val size = reader.readShort()
                repeat(size) {
                    val ver = reader.readShort()
                    val body = reader.readBytes(reader.readShort())
                    // println(ver)
                    // println(body.toHexString())
                    tlvMap[ver] = body
                }
                reader.close()
                return tlvMap
            }

            fun handlePacket(waiter: Waiter) {
                if(waiter.wait(5000)) {
                    successBlock(handleBody(waiter.packet.body))
                } else {
                    successBlock(LoginResult.LoginTimeout)
                }
            }
        })
    }

    /**
     * 上线
     * 开始接受腾讯的主动推送
     */
    fun register() {
        val cmd = "StatSvc.register"
        val requestId = msfRecorder.nextRequestId()
        val android = Android.android
        val register = RegisterReq()
        register.lUin = account.uin
        register.lBid = 1 or 2 or 4
        register.cConnType = 0
        register.iStatus = 11
        register.timeStamp = Random.nextLong(189, 289)
        register.bRegType = 1
        register.iLocaleID = protocolInfo.localeId()
        register.bOpenPush = 1
        register.vecGuid = android.getGuid()
        register.strDevName = android.machineName
        register.strDevType = android.machineManufacturer
        register.strOSVer = android.androidVersion
        register.strVendorName = "[u]${android.machineName}"
        register.strVendorOSName = "?LMY48G test-keys;ao"
        register.bytes0x769ReqBody = "0A 04 08 2E 10 00 0A 05 08 9B 02 10 00".hex2ByteArray()
        val seq = msfRecorder.nextSsoSeq()
        val waiter = nettyListener.addWaiter(cmd, seq)
        client.sendPacket(cmd, seq, JceHelper.encodeTarsPacket(cmd, requestId, register),
            needSize = false,
            d2 = true,
            tgt = true
        )
        val code = if(waiter.wait(5000)) {
            val resp = JceHelper.decodeTarsPacket(cmd, waiter.packet.bodyWithSize(), RegisterResp())
            val code = resp.cReplyCode.toInt()
            if(code == 0) this.status = ToolQStatus.Online
            code
        } else -1
        threadManager.addTask {
            this.listener?.onlineEvent(code)
        }
    }

    /**
     * 魔法代码不要看嗷嗷嗷啊！！！
     */
    fun NettyClient.sendPacket(cmd : String, seq : Int, body : ByteArray, needSize : Boolean = false, d2 : Boolean = false, tgt : Boolean = false) {
        threadManager.addTask {
            val data = PacketUtil.makePacket(
                cmd,
                body,
                status,
                if(d2) msfRecorder.getKey(KeyType.D2) else null,
                msfRecorder.getKey(KeyType.D2Key),
                seq, account.uin, protocolInfo,
                if(tgt) msfRecorder.getKey(KeyType.Tgt) else null,
                Android.android.ksid,
                needSize)
            this.send(data)
        }
    }

    /**
     * 关闭机器人
     */
    fun shut(remove : Boolean = false) {
        try {
            threadManager.shutdown()
            client.shut()
            if(remove) {
                //
                ToolQManager.removeToolQ(account.uin)
            }
        } catch (e : Exception) {
            e.printStackTrace()
        } finally {
            ToolQManager
        }
    }

    override fun toString(): String = "[ToolQ]${account.uin}"

    companion object {
        private val logger = TLog.getLogger("ToolQ")

        enum class LoginResult(val msg : String) {
            Fail("UnknownMistake"),
            LoginTimeout("TimeOut"),
            ServerBusy("ServerBusy"),
            Success("LandedSuccessfully")
        }


    }
}
