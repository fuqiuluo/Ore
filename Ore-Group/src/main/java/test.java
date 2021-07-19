import moe.ore.tars.TarsInputStream;
import moe.ore.tars.TarsStructBase;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test extends TarsStructBase {
    private static Map<Character, Duix> cache_dx;
    private Map<Character, Duix> dx;

    static class Duix {

    }

    @Override
    public void readFrom(@NotNull TarsInputStream input) {
        if(cache_dx == null) {
            cache_dx = new HashMap<>();
            cache_dx.put(Character.valueOf('0'), new Duix());
        }
        this.dx = (Map<Character, Duix>) input.read(cache_dx, 1, false);


    }
}
