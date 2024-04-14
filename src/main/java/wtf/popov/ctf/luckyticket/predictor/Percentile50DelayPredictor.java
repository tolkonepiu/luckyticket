package wtf.popov.ctf.luckyticket.predictor;

public class Percentile50DelayPredictor extends PercentileDelayPredictor {
    public Percentile50DelayPredictor() {
        this(10);
    }

    public Percentile50DelayPredictor(int maxSize) {
        super(50, maxSize);
    }
}
