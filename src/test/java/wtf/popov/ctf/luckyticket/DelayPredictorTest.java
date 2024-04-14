package wtf.popov.ctf.luckyticket;

import org.junit.jupiter.api.Test;
import wtf.popov.ctf.luckyticket.predictor.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DelayPredictorTest {

    @Test
    public void medianDelayPredictorTest() {
        DelayPredictor predictor = new MedianDelayPredictor();

        long finalDelay = 0L;
        for (long delay : List.of(1, 2, 3, 4, 5)) {
            finalDelay = predictor.nextDelay(delay);
        }

        assertEquals(3L, finalDelay);
    }

    @Test
    public void percentile50DelayPredictorTest() {
        DelayPredictor predictor = new Percentile50DelayPredictor();

        long finalDelay = 0L;
        for (long delay : List.of(1, 2, 3, 4, 5)) {
            finalDelay = predictor.nextDelay(delay);
        }

        assertEquals(3L, finalDelay);
    }

    @Test
    public void percentile25DelayPredictorTest() {
        DelayPredictor predictor = new Percentile25DelayPredictor();

        long finalDelay = 0L;
        for (long delay : List.of(1, 2, 3, 4, 5)) {
            finalDelay = predictor.nextDelay(delay);
        }

        assertEquals(2L, finalDelay);
    }

    @Test
    public void percentile95DelayPredictorTest() {
        DelayPredictor predictor = new PercentileDelayPredictor(95);

        long finalDelay = 0L;
        for (long delay : List.of(1, 2, 3, 4, 5)) {
            finalDelay = predictor.nextDelay(delay);
        }

        assertEquals(5L, finalDelay);
    }

    @Test
    public void FrequentDelayDelayPredictorTest() {
        DelayPredictor predictor = new FrequentDelayPredictor();

        long finalDelay = 0L;
        for (long delay : List.of(1, 2, 2, 3, 4, 2, 1 ,1)) {
            finalDelay = predictor.nextDelay(delay);
        }

        assertEquals(1L, finalDelay);

        assertEquals(2L, predictor.nextDelay(2L));
        assertEquals(1L, predictor.nextDelay(1L));
    }

    @Test
    public void knnDelayPredictorTest() {
        DelayPredictor predictor = new KNNDelayPredictor();

        for (long delay : List.of(1, 1, 1, 2, 2, 3, 3 ,3, 4, 5, 5)) {
            predictor.nextDelay(delay);
        }

        assertEquals(3L, predictor.nextDelay(3));
        assertEquals(2L, predictor.nextDelay(1));
        assertEquals(4L, predictor.nextDelay(5));
    }

}
