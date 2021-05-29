package moe.ore.util

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import moe.ore.util.OkhttpUtil.Companion.SSLSocketClient.getHostnameVerifier
import moe.ore.util.OkhttpUtil.Companion.SSLSocketClient.getSSLSocketFactory
import moe.ore.util.OkhttpUtil.Companion.SSLSocketClient.getX509TrustManager
import java.io.IOException
import java.lang.Exception
import java.net.Proxy
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

class OkhttpUtil(
    /**
     * 禁止使用代理
     */
    private val proxy : Boolean = true) {
    private val clientBuilder = OkHttpClient.Builder().apply {
        readTimeout(readTimeOut, TimeUnit.SECONDS)
        connectTimeout(connectTimeout, TimeUnit.SECONDS)
        writeTimeout(writeTimeout, TimeUnit.SECONDS)
        sslSocketFactory(getSSLSocketFactory()!!, getX509TrustManager()!!)
        hostnameVerifier(getHostnameVerifier())
        if(!proxy) proxy(Proxy.NO_PROXY)
    }
    private val requestBuilder = Request.Builder()

    var readTimeOut = 60L
        set(value) {
            field = value
            clientBuilder.readTimeout(field, TimeUnit.SECONDS)
        }
    var connectTimeout = 60L
        set(value) {
            field = value
            clientBuilder.connectTimeout(field, TimeUnit.SECONDS)
        }
    var writeTimeout = 60L
        set(value) {
            field = value
            clientBuilder.writeTimeout(field, TimeUnit.SECONDS)
        }

    fun cookie(content : String) = header("cookie", content)

    fun removeCookie() = removeHeader("cookie")

    fun header(key : String, content : String) = requestBuilder.addHeader(key, content)

    fun removeHeader(key: String) = requestBuilder.removeHeader(key)

    fun get(url : String) : Response? {
        val request = requestBuilder.get().url(url).build()
        val call = clientBuilder.build().newCall(request)
        var response: Response? = null
        try {
            response = call.execute()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return response
    }

    fun getSync(url : String, netCall: NetCall) {
        val request = requestBuilder.get().url(url).build()
        val call = clientBuilder.build().newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = netCall.failed(call, e)

            override fun onResponse(call: Call, response: Response) = netCall.success(call, response)
        })
    }

    fun post(url : String, body : RequestBody) : Response? {
        val call = clientBuilder.build().newCall(requestBuilder.post(body).url(url).build())
        var response: Response? = null
        try {
            response = call.execute()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return response
    }

    fun post(url : String, bodyParams : Map<String, String>) : Response? {
        val body = toRequestBody(bodyParams)
        return post(url, body)
    }

    fun postSync(url : String, bodyParams : Map<String, String>, netCall: NetCall) {
        val body = toRequestBody(bodyParams)
        clientBuilder.build().newCall(requestBuilder.post(body).url(url).build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = netCall.failed(call, e)

            override fun onResponse(call: Call, response: Response) = netCall.success(call, response)
        })
    }

    fun postData(url : String, data : String) : Response? {
        val body = RequestBody.create("text/html;charset=utf-8".toMediaTypeOrNull(), data)
        val call = clientBuilder.build().newCall(requestBuilder.post(body).url(url).build())
        var response: Response? = null
        try {
            response = call.execute()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return response
    }

    fun postDataSync(url : String, data: String, netCall: NetCall) {
        val body = RequestBody.create("text/html;charset=utf-8".toMediaTypeOrNull(), data)
        val call = clientBuilder.build().newCall(requestBuilder.post(body).url(url).build())
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = netCall.failed(call, e)

            override fun onResponse(call: Call, response: Response) = netCall.success(call, response)
        })
    }

    fun postJson(url : String, json : String) : Response? {
        val body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json)
        val call = clientBuilder.build().newCall(requestBuilder.post(body).url(url).build())
        var response: Response? = null
        try {
            response = call.execute()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return response
    }

    fun postJsonSync(url : String, json: String, netCall: NetCall) {
        val body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json)
        val call = clientBuilder.build().newCall(requestBuilder.post(body).url(url).build())
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) = netCall.failed(call, e)

            override fun onResponse(call: Call, response: Response) = netCall.success(call, response)
        })
    }

    fun defaultUserAgent() = header("User-Agent", DefaultUserAgent)

    companion object {
        const val DefaultUserAgent = "Mozilla/5.0 (Linux; Android 11; M2002J9E Build/RKQ1.200826.002; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/77.0.3865.120 MQQBrowser/6.2 TBS/045514 Mobile Safari/537.36 V1_AND_SQ_8.5.5_1630_YYB_D A_8050500 QQ/8.5.5.5105 NetType/WIFI WebP/0.3.0 Pixel/1080 StatusBarHeight/69 SimpleUISwitch/0 QQTheme/1000 InMagicWin/0"

        /**
         * post的请求参数，构造RequestBody
         *
         * @param bodyParams 请求参数
         */
        private fun toRequestBody(bodyParams: Map<String, String>?): RequestBody {
            val formEncodingBuilder = FormBody.Builder()
            if (bodyParams != null) {
                val iterator = bodyParams.keys.iterator()
                var key = ""
                while (iterator.hasNext()) {
                    key = iterator.next()
                    bodyParams[key]?.let { formEncodingBuilder.add(key, it) }
                }
            }
            return formEncodingBuilder.build()
        }

        /**
         * 自定义网络回调接口
         */
        interface NetCall {
            @Throws(IOException::class)
            fun success(call: Call, response: Response)
            fun failed(call: Call, e: IOException)
        }

        object SSLSocketClient {
            fun getSSLSocketFactory(): SSLSocketFactory? {
                return try {
                    val sslContext = SSLContext.getInstance("SSL")
                    sslContext.init(null, getTrustManager(), SecureRandom())
                    sslContext.socketFactory
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }

            //获取TrustManager
            private fun getTrustManager(): Array<TrustManager> {
                return arrayOf(
                    object : X509TrustManager {
                        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    }
                )
            }

            fun getHostnameVerifier(): HostnameVerifier {
                return HostnameVerifier { s: String?, _: SSLSession? -> true }
            }

            fun getX509TrustManager(): X509TrustManager? {
                var trustManager: X509TrustManager? = null
                try {
                    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                    trustManagerFactory.init(null as KeyStore?)
                    val trustManagers = trustManagerFactory.trustManagers
                    check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                        "Unexpected default trust managers:" + Arrays.toString(
                            trustManagers
                        )
                    }
                    trustManager = trustManagers[0] as X509TrustManager
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return trustManager
            }
        }
    }
}