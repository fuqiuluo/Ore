@file:Suppress("EXPERIMENTAL_API_USAGE")

package moe.ore.msg.msg

import kotlinx.io.core.discardExact
import kotlinx.io.core.readUInt
import moe.ore.api.Ore
import moe.ore.core.helper.DataManager
import moe.ore.helper.ifNotNull
import moe.ore.helper.toByteReadPacket
import moe.ore.msg.code.At
import moe.ore.msg.code.BaseCode
import moe.ore.msg.code.OreCode
import moe.ore.msg.code.Text
import moe.ore.msg.protocol.protobuf.*
import moe.ore.msg.request.SendMsg

class MessageBuilder(private val ore: Ore): OreCode() {
    fun build(): MessageSender {
        return MessageSender(ore, this)
    }
}

class MessageSender(
    val ore: Ore,
    val msg: List<BaseCode>
) {
    private val manager = DataManager.manager(ore.uin)
    private val session = manager.session

    fun sendToTroop(troopCode: Long): Result<PbSendMsgResp> {
        val routingHead = RoutingHead(
            grp = Grp(troopCode.toULong())
        )
        return send(routingHead)
    }

    private fun send(routingHead: RoutingHead): Result<PbSendMsgResp> {
        return SendMsg(ore, session.nextMsgSeq(), routingHead, toPb())
    }

    private fun toPb(): MsgBody {
        val richText = RichText()
        val msgBody = MsgBody(richText)
        val elems = ArrayList<Elem>()
        for (msg in msg) {
            when(msg) {
                is Text -> elems.add(text(msg.src))

            }
        }
        elems.add(generalFlags())
        richText.elems = elems
        return msgBody
    }

    /** create protobuf message **/
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

internal fun RichText.toMsg(): String {
    val builder = OreCode()
    if(ptt != null) {
        TODO("voice message not support")
    }
    for (elem in this.elems!!) {
        elem.text.ifNotNull {
            if(it.attr6Buf.isNotEmpty()) {
                val pat = it.attr6Buf.toByteReadPacket()
                // val startPos = pat.readShort()
                // val strLen = pat.readInt()
                // val flag = pat.readByte()
                pat.discardExact(2 + 4 + 1)
                val uin = pat.readUInt().toLong()
                builder.add(At(uin))
            } else {
                builder.add(Text(it.str))
            }
        }


    }
    return builder.toString()
}