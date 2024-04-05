package wtf.popov.ctf.luckyticket;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import wtf.popov.ctf.luckyticket.client.LuckyTicketClient;
import wtf.popov.ctf.luckyticket.client.LuckyTicketHttpClient;
import wtf.popov.ctf.luckyticket.client.Response;
import wtf.popov.ctf.luckyticket.model.EatTicket;
import wtf.popov.ctf.luckyticket.model.Ticket;
import wtf.popov.ctf.luckyticket.model.User;
import wtf.popov.ctf.luckyticket.predictor.DelayPredictor;
import wtf.popov.ctf.luckyticket.predictor.FrequentDelayPredictor;
import wtf.popov.ctf.luckyticket.sleeper.LoopSleeper;
import wtf.popov.ctf.luckyticket.sleeper.Sleeper;
import wtf.popov.ctf.luckyticket.util.TicketUtil;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LuckyTicketApp {

    public final static int TICKET_PRICE = 10;

    private final LuckyTicketClient client;

    private final Sleeper sleeper = new LoopSleeper();

    private final DelayPredictor delayPredictor = new FrequentDelayPredictor();

    private long clientServerDelay = 0;

    public LuckyTicketApp(String serverUrl, String username, String password) {
        this(new LuckyTicketHttpClient(serverUrl, username, password));
    }

    public LuckyTicketApp(LuckyTicketClient client) {
        this.client = client;
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            throw new IllegalArgumentException("Insufficient number of arguments");
        }

        String serverUrl = args[0];
        String username = args[1];
        String password = args[2];

        LuckyTicketApp app = new LuckyTicketApp(serverUrl, username, password);
        app.start();
    }

    @SneakyThrows
    public void start() {
        log.info("Login...");
        client.login();

        User user = client.getUserInfo().getData();

        while (!user.isOutOfBalance() && !user.isFullOfLack()) {
            int ticketCount = user.getBalance() / TICKET_PRICE;

            List<Integer> luckyTicketIds = buyLuckyTickets(ticketCount);
            eatLuckyTickets(luckyTicketIds);

            user = client.getUserInfo().getData();
            log.info("User balance {}, luck {}%, conversion {}%",
                    user.getBalance(),
                    user.getLuck(),
                    Math.round((luckyTicketIds.size() * 100.0) / ticketCount)
            );
        }
    }

    public List<Integer> buyLuckyTickets(int count) {
        List<Integer> luckyTicketIds = new ArrayList<>(count);

        List<Long> luckyTimes = TicketUtil.generateNextLuckyTimes(count);

        for (int id = 0; id < luckyTimes.size(); id++) {
            long nextLuckyTime = luckyTimes.get(id);
            long nextExecutionTime = nextLuckyTime - clientServerDelay;

            if (nextExecutionTime > System.currentTimeMillis()) {
                sleeper.sleepBefore(nextExecutionTime);

                long clientTimeBeforeRequest = System.currentTimeMillis();
                Response<Ticket> ticketResponse = client.buyTicket();
                Ticket ticket = ticketResponse.getData();

                long finalServerTime = TicketUtil.getServerTimeWithMillisByTicketNumber(
                        ticket.getNumber(),
                        ticketResponse.getServerTime()
                );
                long actualServerDelay = finalServerTime - clientTimeBeforeRequest;

                if (!ticket.isLucky()) {
                    log.info(
                            "FAILED: Ticket {} is not lucky, diff {}ms, delay diff {}ms",
                            ticket.getNumber(),
                            nextLuckyTime - finalServerTime,
                            actualServerDelay - clientServerDelay
                    );
                    continue;
                }
                log.info(
                        "SUCCESS: Ticket {} is lucky, diff {}ms, delay diff {}ms",
                        ticket.getNumber(),
                        nextLuckyTime - finalServerTime,
                        actualServerDelay - clientServerDelay
                );
                clientServerDelay = delayPredictor.nextDelay(actualServerDelay);
                luckyTicketIds.add(ticket.getId());
            }
        }
        delayPredictor.reset();

        return luckyTicketIds;
    }

    public void eatLuckyTickets(List<Integer> luckyTicketIds) {
        for (int luckyTicketId : luckyTicketIds) {
            EatTicket eatTicket = client.eatTicket(luckyTicketId).getData();
            log.info("Ticket {} was eaten", eatTicket.getTicket());
        }
    }
}
