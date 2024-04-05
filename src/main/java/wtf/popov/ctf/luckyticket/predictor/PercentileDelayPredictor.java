package wtf.popov.ctf.luckyticket.predictor;

import java.util.ArrayList;
import java.util.List;

public class PercentileDelayPredictor implements DelayPredictor {

    private final List<Long> latencies = new ArrayList<>(100);

    private final double percentile;

    public PercentileDelayPredictor(int percentile) {
        this.percentile = percentile;
    }

    @Override
    public long nextDelay(long currentDelay) {
        latencies.add(currentDelay);
        int index = (int) Math.ceil(percentile / 100.0 * latencies.size());
        return latencies.get(index - 1);
    }

    @Override
    public void reset() {
        latencies.clear();
    }
}
