package wtf.popov.ctf.luckyticket.predictor;

import java.util.*;

public class FrequentDelayPredictor implements DelayPredictor {

    private final LinkedList<Long> latencies = new LinkedList<>();

    private final int maxSize;

    public FrequentDelayPredictor() {
        this(20);
    }

    public FrequentDelayPredictor(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public long nextDelay(long currentDelay) {
        latencies.add(currentDelay);

        if (latencies.size() > maxSize) {
            latencies.removeFirst();
        }

        Map<Long, Integer> delayCounters = new HashMap<>();
        for (long latency : latencies) {
            delayCounters.compute(latency, (k, v) -> v != null ? v + 1 : 1);
        }

        return Collections.max(
                delayCounters.entrySet(),
                Map.Entry.comparingByValue()
        ).getKey();
    }

}
