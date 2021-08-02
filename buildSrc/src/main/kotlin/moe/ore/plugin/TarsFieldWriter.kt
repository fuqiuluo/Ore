package moe.ore.plugin

import moe.ore.plugin.full.ClassFuller
import java.util.*

class TarsFieldWriter(
    private val fuller: ClassFuller,
    private val fields : TreeMap<Int, FieldInfo>
) {
    private val className = fuller.name // not have "L" and ";"

    fun invoke() {



    }
}