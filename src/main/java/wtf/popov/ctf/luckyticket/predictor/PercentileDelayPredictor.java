package wtf.popov.ctf.luckyticket.predictor;

import com.google.common.math.Quantiles;

import java.util.ArrayList;
import java.util.List;

public class PercentileDelayPredictor implements DelayPredictor {

    private final List<Long> latencies = new ArrayList<>(100);

    private final int percentile;

    public PercentileDelayPredictor(int percentile) {
        this.percentile = percentile;
    }

    @Override
    public long nextDelay(long currentDelay) {
        latencies.add(currentDelay);
        return Math.round(Quantiles.percentiles().index(percentile).compute(latencies));
    }

    @Override
    public void reset() {
        latencies.clear();
    }

}
