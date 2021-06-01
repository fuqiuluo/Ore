package moe.ore.core.net.listener

import io.netty.channel.ChannelHandler.Sharable
import java.util.concurrent.TimeUnit

/**
 *@author 飞翔的企鹅
 *create 2021-05-31 13:56
 */
@Sharable
class IdleStateHandler(readerIdleTime: Long, writerIdleTime: Long, allIdleTime: Long, unit: TimeUnit) : io.netty.handler.timeout.IdleStateHandler(readerIdleTime, writerIdleTime, allIdleTime, unit)