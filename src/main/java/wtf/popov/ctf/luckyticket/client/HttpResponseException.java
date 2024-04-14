package wtf.popov.ctf.luckyticket.client;

import lombok.Getter;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.TextUtils;

@Getter
public class HttpResponseException extends RuntimeException {

    private final Request request;
    private final int statusCode;
    private final String reasonPhrase;

    public HttpResponseException(Request request, int statusCode, String reasonPhrase) {
        super(String.format("request: %s, status code: %d" + (TextUtils.isBlank(reasonPhrase) ? "" : ", %s"), request, statusCode, reasonPhrase));
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.request = request;
    }

}
