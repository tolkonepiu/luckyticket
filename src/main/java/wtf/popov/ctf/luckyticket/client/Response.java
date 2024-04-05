package wtf.popov.ctf.luckyticket.client;

import lombok.Data;

import java.time.Instant;

@Data
public class Response<T> {

    private Instant serverTime;

    private T data;

}
