package moe.ore.msg.msg

import moe.ore.api.Ore
import moe.ore.group.troopManager
import moe.ore.helper.newBuilder
import moe.ore.helper.toByteArray
import moe.ore.helper.writeLongToBuf32
import moe.ore.helper.writeShort
import moe.ore.msg.code.At
import moe.ore.msg.code.BaseCode
import moe.ore.msg.code.Face
import moe.ore.msg.code.Text
import moe.ore.msg.protocol.protobuf.*

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
                            ore.troopManager().getTroopMemberInfo(groupCode!!, msg.code).onSuccess {
                                val nick = if(it.card.isNotEmpty()) String(it.card) else String(it.nick)
                                elems.add(at(nick, msg.code))
                                elems.add(text(" "))
                            }
                        }
                    }
                }
                is Face -> elems.add(face(msg.id))


            }
        }
        elems.add(generalFlags())
        richText.elems = elems
        return msgBody
    }

    /** create protobuf message **/
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