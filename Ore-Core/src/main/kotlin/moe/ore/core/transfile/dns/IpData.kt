package moe.ore.core.transfile.dns


data class IpData (
    val ip : String,
    val port : Int = 80, // default value = 80
    val type : Int = IPV4,

    val failedCount : Int = 0 // if count >
) {
    companion object {
        const val IPV4 = 1
        const val IPV6 = 28
    }
}