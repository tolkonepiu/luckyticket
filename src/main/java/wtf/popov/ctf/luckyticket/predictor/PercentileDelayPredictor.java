package wtf.popov.ctf.luckyticket.predictor;

import com.google.common.math.Quantiles;

import java.util.LinkedList;

public class PercentileDelayPredictor implements DelayPredictor {

    private final LinkedList<Long> latencies = new LinkedList<>();

    private final int percentile;

    private final int maxSize;

    public PercentileDelayPredictor(int percentile) {
        this(percentile, 50);
    }

    public PercentileDelayPredictor(int percentile, int maxSize) {
        this.percentile = percentile;
        this.maxSize = maxSize;
    }

    @Override
    public long nextDelay(long currentDelay) {
        latencies.add(currentDelay);

        if (latencies.size() > maxSize) {
            latencies.removeFirst();
        }

        return Math.round(
                Quantiles.percentiles()
                        .index(percentile)
                        .compute(latencies)
        );
    }

}
