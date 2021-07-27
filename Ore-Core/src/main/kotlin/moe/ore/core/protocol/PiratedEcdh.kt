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

package moe.ore.core.protocol

//import com.google.crypto.tink.config.TinkConfig
import moe.ore.helper.hex2ByteArray
import moe.ore.util.MD5
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.ECGenParameterSpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.KeyAgreement


/**
 * ECDH固定 非常规协议操作 跳过ECDH
 * 所以可以固定ECDH
 */

const val ECDH_VERSION = 2

val ECDH_PUBLIC_KEY =
    "04803E9940F3FD8E6474C3CC6994A2F972B1DA6BFDE8DDB4E775E36AB4E439DB8EA7A0E6CAFC722089F4921DFEAEFBA0F56932F3E6AA3ECF81154FD230AF32B18F".hex2ByteArray()

val ECDH_SHARE_KEY = "3F539B2549AB1F71421F2C3A66298D05".hex2ByteArray()

private val x509Prefix = "3059301306072a8648ce3d020106082a8648ce3d030107034200".hex2ByteArray()

/**
 * https://keyrotate.qq.com/rotate_key?cipher_suite_ver=%s&uin=%s
 */
class PiratedEcdh(
    private var svrPubKey : ByteArray = "04EBCA94D733E399B2DB96EACDD3F69A8BB0F74224E2B44E3357812211D2E62EFBC91BB553098E25E33A799ADC7F76FEB208DA7C6522CDB0719A305180CC54A82E".hex2ByteArray(),
    var shareKey : ByteArray = ECDH_SHARE_KEY,
    var publicKey : ByteArray = ECDH_PUBLIC_KEY
) {
    private var hasBcLib = false

    lateinit var x509PublicKey : PublicKey
    lateinit var pkcs8PrivateKey : PrivateKey

    init {
//        TinkConfig.register()
        //if(initShareKeyByBouncycastle() == 0) {
        //    hasBcLib = true
        //    println("can use ecdh lib")
        //}
    }

    private fun constructX509PublicKey(bs: ByteArray): PublicKey {
        val kf = KeyFactory.getInstance("EC", "BC")
        return kf.generatePublic(X509EncodedKeySpec(bs))
    }

    fun calcShareKeyByBouncycastle(bs: ByteArray) : ByteArray? {
        try {
            val pub = constructX509PublicKey(
                x509Prefix + bs
            )
            val agreement = KeyAgreement.getInstance("ECDH", "BC")
            agreement.init(pkcs8PrivateKey)
            agreement.doPhase(pub, true)
            val sKey = ByteArray(16).also { System.arraycopy(agreement.generateSecret(), 0, it, 0, 16) }
            return MD5.toMD5Byte(sKey)
        } catch (e : Exception) {
            println("calc share key fail : " + e.message)
        }
        return null
    }

    fun initShareKeyByBouncycastle() : Int {
        try {
            val pairGenerator = KeyPairGenerator.getInstance("EC", "BC")
            pairGenerator.initialize(ECGenParameterSpec("prime256v1"))
            val pair = pairGenerator.genKeyPair()
            val pubKey = pair.public
            val pubBytes = pubKey.encoded
            val priKey = pair.private
            val priBytes = priKey.encoded
            val pub = constructX509PublicKey(
                x509Prefix + svrPubKey
            )
            val agreement = KeyAgreement.getInstance("ECDH", "BC")
            agreement.init(priKey)
            agreement.doPhase(pub, true)
            val sKey = ByteArray(16).also { System.arraycopy(agreement.generateSecret(), 0, it, 0, 16) }
            this.shareKey = MD5.toMD5Byte(sKey)
            this.publicKey = ByteArray(65).also { System.arraycopy(pubBytes, 26, it, 0, 65) }

            this.pkcs8PrivateKey = priKey
            this.x509PublicKey = pubKey
            return 0
        } catch (e : Exception) {
            println("ecdh lib not found : " + e.message)
        }
        return -1
    }

}
