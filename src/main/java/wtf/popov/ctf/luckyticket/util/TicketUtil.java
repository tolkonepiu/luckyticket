package wtf.popov.ctf.luckyticket.util;

import wtf.popov.ctf.luckyticket.random.GoRandom;

import java.util.List;
import java.util.stream.Stream;

public class TicketUtil {

    public static final int MAX_TICKET_NUMBER = 1000000;

    public static final GoRandom random = new GoRandom();

    public static long getServerTimeWithMillisByTicketNumber(String ticketNumber, long startTime, long endTime) {
        for (long t = startTime; t <= endTime; t++) {
            if (ticketNumber.equals(generateTicketNumberByEpochMillis(t))) {
                return t;
            }
        }

        throw new IllegalArgumentException("No ticket was found for this time period");
    }

    public static List<Long> generateNextLuckyTimes(int count) {
        return generateNextLuckyTimes(count, 1000L, 1L);
    }

    public static List<Long> generateNextLuckyTimes(int count, long startDelay, long timeInterval) {
        return Stream.iterate(System.currentTimeMillis() + startDelay, i -> i + timeInterval)
                .parallel()
                .filter(time -> {
                    String ticketNumber = generateTicketNumberByEpochMillis(time);
                    return isTicketLucky(ticketNumber);
                })
                .limit(count)
                .sorted()
                .toList();
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

    public static String generateTicketNumberByEpochMillis(long epochMillis) {
        return String.format("%06d", random.random(MAX_TICKET_NUMBER, epochMillis));
    }

}
