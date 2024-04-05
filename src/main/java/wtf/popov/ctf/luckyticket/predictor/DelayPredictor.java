package wtf.popov.ctf.luckyticket.predictor;

public interface DelayPredictor {

    long nextDelay(long currentDelay);

    void reset();

}
