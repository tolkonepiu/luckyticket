package wtf.popov.ctf.luckyticket.predictor;

public class SimpleDelayPredictor implements DelayPredictor {
    @Override
    public long nextDelay(long currentDelay) {
        return currentDelay;
    }

    @Override
    public void reset() {

    }

}
