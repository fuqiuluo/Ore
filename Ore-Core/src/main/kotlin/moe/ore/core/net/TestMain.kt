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
 * 使用该项目产生的违法行为，由第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 *  オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 *
 */

package moe.ore.core.net

import moe.ore.core.helper.DataManager

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


    }

}