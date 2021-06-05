setTimeout(function () {
    Java.perform(function () {
        //调用tools里面的监听包工具
        // registeredPackListener()
        //获取类里面的方法 打印hook demo
        // generateAllMethodHookCode("com.tencent.qphone.base.util.CodecWarpper");

        //hook demo
        // var MsfMsgUtil = Java.use("com.tencent.mobileqq.msf.sdk.MsfMsgUtil");
        // MsfMsgUtil.get_wt_VerifyCode.overload("java.lang.String", "java.lang.String", "long", "boolean", "[B", "[I", "int", "long").implementation = function () {
        //     send("get_wt_VerifyCode⤵️");
        //     //打印全部参数信息
        //     send(arguments);
        //     // 打印调用堆栈信息 只能在function里面执行
        //     printStack()
        //     //dump byte[]参数
        //     console.log(hexdumpBytes(arguments[4], true))
        //     //真正的执行方法
        //     var apply = this.get_wt_VerifyCode.apply(this, arguments);
        //     //打印执行结果返回内容
        //     send(apply)
        //     return apply;
        // }
        function getLoadResult( i) {
            return (i & 2) == 2 || (i & 262144) == 262144;
        }


        var CodecWarpper = Java.use("com.tencent.qphone.base.util.CodecWarpper");

        CodecWarpper.checkSOVersion.overload().implementation = function (){send("checkSOVersion⤵️"); send(arguments);  return this.checkSOVersion.apply(this, arguments)}
        CodecWarpper.closeReceData.overload().implementation = function (){send("closeReceData⤵️"); send(arguments);  return this.closeReceData.apply(this, arguments)}
        CodecWarpper.encodeRequest.overload("int", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "[B", "int", "int", "java.lang.String", "byte", "byte", "byte", "[B", "[B", "[B", "boolean").implementation = function (){send("encodeRequest⤵️"); send(arguments);  return this.encodeRequest.apply(this, arguments)}
        CodecWarpper.encodeRequest.overload("int", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "[B", "int", "int", "java.lang.String", "byte", "byte", "[B", "boolean").implementation = function (){send("encodeRequest⤵️"); send(arguments);  return this.encodeRequest.apply(this, arguments)}
        CodecWarpper.encodeRequest.overload("int", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "[B", "int", "int", "java.lang.String", "byte", "byte", "[B", "[B", "[B", "boolean").implementation = function (){send("encodeRequest⤵️"); send(arguments);  return this.encodeRequest.apply(this, arguments)}
        CodecWarpper.getAppid.overload().implementation = function (){send("getAppid⤵️"); send(arguments);  return this.getAppid.apply(this, arguments)}
        CodecWarpper.getFileStoreKey.overload().implementation = function (){send("getFileStoreKey⤵️"); send(arguments);  return this.getFileStoreKey.apply(this, arguments)}
        CodecWarpper.getMaxPackageSize.overload().implementation = function (){send("getMaxPackageSize⤵️"); send(arguments);  return this.getMaxPackageSize.apply(this, arguments)}
        CodecWarpper.getPacketLossLength.overload("int").implementation = function (){send("getPacketLossLength⤵️"); send(arguments);  return this.getPacketLossLength.apply(this, arguments)}
        CodecWarpper.getSOVersion.overload().implementation = function (){send("getSOVersion⤵️"); send(arguments);  return this.getSOVersion.apply(this, arguments)}
        CodecWarpper.getSharedObjectVersion.overload().implementation = function (){send("getSharedObjectVersion⤵️"); send(arguments);  return this.getSharedObjectVersion.apply(this, arguments)}
        CodecWarpper.getVersionCode.overload().implementation = function (){send("getVersionCode⤵️"); send(arguments);  return this.getVersionCode.apply(this, arguments)}
        CodecWarpper.nativeEncodeRequest.overload("int", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "[B", "int", "int", "java.lang.String", "byte", "byte", "byte", "[B", "[B", "[B", "boolean").implementation = function (){send("nativeEncodeRequest⤵️"); send(arguments);  return this.nativeEncodeRequest.apply(this, arguments)}
        CodecWarpper.nativeEncodeRequest.overload("int", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "[B", "int", "int", "java.lang.String", "byte", "byte", "[B", "boolean").implementation = function (){send("nativeEncodeRequest⤵️"); send(arguments);  return this.nativeEncodeRequest.apply(this, arguments)}
        CodecWarpper.nativeEncodeRequest.overload("int", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "[B", "int", "int", "java.lang.String", "byte", "byte", "[B", "[B", "[B", "boolean").implementation = function (){send("nativeEncodeRequest⤵️"); send(arguments);  return this.nativeEncodeRequest.apply(this, arguments)}
        CodecWarpper.nativeOnConnClose.overload().implementation = function (){send("nativeOnConnClose⤵️"); send(arguments);  return this.nativeOnConnClose.apply(this, arguments)}
        CodecWarpper.nativeRemoveAccountKey.overload("java.lang.String").implementation = function (){send("nativeRemoveAccountKey⤵️"); send(arguments);  return this.nativeRemoveAccountKey.apply(this, arguments)}
        CodecWarpper.nativeSetAccountKey.overload("java.lang.String", "[B", "[B", "[B", "[B", "[B", "[B", "[B", "[B", "java.lang.String").implementation = function (){send("nativeSetAccountKey⤵️"); send(arguments);  return this.nativeSetAccountKey.apply(this, arguments)}
        CodecWarpper.nativeSetKsid.overload("[B").implementation = function (){send("nativeSetKsid⤵️"); send(arguments);  return this.nativeSetKsid.apply(this, arguments)}
        CodecWarpper.nativeSetUseSimpleHead.overload("java.lang.String", "boolean").implementation = function (){send("nativeSetUseSimpleHead⤵️"); send(arguments);  return this.nativeSetUseSimpleHead.apply(this, arguments)}
        CodecWarpper.onConnClose.overload().implementation = function (){send("onConnClose⤵️"); send(arguments);  return this.onConnClose.apply(this, arguments)}
        CodecWarpper.onReceData.overload("[B", "int").implementation = function (){send("onReceData⤵️"); send(arguments);  return this.onReceData.apply(this, arguments)}
        CodecWarpper.parseData.overload("[B").implementation = function (){send("parseData⤵️"); send(arguments);  return this.parseData.apply(this, arguments)}
        CodecWarpper.printBytes.overload("java.lang.String", "[B", "java.lang.StringBuilder").implementation = function (){send("printBytes⤵️"); send(arguments);  return this.printBytes.apply(this, arguments)}
        CodecWarpper.removeAccountKey.overload("java.lang.String").implementation = function (){send("removeAccountKey⤵️"); send(arguments);  return this.removeAccountKey.apply(this, arguments)}
        CodecWarpper.setAccountKey.overload("java.lang.String", "[B", "[B", "[B", "[B", "[B", "[B", "[B", "[B", "java.lang.String").implementation = function (){send("setAccountKey⤵️"); send(arguments);  return this.setAccountKey.apply(this, arguments)}
        CodecWarpper.setKsid.overload("[B").implementation = function (){send("setKsid⤵️"); send(arguments);  return this.setKsid.apply(this, arguments)}
        CodecWarpper.setMaxPackageSize.overload("int").implementation = function (){send("setMaxPackageSize⤵️"); send(arguments);  return this.setMaxPackageSize.apply(this, arguments)}
        CodecWarpper.setUseSimpleHead.overload("java.lang.String", "boolean").implementation = function (){send("setUseSimpleHead⤵️"); send(arguments);  return this.setUseSimpleHead.apply(this, arguments)}
        CodecWarpper.init.overload("android.content.Context", "boolean").implementation = function (){send("init⤵️"); send(arguments);  return this.init(arguments[0],true)}
        CodecWarpper.nativeClearReceData.overload().implementation = function (){send("nativeClearReceData⤵️"); send(arguments);  return this.nativeClearReceData.apply(this, arguments)}
        CodecWarpper.nativeOnReceData.overload("[B", "int").implementation = function (){send("nativeOnReceData⤵️"); send(arguments);  return this.nativeOnReceData.apply(this, arguments)}
        CodecWarpper.nativeParseData.overload("[B").implementation = function (){send("nativeParseData⤵️"); send(arguments);  return this.nativeParseData.apply(this, arguments)}
        CodecWarpper.onInvalidData.overload("int", "int").implementation = function (){send("onInvalidData⤵️"); send(arguments);  return this.onInvalidData.apply(this, arguments)}
        CodecWarpper.onInvalidDataNative.overload("int").implementation = function (){send("onInvalidDataNative⤵️"); send(arguments);  return this.onInvalidDataNative.apply(this, arguments)}
        CodecWarpper.onInvalidSign.overload().implementation = function (){send("onInvalidSign⤵️"); send(arguments);  return this.onInvalidSign.apply(this, arguments)}
        CodecWarpper.onResponse.overload("int", "java.lang.Object", "int").implementation = function (){send("onResponse⤵️"); send(arguments);  return this.onResponse.apply(this, arguments)}
        CodecWarpper.onResponse.overload("int", "java.lang.Object", "int", "[B").implementation = function (){send("onResponse⤵️"); send(arguments);  return this.onResponse.apply(this, arguments)}
        CodecWarpper.onSSOPingResponse.overload("[B", "int").implementation = function (){send("onSSOPingResponse⤵️"); send(arguments);  return this.onSSOPingResponse.apply(this, arguments)}


        let msfLoadSo = Java.use("com.tencent.qphone.base.util.StringUtils").msfLoadSo("MSF.C.CodecWarpper", "codecwrapperV2");
        console.log(msfLoadSo)
        console.log(getLoadResult(msfLoadSo))
        // Java.use("android.os.Looper").prepare()
        // var MsfService = Java.use("com.tencent.mobileqq.msf.core.MsfCore").$new().init(centext,true)

        CodecWarpper.$new().init(getAndroidContext(), true)
        CodecWarpper.isLoaded = true
        CodecWarpper.checkSOVersion()

    });
});