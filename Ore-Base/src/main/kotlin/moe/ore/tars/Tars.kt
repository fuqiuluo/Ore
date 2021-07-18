package moe.ore.tars

@Target(AnnotationTarget.CLASS)
annotation class TarsClass(
    val requireRead: Boolean = false,
    val requireWrite : Boolean = false,
    val servantName : String = "",
    val funcName : String = "",
    val reqName : String = "",
    val respName : String = ""
)

@Target(AnnotationTarget.FIELD)
annotation class TarsField (
    val id : Int,
    val require : Boolean = false
)
