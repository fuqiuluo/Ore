package moe.ore.tars

data class FieldInfo(
    val name : String,
    val id : Int,
    val type : String,
    val sign : String? = null,
    val require : Boolean = false,
    val isEnum : Boolean = false
) {
    val bindMethod : ArrayList<MethodInfo> = arrayListOf()

    fun bind(method : MethodInfo) {
        bindMethod.add(method)
    }
}

private val BaseTypeArray = arrayOf(
    "Ljava/lang/String;",
    "B", "S", "I", "J", "F", "D", "C", "Z"
)

private val NotNeedCheck = arrayOf(
    "B", "S", "I", "J", "F", "D", "C", "Z"
)

private val systemClass = arrayOf(
    "Ljava/lang/Byte;",
    "Ljava/lang/Short;",
    "Ljava/lang/Integer;",
    "Ljava/lang/Character;",
    "Ljava/lang/Long;",
    "Ljava/lang/Float;",
    "Ljava/lang/Double;",
    "Ljava/lang/Boolean;",
)

fun FieldInfo.isTarsObject(): Boolean {
    if(!isBaseType()) {
        return !systemClass.contains(this.type)
    }
    return false
}

fun FieldInfo.isBaseType() = isBaseType(this.type)

fun FieldInfo.needCheckNull() = needCheckNull(this.type)

fun isBaseType(string: String) = BaseTypeArray.contains(string)

fun needCheckNull(string: String) = !NotNeedCheck.contains(string)