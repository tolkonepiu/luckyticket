package wtf.popov.ctf.luckyticket.predictor;

import com.google.common.math.Quantiles;

import java.util.ArrayList;
import java.util.List;

public class MedianDelayPredictor implements DelayPredictor {
    private final List<Long> latencies = new ArrayList<>(100);

    @Override
    public long nextDelay(long currentDelay) {
        latencies.add(currentDelay);
        return Math.round(
                Quantiles.median()
                        .compute(
                                latencies.stream()
                                        .mapToInt(Long::intValue)
                                        .toArray()
                        )
        );
    }

    @Override
    public void reset() {
        latencies.clear();
    }

}
