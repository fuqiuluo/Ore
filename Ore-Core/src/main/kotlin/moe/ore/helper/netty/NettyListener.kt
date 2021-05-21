package moe.ore.helper.netty

interface NettyListener {
    /**
     * 接包啦！！！
     * @param body ByteArray
     */
    fun receive(body : ByteArray)

    /**
     * 心跳
     * @return ByteArray
     */
    fun heartbeat() : ByteArray

    /**
     * 连接到服务器了
     */
    fun channelActive()

    /**
     * 断开连接了嗷！！！
     */
    fun channelInactive()
}