/*
 * English :
 *  The project is protected by the MPL open source agreement.
 * Open source agreement warning that prohibits deletion of project source code files.
 * The project is prohibited from acting in illegal areas.
 * All illegal activities arising from the use of this project are the responsibility of the second author, and the original author of the project is not responsible
 *
 *  中文：
 *  该项目由MPL开源协议保护。
 *  禁止删除项目源代码文件的开源协议警告内容。
 * 禁止使用该项目在非法领域行事。
 * 使用该项目产生的违法行为，由使用者或第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 *  オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 *
 */

package moe.ore.core.protocol.wtlogin

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import kotlinx.io.core.*
import moe.ore.api.LoginResult
import moe.ore.api.listener.CaptchaChannel
import moe.ore.api.listener.OreListener
import moe.ore.core.bot.BytesTicket
import moe.ore.core.bot.LoginExtraData
import moe.ore.core.bot.StringTicket
import moe.ore.core.helper.DataManager
import moe.ore.core.net.BotClient
import moe.ore.core.net.packet.PacketSender
import moe.ore.core.net.packet.PacketSender.Companion.sync
import moe.ore.core.protocol.ECDH_SHARE_KEY
import moe.ore.core.protocol.SvcRegisterHelper
import moe.ore.helper.*
import moe.ore.helper.thread.ThreadManager
import moe.ore.util.MD5
import moe.ore.util.TeaUtil
import okhttp3.internal.toHexString
import java.lang.Exception

/**
 * 登录器
 * @property uin Long
 * @property listener OreListener?
 * @constructor
 */
internal class WtLoginHelper(private val uin: Long, private val client: BotClient, private val listener: OreListener?) : Thread() {

    private val threadManager = ThreadManager.getInstance(uin)
    private val manager = DataManager.manager(uin)
    private val userStInfo = manager.wLoginSigInfo
    private val device = manager.deviceInfo
    private val session = manager.session

    override fun run() {
        invoke()
    }

    private fun invoke() {
        // println(Thread.currentThread().name)
        // 禁止使用nio线程进行堵塞等包操作
        handle(sender = WtLoginPassword(uin).sendTo(client))
        if (true)return
//        上线要的参数
        userStInfo.d2 = BytesTicket("a7d6c39e3a9a039a945bbe7ca8c00b193278c75a82137b7d10530fde75adb47d0997b78a1a0b30a0f35b1c0cbf90d7eda07bb1453f115d2c25d587803d6e757a".hex2ByteArray(), System.currentTimeMillis())
        userStInfo.d2Key = BytesTicket("704e774e2c412a47473b717432387549".hex2ByteArray(), System.currentTimeMillis())
        userStInfo.tgt = BytesTicket("d28961ac3fdd8c9938aebe9e4343fb44f2fb7dd066288b2795309753d0c256e753639902709f19138ab7dccd573f27409e3dab5ee4b93adba0e6e438cddb6733b644db6fb2ba48e4".hex2ByteArray(), System.currentTimeMillis())

//        emp要的参数
        userStInfo.encryptA1 = BytesTicket("a3e2071ae7b24b10bbf0c6a749d898eecfcae15e7b5a18376675a1583045ead81e2e23b48fdbf453f3ce18ec08597f2e70175fe9fdd8ebde410f455b8befa316a62419ebf3d792f1fda9b0fa6227cafa6b7bfccd19ed301f77a5b30b082bbce896c434b030e7f8c48a6d3cd61d8c48ed079103ffb3748f33".hex2ByteArray(), System.currentTimeMillis())
        userStInfo.gtKey = BytesTicket("3f67254b684e496a7066524a4871534d".hex2ByteArray(), System.currentTimeMillis())
        userStInfo.noPicSig = BytesTicket("99e5f1760879377b68e713d1a86dbf6b92bf14f8a6252cef2d573976274191a9e08abc7f0b903f4795251a4f783cba901c0c03cabeb3e47f".hex2ByteArray(), System.currentTimeMillis())
        userStInfo.wtSessionTicket = BytesTicket("6ee7b5f7237fd575eb073cf991f01e107bdcadedfd74cf547dbdf66facc4bf6094a832bc6f792360813ffd67428430fe".hex2ByteArray(), System.currentTimeMillis())
        userStInfo.wtSessionTicketKey = BytesTicket("45a77e0e3ee55319acd9f12e1d9529e6".hex2ByteArray(), System.currentTimeMillis())
        userStInfo.G = "5ed21043bc88f1f7916e837c2b815d1d".hex2ByteArray()
        userStInfo.d2Key =BytesTicket("704e774e2c412a47473b717432387549".hex2ByteArray(),System.currentTimeMillis())
        device.tgtgt =BytesTicket("d94804d2ba7c69e3af5ee7298c8c6bc422ae0f3e7a092ded986817a0aecd56682334288c4164e4579b2bfb61d9e800d3fc6ab5164ab92bf749a8ec902500fea1beca992ce3bb12f0bac6c2fb1d97b53fc69e78dda18e59d4d8bac10dd740c8c6fc9c284dde2fc55b0f2dd110c1423d828eabdfb4f102dc55".hex2ByteArray(),System.currentTimeMillis()).ticket()

        session.randSeed="455e505d712d5a49".hex2ByteArray()
        val ret = SvcRegisterHelper(uin).register()
        println(ret)
        if (ret == 0) {
            // 清空回滚
            session.rollBackCount = 0
            callback(LoginResult.Success)
        } else callback(LoginResult.RegisterFail)


        val sender = WtLoginEmp(uin).sendTo(client)
        val from = sender sync 20 * 1000
        from?.body?.readLoginPacket(session.randomKey) { result, tlvMap ->
            println("EMP : $result")
            tlvMap[0x146]?.let {
                // TODO: 2021/6/16 错误消息解析
                val toByteReadPacket = it.toByteReadPacket()
//                    val ver = toByteReadPacket.readInt()
                val code = toByteReadPacket.readInt()
                val title = toByteReadPacket.readString(toByteReadPacket.readShort().toInt())
                val message = toByteReadPacket.readString(toByteReadPacket.readShort().toInt())
                val errorInfo = toByteReadPacket.readString(toByteReadPacket.readShort().toInt())
                println("code = [${code}], title = [${title}], message = [${message}], errorInfo = [${errorInfo}]")
            }
            if (result == 204) {
                tlvMap[0x104]?.let { userStInfo.t104 = it }
                onDevicePass(userStInfo.G, tlvMap[0x403])
            } else {
                TODO("意料之外的情况result：$result")
            }
        } ?: callback(LoginResult.ServerTimeout)

    }

    private inline fun onCaptcha(t192: ByteArray, t546: ByteArray?) {
        val url = String(t192)
        listener?.onCaptcha(object : CaptchaChannel(url) {
            override fun submitTicket(ticket: String) {
                handle(WtLoginTicket(ticket, t546, uin).sendTo(client))
            }
        })
    }

    private inline fun onRollBack(t161: ByteArray?) {
        t161?.let { it ->
            val tlv = it.toByteReadPacket().withUse { decodeTlv(this) }
            tlv[0x173]?.let {
                println("T173 : " + it.toHexString())
            }
            tlv[0x17f]?.let {
                println("T17F : " + it.toHexString())
            }
            // 等待遇到实际的包 再进行分析

            /**
            tlv[0x173]?.let {
            it.reader {
            val type = readByte()
            val host = readString(readUShort().toInt())
            val port = readShort()
            }
            }
            tlv[0x17f]?.let {
            it.reader {
            val type = readByte()
            val host = readString(readUShort().toInt())
            val port = readShort()
            }
            } **/
            tlv[0x172]?.let {
                session.rollBackCount++
                session.rollbackSig = it
                // println("0x172 call rollbackSig")
                client.connect()
            }
        }

    }

    private inline fun onSuccess(t119: ByteArray?) {
        t119?.let { source ->
            val map = try{decodeTlv(TeaUtil.decrypt(source, device.tgtgt).toByteReadPacket())}catch (e:Exception) {
                decodeTlv(TeaUtil.decrypt(source, userStInfo.gtKey.ticket()).toByteReadPacket())
            }
            println("t119 --> tlvMap: " + map.keys.map { "0x" + it.toHexString() })
//            lgi    t119 --> tlvMap: [0x103, 0x203, 0x143, 0x305, 0x106, 0x10a, 0x10c, 0x10d, 0x10e, 0x550, 0x512, 0x114, 0x118, 0x11a, 0x11d, 0x11f, 0x120, 0x322, 0x522, 0x163, 0x528, 0x16a, 0x16d, 0x130, 0x133, 0x134, 0x537, 0x138]
//            exp    t119 --> tlvMap: [0x103, 0x203, 0x108,        0x106, 0x10a, 0x10c, 0x10d, 0x10e, 0x550, 0x512, 0x114, 0x118, 0x11a, 0x11d, 0x11f, 0x120, 0x322, 0x522, 0x163, 0x528, 0x16a, 0x16d, 0x130, 0x133, 0x134, 0x537, 0x138]
//                     t119 --> tlvMap: [0x103, 0x203,               0x106, 0x10a, 0x10c, 0x10d, 0x10e, 0x550, 0x512, 0x114, 0x118, 0x11a, 0x11d, 0x11f, 0x120, 0x322, 0x522, 0x163, 0x528, 0x16a, 0x16d, 0x130, 0x133, 0x134, 0x537, 0x138]

            val now = System.currentTimeMillis()
            val shelfLife = 86400L // 默认保质期一天
            val bsTicket: (ByteArray) -> BytesTicket = { BytesTicket(it, now, shelfLife) }
            val strTicket: (String) -> StringTicket = { StringTicket(it.toByteArray(), now, shelfLife) }

            map[0x103]?.let { userStInfo.webSig = bsTicket(it) }

            map[0x203]?.let { userStInfo.da2 = bsTicket(it) }

            (map[0x143]!! to map[0x305]!!).run {
                userStInfo.d2 = bsTicket(first)
                userStInfo.d2Key = bsTicket(second)
            }

            (map[0x106]!! to map[0x10c]!!).run {
                userStInfo.tgtgt = bsTicket(first)
                userStInfo.gtKey = bsTicket(second)
                // println("t106 size : %s".format(first.size))
                // println("t10c size : %s".format(second.size))
                userStInfo.encryptA1 = bsTicket(first)
                // userStInfo.encryptA1 = bsTicket(first + sec)
                // 傻卵，不知道你去什么鸟屎开源协议项目看来的代码，
                // 自己逆向QQ去看，a1就是这么算 天王老子来了也是这样
                // QQ 8.6.0
            }

            map[0x10a]?.let { userStInfo.tgt = bsTicket(it) }
            map[0x10d]?.let { userStInfo.tgtKey = bsTicket(it) }

            map[0x10e]?.let { userStInfo.stKey = bsTicket(it) }

            map[0x512]?.reader {
                val size = readUShort().toInt()
                repeat(size) {
                    val domain = readString(readUShort().toInt())
                    val pskey = readString(readUShort().toInt())
                    val p4token = readString(readUShort().toInt())
                    session.pSKeyMap[domain] = hashMapOf(
                        "pskey" to strTicket(pskey),
                        "p4token" to strTicket(p4token)
                    )
                }
            }

            map[0x114]?.let {
                userStInfo.st = bsTicket(it)
            }

            map[0x118]?.let {
                session.uinInfo.mainDisplayName = it
                // 用都没见腾讯用过
            }

            map[0x199]?.let {
                userStInfo.payToken = it
            }

            map[0x11a]?.reader {
                val simple = session.uinInfo
                simple.face = readShort()
                simple.age = readByte()
                simple.gender = readByte()
                simple.nick = readString(readUByte().toInt())
            }

            map[0x11d]?.reader {
                session.encryptAppid = readUInt().toLong()
                userStInfo.downloadStKey = bsTicket(readBytes(16))
                userStInfo.downloadSt = bsTicket(readBytes(readUShort().toInt()))
            }

            map[0x11f]?.reader {
                session.appPriChangeTime = readUInt().toLong()
                session.appPri = readUInt().toLong()
            }

            map[0x120]?.let {
                userStInfo.sKey = strTicket(String(it))
            }

            map[0x322]?.let {
                userStInfo.deviceToken = bsTicket(it)
            }

            /*
            map[0x522]?.let {
                // 不知道是什么东西
            }
            map[0x163]?.let {
                // 不知道是什么东西
            }
            map[0x550]?.let {
                // println(it.toHexString())
                // QQ内测的Token
            }
            */

            map[0x108]?.let {
                device.ksid = it
            }

            map[0x528]?.let {
                userStInfo.t528 = it
            }

            map[0x16d]?.let {
                userStInfo.superKey = bsTicket(it)
            }

            map[0x16a]?.let {
                userStInfo.noPicSig = bsTicket(it)
            }

            map[0x130]?.reader {
                val version = readShort()
                val time = readUInt().toLong()
                // val ipAddr = readUInt().toLong()
                session.clientIp = readBytes(4)
            }

            map[0x133]?.let {
                userStInfo.wtSessionTicket = bsTicket(it)
            }

            map[0x134]?.let {
                userStInfo.wtSessionTicketKey = bsTicket(it)
                // println("ticketKey;" + DataManager.manager(uin).wLoginSigInfo.wtSessionTicketKey.ticket().toHexString())

            }

            map[0x537]?.reader {
                val version = readByte()
                repeat(readUByte().toInt()) {
                    userStInfo.extraDataList.add(
                        LoginExtraData(
                            uin = readLong(),
                            ip = readBytes(4),
                            time = readInt(),
                            appId = readInt()
                        )
                    )
                }
            }

            // key的刷新时间
            map[0x138]?.reader {
                val count = readInt()
                repeat(count) {
                    val ver = readShort().toInt()
                    val time = readUInt().toInt() * 1000L
                    when (ver) {
                        0x106 -> userStInfo.encryptA1.shelfLife = time // 3 days
                        0x10a -> {
                            userStInfo.tgt.shelfLife = time
                            userStInfo.tgtKey.shelfLife = time
                        }
                        0x11c -> {

                        }
                        0x102 -> {

                        }
                        0x103 -> {
                            userStInfo.webSig.shelfLife = time
                        }
                        0x120 -> {
                            userStInfo.sKey.shelfLife = time
                        }
                        0x136 -> {

                        }
                        0x143 -> {
                            userStInfo.d2.shelfLife = time
                            userStInfo.d2Key.shelfLife = time
                        }
                        0x164 -> {

                        }

                        else -> error("unknown tlv : $ver")
                    }
                    discardExact(4)
                }
            }

            // handle(WtLoginEmp(uin).sendTo(client), userStInfo.wtSessionTicketKey.ticket())


            val ret = SvcRegisterHelper(uin).register()

            if(ret == 0) {
                // 清空回滚
                session.rollBackCount = 0
                callback(LoginResult.Success)
            } else callback(LoginResult.RegisterFail)


            val loginJson = JsonObject()
            loginJson.addProperty("uin", uin)
            loginJson.addProperty("d2", userStInfo.d2.ticket().toHexString())
            loginJson.addProperty("tgt", userStInfo.tgt.ticket().toHexString())
            loginJson.addProperty("gtKey", userStInfo.gtKey.ticket().toHexString())
            loginJson.addProperty("d2Key", userStInfo.d2Key.ticket().toHexString())
            loginJson.addProperty("encryptA1", userStInfo.encryptA1.ticket().toHexString())
            loginJson.addProperty("gtKey", userStInfo.gtKey.ticket().toHexString())
            loginJson.addProperty("tgtgt", userStInfo.tgtgt.ticket().toHexString())
            loginJson.addProperty("noPicSig", userStInfo.noPicSig.ticket().toHexString())
            loginJson.addProperty("wtSessionTicketKey", userStInfo.wtSessionTicketKey.ticket().toHexString())
            loginJson.addProperty("wtSessionTicket", userStInfo.wtSessionTicket.ticket().toHexString())
            loginJson.addProperty("G", userStInfo.G.toHexString())
            loginJson.addProperty("payToken", userStInfo.payToken.toHexString())
            loginJson.addProperty("randSeed", session.randSeed.toHexString())
            val gson = GsonBuilder().setPrettyPrinting().create()
            println(gson.toJson(loginJson))
//            val sender = WtLoginEmp(uin).sendTo(client)
//            val from = sender sync 20 * 1000
//            from?.body?.readLoginPacket(userStInfo.wtSessionTicketKey.ticket()) { result, tlvMap ->
//                println("EMP : $result")
//                if (result==204){
//                    onDevicePass(userStInfo.G, tlvMap[0x403])
//                }else{
//                    val get = decodeTlv(TeaUtil.decrypt(tlvMap[0x119],userStInfo.gtKey.ticket()).toByteReadPacket())
//                    println(get.keys.map { "0x" + it.toHexString() })
//                }
//            } ?: callback(LoginResult.ServerTimeout)

        }
    }

    private inline fun onDevicePass(t402: ByteArray?, t403: ByteArray?) {
        handle(sender = WtLoginDeviceLockPass(t402, t403, uin).sendTo(client))
    }

    /**
     * 密码错误
     */
    private inline fun onPasswordWrong() {
        callback(LoginResult.PasswordWrong)
    }

    private fun handle(sender: PacketSender, key: ByteArray = ECDH_SHARE_KEY) {
        val from = sender sync 20 * 1000
        if (from == null) {
            callback(LoginResult.ServerTimeout)
        } else {
            from.body.readLoginPacket(key) { result, tlvMap ->
                tlvMap[0x146]?.let {
                    println(String(it))
                }

                tlvMap[0x508]?.let {
                    println(String(it))
                }

                tlvMap[0x104]?.let { userStInfo.t104 = it }
                tlvMap[0x403]?.let { session.randSeed = it }

                val t402 = tlvMap[0x402]?.also {
                    userStInfo.G = MD5.toMD5Byte(device.guid + session.pwd + it)
                    // 字节组拼接
                }

                tlvMap[0x199]?.let {
                    userStInfo.payToken = it
                }

                println("result: $result tlvMap: " + tlvMap.keys.map { "0x" + it.toHexString() })
                when (result) {
                    0 -> onSuccess(tlvMap[0x119])
                    1 -> onPasswordWrong()
                    2 -> onCaptcha(tlvMap[0x192]!!, tlvMap[0x546])
                    6 -> callback(LoginResult.SliderVerifyFail)
                    9 -> error("协议参数错误，请检查你的TLV是否正确")
                    180 -> onRollBack(tlvMap[0x161])
                    204 -> onDevicePass(t402, tlvMap[0x403])
                    else -> error("unknown login result : $result")
                }
            }
        }
    }

    private fun callback(loginResult: LoginResult) = threadManager.addTask { listener?.onLoginFinish(loginResult) }
}

private inline fun ByteArray.readLoginPacket(key: ByteArray, block: (Int, Map<Int, ByteArray>) -> Unit) {
    val reader = this.toByteReadPacket()
    reader.discardExact(1 + 2 + 2 + 2 + 2)
    // 02 (dis) xx xx (dis) 1f 41 (dis) 08 01 (dis) 00 01 (dis)
    val uin = reader.readUInt().toLong()
    // println(uin)
    val manager = DataManager.manager(uin)
    reader.discardExact(2)
    // 00 00 (dis)
//            val subCommand = reader.readShort().toInt() // subCommand discardExact 2
//            println(subCommand)
    val result = reader.readByte().toInt() and 0xff
    // 235 协议版本过低
    val teaKey = if (result == 180) manager.session.randomKey else key
    val tlvBody = TeaUtil.decrypt(reader.readBytes(reader.remaining.toInt() - 1), teaKey).toByteReadPacket().also { it.discardExact(3) }
    block.invoke(result, decodeTlv(tlvBody))
}

private fun decodeTlv(bs: ByteReadPacket): Map<Int, ByteArray> {
    val size = bs.readUShort().toInt()
    val map = hashMapOf<Int, ByteArray>()
    repeat(size) {
        val ver = bs.readUShort().toInt()
        val tSize = bs.readUShort().toInt()
        val content = bs.readBytes(tSize)
        map[ver] = content
    }
    return map
}

