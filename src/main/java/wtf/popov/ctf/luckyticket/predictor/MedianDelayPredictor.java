package wtf.popov.ctf.luckyticket.predictor;

import com.google.common.math.Quantiles;

import java.util.LinkedList;

public class MedianDelayPredictor implements DelayPredictor {
    private final LinkedList<Long> latencies = new LinkedList<>();

    private final int maxSize;

    public MedianDelayPredictor() {
        this(10);
    }

    public MedianDelayPredictor(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public long nextDelay(long currentDelay) {
        if (!latencies.isEmpty() && latencies.size() >= maxSize) {
            latencies.removeFirst();
        }

        latencies.add(currentDelay);
        return Math.round(
                Quantiles.median()
                        .compute(latencies)
        );
    }

}
