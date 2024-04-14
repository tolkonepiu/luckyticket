package wtf.popov.ctf.luckyticket;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import wtf.popov.ctf.luckyticket.sleeper.LoopSleeper;
import wtf.popov.ctf.luckyticket.sleeper.Sleeper;
import wtf.popov.ctf.luckyticket.sleeper.ThreadSleeper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleeperTest {

    @Test
    @Disabled("Doesn't work on time, expectedly")
    public void threadSleeperTest() {
        Sleeper sleeper = new ThreadSleeper();

        long sleepBefore = System.currentTimeMillis() + 1L;
        sleeper.sleepBefore(sleepBefore);
        assertEquals(System.currentTimeMillis(), sleepBefore);

        sleepBefore = System.currentTimeMillis() + 5L;
        sleeper.sleepBefore(sleepBefore);
        assertEquals(System.currentTimeMillis(), sleepBefore);

        sleepBefore = System.currentTimeMillis() + 10L;
        sleeper.sleepBefore(sleepBefore);
        assertEquals(System.currentTimeMillis(), sleepBefore);
    }

    @Test
    public void loopSleeperTest() {
        Sleeper sleeper = new LoopSleeper();

        long sleepBefore = System.currentTimeMillis() + 1L;
        sleeper.sleepBefore(sleepBefore);
        assertEquals(System.currentTimeMillis(), sleepBefore);

        sleepBefore = System.currentTimeMillis() + 5L;
        sleeper.sleepBefore(sleepBefore);
        assertEquals(System.currentTimeMillis(), sleepBefore);

        sleepBefore = System.currentTimeMillis() + 10L;
        sleeper.sleepBefore(sleepBefore);
        assertEquals(System.currentTimeMillis(), sleepBefore);
    }

}
