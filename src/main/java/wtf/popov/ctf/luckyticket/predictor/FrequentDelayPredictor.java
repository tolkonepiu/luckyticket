package wtf.popov.ctf.luckyticket.predictor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FrequentDelayPredictor implements DelayPredictor {

    private final Map<Long, Integer> delayCounters = new HashMap<>();

    @Override
    public long nextDelay(long currentDelay) {
        Integer count = delayCounters.get(currentDelay);
        delayCounters.put(currentDelay, count != null ? count + 1 : 1);
        return Collections.max(
                delayCounters.entrySet(),
                Map.Entry.comparingByValue()
        ).getKey();
    }

    @Override
    public void reset() {
        delayCounters.clear();
    }
}
