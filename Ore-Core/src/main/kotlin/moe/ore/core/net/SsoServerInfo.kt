package moe.ore.core.net

import moe.ore.tars.TarsInputStream
import moe.ore.tars.TarsOutputStream
import moe.ore.tars.TarsStructBase

/**
 *@author 飞翔的企鹅
 *create 2021-06-18 23:49
 */
//class SsoServerInfoResp : TarsStructBase() {
//    var server: String = ""
//    var port: Int = 0
//    var location: String = ""
//
//    override fun writeTo(output: TarsOutputStream) {
//        output.write(server, 1)
//        output.write(port, 2)
//        output.write(location, 8)
//    }
//
//    override fun readFrom(input: TarsInputStream) {
//        server = input.read(server, 1, false)
//        port = input.read(port, 2, false)
//        location = input.read(location, 8, false)
//    }
//}

class SsoServerInfoReq : TarsStructBase() {
    var a1: Long = 0
    var a2: Long = 0
    var a3: Byte = 1
    var a4: String = "46000"
    var a5: Int = 100
    var a6: Long = 0x200302d5
    var a7: String = "867109044454073"
    var a8: Long = 0
    var a9: Long = 0
    var a10: Long = 0
    var a11: Long = 0
    var a12: Byte = 0
    var a13: Long = 0
    var a14: Byte = 1

    override fun writeTo(output: TarsOutputStream) {
        output.write(a1, 1)
        output.write(a2, 2)
        output.write(a3, 3)
        output.write(a4, 4)
        output.write(a5, 5)
        output.write(a6, 6)
        output.write(a7, 7)
        output.write(a8, 8)
        output.write(a9, 9)
        output.write(a10, 10)
        output.write(a11, 11)
        output.write(a12, 12)
        output.write(a13, 13)
        output.write(a14, 14)
    }
}

class SsoServerInfoResp : TarsStructBase() {
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
    class IPAddressInfo : TarsStructBase {
        var ip = ""
        var port = 0
        var c: Byte = 0
        var d: Byte = 0
        var e: Byte = 0
        var f = 8
        var g: Byte = 0
        var h = ""
        var i = ""

        constructor() {}
        constructor(str: String, i2: Int, b2: Byte, b3: Byte, b4: Byte, i3: Int, b5: Byte, str2: String, str3: String) {
            ip = str
            port = i2
            c = b2
            d = b3
            e = b4
            f = i3
            g = b5
            h = str2
            i = str3
        }

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
