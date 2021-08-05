package moe.ore.msg.msg

class MessageBuilder: ArrayList<Msg>() {

    fun text(string: String) = add(Text(string))

    fun build(): MessageSender {
        return MessageSender(this)
    }
}

class MessageSender(
    val msg: List<Msg>
) {

    fun sendToTroop(troopCode: Long) {


    }

    private fun toPb() {



    }
}