package moe.ore.msg.event

import moe.ore.api.Ore

abstract class BaseEvent {
    /**
     * 在setEvent后被赋值
     */
    lateinit var ore: Ore


}