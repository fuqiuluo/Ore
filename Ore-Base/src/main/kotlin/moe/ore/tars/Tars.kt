package moe.ore.tars

@Target(AnnotationTarget.CLASS)
annotation class TarsClass(
    val requireRead: Boolean = true,
    val requireWrite : Boolean = true,
    val servantName : String = "",
    val funcName : String = "",
    val reqName : String = "",
    val respName : String = ""
)

@Target(AnnotationTarget.FIELD)
annotation class TarsField (
    val id : Int,
    val require : Boolean = false,
    val isEnum : Boolean = false
)

@Target(AnnotationTarget.FUNCTION)
annotation class TarsMethod(
    val fieldTag : Int
)