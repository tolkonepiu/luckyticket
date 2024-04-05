package wtf.popov.ctf.luckyticket.random;

import com.sun.jna.Library;
import com.sun.jna.Native;
import lombok.SneakyThrows;
import org.scijava.nativelib.NativeLoader;


public class GoRandom {

    static {
        loadNative();
    }

    public static final NativeRandom RANDOM = Native.load(NativeRandom.class);

    public interface NativeRandom extends Library {

        int Random(int n, long seed);

    }

    public int random(int n, long seed) {
        return RANDOM.Random(n, seed);
    }

    @SneakyThrows
    private static void loadNative() {
        NativeLoader.loadLibrary("random");
    }
}
