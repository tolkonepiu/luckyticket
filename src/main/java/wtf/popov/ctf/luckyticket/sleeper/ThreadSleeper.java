package wtf.popov.ctf.luckyticket.sleeper;

import lombok.SneakyThrows;

public class ThreadSleeper implements Sleeper {
    @Override
    @SneakyThrows
    public void sleepBefore(long millis) {
        Thread.sleep(millis - System.currentTimeMillis());
    }
}
