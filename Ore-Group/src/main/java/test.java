import moe.ore.tars.TarsInputStream;
import moe.ore.tars.TarsStructBase;
import org.jetbrains.annotations.NotNull;

public class test extends TarsStructBase {
    public char aa;
    private static Byte a;
    private static Short b;
    private static Integer c;
    private static Character d;
    private static Long l;
    private static Float aFloat;
    private static Double aDouble;
    private static Duix[] cache_dx;
    private Duix[] dx;

    static class Duix {
        int a = 0;
    }

    @Override
    public void readFrom(@NotNull TarsInputStream input) {
        if(cache_dx == null) {
            cache_dx = new Duix[1];
            cache_dx[0] = new Duix();
        }
        this.dx = (Duix[]) input.read(cache_dx, 1, false);
    }
}
