package moe.ore.tars.util

import moe.ore.tars.FieldInfo

object TarsUtil {
    fun quickSort(list: ArrayList<FieldInfo>) : ArrayList<FieldInfo> {
        if (list.size <= 1) {
            return list
        }
        val listSort = ArrayList<FieldInfo>()
        listSort.addAll(quickSort(list.filter {it.id < list[0].id} as ArrayList<FieldInfo>))
        listSort.addAll(list.filter {it.id == list[0].id})
        listSort.addAll(quickSort(list.filter{it.id > list[0].id} as ArrayList<FieldInfo>))
        return listSort
    }
}
