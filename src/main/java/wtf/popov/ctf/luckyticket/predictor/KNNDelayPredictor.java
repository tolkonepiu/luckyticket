package wtf.popov.ctf.luckyticket.predictor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class KNNDelayPredictor implements DelayPredictor {

    private final int neighborhoodCount;
    private final Set<Long> delays = new TreeSet<>();

    public KNNDelayPredictor() {
        this(3);
    }

    public KNNDelayPredictor(int neighborhoodCount) {
        this.neighborhoodCount = neighborhoodCount;
    }

    @Override
    public long nextDelay(long currentDelay) {
        delays.add(currentDelay);

        List<Long> delaysArray = new ArrayList<>(delays);
        int count = Math.min(delaysArray.size(), neighborhoodCount);
        int index = delaysArray.indexOf(currentDelay);
        int maxIndex = delaysArray.size() - 1;
        int half = count / 2;
        int start = Math.max(index - half - Math.max(index + half - maxIndex, 0), 0);
        int end = Math.min(index + half - Math.min(index - half, 0), maxIndex);

        long delaySum = 0;
        for (int i = start; i <= end; i++) {
            delaySum += delaysArray.get(i);
        }

        return Math.round((float) delaySum / count);
    }

}