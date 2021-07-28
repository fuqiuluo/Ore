package moe.ore.core.protocol.tars.configpush

import moe.ore.tars.*

/**
 *@author 飞翔的企鹅
 *create 2021-06-18 23:49
 */
@TarsClass(requireWrite = true)
internal class SsoServerInfoReq : TarsBase() {
    @TarsField(id = 1) var uin: Long = 0

    @TarsField(id = 2) var timeStamp: Long = 0

    @TarsField(id = 3) var type: Byte = 1

    @TarsField(id = 4) var imsi: String = "46000"

    @TarsField(id = 5) var netType: Int = 100

    @TarsField(id = 6) var appId: Long = 0

    @TarsField(id = 7) var imei: String = "867109044454073"

    @TarsField(id = 8) var gsmCid: Long = 0

    @TarsField(id = 9) var i: Long = 0

    @TarsField(id = 10) var j: Long = 0

    @TarsField(id = 11) var checkType: Byte = 0

    @TarsField(id = 12) var activeNetIp: Byte = 0

    @TarsField(id = 13) var ipAddress: Long = 0
}

internal class SsoServerInfoResp : TarsBase() {
    var A: ArrayList<IPAddressInfo>? = null
    var B1: ArrayList<IPAddressInfo>? = null
    var u: ArrayList<IPAddressInfo>? = null
    var v: ArrayList<IPAddressInfo>? = null
    var w: ArrayList<IPAddressInfo>? = null
    var x: ArrayList<IPAddressInfo>? = null
    var y: ByteArray? = null
    var z: ArrayList<IPAddressInfo>? = null
    var a = 0
    var b: ArrayList<IPAddressInfo>? = null
    var c: ArrayList<IPAddressInfo>? = null
    var d = 0
    var e = 86400
    var f: Byte = 0
    var g: Byte = 0
    var h = 1
    var i = 5
    var j: Long = 0
    var k = 0
    var l: ArrayList<IPAddressInfo>? = null
    var m: ArrayList<IPAddressInfo>? = null
    var n: ByteArray? = null
    var o: ArrayList<IPAddressInfo>? = null
    var p: ArrayList<IPAddressInfo>? = null
    var q: ArrayList<IPAddressInfo>? = null
    var r: Byte = 0 //nettype
    var s = 0 //he_threshold
    var t = "" //policy_id


    // com.qq.taf.jce.JceStruct
    override fun writeTo(jceOutputStream: TarsOutputStream) {
        jceOutputStream.write(a, 1)
        jceOutputStream.write(b, 2)
        jceOutputStream.write(c, 3)
        jceOutputStream.write(d, 4)
        jceOutputStream.write(e, 5)
        jceOutputStream.write(f, 6)
        jceOutputStream.write(g, 7)
        jceOutputStream.write(h, 8)
        jceOutputStream.write(i, 9)
        jceOutputStream.write(j, 10)
        jceOutputStream.write(k, 11)
        val arrayList = l
        if (arrayList != null) {
            jceOutputStream.write(arrayList, 12)
        }
        val arrayList2 = m
        if (arrayList2 != null) {
            jceOutputStream.write(arrayList2, 13)
        }
        val bArr = n
        if (bArr != null) {
            jceOutputStream.write(bArr, 14)
        }
        val arrayList3 = o
        if (arrayList3 != null) {
            jceOutputStream.write(arrayList3, 15)
        }
        val arrayList4 = p
        if (arrayList4 != null) {
            jceOutputStream.write(arrayList4, 16)
        }
        val arrayList5 = q
        if (arrayList5 != null) {
            jceOutputStream.write(arrayList5, 17)
        }
        jceOutputStream.write(r, 18)
        jceOutputStream.write(s, 19)
        jceOutputStream.write(t, 20)
    }

    // com.qq.taf.jce.JceStruct
    override fun readFrom(jceInputStream: TarsInputStream) {
        a = jceInputStream.read(a, 1, true)
        if (u == null) {
            u = ArrayList()
            u!!.add(IPAddressInfo())
        }
        b = jceInputStream.read(u, 2, true) as ArrayList<IPAddressInfo>
        if (v == null) {
            v = ArrayList()
            v!!.add(IPAddressInfo())
        }
        c = jceInputStream.read(v, 3, true) as ArrayList<IPAddressInfo>
        d = jceInputStream.read(d, 4, true)
        e = jceInputStream.read(e, 5, true)
        f = jceInputStream.read(f, 6, false)
        g = jceInputStream.read(g, 7, false)
        h = jceInputStream.read(h, 8, false)
        i = jceInputStream.read(i, 9, false)
        j = jceInputStream.read(j, 10, false)
        k = jceInputStream.read(k, 11, false)
        if (w == null) {
            w = ArrayList()
            w!!.add(IPAddressInfo())
        }
        l = jceInputStream.read(w, 12, false) as ArrayList<IPAddressInfo>
        if (x == null) {
            x = ArrayList()
            x!!.add(IPAddressInfo())
        }
        m = jceInputStream.read(x, 13, false) as ArrayList<IPAddressInfo>
        if (y == null) {
            y = ByteArray(1)
            y!![0] = 0
        }
        n = jceInputStream.read(y, 14, false)
        if (z == null) {
            z = ArrayList()
            z!!.add(IPAddressInfo())
        }
        o = jceInputStream.read(z, 15, false) as ArrayList<IPAddressInfo>
        if (A == null) {
            A = ArrayList()
            A!!.add(IPAddressInfo())
        }
        p = jceInputStream.read(A, 16, false) as ArrayList<IPAddressInfo>
        if (B1 == null) {
            B1 = ArrayList()
            B1!!.add(IPAddressInfo())
        }
        q = jceInputStream.read(B1, 17, false) as ArrayList<IPAddressInfo>
        r = jceInputStream.read(r, 18, false)
        s = jceInputStream.read(s, 19, false)
        t = jceInputStream.readString(20, false)
    }


    class IPAddressInfo : TarsBase() {
        var ip = ""
        var port = 0
        var c: Byte = 0
        var d: Byte = 0
        var e: Byte = 0
        var f = 8
        var g: Byte = 0
        var h = ""
        var i = ""

        // com.qq.taf.jce.JceStruct
        override fun writeTo(jceOutputStream: TarsOutputStream) {
            jceOutputStream.write(ip, 1)
            jceOutputStream.write(port, 2)
            jceOutputStream.write(c, 3)
            jceOutputStream.write(d, 4)
            jceOutputStream.write(e, 5)
            jceOutputStream.write(f, 6)
            jceOutputStream.write(g, 7)
            jceOutputStream.write(h, 8)
            jceOutputStream.write(i, 9)
        }

        // com.qq.taf.jce.JceStruct
        override fun readFrom(jceInputStream: TarsInputStream) {
            ip = jceInputStream.readString(1, true)
            port = jceInputStream.read(port, 2, true)
            c = jceInputStream.read(c, 3, true)
            d = jceInputStream.read(d, 4, true)
            e = jceInputStream.read(e, 5, false)
            f = jceInputStream.read(f, 6, false)
            g = jceInputStream.read(g, 7, false)
            h = jceInputStream.readString(8, true)
            i = jceInputStream.readString(9, true)
        }
    }
}
