setTimeout(function () {
    Java.perform(function () {
        //调用tools里面的监听包工具
        // registeredPackListener()
        //获取类里面的方法 打印hook demo
        // generateAllMethodHookCode("com.tencent.open.agent.QrAgentLoginManager");

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
        // function getLoadResult( i) {
        //     return (i & 2) == 2 || (i & 262144) == 262144;
        // }
        //
        //
        // var CodecWarpper = Java.use("com.tencent.qphone.base.util.CodecWarpper");
        //
        // CodecWarpper.checkSOVersion.overload().implementation = function (){send("checkSOVersion⤵️"); send(arguments);  return this.checkSOVersion.apply(this, arguments)}
        // CodecWarpper.closeReceData.overload().implementation = function (){send("closeReceData⤵️"); send(arguments);  return this.closeReceData.apply(this, arguments)}
        // CodecWarpper.encodeRequest.overload("int", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "[B", "int", "int", "java.lang.String", "byte", "byte", "byte", "[B", "[B", "[B", "boolean").implementation = function (){send("encodeRequest⤵️"); send(arguments);  return this.encodeRequest.apply(this, arguments)}
        // CodecWarpper.encodeRequest.overload("int", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "[B", "int", "int", "java.lang.String", "byte", "byte", "[B", "boolean").implementation = function (){send("encodeRequest⤵️"); send(arguments);  return this.encodeRequest.apply(this, arguments)}
        // CodecWarpper.encodeRequest.overload("int", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "[B", "int", "int", "java.lang.String", "byte", "byte", "[B", "[B", "[B", "boolean").implementation = function (){send("encodeRequest⤵️"); send(arguments);  return this.encodeRequest.apply(this, arguments)}
        // CodecWarpper.getAppid.overload().implementation = function (){send("getAppid⤵️"); send(arguments);  return this.getAppid.apply(this, arguments)}
        // CodecWarpper.getFileStoreKey.overload().implementation = function (){send("getFileStoreKey⤵️"); send(arguments);  return this.getFileStoreKey.apply(this, arguments)}
        // CodecWarpper.getMaxPackageSize.overload().implementation = function (){send("getMaxPackageSize⤵️"); send(arguments);  return this.getMaxPackageSize.apply(this, arguments)}
        // CodecWarpper.getPacketLossLength.overload("int").implementation = function (){send("getPacketLossLength⤵️"); send(arguments);  return this.getPacketLossLength.apply(this, arguments)}
        // CodecWarpper.getSOVersion.overload().implementation = function (){send("getSOVersion⤵️"); send(arguments);  return this.getSOVersion.apply(this, arguments)}
        // CodecWarpper.getSharedObjectVersion.overload().implementation = function (){send("getSharedObjectVersion⤵️"); send(arguments);  return this.getSharedObjectVersion.apply(this, arguments)}
        // CodecWarpper.getVersionCode.overload().implementation = function (){send("getVersionCode⤵️"); send(arguments);  return this.getVersionCode.apply(this, arguments)}
        // CodecWarpper.nativeEncodeRequest.overload("int", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "[B", "int", "int", "java.lang.String", "byte", "byte", "byte", "[B", "[B", "[B", "boolean").implementation = function (){send("nativeEncodeRequest⤵️"); send(arguments);  return this.nativeEncodeRequest.apply(this, arguments)}
        // CodecWarpper.nativeEncodeRequest.overload("int", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "[B", "int", "int", "java.lang.String", "byte", "byte", "[B", "boolean").implementation = function (){send("nativeEncodeRequest⤵️"); send(arguments);  return this.nativeEncodeRequest.apply(this, arguments)}
        // CodecWarpper.nativeEncodeRequest.overload("int", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "[B", "int", "int", "java.lang.String", "byte", "byte", "[B", "[B", "[B", "boolean").implementation = function (){send("nativeEncodeRequest⤵️"); send(arguments);  return this.nativeEncodeRequest.apply(this, arguments)}
        // CodecWarpper.nativeOnConnClose.overload().implementation = function (){send("nativeOnConnClose⤵️"); send(arguments);  return this.nativeOnConnClose.apply(this, arguments)}
        // CodecWarpper.nativeRemoveAccountKey.overload("java.lang.String").implementation = function (){send("nativeRemoveAccountKey⤵️"); send(arguments);  return this.nativeRemoveAccountKey.apply(this, arguments)}
        // CodecWarpper.nativeSetAccountKey.overload("java.lang.String", "[B", "[B", "[B", "[B", "[B", "[B", "[B", "[B", "java.lang.String").implementation = function (){send("nativeSetAccountKey⤵️"); send(arguments);  return this.nativeSetAccountKey.apply(this, arguments)}
        // CodecWarpper.nativeSetKsid.overload("[B").implementation = function (){send("nativeSetKsid⤵️"); send(arguments);  return this.nativeSetKsid.apply(this, arguments)}
        // CodecWarpper.nativeSetUseSimpleHead.overload("java.lang.String", "boolean").implementation = function (){send("nativeSetUseSimpleHead⤵️"); send(arguments);  return this.nativeSetUseSimpleHead.apply(this, arguments)}
        // CodecWarpper.onConnClose.overload().implementation = function (){send("onConnClose⤵️"); send(arguments);  return this.onConnClose.apply(this, arguments)}
        // CodecWarpper.onReceData.overload("[B", "int").implementation = function (){send("onReceData⤵️"); send(arguments);  return this.onReceData.apply(this, arguments)}
        // CodecWarpper.parseData.overload("[B").implementation = function (){send("parseData⤵️"); send(arguments);  return this.parseData.apply(this, arguments)}
        // CodecWarpper.printBytes.overload("java.lang.String", "[B", "java.lang.StringBuilder").implementation = function (){send("printBytes⤵️"); send(arguments);  return this.printBytes.apply(this, arguments)}
        // CodecWarpper.removeAccountKey.overload("java.lang.String").implementation = function (){send("removeAccountKey⤵️"); send(arguments);  return this.removeAccountKey.apply(this, arguments)}
        // CodecWarpper.setAccountKey.overload("java.lang.String", "[B", "[B", "[B", "[B", "[B", "[B", "[B", "[B", "java.lang.String").implementation = function (){send("setAccountKey⤵️"); send(arguments);  return this.setAccountKey.apply(this, arguments)}
        // CodecWarpper.setKsid.overload("[B").implementation = function (){send("setKsid⤵️"); send(arguments);  return this.setKsid.apply(this, arguments)}
        // CodecWarpper.setMaxPackageSize.overload("int").implementation = function (){send("setMaxPackageSize⤵️"); send(arguments);  return this.setMaxPackageSize.apply(this, arguments)}
        // CodecWarpper.setUseSimpleHead.overload("java.lang.String", "boolean").implementation = function (){send("setUseSimpleHead⤵️"); send(arguments);  return this.setUseSimpleHead.apply(this, arguments)}
        // CodecWarpper.init.overload("android.content.Context", "boolean").implementation = function (){send("init⤵️"); send(arguments);  return this.init(arguments[0],true)}
        // CodecWarpper.nativeClearReceData.overload().implementation = function (){send("nativeClearReceData⤵️"); send(arguments);  return this.nativeClearReceData.apply(this, arguments)}
        // CodecWarpper.nativeOnReceData.overload("[B", "int").implementation = function (){send("nativeOnReceData⤵️"); send(arguments);  return this.nativeOnReceData.apply(this, arguments)}
        // CodecWarpper.nativeParseData.overload("[B").implementation = function (){send("nativeParseData⤵️"); send(arguments);  return this.nativeParseData.apply(this, arguments)}
        // CodecWarpper.onInvalidData.overload("int", "int").implementation = function (){send("onInvalidData⤵️"); send(arguments);  return this.onInvalidData.apply(this, arguments)}
        // CodecWarpper.onInvalidDataNative.overload("int").implementation = function (){send("onInvalidDataNative⤵️"); send(arguments);  return this.onInvalidDataNative.apply(this, arguments)}
        // CodecWarpper.onInvalidSign.overload().implementation = function (){send("onInvalidSign⤵️"); send(arguments);  return this.onInvalidSign.apply(this, arguments)}
        // CodecWarpper.onResponse.overload("int", "java.lang.Object", "int").implementation = function (){send("onResponse⤵️"); send(arguments);  return this.onResponse.apply(this, arguments)}
        // CodecWarpper.onResponse.overload("int", "java.lang.Object", "int", "[B").implementation = function (){send("onResponse⤵️"); send(arguments);  return this.onResponse.apply(this, arguments)}
        // CodecWarpper.onSSOPingResponse.overload("[B", "int").implementation = function (){send("onSSOPingResponse⤵️"); send(arguments);  return this.onSSOPingResponse.apply(this, arguments)}
        //
        //
        // let msfLoadSo = Java.use("com.tencent.qphone.base.util.StringUtils").msfLoadSo("MSF.C.CodecWarpper", "codecwrapperV2");
        // console.log(msfLoadSo)
        // console.log(getLoadResult(msfLoadSo))
        // // Java.use("android.os.Looper").prepare()
        // // var MsfService = Java.use("com.tencent.mobileqq.msf.core.MsfCore").$new().init(centext,true)
        //
        // CodecWarpper.$new().init(getAndroidContext(), true)
        // CodecWarpper.isLoaded = true
        // CodecWarpper.checkSOVersion()

        var MsfMsgUtil = Java.use("com.tencent.mobileqq.msf.sdk.MsfMsgUtil");
        MsfMsgUtil.get_wt_AskDevLockSms.overload("java.lang.String", "java.lang.String", "long").implementation = function () {
            send("get_wt_AskDevLockSms⤵️");
            send(arguments);
            return this.get_wt_AskDevLockSms.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_CancelCode.overload("java.lang.String", "java.lang.String", "long", "[B", "long").implementation = function () {
            send("get_wt_CancelCode⤵️");
            send(arguments);
            return this.get_wt_CancelCode.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_CheckDevLockSms.overload("java.lang.String", "java.lang.String", "long", "java.lang.String", "[B", "long").implementation = function () {
            send("get_wt_CheckDevLockSms⤵️");
            send(arguments);
            return this.get_wt_CheckDevLockSms.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_CheckDevLockStatus.overload("java.lang.String", "java.lang.String", "long", "long").implementation = function () {
            send("get_wt_CheckDevLockStatus⤵️");
            send(arguments);
            return this.get_wt_CheckDevLockStatus.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_CheckPictureAndGetSt.overload("java.lang.String", "java.lang.String", "[B", "long").implementation = function () {
            send("get_wt_CheckPictureAndGetSt⤵️");
            send(arguments);
            return this.get_wt_CheckPictureAndGetSt.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_CheckSMSAndGetSt.overload("java.lang.String", "java.lang.String", "[B", "long").implementation = function () {
            send("get_wt_CheckSMSAndGetSt⤵️");
            send(arguments);
            return this.get_wt_CheckSMSAndGetSt.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_CheckSMSAndGetStExt.overload("java.lang.String", "java.lang.String", "[B", "long").implementation = function () {
            send("get_wt_CheckSMSAndGetStExt⤵️");
            send(arguments);
            return this.get_wt_CheckSMSAndGetStExt.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_CheckSMSVerifyLoginAccount.overload("java.lang.String", "java.lang.String", "java.lang.String", "int", "long").implementation = function () {
            send("get_wt_CheckSMSVerifyLoginAccount⤵️");
            send(arguments);
            return this.get_wt_CheckSMSVerifyLoginAccount.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_CloseCode.overload("java.lang.String", "java.lang.String", "long", "[B", "int", "java.util.ArrayList", "long").implementation = function () {
            send("get_wt_CloseCode⤵️");
            send(arguments);
            return this.get_wt_CloseCode.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_CloseDevLock.overload("java.lang.String", "java.lang.String", "long", "long").implementation = function () {
            send("get_wt_CloseDevLock⤵️");
            send(arguments);
            return this.get_wt_CloseDevLock.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_GetA1WithA1.overload("java.lang.String", "java.lang.String", "long", "long", "[B", "long", "long", "long", "[B", "[B", "oicq.wlogin_sdk.request.WFastLoginInfo", "long").implementation = function () {
            send("get_wt_GetA1WithA1⤵️");
            send(arguments);
            return this.get_wt_GetA1WithA1.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_GetOpenKeyWithoutPasswd.overload("java.lang.String", "java.lang.String", "long", "long", "long").implementation = function () {
            send("get_wt_GetOpenKeyWithoutPasswd⤵️");
            send(arguments);
            return this.get_wt_GetOpenKeyWithoutPasswd.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_GetStViaSMSVerifyLogin.overload("java.lang.String", "java.lang.String", "java.lang.String", "int", "long").implementation = function () {
            send("get_wt_GetStViaSMSVerifyLogin⤵️");
            send(arguments);
            return this.get_wt_GetStViaSMSVerifyLogin.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_GetStWithPasswd.overload("java.lang.String", "java.lang.String", "long", "java.lang.String", "long").implementation = function () {
            send("get_wt_GetStWithPasswd⤵️");
            send(arguments);
            return this.get_wt_GetStWithPasswd.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_GetStWithoutPasswd.overload("java.lang.String", "java.lang.String", "long", "long", "long").implementation = function () {
            send("get_wt_GetStWithoutPasswd⤵️");
            send(arguments);
            return this.get_wt_GetStWithoutPasswd.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_GetSubaccountStViaSMSVerifyLogin.overload("java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "int", "long").implementation = function () {
            send("get_wt_GetSubaccountStViaSMSVerifyLogin⤵️");
            send(arguments);
            return this.get_wt_GetSubaccountStViaSMSVerifyLogin.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_QuickLoginByGateway.overload("java.lang.String", "android.content.Intent", "long").implementation = function () {
            send("get_wt_QuickLoginByGateway⤵️");
            send(arguments);
            return this.get_wt_QuickLoginByGateway.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_RefreshPictureData.overload("java.lang.String", "java.lang.String", "long").implementation = function () {
            send("get_wt_RefreshPictureData⤵️");
            send(arguments);
            return this.get_wt_RefreshPictureData.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_RefreshSMSData.overload("java.lang.String", "java.lang.String", "long").implementation = function () {
            send("get_wt_RefreshSMSData⤵️");
            send(arguments);
            return this.get_wt_RefreshSMSData.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_RefreshSMSVerifyLoginCode.overload("java.lang.String", "java.lang.String", "java.lang.String", "long").implementation = function () {
            send("get_wt_RefreshSMSVerifyLoginCode⤵️");
            send(arguments);
            return this.get_wt_RefreshSMSVerifyLoginCode.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_RegGetSMSVerifyLoginAccount.overload("java.lang.String", "[B", "[B", "java.lang.String", "long").implementation = function () {
            send("get_wt_RegGetSMSVerifyLoginAccount⤵️");
            send(arguments);
            return this.get_wt_RegGetSMSVerifyLoginAccount.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_RegGetSMSVerifyLoginAccount.overload("java.lang.String", "[B", "[B", "java.lang.String", "java.lang.String", "java.lang.String", "long").implementation = function () {
            send("get_wt_RegGetSMSVerifyLoginAccount⤵️");
            send(arguments);
            return this.get_wt_RegGetSMSVerifyLoginAccount.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_SetDevlockMobileType.overload("java.lang.String", "int", "long").implementation = function () {
            send("get_wt_SetDevlockMobileType⤵️");
            send(arguments);
            return this.get_wt_SetDevlockMobileType.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_VerifyCode.overload("java.lang.String", "java.lang.String", "long", "boolean", "[B", "[I", "int", "long").implementation = function () {
            send("get_wt_VerifyCode⤵️");
            send(arguments);
            return this.get_wt_VerifyCode.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_VerifySMSVerifyLoginCode.overload("java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "long").implementation = function () {
            send("get_wt_VerifySMSVerifyLoginCode⤵️");
            send(arguments);
            return this.get_wt_VerifySMSVerifyLoginCode.apply(this, arguments)
        }
        MsfMsgUtil.get_wt_setRegDevLockFlag.overload("java.lang.String", "int", "long").implementation = function () {
            send("get_wt_setRegDevLockFlag⤵️");
            send(arguments);
            return this.get_wt_setRegDevLockFlag.apply(this, arguments)
        }

        var UniPacket = Java.use("com.qq.jce.wup.UniPacket");
        // var JceUtil = Java.use("com.qq.taf.jce.JceUtil");
        var StringBuilder = Java.use("java.lang.StringBuilder");
        // UniPacket.decode.overload("[B").implementation = function () {
        //     send("decode⤵️");
        //     let apply = this.decode.apply(this, arguments);
        //     let newq = StringBuilder.$new();
        //     this.display(newq,0)
        //     console.log(newq.toString())
        //
        //     return apply;
        // }
        // UniPacket.encode.overload().implementation = function () {
        //     send("encode⤵️");
        //     var uint8Array = this.encode.apply(this, arguments);
        //     let newq = StringBuilder.$new();
        //     this.display(newq,0)
        //     console.log(newq.toString())
        //
        //     return uint8Array
        // }
        // var k$a = Java.use("com.tencent.beacon.base.net.a.k$a");
        // k$a.a.overload().implementation = function () {
        //     send("a⤵️");
        //     send(arguments);
        //      let apply = this.a.apply(this, arguments);
        //      send(apply.toString())
        //     return apply
        // }

        var SSOLog = Java.use("com.tencent.open.agent.util.SSOLog");
        SSOLog.a.overload("java.lang.String", "java.lang.String").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        SSOLog.a.overload("java.lang.String", "java.lang.String", "java.lang.Throwable").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        SSOLog.a.overload("java.lang.String", "[Ljava.lang.Object;").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        SSOLog.b.overload("java.lang.String", "java.lang.String").implementation = function (){send("b⤵️"); send(arguments);  return this.b.apply(this, arguments)}
        SSOLog.b.overload("java.lang.String", "java.lang.String", "java.lang.Throwable").implementation = function (){send("b⤵️"); send(arguments);  return this.b.apply(this, arguments)}
        SSOLog.b.overload("java.lang.String", "[Ljava.lang.Object;").implementation = function (){send("b⤵️"); send(arguments);  return this.b.apply(this, arguments)}
        SSOLog.c.overload("java.lang.String", "java.lang.String", "java.lang.Throwable").implementation = function (){send("c⤵️"); send(arguments);  return this.c.apply(this, arguments)}


        var OpenAuthorityFragment = Java.use("com.tencent.open.agent.OpenAuthorityFragment");
        // OpenAuthorityFragment.a.overload("com.tencent.open.agent.OpenAuthorityFragment").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        // OpenAuthorityFragment.a.overload("com.tencent.open.agent.OpenAuthorityFragment").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        // OpenAuthorityFragment.a.overload("android.os.Bundle").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        // OpenAuthorityFragment.a.overload("android.view.View", "android.os.Bundle").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        // OpenAuthorityFragment.a.overload("com.tencent.open.agent.OpenAuthorityFragment").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        // OpenAuthorityFragment.a.overload().implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        // OpenAuthorityFragment.a.overload("com.tencent.open.agent.OpenAuthorityFragment").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        // OpenAuthorityFragment.b.overload("java.lang.String").implementation = function (){send("b⤵️"); send(arguments);  return this.b.apply(this, arguments)}
        // OpenAuthorityFragment.d.overload().implementation = function (){send("d⤵️"); send(arguments);  return this.d.apply(this, arguments)}
        // OpenAuthorityFragment.e.overload().implementation = function (){send("e⤵️"); send(arguments);  return this.e.apply(this, arguments)}
        // OpenAuthorityFragment.a.overload().implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        // OpenAuthorityFragment.a.overload("int").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        // OpenAuthorityFragment.a.overload().implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        // OpenAuthorityFragment.a.overload("int").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        // OpenAuthorityFragment.a.overload("int", "java.util.List", "long").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        // OpenAuthorityFragment.a.overload("long").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        // OpenAuthorityFragment.a.overload("com.tencent.open.agent.auth.INewAuthorityContract$Presenter").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        // OpenAuthorityFragment.a.overload("java.lang.String").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        // OpenAuthorityFragment.a.overload("java.lang.String", "android.graphics.Bitmap").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        // OpenAuthorityFragment.a.overload("java.lang.String", "boolean").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        // OpenAuthorityFragment.a.overload("java.util.List", "java.lang.String").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        // OpenAuthorityFragment.a.overload("boolean").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        // OpenAuthorityFragment.a.overload("java.util.List", "java.lang.String").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        // OpenAuthorityFragment.b.overload().implementation = function (){send("b⤵️"); send(arguments);  return this.b.apply(this, arguments)}
        // OpenAuthorityFragment.c.overload().implementation = function (){send("c⤵️"); send(arguments);  return this.c.apply(this, arguments)}
        // OpenAuthorityFragment.isWrapContent.overload().implementation = function (){send("isWrapContent⤵️"); send(arguments);  return this.isWrapContent.apply(this, arguments)}
        // OpenAuthorityFragment.needImmersive.overload().implementation = function (){send("needImmersive⤵️"); send(arguments);  return this.needImmersive.apply(this, arguments)}
        OpenAuthorityFragment.onActivityResult.overload("int", "int", "android.content.Intent").implementation = function (){send("onActivityResult⤵️"); send(arguments);  return this.onActivityResult.apply(this, arguments)}
        OpenAuthorityFragment.onClick.overload("android.view.View").implementation = function (){send("onClick⤵️"); send(arguments);  return this.onClick.apply(this, arguments)}
        // OpenAuthorityFragment.onCreateView.overload("android.view.LayoutInflater", "android.view.ViewGroup", "android.os.Bundle").implementation = function (){send("onCreateView⤵️"); send(arguments);  return this.onCreateView.apply(this, arguments)}
        // OpenAuthorityFragment.onDestroy.overload().implementation = function (){send("onDestroy⤵️"); send(arguments);  return this.onDestroy.apply(this, arguments)}
        // OpenAuthorityFragment.onPause.overload().implementation = function (){send("onPause⤵️"); send(arguments);  return this.onPause.apply(this, arguments)}
        // OpenAuthorityFragment.onResume.overload().implementation = function (){send("onResume⤵️"); send(arguments);  return this.onResume.apply(this, arguments)}

        var QrAgentLoginManager = Java.use("com.tencent.open.agent.QrAgentLoginManager");
        QrAgentLoginManager.a.overload().implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        QrAgentLoginManager.a.overload().implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        QrAgentLoginManager.a.overload().implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        QrAgentLoginManager.a.overload("com.tencent.open.agent.QrAgentLoginManager").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        QrAgentLoginManager.a.overload().implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        QrAgentLoginManager.a.overload("com.tencent.mobileqq.app.QQAppInterface", "[B").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        QrAgentLoginManager.a.overload("com.tencent.open.agent.QrAgentLoginManager", "com.tencent.mobileqq.app.QQAppInterface", "[B").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        QrAgentLoginManager.a.overload("com.tencent.open.agent.QrAgentLoginManager", "[B").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        QrAgentLoginManager.a.overload("[B").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        QrAgentLoginManager.a.overload().implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        QrAgentLoginManager.a.overload("com.tencent.open.agent.QrAgentLoginManager").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        QrAgentLoginManager.a.overload("com.tencent.open.agent.QrAgentLoginManager").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        QrAgentLoginManager.b.overload("com.tencent.open.agent.QrAgentLoginManager").implementation = function (){send("b⤵️"); send(arguments);  return this.b.apply(this, arguments)}
        QrAgentLoginManager.b.overload().implementation = function (){send("b⤵️"); send(arguments);  return this.b.apply(this, arguments)}
        QrAgentLoginManager.b.overload("com.tencent.open.agent.QrAgentLoginManager").implementation = function (){send("b⤵️"); send(arguments);  return this.b.apply(this, arguments)}
        QrAgentLoginManager.c.overload("com.tencent.open.agent.QrAgentLoginManager").implementation = function (){send("c⤵️"); send(arguments);  return this.c.apply(this, arguments)}
        QrAgentLoginManager.a.overload("long", "java.lang.String").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        QrAgentLoginManager.a.overload("android.os.Bundle", "boolean").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}
        QrAgentLoginManager.a.overload("com.tencent.mobileqq.app.QQAppInterface", "java.lang.String", "com.tencent.mobileqq.qrscan.OnQRHandleResultCallback", "boolean").implementation = function (){send("a⤵️"); send(arguments);  return this.a.apply(this, arguments)}

        Java.use("com.tencent.mobileqq.app.QBaseActivity").startActivity.overload("android.content.Intent").implementation = function (){
            this.startActivity(arguments[0])
            send(arguments[0].getComponent().getClassName())
            send(arguments[0].getDataString())
            send(arguments[0].getExtras().toString())
            send(arguments[0].getBundleExtra("key_params").toString())
        }
    })
});