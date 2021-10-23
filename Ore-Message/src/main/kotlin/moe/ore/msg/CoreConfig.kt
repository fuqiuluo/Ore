package moe.ore.msg

open class CoreConfig {

    /**
     * 处理好友消息
     */
    var handleFriendMessage: Boolean = true

    /**
     * 接到消息后自动设为已读
     */
    var autoReaded: Boolean = true

    // internal val troopShut = hashMapOf<Long, Int>()
    // var 是开启消息自动分包 可以将一条很长的消息分多条发送 规避长消息上传
}