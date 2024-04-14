package wtf.popov.ctf.luckyticket.predictor;

public class Percentile25DelayPredictor extends PercentileDelayPredictor {
    public Percentile25DelayPredictor() {
        this(6);
    }

    public Percentile25DelayPredictor(int maxSize) {
        super(25, maxSize);
    }
}
