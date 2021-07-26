package moe.ore.core.transfile.dns

data class DomainData(
    var domain: String,
    val ipList : ArrayList<IpData> = arrayListOf()
) {
    operator fun get(i : Int) : IpData = ipList[i]

    fun size() : Int = ipList.size
}