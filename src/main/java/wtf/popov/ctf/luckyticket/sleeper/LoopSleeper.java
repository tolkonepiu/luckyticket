package wtf.popov.ctf.luckyticket.sleeper;

public class LoopSleeper implements Sleeper {
    @Override
    public void sleepBefore(long millis) {
        while (true) {
            if (millis - System.currentTimeMillis() <= 0) break;
        }
    }
}
