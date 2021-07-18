package moe.ore.tars

data class FieldInfo(
    val name : String,
    val id : Int,
    val type : String,
    val require : Boolean = false
)

private val BaseTypeArray = arrayOf(
    "Ljava/lang/String;",
    "B", "S", "I", "J", "F", "D"
)

private val NotNeedCheck = arrayOf(
    "B", "S", "I", "J", "F", "D"
)

fun FieldInfo.isBaseType() = BaseTypeArray.contains(this.type)

fun FieldInfo.needCheckNull() = !NotNeedCheck.contains(this.type)