package wtf.popov.ctf.luckyticket.client;

import lombok.Data;

@Data
public class Response<T> {

    private long serverTime;

    private T data;

}
