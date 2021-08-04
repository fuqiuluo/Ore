package moe.ore.msg

open class CoreConfig {

    /**
     * 处理好友消息
     */
    var handleFriendMessage: Boolean = true

    // var 是开启消息自动分包 可以将一条很长的消息分多条发送 规避长消息上传
}