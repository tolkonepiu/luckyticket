package wtf.popov.ctf.luckyticket;

import wtf.popov.ctf.luckyticket.client.LuckyTicketClient;
import wtf.popov.ctf.luckyticket.client.LuckyTicketHttpClient;
import wtf.popov.ctf.luckyticket.client.Response;
import wtf.popov.ctf.luckyticket.model.Ticket;
import wtf.popov.ctf.luckyticket.model.User;
import wtf.popov.ctf.luckyticket.util.TicketUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;

@Slf4j
public class LuckyTicketApp {

    public static void main(String[] args) {
        if (args.length < 3) {
            throw new IllegalArgumentException("Insufficient number of arguments");
        }

        String serverUrl = args[0];
        String username = args[1];
        String password = args[2];

        LuckyTicketClient luckyTicketClient = new LuckyTicketHttpClient(serverUrl, username, password);

        log.info("Login...");
        luckyTicketClient.login();

        startBuyingProcess(luckyTicketClient);
    }

    @SneakyThrows
    public static void startBuyingProcess(LuckyTicketClient luckyTicketClient) {
        Duration clientServerDelay = Duration.ZERO;

        User user = luckyTicketClient.getUserInfo().getData();
        while (!user.isOutOfBalance() && !user.isFullOfLack()) {
            for (Instant nextLuckyTime : TicketUtil.generateNextLuckyTimes()) {
                if (nextLuckyTime.minus(clientServerDelay).isAfter(Instant.ofEpochMilli(System.currentTimeMillis()))) {
                    Duration sleepDuration = Duration.between(Instant.ofEpochMilli(System.currentTimeMillis()), nextLuckyTime.minus(clientServerDelay));
                    log.info("Sleep to {}ms", sleepDuration.toMillis());
                    Thread.sleep(sleepDuration.toMillis());

                    Instant clientTimeBeforeRequest = Instant.ofEpochMilli(System.currentTimeMillis());
                    Response<Ticket> ticketResponse = luckyTicketClient.buyTicket();
                    Ticket ticket = ticketResponse.getData();

                    Instant finalServerTime = TicketUtil.getServerTimeWithMillisByTicketNumber(
                            ticket.getNumber(),
                            ticketResponse.getServerTime()
                    );

                    if (ticket.isLucky()) {
                        log.info(
                                "SUCCESS: Ticket {} is lucky, diff {}ms",
                                ticket.getNumber(),
                                Duration.between(nextLuckyTime, finalServerTime).toMillis()
                        );
                        luckyTicketClient.eatTicket(ticketResponse.getData().getId());
                    } else {
                        log.info(
                                "FAILED: Ticket {} is not lucky, diff {}ms",
                                ticket.getNumber(),
                                Duration.between(nextLuckyTime, finalServerTime).toMillis()
                        );
                    }
                    clientServerDelay = Duration.between(clientTimeBeforeRequest, finalServerTime);
                    log.info("Client server delay: {}ms", clientServerDelay.toMillis());

                    user = luckyTicketClient.getUserInfo().getData();
                    log.info("User balance {}, luck {}%", user.getBalance(), user.getLuck());
                }
            }
        }
    }
}
