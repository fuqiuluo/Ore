package moe.ore.pay.data

data class QPayHbPack(
    val retcode : Int,
    val retmsg : String,
    val is_confirm : String,
    val token_id : String
)