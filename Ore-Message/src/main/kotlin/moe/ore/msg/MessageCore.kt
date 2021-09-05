package moe.ore.msg

import kotlinx.io.core.discardExact
import kotlinx.io.core.readUInt
import moe.ore.api.Ore
import moe.ore.core.OreBot
import moe.ore.core.helper.DataManager
import moe.ore.core.net.packet.FromService
import moe.ore.core.servlet.MSFServlet
import moe.ore.helper.ifNotNull
import moe.ore.helper.toByteReadPacket
import moe.ore.msg.cache.ImageCache
import moe.ore.msg.code.*
import moe.ore.msg.event.TroopMsgEvent
import moe.ore.msg.msg.MsgType
import moe.ore.msg.msg.MsgType.C2C
import moe.ore.msg.msg.MsgType.TROOP
import moe.ore.msg.protocol.protobuf.MsgElemInfoServiceType3
import moe.ore.msg.protocol.protobuf.MsgElemInfoServiceType33
import moe.ore.msg.protocol.protobuf.PbPushMsg
import moe.ore.msg.protocol.protobuf.RichText
import moe.ore.protobuf.decodeProtobuf

const val TAG_MESSAGE_CENTER = "MESSAGE_CENTER"

@ExperimentalUnsignedTypes
class MessageCenter(
    private val ore: OreBot
): MSFServlet(arrayOf(
    "OnlinePush.PbPushGroupMsg"
    // OnlinePush.PbPushGroupMsg || OnlinePush.PbPushDisMsg || OnlinePush.PbC2CMsgSync || OnlinePush.PbPushC2CMsg || OnlinePush.PbPushBindUinMsg
)) {
    private val config: CoreConfig = CoreConfig()
    private val manager = DataManager.manager(ore.uin)

    init {
        super.init(ore.client)
        ImageCache.init(ore.uin, manager.dataPath)
    }

    /** event **/
    private lateinit var troopMsgEvent: TroopMsgEvent

    override fun onReceive(from: FromService) {
        when(from.commandName) {
            "OnlinePush.PbPushGroupMsg" -> {
                val pushMsg = decodeProtobuf<PbPushMsg>(from.body)
                val msg = pushMsg.msg
                val msgHead = msg.msgHead
                if(this@MessageCenter::troopMsgEvent.isInitialized && msgHead.fromUin != ore.uin.toULong()) { // 自己的消息不处理

                    val codeMsg = msg.msgBody.richText.toMsg(TROOP)

                    if(codeMsg.isNotEmpty()) {
                        val troopInfo = msgHead.groupInfo!!

                        troopMsgEvent.onTroopMsg(
                            troopInfo.groupCode.toLong(),
                            msgHead.fromUin.toLong(),
                            troopInfo.groupName,
                            troopInfo.groupCard,
                            msgHead.msgTime.toLong(),
                            msgHead.msgSeq.toInt(),
                            codeMsg
                        )
                    }
                }
            }

        }
    }

    fun setTroopMsgEvent(event: TroopMsgEvent) {
        this.troopMsgEvent = event
    }

    private fun RichText.toMsg(msgType: MsgType): String {
        val builder = OreCode()
        if(ptt != null) {
            TODO("voice message not support")
        }

        var ignoreNextMsg = false
        for (elem in this.elems!!) {
            if (ignoreNextMsg) {
                ignoreNextMsg = false
                continue
            }

            elem.text.ifNotNull {
                if(it.attr6Buf.isNotEmpty()) {
                    it.attr6Buf.toByteReadPacket().use { pat ->
                        pat.discardExact(2) // version
                        pat.discardExact(2) // startPos
                        pat.discardExact(2) // textLen
                        pat.discardExact(1) // flag
                        val uin = pat.readUInt().toLong()
                        // pat.discardExact(2) // 0
                        builder.add(At(uin))
                    }
                } else {
                    builder.add(Text(it.str))
                }
            }
            elem.face.ifNotNull { builder.add(Face(it.index.toInt())) }

            elem.customFace.ifNotNull {
                val fileName = it.filePath.replace("[{}\\-]".toRegex(), "")
                when(msgType) {
                    TROOP -> {
                        saveImage(ore.uin, fileName, it.md5)
                    }
                    C2C -> TODO("not support c2c image")
                }
                builder.add(Image(file = fileName))
            }

            elem.commonElem.ifNotNull { comm ->
                if(comm.serviceType == 33u && comm.businessType == 1u) {
                    val type33 = decodeProtobuf<MsgElemInfoServiceType33>(comm.elem)
                    builder.add(SuperFace(
                        id = type33.index.toInt(),
                        name = type33.text
                    ))
                }
                else if(comm.serviceType == 3u) { // 闪图啊
                    ignoreNextMsg = true // 忽略接下来的那个狗屎消息
                    val type3 = decodeProtobuf<MsgElemInfoServiceType3>(comm.elem)
                    type3.flashC2cPic?.let {
                        TODO("c2c flash image")
                    }
                    type3.flashTroopPic?.let {
                        val fileName = it.filePath.replace("[{}\\-]".toRegex(), "")
                        when(msgType) {
                            TROOP -> {
                                saveImage(ore.uin, fileName, it.md5)
                            }
                            C2C -> TODO("not support c2c image")
                        }
                        builder.add(FlashImage(file = fileName))
                    }
                }


            }
        }
        return builder.toString()
    }

    private fun saveImage(uin: Long, name: String, md5: ByteArray) {
        ImageCache.saveTroopImage(uin, name, md5)
    }
}

@ExperimentalUnsignedTypes
fun Ore.messageCenter(): MessageCenter {
    return getServletOrPut(TAG_MESSAGE_CENTER) { MessageCenter(this as OreBot) }
}

