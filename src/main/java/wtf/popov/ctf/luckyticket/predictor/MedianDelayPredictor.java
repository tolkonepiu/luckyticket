package wtf.popov.ctf.luckyticket.predictor;

import java.util.ArrayList;
import java.util.List;

public class MedianDelayPredictor implements DelayPredictor {

    private final List<Long> latencies = new ArrayList<>(100);

    @Override
    public long nextDelay(long currentDelay) {
        latencies.add(currentDelay);
        return latencies.stream().reduce(Long::sum).get() / latencies.size();
    }

    @Override
    public void reset() {
        latencies.clear();
    }
}
