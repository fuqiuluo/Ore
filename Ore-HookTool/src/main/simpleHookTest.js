setTimeout(function () {
    Java.perform(function () {
        //调用tools里面的监听包工具
        registeredPackListener()
        //获取类里面的方法 打印hook demo
        // generateAllMethodHookCode("com.tencent.mobileqq.msf.sdk.MsfMsgUtil");

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


    });
});