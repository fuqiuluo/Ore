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

@file:Suppress("EXPERIMENTAL_API_USAGE_FUTURE_ERROR")

package moe.ore.core.protocol

import kotlinx.io.core.BytePacketBuilder
import kotlinx.io.core.toByteArray
import kotlinx.io.core.writeFully
import kotlinx.io.core.writeIntLittleEndian
import moe.ore.core.bot.BotAccount
import moe.ore.core.bot.DeviceInfo
import moe.ore.core.bot.LoginExtraData
import moe.ore.core.bot.SsoSession
import moe.ore.helper.*
import moe.ore.util.MD5
import okhttp3.internal.and
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.util.zip.CRC32
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or
import kotlin.experimental.xor
import kotlin.random.Random


class Tlv(
    private val account: BotAccount,
    private val deviceInfo : DeviceInfo,
    private val session: SsoSession,
    protocolType: ProtocolInternal.ProtocolType
) {
    /**
     * 协议信息
     */
    private val protocolInfo = ProtocolInternal[protocolType]

    fun t1() = buildTlv(0x1) {
        writeShort(protocolInfo.ipVersion)
        writeInt(Random.nextInt())
        writeLongToBuf32(account.uin)
        writeInt(currentTimeSeconds())
        writeFully(deviceInfo.clientIp)
        writeShort(0)
    }

    fun t8() = buildTlv(0x8) {
        writeShort(0)
        writeInt(protocolInfo.localId)
        writeShort(0)
    }

    fun t18() = buildTlv(0x18) {
        writeShort(protocolInfo.pingVersion)
        writeInt(protocolInfo.ssoVersion)
        writeInt(protocolInfo.subAppId)
        writeInt(0)
        writeLongToBuf32(account.uin)
        writeInt(session.rollBackCount)
        // 默认开始是0，回滚（rollback）一次就+1
    }

    fun t10a(tgt : ByteArray) = buildTlv(0x10a) {
        writeBytes(tgt)
    }

    fun t100(appId : Int = protocolInfo.appId, mainSigMap : Int = protocolInfo.mainSigMap) = buildTlv(0x100) {
        writeShort(protocolInfo.dbVersion)
        writeInt(protocolInfo.msfSsoVersion)
        writeInt(protocolInfo.subAppId)
        writeInt(appId)
        writeInt(0)
        writeInt(mainSigMap)
        // mainSigMap
    }

    fun t104(dt104: ByteArray?) = buildTlv(0x104) {
        writeBytes(dt104 ?: byteArrayOf())
    }

    fun t106() = buildTlv(0x106) {
        writeTeaEncrypt(account.bytesMd5PasswordWithQQ) {
            writeShort(protocolInfo.tgtgVersion)
            writeInt(Random.nextInt())
            writeInt(protocolInfo.msfSsoVersion)
            writeInt(protocolInfo.subAppId)
            writeInt(0)
            writeInt(0)
            writeLongToBuf32(account.uin)
            writeInt(currentTimeSeconds())
            writeFully(deviceInfo.clientIp)
            writeByte(1.toByte())
            writeBytes(account.bytesMd5Password)
            writeBytes(deviceInfo.tgtgt)
            writeInt(0)
            writeBoolean(protocolInfo.isGuidAvailable)
            writeBytes(deviceInfo.guid)
            writeInt(protocolInfo.appId)
            writeInt(protocolInfo.loginType)
            writeStringWithShortLen(account.uin.toString())
            writeShort(0)
        }
    }

    fun t107() = buildTlv(0x107) {
        writeShort(0) // picType
        writeByte(0.toByte())
        writeShort(0)
        writeByte(1.toByte())
    }

    fun t108() = buildTlv(0x108) {
        writeBytes(deviceInfo.ksid)
    }

    private fun t109() = buildTlv(0x109) {
        writeBytes(MD5.toMD5Byte(deviceInfo.androidId.ifEmpty { deviceInfo.imei }))
    }

    fun t112() = buildTlv(0x112) {
        writeString(account.uin.toString())
    }

    fun t116(appidArray : IntArray = intArrayOf(0x5f5e10e2)) = buildTlv(0x116) {
        writeByte(0.toByte())
        // ver
        writeInt(protocolInfo.miscBitmap)
        writeInt(protocolInfo.subSigMap)
        writeByte(appidArray.size.toByte())
        for (appid in appidArray) {
            writeInt(appid)
        }
    }

    private fun t124() = buildTlv(0x124) {
        writeStringWithShortLen(deviceInfo.osType)
        writeStringWithShortLen(deviceInfo.androidVersion)
        writeShort(deviceInfo.netType.value)
        writeStringWithShortLen(deviceInfo.apnName)
        writeStringWithIntLen(deviceInfo.apn)
    }

    private fun t128() = buildTlv(0x128) {
        writeShort(0)
        writeBoolean(protocolInfo.isGuidFromFileNull)
        writeBoolean(protocolInfo.isGuidAvailable)
        writeBoolean(protocolInfo.isGuidChanged)
        writeInt(0x01000000)
        writeStringWithShortLen(deviceInfo.model)
        writeBytesWithShortLen(deviceInfo.guid)
        writeStringWithShortLen(deviceInfo.brand)
    }

    fun t141() = buildTlv(0x141) {
        writeShort(1)
        // version
        writeStringWithShortLen(deviceInfo.apnName)
        writeShort(deviceInfo.netType.value)
        writeStringWithShortLen(deviceInfo.apn)
    }

    fun t142() = buildTlv(0x142) {
        writeShort(0)
        writeStringWithShortLen(protocolInfo.packageName)
    }

    fun t143(d2 : ByteArray) = buildTlv(0x143) {
        writeBytes(d2)
        // writeBytesWithShortLen(d2)
    }

    fun t144(key : ByteArray = deviceInfo.tgtgt) = buildTlv(0x144) {
        writeTeaEncrypt(key) {
            writeShort(5)
            writeBytes(t109())
            writeBytes(t52d())
            writeBytes(t124())
            writeBytes(t128())
            writeBytes(t16e())
        }
    }

    fun t145() = buildTlv(0x145) {
        writeBytes(deviceInfo.guid)
    }

    fun t147() = buildTlv(0x147) {
        writeInt(protocolInfo.subAppId)
        writeStringWithShortLen(protocolInfo.packageVersion)
        writeBytesWithShortLen(protocolInfo.tencentSdkMd5)
    }

    fun t154(seq: Int) = buildTlv(0x154) {
        writeInt(seq)
    }

    fun t166() = buildTlv(0x166) {
        writeByte(session.imgType.toByte())
        // imgType
    }

    fun t16a(noPicSig: ByteArray) = buildTlv(0x16a) {
        /**
         * 20 B5 33 79 18 79 9C AB E4 4A 8E F8 0D 66 84 B81F 8C 15 24 AD 46 D6 D7 7A AF 24 6A 09 16 0A 59AF 22 ED 5B 14 A8 B4 78 36 F2 AC 9A 34 61 15 3A
         */
        writeBytes(noPicSig)
    }

    /**
     * 登录过 设备所显示名称
     */
    fun t16b() = buildTlv(0x16b) {
        val domains = arrayOf("tenpay.com", "qzone.qq.com", "qun.qq.com", "mail.qq.com", "openmobile.qq.com", "qzone.com", "game.qq.com", "vip.qq.com")
        writeShort(domains.size)
        for (s in domains) {
            writeStringWithShortLen(s)
        }
    }

    private fun t16e() = buildTlv(0x16e) {
        writeString(deviceInfo.model)
    }

    fun t172() = buildTlv(0x172) {
        writeBytes(session.rollbackSig ?: byteArrayOf())
    }

    fun t174(dt174: ByteArray?) = buildTlv(0x174) {
        writeBytes(dt174 ?: byteArrayOf())
    }

    fun t177() = buildTlv(0x177) {
        writeBoolean(true)
        writeInt(protocolInfo.buildTime)
        writeStringWithShortLen(protocolInfo.sdkVersion)
    }

    fun t17a() = buildTlv(0x17a) {
        // 这个9 是smsAppid
        writeInt(9)
    }

    fun t17c(code: String) = buildTlv(0x17c) {
        writeShort(code.length)
        writeString(fun(): String {
            val newCode = code.replace(" ", "")
            if (newCode.length == 6) {
                return newCode
            }
            runtimeError("验证码不合法")
        }())
    }

    fun t187() = buildTlv(0x187) {
        writeBytes(MD5.toMD5Byte(deviceInfo.macAddress))
    }

    fun t188() = buildTlv(0x188) {
        writeBytes(MD5.toMD5Byte(deviceInfo.androidId.ifEmpty { deviceInfo.imei }))
    }

    fun t191() = buildTlv(0x191) {
        writeByte(0x82.toByte())
    }

    fun t194() = buildTlv(0x194) {
        writeBytes(MD5.toMD5Byte(deviceInfo.imsi))
    }

    fun t197() = buildTlv(0x197) {
        writeByte(0.toByte())
    }

    fun t198() = buildTlv(0x198) {
        writeByte(0.toByte())
    }

    fun t201(payToken : ByteArray) = buildTlv(0x201) {
        writeBytesWithShortLen(payToken)
        writeStringWithShortLen("")
        writeStringWithShortLen("qq")
        writeStringWithShortLen("") // channelId
    }

    fun t202() = buildTlv(0x202) {
        writeBytesWithShortLen(MD5.toMD5Byte(deviceInfo.wifiBSsid))
        writeStringWithShortLen(deviceInfo.wifiSsid)
    }

    fun t400(g: ByteArray) = buildTlv(0x400) {
        writeTeaEncrypt(g) {
            writeByte(1) // version
            writeLong(account.uin)
            writeFully(deviceInfo.guid)
            writeFully(session.pwd)
            writeInt(protocolInfo.appId)
            writeInt(protocolInfo.subAppId)
            writeInt(currentTimeSeconds())
            writeFully(session.randSeed)
        }
    }

    fun t318(t318 : ByteArray) = buildTlv(0x318) {
        writeBytes(t318)
    }

    fun t401(g: ByteArray) = buildTlv(0x401) {
        /**
        builder.writeBytes(deviceInfo.guid)
        builder.writeBytes(dataManager.wLoginSigInfo.dpwd)
        builder.writeBytes(dt402)
         **/
        writeBytes(g)
    }

    fun t402(dt402: ByteArray) = buildTlv(0x402) {
        writeBytes(dt402)
    }

    fun t403(dt403: ByteArray) = buildTlv(0x403) {
        writeBytes(dt403)
    }

    fun t511(domains : Array<String> = arrayOf(
        "accounts.qq.com",
        "aq.qq.com",
        "buluo.qq.com",
        "connect.qq.com",
        "docs.qq.com",
        "game.qq.com",
        "gamecenter.qq.com",
        "graph.qq.com",
        "id.qq.com",
        "imgcache.qq.com",
        "mail.qq.com",
        "openmobile.qq.com",
        "qun.qq.com",
        "qzone.com",
        "tenpay.com",
        "ti.qq.com",
        "v.qq.com",
        "vip.qq.com",
        "y.qq.com",
        "office.qq.com"
    )) = buildTlv(0x511) {
        // 备选  "haoma.qq.com","mma.qq.com","om.qq.com","kg.qq.com",
        writeShort(domains.size)
        for (domain in domains) {
            val start = domain.indexOf('(')
            val end = domain.indexOf(')')
            var b: Byte = 1
            if (start == 0 || end > 0) {
                val i = domain.substring(start + 1, end).toInt()
                b = if (1048576 and i > 0) {
                    1
                } else {
                    0
                }
                if (i and 0x08000000 > 0) {
                    b = (b or 2.toByte())
                }
            }
            writeByte(b)
            writeStringWithShortLen(domain)
        }
    }

    fun t516() = buildTlv(0x516) {
        writeInt(0)
    }

    fun t521() = buildTlv(0x521) {
        writeInt(0)
        writeShort(0)
    }

    private fun t536(loginExtraData: Collection<LoginExtraData> = listOf()) = buildTlv(0x536) {
        writeByte(1)
        writeByte(loginExtraData.size.toByte())
        for (extraData in loginExtraData) {
            writeLong(extraData.uin)
            writeByte(extraData.ip.size.toByte())
            writeBytes(extraData.ip)
            writeInt(extraData.time)
            writeInt(extraData.appId)
        }
    }

    fun t525(loginExtraData: Collection<LoginExtraData> = listOf()) = buildTlv(0x525) {
        writeShort(1)
        writeBytes(t536(loginExtraData))
    }

    private fun t52d() = buildTlv(0x52d) {
        writeBytes(deviceInfo.deviceReport.toByteArray())
    }

    fun t542() = buildTlv(0x542) {
        writeByte(0.toByte())
        writeByte(0.toByte())
    }

    fun t545() = buildTlv(0x545) {
        // qimei
        writeBytes(deviceInfo.qimei.toByteArray())
    }

    fun t547(byteArray: ByteArray) = buildTlv(0x547) {
        writeFully(byteArray)
    }

    fun t193(ticket: String) = buildTlv(0x193) {
        writeString(ticket)
    }

    fun t544(subCmd: Int) = buildTlv(0x544) {
        val randSeed = "Lhv2hDi3Ew".toByteArray() // $hv2hDi3Ew 10位随机种子（我固定了而已）
        val hashKey = ByteDataHash.generateKey(randSeed) // https://blog.seeflower.dev/archives/54/
        val content = newBuilder().apply {
            writeLongToBuf32(account.uin) // 4 bytes
            writeBytesWithShortLen(deviceInfo.guid) // short len + body
            writeBytesWithShortLen(protocolInfo.sdkVersion.toByteArray()) // short len + body (6.0.0.2475)
            writeInt(subCmd) // 登录的subCmd 例如：密码登录是9 滑块是2
        }.toByteArray()

        writeString("heha") // 固定

        writeInt(1)
        writeIntLittleEndian(1) // 写数字1 然后分别翻转 （大，小端序）

        writeByte(0)
        writeInt(1)

        writeInt(0x308)

        val builder = newBuilder()
        builder.writeInt(1)
        builder.writeBlockWithIntLen {
            // 这里的id可能是一种序列化格式 结构和tlv差不多
            // id
            // short size
            // body

            writeShort(1)
            writeBlockWithShortLen {
                writeLong(System.currentTimeMillis())
            }

            writeShort(2)
            writeBlockWithShortLen {
                writeBytes(randSeed)
            }

            writeShort(3)
            writeBlockWithShortLen {
                writeShort(256)
                writeShort(1)
            }

            writeShort(5)
            writeBlockWithShortLen {
                writeShort(256)
                writeShort(1)
            }

            writeShort(4)
            writeBlockWithShortLen {
                writeInt(0)
            }

            writeShort(6)
            writeBlockWithShortLen {
                writeShort(256)
                writeShort(4)
            }

            writeShort(7)
            writeBlockWithShortLen {
                writeShort(256)
                writeShort(2)
            }

            writeShort(8)
            writeBlockWithShortLen {
                writeShort(256)
                writeShort(3)
            }

            writeShort(9)
            writeBlockWithShortLen {
                writeBytes(ByteDataHash.generateHash(hashKey, content))
            }

            writeShort(10)
            writeBlockWithShortLen {
                writeBytes(MD5.toMD5Byte(ByteDataHash.encryptData(content, hashKey)))
            }

            writeShort(11)
            writeBlockWithShortLen {
                writeBytes( MD5.toMD5Byte(
                    fun(): ByteArray {
                        val data = ByteDataHash.salsa20(hashKey)
                        return ByteArray(content.size) {
                            content[it].xor(data[it])
                        }
                    }()
                ))
            }
        }

        val crc32 = CRC32()
        val data = builder.toByteArray()
        crc32.update(data)

        writeLong(crc32.value)

        writeBytes(data)
    }
}

internal inline fun buildTlv(tlvVer: Int, block: BytePacketBuilder.() -> Unit): ByteArray {
    val bodyBuilder = BytePacketBuilder()
    val out = BytePacketBuilder()
    bodyBuilder.block()
    out.writeShort(tlvVer.toShort())
    out.writeShort(bodyBuilder.size.toShort())
    out.writePacket(bodyBuilder)
    return out.toByteArray()
}

// fuqiuluo: Woc!
// fbk: rubbish code...
internal object ByteDataHash {
    fun encryptData(p0: ByteArray, p1: ByteArray): ByteArray {
        val state = initState(p1)
        var i1 = 0
        var i2 = 0
        val p01 = ByteArray(p0.size)
        p0.forEachIndexed { index, byte ->
            i1 = (i1 + 1) % 256
            val t2 = i1
            if (i1 < 0) {
                i1 += state.size
            }
            val tmp = state[i1]
            i2 = (i2 + tmp) % 256
            val t1 = i2
            if (i2 < 0) {
                i2 += state.size
            }
            state[i1] = state[i2]
            state[i2] = tmp
            val ki = (state[i2] + state[i1]) and 0xff
            i2 = t1
            i1 = t2
            p01[index] = byte.and(state[ki].inv()).or(state[ki].and(byte.inv()))
        }
        return p01
    }

    private fun initState(key: ByteArray): ByteArray {
        var index = 0
        val state = ByteArray(256) { it.toByte() }
        var k = key.copyOf()
        repeat(256 / key.size) { k += key }
        k = k.sub(0, 256)!!
        repeat(256) { i ->
            index = (index + k[i] + state[i]) % 256
            val t1 = index
            if (index < 0) {
                index += state.size
            }
            val tmp = state[i]
            state[i] = state[index]
            state[index] = tmp
            index = t1
        }
        return state
    }

    fun generateHash(p0: ByteArray, p1: ByteArray): ByteArray { // HMAC-SHA256
        val v1 = ByteArray(64) { 0x36 }
        val v2 = ByteArray(64) { 0x5c }
        repeat(if (p0.size < v1.size) p0.size else v1.size) { v3 -> v1[v3] = p0[v3].xor(v1[v3]) }
        repeat(if (p0.size < v2.size) p0.size else v2.size) { v3 ->
            v2[v3] = ((p0[v3].and(0xe5) + p0[v3].inv().and(0x1a))
                .xor(v2[v3].and(0xe5) + v2[v3].inv().and(0x1a))).toByte()
        }
        return hash(v2 + hash(v1 + p1))
    }

    fun generateKey(p0: ByteArray): ByteArray {
        val v1 = "4c3f286b54372a406124".hex2ByteArray()
        val v2 = "4061392b3e365829264f".hex2ByteArray()
        val v3 = if (p0.size < v2.size) p0.size else v2.size
        val v4 = ByteArray(v3)
        repeat(v3) { v5 -> v4[v5] = p0[v5].xor(v2[v5]) }
        val v5 = v4.sub(0, 8)!!
        val v6 = v4.sub(8, 2)!!
        return v6 + v1 + v5 // such as: 4f274c3f286b54372a40612428095143565e3140
    }

    /**
     * SHA-256
     */
    private val K = intArrayOf(
        0x428a2f98, 0x71374491, -0x4a3f0431, -0x164a245b, 0x3956c25b, 0x59f111f1, -0x6dc07d5c, -0x54e3a12b,
        -0x27f85568, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, -0x7f214e02, -0x6423f959, -0x3e640e8c,
        -0x1b64963f, -0x1041b87a, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
        -0x67c1aeae, -0x57ce3993, -0x4ffcd838, -0x40a68039, -0x391ff40d, -0x2a586eb9, 0x06ca6351, 0x14292967,
        0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, -0x7e3d36d2, -0x6d8dd37b,
        -0x5d40175f, -0x57e599b5, -0x3db47490, -0x3893ae5d, -0x2e6d17e7, -0x2966f9dc, -0xbf1ca7b, 0x106aa070,
        0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
        0x748f82ee, 0x78a5636f, -0x7b3787ec, -0x7338fdf8, -0x6f410006, -0x5baf9315, -0x41065c09, -0x398e870e
    )
    private val H0 = intArrayOf(0x6A09E669, -0x44985179, 0x3C6BF372, -0x5ab00ac6, -0x64da9774, 0x511E527F, 0x1F73D9AB, 0x5BD0CD19)

    private val BLOCK_BITS = 512
    private val BLOCK_BYTES = BLOCK_BITS / 8

    // working arrays
    private val W = IntArray(64)
    private val H = IntArray(8)
    private val TEMP = IntArray(8)

    private fun hash(message: ByteArray): ByteArray {
        // let H = H0

        System.arraycopy(H0, 0, H, 0, H0.size)

        // initialize all words
        val words = pad(message)

        // enumerate all blocks (each containing 16 words)
        var i = 0
        val n = words.size / 16
        while (i < n) {


            // initialize W from the block's words
            System.arraycopy(words, i * 16, W, 0, 16)
            for (t in 16 until W.size) {
                W[t] = smallSig1(W[t - 2]) + W[t - 7] + smallSig0(W[t - 15]) + W[t - 16]
            }

            // let TEMP = H
            System.arraycopy(H, 0, TEMP, 0, H.size)

            // operate on TEMP
            for (t in W.indices) {
                val t1 = TEMP[7] + bigSig1(TEMP[4]) + ch(
                    TEMP[4],
                    TEMP[5], TEMP[6]
                ) + K[t] + W[t]
                val t2 = bigSig0(TEMP[0]) + maj(TEMP[0], TEMP[1], TEMP[2])
                System.arraycopy(TEMP, 0, TEMP, 1, TEMP.size - 1)
                TEMP[4] += t1
                TEMP[0] = t1 + t2
            }

            // add values in TEMP to values in H
            for (t in H.indices) {
                H[t] += TEMP[t]
            }
            ++i
        }
        val buf: ByteBuffer = ByteBuffer.allocate(H.size * Integer.BYTES)
        H.forEach { buf.putInt(it) }
        return buf.array()
    }

    private fun pad(message: ByteArray): IntArray {
        // new message length: original + 1-bit and padding + 8-byte length
        // --> block count: whole blocks + (padding + length rounded up)
        val finalBlockLength = message.size % BLOCK_BYTES
        val blockCount = message.size / BLOCK_BYTES + if (finalBlockLength + 1 + 8 > BLOCK_BYTES) 2 else 1
        val result = IntBuffer.allocate(blockCount * (BLOCK_BYTES / Integer.BYTES))

        // copy as much of the message as possible
        val buf: ByteBuffer = ByteBuffer.wrap(message)
        var i = 0
        val n = message.size / Integer.BYTES
        while (i < n) {
            result.put(buf.getInt())
            ++i
        }
        // copy the remaining bytes (less than 4) and append 1 bit (rest is zero)
        val remainder: ByteBuffer = ByteBuffer.allocate(4)
        remainder.put(buf).put(128.toByte()).rewind()
        result.put(remainder.getInt())

        // ignore however many pad bytes (implicitly calculated in the beginning)
        result.position(result.capacity() - 2)
        // place original message length as 64-bit integer at the end
        val msgLength = message.size * 8L
        result.put((msgLength ushr 32).toInt())
        result.put(msgLength.toInt())
        return result.array()
    }

    private fun ch(x: Int, y: Int, z: Int): Int {
        return x and y or (x.inv() and z)
    }

    private fun maj(x: Int, y: Int, z: Int): Int {
        return x and y or (x and z) or (y and z)
    }

    private fun bigSig0(x: Int): Int {
        return (Integer.rotateRight(x, 2)
                xor Integer.rotateRight(x, 13)
                xor Integer.rotateRight(x, 22))
    }

    private fun bigSig1(x: Int): Int {
        return (Integer.rotateRight(x, 6)
                xor Integer.rotateRight(x, 11)
                xor Integer.rotateRight(x, 25))
    }

    private fun smallSig0(x: Int): Int {
        return (Integer.rotateRight(x, 7)
                xor Integer.rotateRight(x, 18)
                xor (x ushr 3))
    }

    private fun smallSig1(x: Int): Int {
        return (Integer.rotateRight(x, 17)
                xor Integer.rotateRight(x, 19)
                xor (x ushr 10))
    }

    /**
     * salsa20
     */
    private const val ROUNDS = 10 // tx魔改为10
    private const val MASK = 0xffffffff

    fun salsa20(randSeedKey: ByteArray): ByteArray { // 乱写的
        // require(nonce.size == 8)
        // require(blockCounter.size == 8)

        //  4 bytes + 20 bytes + 8 bytes = 32 bytes
        val key = randSeedKey + 0x4adc4d1f.toByteArray() + 0x9ad47e38.toByteArray()
        val nonce = 0x9ad47e38.toByteArray() // 8 bytes
        val blockCounter = 0x90a5eb50.toByteArray() // 8 bytes

        val k = Array(8) { littleEndian(key.sliceArray(( (4 * it) until (4 * it + 4) ))).toLong() }
        val n = Array(2) { littleEndian(nonce.sliceArray(( (4 * it) until (4 * it + 4) ))).toLong() }
        val b = Array(2) { littleEndian(blockCounter.sliceArray(( (4 * it) until (4 * it + 4) ))).toLong() }
        val sigma = arrayOf(0x61707865L, 0x3320646e, 0x79622d32, 0x6b206574)

        /*
        val result = ByteArray(this.size)
        for (i in 0 until this.size) {
        result[i] = (this[i] xor key[i % key.size] xor ((i and 0xFF).toByte()))
        }
        return result*/

        var s = arrayOf(
            sigma[0],
            k[0], k[1], k[2], k[3],
            sigma[1],
            n[0], n[1], b[0], b[1],
            sigma[2],
            k[4], k[5], k[6], k[7],
            sigma[3]
        )

        repeat(ROUNDS) {
            s = round(s)
        }

        val outv = ByteArray(64)

        storeLittleEndian(outv, 0, s[0])
        storeLittleEndian(outv, 4, s[1])
        storeLittleEndian(outv, 8, s[2])
        storeLittleEndian(outv, 12, s[3])
        storeLittleEndian(outv, 16, s[4])
        storeLittleEndian(outv, 20, s[5])
        storeLittleEndian(outv, 24, s[6])
        storeLittleEndian(outv, 28, s[7])
        storeLittleEndian(outv, 32, s[8])
        storeLittleEndian(outv, 36, s[9])
        storeLittleEndian(outv, 40, s[10])
        storeLittleEndian(outv, 44, s[11])
        storeLittleEndian(outv, 48, s[12])
        storeLittleEndian(outv, 52, s[13])
        storeLittleEndian(outv, 56, s[14])
        storeLittleEndian(outv, 60, s[15])

        return outv
    }

    private fun storeLittleEndian(a: ByteArray, offset: Int, b: Long) {
        var tmp = b
        a[offset] = tmp.toByte()
        tmp = tmp ushr 8
        a[offset + 1] = tmp.toByte()
        tmp = tmp ushr 8
        a[offset + 2] = tmp.toByte()
        tmp = tmp ushr 8
        a[offset + 3] = tmp.toByte()
    }

    private fun round(s: Array<Long>): Array<Long> {
        // 12 4 13 5 14 6 15 7 0 8 1 9 2 10 3 11
        gen(s, 0x0, 0x4, 0xc, 0x10)
        gen(s, 0x8, 0xc, 0x4, 0x0c, false)
        gen(s, 0x0, 0x4, 0xc, 0x08)
        gen(s, 0x8, 0xc, 0x4, 0x07)
        gen(s, 0x1, 0x5, 0xd, 0x10)
        gen(s, 0x9, 0xd, 0x5, 0x0c, false)
        gen(s, 0x1, 0x5, 0xd, 0x08)
        gen(s, 0x9, 0xd, 0x5, 0x07)

        gen(s, 0x2, 0x6, 0xe, 0x10)
        gen(s, 0xa, 0xe, 0x6, 0x0c, false)
        gen(s, 0x2, 0x6, 0xe, 0x08)
        gen(s, 0xa, 0xe, 0x6, 0x07)
        gen(s, 0x3, 0x7, 0xf, 0x10)
        gen(s, 0xb, 0xf, 0x7, 0x0c, false)
        gen(s, 0x3, 0x7, 0xf, 0x08)
        gen(s, 0xb, 0xf, 0x7, 0x07)

        gen(s, 0x4, 0x8, 0x0, 0x10)
        gen(s, 0xc, 0x0, 0x8, 0x0c, false)
        gen(s, 0x4, 0x8, 0x0, 0x08)
        gen(s, 0xc, 0x0, 0x8, 0x07)
        gen(s, 0x5, 0x9, 0x1, 0x10)
        gen(s, 0xd, 0x1, 0x9, 0x0c, false)
        gen(s, 0x5, 0x9, 0x1, 0x08)
        gen(s, 0xd, 0x1, 0x9, 0x07)

        gen(s, 0x6, 0xa, 0x2, 0x10)
        gen(s, 0xe, 0x2, 0xa, 0x0c, false)
        gen(s, 0x6, 0xa, 0x2, 0x08)
        gen(s, 0xe, 0x2, 0xa, 0x07)
        gen(s, 0x7, 0xb, 0x3, 0x10)
        gen(s, 0xf, 0x3, 0xb, 0x0c, false)
        gen(s, 0x7, 0xb, 0x3, 0x08)
        gen(s, 0xf, 0x3, 0xb, 0x07)

        return arrayOf(
            s[0], s[4], s[8], s[12],
            s[1], s[5], s[9], s[13],
            s[2], s[6], s[10], s[14],
            s[3], s[7], s[11], s[15]
        )
    }

    private fun gen(s: Array<Long>, i1: Int, i2: Int, ti: Int, lsl: Int, xor: Boolean = true) {
        s[i1] = (s[i1] + s[i2]).and(MASK)
        if (xor) {
            s[ti] = s[ti].xor(s[i1])
        } else {
            s[ti] = calc(s[ti], s[i1])
        }
        s[ti] = rotl32(s[ti].and(MASK), lsl)
    }

    private fun rotl32(w: Long, v: Int): Long { // rotate left for 32-bits
        return w.shl(v).and(MASK).or(w.shr(32 - v))
    }

    private fun calc(a: Long, b: Long): Long {
        return a and b.inv() or (b and a.inv())
    }

    private fun littleEndian(b: ByteArray): Int {
        require(b.size == 4)
        return b[0].toInt() xor (b[1].toInt() shl 8) xor (b[2].toInt() shl 16) xor (b[3].toInt() shl 24)
    }
}
