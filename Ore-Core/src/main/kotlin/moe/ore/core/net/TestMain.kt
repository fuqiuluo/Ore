package moe.ore.core.net

import kotlin.Throws
import java.lang.InterruptedException
import kotlin.jvm.JvmStatic
import moe.ore.core.net.PackRequest.OnDataListener
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object TestMain {
    @Throws(InterruptedException::class)
    @JvmStatic
    fun main(args: Array<String>) {
//        val reqBody = ByteArray(10)
//
//        val botClient: BotClient = BotClient().connect()
//
//        val bytes = botClient.newPackRequest("cmda", 1, reqBody).await()
//
//        println("result：" + Arrays.toString(bytes))
//
//        botClient.newPackRequest("cmdb", 2, reqBody).async(object : OnDataListener {
//            override fun onReceive(data: ByteArray?) {
//                println("result：" + Arrays.toString(data))
//            }
//        })
//        println(Objects.hash("cmdName", "requestId"))
//        println(Objects.hash("cmdName", "requestId"))
//        AA().setPa(1,"PackRequest(1)")
//        var aa = AA()
//        aa.setPa(2,"PackRequest(2)")
//        val aa1 = AA();
//        aa1.setPa(3,"PackRequest(3)")
//        println(aa)
//        println(aa1)
//        println(AA().get())

        val cmdName = null
        val requestId = 121
        println("$cmdName,$requestId")

    }

    class AA {
        companion object{
            private val packHandlerMap: ConcurrentHashMap<Int, String> = ConcurrentHashMap()
        }
        fun setPa(int: Int,packRequest: String){
            packHandlerMap[int] = packRequest
        }
        fun get(): ConcurrentHashMap<Int, String> {
            return packHandlerMap
        }

    }
}