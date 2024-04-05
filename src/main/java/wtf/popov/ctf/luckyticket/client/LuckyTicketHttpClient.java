package wtf.popov.ctf.luckyticket.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import wtf.popov.ctf.luckyticket.model.EatTicket;
import wtf.popov.ctf.luckyticket.model.Ticket;
import wtf.popov.ctf.luckyticket.model.User;
import lombok.SneakyThrows;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LuckyTicketHttpClient implements LuckyTicketClient {

    private final String serverUrl;

    private final String username;

    private final String password;

    private final Executor executor = Executor.newInstance(

            HttpClientBuilder.create()
                    .setDefaultRequestConfig(
                            RequestConfig.custom()
                                    .setCookieSpec(CookieSpecs.STANDARD)
                                    .build()
                    )
                    .build()

    );

    public LuckyTicketHttpClient(String serverUrl, String username, String password) {
        this.serverUrl = serverUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    @SneakyThrows
    public void login() {
        Request request = Request.Post(serverUrl + "/api/auth/login")
                .bodyForm(Form.form()
                        .add("username", username)
                        .add("password", password)
                        .build());
        execute(request);
    }

    @Override
    public Response<User> getUserInfo() {
        Request request = Request.Get(serverUrl + "/api/user/me");
        return execute(request, User.class);
    }

    @Override
    public Response<Ticket> buyTicket() {
        Request request = Request.Post(serverUrl + "/api/ticket/buy");

        return execute(request, Ticket.class);
    }

    @Override
    public Response<EatTicket> eatTicket(int id) {
        Request request = Request.Post(serverUrl + "/api/ticket/eat")
                .bodyString(
                        "{\"id\": " + id + "}",
                        ContentType.APPLICATION_JSON
                );

        return execute(request, EatTicket.class);
    }

    @Override
    // TODO
    public String getFlag() {
        return null;
    }

    @SneakyThrows
    private HttpResponse execute(Request request) {
        HttpResponse response = executor.execute(request).returnResponse();

        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new HttpResponseException(response.getStatusLine().getStatusCode(), "Invalid status code, expected 200 OK");
        }

        return response;
    }

    @SneakyThrows
    private <T> Response<T> execute(Request request, Class<T> clazz) {
        HttpResponse httpResponse = execute(request);

        Response<T> response = new Response<>();
        response.setServerTime(
                ZonedDateTime.parse(
                        httpResponse.getFirstHeader(HttpHeaders.DATE).getValue(),
                        DateTimeFormatter.RFC_1123_DATE_TIME
                ).toInstant()
        );
        response.setData(
                new ObjectMapper().readValue(
                        EntityUtils.toByteArray(httpResponse.getEntity()),
                        clazz)
        );

        return response;
    }
}
