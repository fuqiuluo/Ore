package moe.ore.core.net

import kotlin.Throws
import java.lang.InterruptedException
import kotlin.jvm.JvmStatic
import java.util.Arrays
import moe.ore.core.net.PackRequest.OnDataListener

object TestMain {
    @Throws(InterruptedException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val reqBody = ByteArray(10)

        val botClient: BotClient = BotClient().connect()

        val bytes = botClient.newPackRequest("cmda", 1, reqBody).await()

        println("result：" + Arrays.toString(bytes))

        botClient.newPackRequest("cmdb", 2, reqBody).async(object : OnDataListener {
            override fun onReceive(data: ByteArray?) {
                println("result：" + Arrays.toString(data))
            }
        })
    }
}