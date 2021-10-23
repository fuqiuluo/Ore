package moe.ore.msg.helper

import com.google.gson.JsonObject
import kotlinx.io.core.discardExact
import kotlinx.io.core.readUInt
import moe.ore.api.Ore
import moe.ore.helper.ifNotNull
import moe.ore.helper.toByteReadPacket
import moe.ore.msg.cache.ImageCache
import moe.ore.msg.code.*
import moe.ore.msg.msg.MsgType
import moe.ore.msg.protocol.protobuf.MsgElemInfoServiceType14
import moe.ore.msg.protocol.protobuf.MsgElemInfoServiceType3
import moe.ore.msg.protocol.protobuf.MsgElemInfoServiceType33
import moe.ore.msg.protocol.protobuf.RichText
import moe.ore.protobuf.decodeProtobuf
import moe.ore.util.JsonUtil
import moe.ore.util.ZipUtil

internal class MsgHandleHelper(val ore: Ore) {
    fun toMsg(richText: RichText, msgType: MsgType): String = richText.run {
        val builder = OreCode()

        if(ptt != null) {
            TODO("voice message not support")
        }

        //var ignoreNextMsg = false
        var actionMsg = false

        for (elem in this.elems!!) {
            //if (ignoreNextMsg) {
            //    ignoreNextMsg = false
            //    continue
            //}

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
                    MsgType.TROOP -> {
                        saveImage(ore.uin, fileName, it.md5)
                    }
                    MsgType.C2C -> TODO("not support c2c image")
                }
                builder.add(Image(file = fileName))
            }


            elem.commonElem.ifNotNull { comm ->
                if(comm.serviceType == 33u && comm.businessType == 1u) {
                    val type33 = decodeProtobuf<MsgElemInfoServiceType33>(comm.elem)
                    builder.add(
                        SuperFace(
                            id = type33.index.toInt(),
                            name = type33.text
                        )
                    )
                }
                else if(comm.serviceType == 3u) { // 闪图啊
                    // ignoreNextMsg = true // 忽略接下来的那个狗屎消息
                    actionMsg = true // 行为消息，不会出现与其它消息同时显示
                    val type3 = decodeProtobuf<MsgElemInfoServiceType3>(comm.elem)
                    type3.flashC2cPic?.let {
                        TODO("c2c flash image")
                    }
                    type3.flashTroopPic?.let {
                        val fileName = it.filePath.replace("[{}\\-]".toRegex(), "")
                        when(msgType) {
                            MsgType.TROOP -> {
                                saveImage(ore.uin, fileName, it.md5)
                            }
                            MsgType.C2C -> TODO("not support c2c image")
                        }
                        builder.add(FlashImage(file = fileName))
                    }
                } else if(comm.serviceType == 14u) { // 闪字
                    actionMsg = true
                    val type14 = decodeProtobuf<MsgElemInfoServiceType14>(comm.elem)
                    val json = JsonUtil.from(unzipOrSourceInfo(type14.reserveInfo)) as JsonObject
                    builder.add(FlashText(src = json["prompt"].asString, id = type14.id ))
                }

            }
        }

        if (actionMsg) // 移除非行为消息的附带消息
            builder.removeAll { !it.isActionMsg() }

        return builder.toString()
    }

    private fun saveImage(uin: Long, name: String, md5: ByteArray) {
        ImageCache.saveTroopImage(uin, name, md5)
    }

    private fun unzipOrSourceInfo(bs: ByteArray): String {
        val outBs = if(bs.first() == 1.toByte()) {
            ZipUtil.unCompress(bs.slice(1 until bs.size).toByteArray())
        } else bs
        return String(outBs)
    }
}