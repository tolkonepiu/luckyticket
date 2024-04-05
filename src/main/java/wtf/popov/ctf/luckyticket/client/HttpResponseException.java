package wtf.popov.ctf.luckyticket.client;

import lombok.Getter;
import org.apache.http.util.TextUtils;

@Getter
public class HttpResponseException extends RuntimeException {

    private final int statusCode;
    private final String reasonPhrase;

    public HttpResponseException(int statusCode, String reasonPhrase) {
        super(String.format("status code: %d" + (TextUtils.isBlank(reasonPhrase) ? "" : ", %s"), statusCode, reasonPhrase));
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

}
