package wtf.popov.ctf.luckyticket.model;

import lombok.Data;

@Data
public class User {

    private String username;

    private int balance;

    private int luck;

    public boolean isOutOfBalance() {
        return this.balance == 0;
    }

    public boolean isFullOfLack() {
        return this.luck >= 100;
    }

}
