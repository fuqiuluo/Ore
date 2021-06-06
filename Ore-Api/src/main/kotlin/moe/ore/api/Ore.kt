package moe.ore.api


/**
 * Ore实体对象接口文件
 */
abstract class Ore {

    /**
     * 登录
     */
    abstract fun login()

    /**
     * 获取机器人状态
     * @return OreStatus
     */
    abstract fun status() : OreStatus

    /**
     * 关闭机器人
     */
    abstract fun shut()
}