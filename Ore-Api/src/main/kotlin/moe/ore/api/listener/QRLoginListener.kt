package moe.ore.api.listener

import moe.ore.api.Ore

interface QRLoginListener {
    /**
     * 二维码确认成功
     * 但是没有登录
     * 过一会就自动登录了哦
     */
    fun onQRSuccess(ore : Ore)

    /**
     * 二维码过期
     */
    fun onQRExpired()

    /**
     * 成功获取到二维码
     */
    fun onGetQRSuccess(imgBuf : ByteArray)

    /**
     * 获取二维码失败
     */
    fun onGetQRFailure()

    /**
     * 连接服务器失败
     */
    fun onFailConnect()

    /**
     * 有设备扫描了二维码，但没有确认登录
     */
    fun onScanCode()
}