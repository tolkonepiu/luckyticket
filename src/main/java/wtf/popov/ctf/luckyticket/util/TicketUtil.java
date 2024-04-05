package wtf.popov.ctf.luckyticket.util;

import wtf.popov.ctf.luckyticket.random.GoRandom;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class TicketUtil {

    public static final int MAX_TICKET_NUMBER = 1000000;

    public static final GoRandom random = new GoRandom();

    public static Instant getServerTimeWithMillisByTicketNumber(String ticketNumber, Instant serverTime) {
        Instant startTime = serverTime.minusSeconds(5);
        Instant endTime = serverTime.plusSeconds(5);
        for (Instant t = startTime; t.isBefore(endTime); t = t.plus(1L, ChronoUnit.MILLIS)) {
            if (ticketNumber.equals(generateTicketNumberByInstant(t))) {
                return t;
            }
        }

        throw new IllegalArgumentException("No ticket was found for this time period");
    }

    public static List<Instant> generateNextLuckyTimes() {
        long time = System.currentTimeMillis();

        return LongStream.range(time, time + Duration.ofSeconds(10L).toMillis())
                .parallel()
                .filter(value -> {
                    String ticketNumber = generateTicketNumberByEpochMillis(value);
                    return isTicketLucky(ticketNumber);
                })
                .sorted()
                .mapToObj(Instant::ofEpochMilli)
                .collect(Collectors.toList());
    }

    public static boolean isTicketLucky(String ticketNumber) {
        if (ticketNumber.length() != 6) {
            return false;
        }

        int firstSum = 0;
        int secondSum = 0;
        for (int i = 0; i < 3; i++) {

            firstSum += Character.getNumericValue(ticketNumber.charAt(i));
            secondSum += Character.getNumericValue(ticketNumber.charAt(i + 3));
        }
        return firstSum == secondSum;
    }

    public static String generateTicketNumberByInstant(Instant instant) {
        return generateTicketNumberByEpochMillis(instant.toEpochMilli());
    }

    public static String generateTicketNumberByEpochMillis(long epochMillis) {
        return String.format("%06d", random.random(MAX_TICKET_NUMBER, epochMillis));
    }

}
