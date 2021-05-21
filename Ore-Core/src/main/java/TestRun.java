import moe.ore.ToolQ;
import moe.ore.ToolQManager;
import moe.ore.helper.toolq.ToolQListener;
import moe.ore.util.DebugUtil;
import org.jetbrains.annotations.NotNull;

public class TestRun {

    public static void main(String[] args) {
        long st = System.currentTimeMillis();

        ToolQ toolQ = ToolQManager.addToolQ(1372362033, "662899ABc", false);

        toolQ.setListener(new ToolQListener() {
            @Override
            public void heartbeatEvent() {

            }

            @Override
            public void loginEvent(@NotNull ToolQ.Companion.LoginResult result) {
                println("登录结果：" + result);
                toolQ.register();
            }

            @Override
            public void onlineEvent(int ret) {
                println("上线返回结果：" + ret);
            }

            @Override
            public void serverDisconnectionEvent() {

            }

            @Override
            public void serverConnectionSuccessEvent() {

            }
        });

        toolQ.login();

        println("Spend: " + (System.currentTimeMillis() - st) + "Ms");


        // System.exit(0);

    }

    /**
     * 打印正在运行的所有线程
     */
    public static void printAliveThread() {
        Thread[] threads = DebugUtil.getRunningThread();
        for (Thread thread : threads) {
            println(String.format("Running thread：%s[%s], daemon: %s, alive: %s", thread.getName(), thread.getId(), thread.isDaemon(), thread.isAlive()));
        }
    }

    public static void println(Object o) {
        System.out.println(o);
    }

}
