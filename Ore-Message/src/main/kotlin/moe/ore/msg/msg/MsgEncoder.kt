package moe.ore.msg.msg

import moe.ore.api.Ore
import moe.ore.group.troopManager
import moe.ore.helper.*
import moe.ore.highway.bdh
import moe.ore.highway.data.FileMsg
import moe.ore.msg.cache.ImageCache
import moe.ore.msg.code.*
import moe.ore.msg.protocol.protobuf.*
import java.io.File

internal class MsgEncoder(
    val ore: Ore,
    val msgType: MsgType,
    val routingHead: RoutingHead,
    val msg: List<BaseCode>
) {
    private val groupCode: Long? = routingHead.grp?.groupCode?.toLong()

    operator fun invoke(): MsgBody {
        val richText = RichText()
        val msgBody = MsgBody(richText)
        val elems = ArrayList<Elem>()
        for (msg in msg) {
            when(msg) {
                is Text -> elems.add(text(msg.src))
                is At -> {
                    if(msgType == MsgType.TROOP) {
                        if(msg.all) {
                            elems.add(at("全体成员", 0))
                            elems.add(text(" "))
                        } else {
                            ore.troopManager().getTroopMemberInfo(groupCode!!, msg.qq).onSuccess {
                                val nick = if(it.card.isNotEmpty()) String(it.card) else String(it.nick)
                                elems.add(at(nick, msg.qq))
                                elems.add(text(" "))
                            }
                        }
                    }
                }
                is Face -> elems.add(face(msg.id))
                is SuperFace -> elems.add(sface(msg.id, msg.name))
                is Image -> {
                    when(msgType) {
                        MsgType.TROOP -> {
                            val file = ImageCache.getImage(ore.uin, msg.file)
                            val fileMd5: ByteArray = msg.file.replace("[{}\\-]".toRegex(), "").split("\\.".toRegex())[0].hex2ByteArray()

                            var fileId: ULong = 0u
                            var upServer = 3070484794 to 80

                            trySendImage(file)?.let {
                                fileId = it.fileId
                                upServer = it.upServer
                            }

                            elems.add(image(fileMd5, fileId, upServer))
                        }
                        MsgType.C2C -> TODO("send c2c img")
                    }
                }
                is FlashImage -> {
                    when(msgType) {
                        MsgType.TROOP -> {
                            val file = ImageCache.getImage(ore.uin, msg.file)
                            val fileMd5: ByteArray = msg.file.replace("[{}\\-]".toRegex(), "").split("\\.".toRegex())[0].hex2ByteArray()

                            var fileId: ULong = 0u
                            var upServer = 3070484794 to 80

                            trySendImage(file)?.let {
                                fileId = it.fileId
                                upServer = it.upServer
                            }

                            elems.add(fimage(fileMd5, fileId, upServer))
                            elems.add(text("[闪照]请使用新版手机QQ查看闪照。"))
                        }
                        MsgType.C2C -> TODO("send c2c img")
                    }
                }
            }
        }
        elems.add(generalFlags())
        richText.elems = elems
        return msgBody
    }

    // 尝试发送图片哦
    private fun trySendImage(file: File): FileMsg? {
        if(file.exists()) {
            val bdh = ore.bdh()
            bdh.trySendTroopImage(groupCode!!, file).let {
                // println("图片是否存在：${it.exits}, id: $fileId, md51: ${it.fileMd5.toHexString()}, md52: ${file.md5().toHexString()}")
                if(!it.exits) bdh.tryUpTroopImage(it)
                return it
            }
        }
        return null
    }

    /** create protobuf message **/
    private fun fimage(md5: ByteArray, fileId: ULong, upServer: Pair<Long, Int>): Elem = Elem(
        commonElem = CommonElem(
            serviceType = 3u,
            elem = MsgElemInfoServiceType3().also {
                val image = CustomFace(
                    md5 = md5,
                    filePath = md5.toHexString() + ".ore",
                    fileId = fileId.toUInt(),
                    serverIp = upServer.first.toUInt(),
                    serverPort = upServer.second.toUInt(),
                    filetType = 66u,
                    useful = 1u,
                    imageType = 1000u,
                    bizType = 0u, origin = 1u,
                    height = 200u, width = 200u, source = 200u,
                )
                when(msgType) {
                    MsgType.TROOP -> {
                        it.flashTroopPic = image
                    }
                    MsgType.C2C -> TODO("c2c flash")
                }
            }.toByteArray(),
        )
    )

    private fun image(md5: ByteArray, fileId: ULong, upServer: Pair<Long, Int>): Elem = Elem(
        customFace = CustomFace(
            md5 = md5,
            filePath = md5.toHexString() + ".ore",
            fileId = fileId.toUInt(),
            serverIp = upServer.first.toUInt(),
            serverPort = upServer.second.toUInt(),
            filetType = 66u,
            useful = 1u,
            imageType = 1000u,
            bizType = 0u, origin = 1u,
            height = 200u, width = 200u, source = 200u,
        )
    )

    private fun sface(id: Int, name: String): Elem = Elem(
        commonElem = CommonElem(
            serviceType = 33u,
            elem = MsgElemInfoServiceType33(
                index = id.toUInt(),
                text = name,
                compat = name
            ).toByteArray(),
            businessType = 1u
        )
    )

    private fun face(id: Int): Elem = Elem(
        face = FaceMsg(index = id.toUInt())
    )

    private fun at(nick: String, uin: Long): Elem = Elem(
        text = TextMsg(str = "@$nick", attr6Buf = newBuilder().also {
            it.writeShort(1)
            it.writeShort(0)
            it.writeShort(1 + nick.length)
            it.writeByte(if(uin == 0L) 1 else 0)
            it.writeLongToBuf32(uin)
            it.writeShort(0)
        }.toByteArray())
    )

    private fun text(string: String): Elem = Elem(
        text = TextMsg(str = string)
    )

    private fun generalFlags(): Elem = Elem(
        generalFlags = GeneralFlags(
            pendantId = 0u,
            reserve = GeneralFlagsReserveAttr(
                mobileCustomFont = 65536u,
                diyFontTimestamp = 0u,
                subFontId = 0u
            ).toByteArray()
        )
    )
}