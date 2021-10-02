/**
 * 为一个类生成完整的Hook示例代码
 * @param className
 * */
function generateAllMethodHookCode(className) {
    var split = className.split(".");
    let name = split[split.length - 1];
    console.log("var " + name + " = Java.use(\"" + className + "\");");
    var claszz = Java.use(className)
    var mhd_array = claszz.class.getDeclaredMethods();
    //hook 类所有方法 （所有重载方法也要hook)
    for (var i = 0; i < mhd_array.length; i++) {
        var mhd_cur = mhd_array[i]; //当前方法
        var str_mhd_name = mhd_cur.getName(); //当前方法名

        var parameterTypes = mhd_cur.getParameterTypes();
        // console.log(parameterTypes)
        var pms = "";
        for (var j = 0; j < parameterTypes.length; j++) {
            var parameterType = parameterTypes[j] + "";
            if (parameterType) {
                //todo 待完善测试 目前暂无发现问题但是肯定有替换错的东西
                // parameterType = parameterType.replace(";", "").replace("class [Ljava.lang.String", "Strings").replace("class [B", "bytes").replace("class [I", "ints").replace("java.lang.String", "String").replace("class ", "");
                parameterType = parameterType.replace("class ", "").replace("interface ", "");

                parameterType = "\"" + parameterType + "\""

                pms += parameterType + ", ";
            }
        }
        // console.log()
        console.log(name + "." + str_mhd_name.replace("()", "") + ".overload(" + pms.substring(0, pms.length - 2) + ").implementation = function (){send(\"" + str_mhd_name + "⤵️\"); send(arguments);  return this." + str_mhd_name + ".apply(this, arguments)}");
    }
}

/**
 * 010 Editer format
 * dump byte[]
 * */
function hexdumpBytes(bytes, simple = true) {
    if (bytes == null || bytes.length === 0) return "null"
    var buf = Memory.alloc(bytes.length);
    Memory.writeByteArray(buf, byte_to_ArrayBuffer(bytes));
    var hexdump1 = hexdump(buf, {
        offset: 0,
        length: bytes.length,
        header: false,
        ansi: true
    });
    if (simple) {
        return hexdump1 + "\nSimpleHex:" + bytes2Hex(bytes);
    } else {
        return hexdump1;
    }
}

function byte_to_ArrayBuffer(bytes) {
    var size = bytes.length;
    var tmparray = [];
    for (var i = 0; i < size; i++) {
        var val = bytes[i];
        if (val < 0) {
            val += 256;
        }
        tmparray[i] = val
    }
    return tmparray;
}

/**
 * 打印调用堆栈 看看方法是谁调用的
 * 两种实现方式 方法1必须安卓平台 2java通用
 * */
function printStack() {
    // Java.perform(function () {
    //     console.log("=============================Stack strat=======================");
    //     console.log(Java.use("android.util.Log").getStackTraceString(Java.use("java.lang.Exception").$new()))
    //     console.log("=============================Stack end=======================\r\n");
    // });
    Java.perform(function () {
        var Exception = Java.use("java.lang.Exception");
        var ins = Exception.$new("Exception");
        var straces = ins.getStackTrace();
        if (straces != undefined && straces != null) {
            var strace = straces.toString();
            var replaceStr = strace.replace(/,/g, "\r\n");
            console.log("=============================Stack strat=======================");
            console.log(replaceStr);
            console.log("=============================Stack end=======================\r\n");
            Exception.$dispose();
        }
    });
}

/**
 * QQ里面的一个类拿来用了 别的软件没用
 * */
function bytes2Hex(value) {
    return Java.use("com.huawei.secure.android.common.util.HexUtil").byteArray2HexStr(value);
}


/**
 * 收发包监视器 不保证所有包都能监视到 过滤打印1024*4
 * */
function registeredPackListener() {
    Java.use("com.tencent.qphone.base.remote.ToServiceMsg").writeToParcel.overload("android.os.Parcel", "int").implementation = function (v1, v2) {
        var writeToParcel = this.writeToParcel(v1, v2);
        print(this);
        return writeToParcel;
    }

    Java.use("com.tencent.qphone.base.remote.FromServiceMsg").readFromParcel.overload("android.os.Parcel").implementation = function (v1) {
        var readFromParcel = this.readFromParcel(v1);
        print(this)
        return readFromParcel;
    }

    function print(va) {
        if (filter(va)) {
            if (va.getWupBuffer().length > 1024 * 4) {
                console.warn("###############过滤了一个很大的包：" + va.getServiceCmd() + " 大小：" + va.getWupBuffer().length + " 如果没有头绪可以看看这个包")
            } else
                send("=============start===============" + "\n" + va.getStringForLog() + "\n" + hexdumpBytes(va.getWupBuffer(), true) + "\n=============end===============")
        } else {
            console.error(">>>已过滤包：" + va.getServiceCmd() + " 大小：" + va.getWupBuffer().length + " 如果没有头绪可以看看这个包")
        }

        function filter(sv) {
            var s = sv.getServiceCmd();
            var svm = sv.tag.value;
            if (svm === "ToServiceMsg") {
                svm = sv.getServiceName();

            }
            // && svm !== "com.tencent.mobileqq.msf.service.MsfService"
            return s !== "apollo_monitor.report_trace" && s !== "App_reportRDM" && s !== "OnlinePush.PbPushGroupMsg" && s !== "MultibusidURLSvr.HeadUrlReq" && s !== "CliLogSvc.UploadReq" && s !== "SQQzoneSvc.getUndealCount" && s !== "cmd_pushSetConfig";
        }
    }
}

function getAndroidContext() {
    return Java.use("android.app.ActivityThread").currentApplication();

}

function hookAllOverloads(targetClass, targetMethod) {
    Java.perform(function () {
        var targetClassMethod = targetClass + '.' + targetMethod;
        var hook = Java.use(targetClass);
        var overloadCount = hook[targetMethod].overloads.length;
        for (var i = 0; i < overloadCount; i++) {
            hook[targetMethod].overloads[i].implementation = function () {
                var retval = this[targetMethod].apply(this, arguments);
                //这里可以打印结果和参数
                return retval;
            }
        }
    });
}

function jobj2Str(jobject) {
    var ret = JSON.stringify(jobject);
    return ret;
}

function enumMethods(targetClass) {
    var ret;
    Java.perform(function () {
        var hook = Java.use(targetClass);
        var ret = hook.class.getDeclaredMethods();
        ret.forEach(function (s) {
            console.log(s);
        })
    })
    return ret;
}

function getMethodName() {
    var ret;
    Java.perform(function () {
        var Thread = Java.use("java.lang.Thread")
        ret = Thread.currentThread().getStackTrace()[2].getMethodName();
    });
    return ret;
}

function bytes2Hex(arr) {
    var str = "[";
    for (var i = 0; i < arr.length; i++) {
        var z = parseInt(arr[i]);
        if (z < 0) z = 255 + z;
        var tmp = z.toString(16);
        if (tmp.length == 1) {
            tmp = "0" + tmp;
        }
        str = str + " " + tmp;
    }
    return (str + " ]").toUpperCase();
}

function jstring2Str(jstring) {
    var ret;
    Java.perform(function () {
        var String = Java.use("java.lang.String");
        ret = Java.cast(jstring, String);
    });
    return ret;
}

function jbyteArray2Array(jbyteArray) {
    var ret;
    Java.perform(function () {
        var b = Java.use('[B');
        var buffer = Java.cast(jbyteArray, b);
        ret = Java.array('byte', buffer);
    });
    return ret;
}

function dumpAddr(address, length) {
    length = length || 1024;
    console.log(hexdump(address, {
        offset: 0,
        length: length,
        header: true,
        ansi: false
    }));
}

function ab2Hex(buffer) {
    var arr = Array.prototype.map.call(new Uint8Array(buffer), function (x) {
        return ('00' + x.toString(16)).slice(-2)
    }).join(" ").toUpperCase();
    return "[" + arr + "]";
}

function ab2Str(buffer) {
    return String.fromCharCode.apply(null, new Uint8Array(buffer));
}

function getParamType(obj) {
    return obj == null ? String(obj) : Object.prototype.toString.call(obj).replace(/\[object\s+(\w+)\]/i, "$1") || "object";
}

function hookNativeFun(callback, funName, moduleName) {
    var time = 1000;
    moduleName = moduleName || null;
    if (!(callback && callback.onEnter && callback.onLeave)) {
        console.log("callback error");
        return
    }
    var address = Module.findExportByName(moduleName, funName);
    if (address == null) {
        setTimeout(hookNativeFun, time, callback, funName, moduleName);
    } else {
        console.log(funName + " hook ok")
        var nativePointer = new NativePointer(address);
        Interceptor.attach(nativePointer, callback);
    }
}


function inspectObject(obj) {
    const Class = Java.use("java.lang.Class");
    const obj_class = Java.cast(obj.getClass(), Class);
    const fields = obj_class.getDeclaredFields();
    console.log("Inspecting " + obj.getClass().toString()+" {");
    console.log("\tFields:");
    for (let i in fields) {
        if (fields[i].toString().startsWith("[")) break;

        if (fields[i].getType().getSimpleName() == "byte[]") {
            console.log("\t\t" + fields[i].toString() + " -> " + bytes2Hex(fields[i].get(obj)));
        }else {
            console.log("\t\t" + fields[i].toString() + " -> " + fields[i].get(obj));
        }
    }
    console.log("}")
    console.log("----------------------------------------------------------")

}