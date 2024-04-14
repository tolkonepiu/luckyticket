package wtf.popov.ctf.luckyticket.predictor;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Map.Entry.comparingByValue;

@Slf4j
public class CompositeDelayPredictor implements DelayPredictor {

    private final List<DelayPredictor> predictors;

    private final double percentDiff;

    private final Map<String, Long> predictorDelays = new HashMap<>(20);

    private final Map<String, Long> predictorPoints = new HashMap<>(20);

    private final AtomicReference<String> currentPredictor = new AtomicReference<>(null);

    private final AtomicInteger predictCounter = new AtomicInteger(0);

    public CompositeDelayPredictor() {
        this(
                new SimpleDelayPredictor(),
                new Percentile25DelayPredictor(),
                new Percentile50DelayPredictor()
        );
    }

    public CompositeDelayPredictor(DelayPredictor... predictors) {
        this(0.75d, predictors);
    }

    public CompositeDelayPredictor(double percentDiff, DelayPredictor... predictors) {
        this.predictors = Arrays.asList(predictors);
        this.percentDiff = percentDiff;
    }

    @Override
    public long nextDelay(long currentDelay) {
        predictCounter.incrementAndGet();

        predictors.forEach(predictor -> processDelay(currentDelay, predictor));

        return getPredictorDelay(choosePredictor(), currentDelay);
    }

    private long calculateExp(long currentExpValue, boolean match) {
        long exp = getExpValue();
        return Math.max(currentExpValue + (match ? exp * 5 : -exp), 0);
    }

    private long getLevel() {
        return (long) (1 * Math.sqrt(predictCounter.get()));
    }

    private long getExpValue() {
        long level = getLevel();
        return BigDecimal.valueOf((level * Math.log10(level) + 1))
                .setScale(0, RoundingMode.UP)
                .longValue();
    }

    private void processDelay(long delay, DelayPredictor predictor) {
        String predictorName = predictor.getClass().getSimpleName();

        boolean match = Objects.equals(predictorDelays.get(predictorName), delay);
        predictorPoints.compute(
                predictorName,
                (k, v) -> v != null ? calculateExp(v, match) : 0L
        );

        if (match && predictorName.equals(currentPredictor.get())) {
            log.info("{} match", predictorName);
        }

        long nextDelay = predictor.nextDelay(delay);
        predictorDelays.put(predictorName, nextDelay);
    }

    private String choosePredictor() {
        String previousPredictor = currentPredictor.get();

        String newPredictor = Collections.max(
                predictorPoints.entrySet(),
                comparingByValue()
        ).getKey();

        if (previousPredictor == null) {
            currentPredictor.set(newPredictor);
            return newPredictor;
        }

        if (!previousPredictor.equals(newPredictor)) {
            long previousPredictorPoints = predictorPoints.get(previousPredictor);
            long newPredictorPoints = predictorPoints.get(newPredictor);

            if (previousPredictorPoints != newPredictorPoints
                    && (double) previousPredictorPoints / newPredictorPoints > percentDiff) {
                currentPredictor.set(newPredictor);
                return newPredictor;
            }
        }

        return previousPredictor;
    }

    private long getPredictorDelay(String predictorName, long defaultDelay) {
        return predictorDelays.getOrDefault(predictorName, defaultDelay);
    }
}
