import frida, sys


def on_message(message, data):
    if message['type'] == 'send':
        print("[*] {0}".format(message['payload']))
    else:
        print(message)


def read_file(filepath):
    with open(filepath) as fp:
        content = fp.read()
    return content


rdev = frida.get_usb_device()
# 附加到的包名 如果包没有运行会报错
session = rdev.attach("com.tencent.mobileqq")
# js文件 txt方式拼接 不要怀疑人生
text = read_file("tools.js") + \
       read_file("simpleHookTest.js")
script = session.create_script(text)
script.on('message', on_message)

print('[*] Running')
script.load()
sys.stdin.read()
