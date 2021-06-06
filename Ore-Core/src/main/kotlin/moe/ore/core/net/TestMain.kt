package moe.ore.core.net

import moe.ore.core.DataManager
import kotlin.Throws
import java.lang.InterruptedException
import kotlin.jvm.JvmStatic
import moe.ore.core.protocol.util.toAsciiHexString

object TestMain {
    @Throws(InterruptedException::class)
    @JvmStatic
    fun main(args: Array<String>) {
//        val reqBody = ByteArray(10)
//
//        val botClient: BotClient = BotClient().connect()
////
//        var reqV2Simplify = GetTroopListRespV2()
//        val reqBody = buildJcePacket("friendlist.GetTroopListReqV2", reqV2Simplify)
//        val result = botClient.newPackRequest("cmda", 1, reqBody).await()
//        if (Objects.isNull(result)){
////            处理重试或者提示错误给gui
//            return
//        }
//       val getTroopListRespV2 = useJceByWaiter(result,GetTroopListRespV2())
//        ........
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

//        val manager1 = DataManager.init(1234u,"data")
        val manager = DataManager.manager(1234u)
        println(manager.dataPath)
        println(manager.protocol)
        println(manager.sigInfo)
        println(manager.deviceInfo.androidId)
        DataManager.destroy(1234u)
        println("E"+ DataManager.manager(1234u).deviceInfo.toByteArray().toAsciiHexString())
    }

}