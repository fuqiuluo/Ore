package moe.ore.core.net

import moe.ore.core.DataManager

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
        val listOfAuthorizedDomainNames = arrayOf("accounts.qq.com", "aq.qq.com", "buluo.qq.com", "connect.qq.com", "docs.qq.com", "game.qq.com", "gamecenter.qq.com", "graph.qq.com", "haoma.qq.com", "id.qq.com", "imgcache.qq.com", "kg.qq.com", "mail.qq.com", "mma.qq.com", "office.qq.com", "openmobile.qq.com", "ptlogin2.qq.com", "qqweb.qq.com", "qun.qq.com", "qzone.com", "qzone.qq.com", "tenpay.com", "ti.qq.com", "v.qq.com", "vip.qq.com")

//        初始化
        DataManager.init(1234u, "/Users/Smile/Desktop")

//        获取
        val manager = DataManager.manager(1234u)
        println(manager.dataPath)
        println(manager.protocolInfo)
        println(manager.wLoginSigInfo.d2Key)
        println(manager.recorder.nextSeq())
        manager.deviceInfo.wifiBSsid = "jjjjjj"

//        保存到本地
        DataManager.flush(1234u)
//        DataManager.destroy(1234u)
//          DataManager.init(1234u,"/Users/Smile/Desktop")
        println(DataManager.manager(1234u).deviceInfo.imei)
    }

}