package moe.ore.tars

data class FieldInfo(
    val name : String,
    val id : Int,
    val type : String,
    val require : Boolean = false
)

private val BaseTypeArray = arrayOf(
    "Ljava/lang/String;",
    "B", "S", "I", "J", "F", "D", "C"
)

private val NotNeedCheck = arrayOf(
    "B", "S", "I", "J", "F", "D", "C"
)

private val systemClass = arrayOf(
    "Ljava/lang/Byte;",
    "Ljava/lang/Short;",
    "Ljava/lang/Integer;",
    "Ljava/lang/Character;",
    "Ljava/lang/Long;",
    "Ljava/lang/Float;",
    "Ljava/lang/Double;"
)

fun FieldInfo.isTarsObject(): Boolean {
    if(!isBaseType()) {
        return !systemClass.contains(this.type)
    }
    return false
}

fun FieldInfo.isBaseType() = BaseTypeArray.contains(this.type)

fun FieldInfo.needCheckNull() = !NotNeedCheck.contains(this.type)