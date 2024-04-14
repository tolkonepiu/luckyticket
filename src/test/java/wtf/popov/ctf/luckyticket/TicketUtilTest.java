package wtf.popov.ctf.luckyticket;

import org.junit.jupiter.api.Test;
import wtf.popov.ctf.luckyticket.util.TicketUtil;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TicketUtilTest {

    @Test
    public void isTicketLuckyTest() {
        assertTrue(TicketUtil.isTicketLucky("000000"));
        assertTrue(TicketUtil.isTicketLucky("999999"));
        assertTrue(TicketUtil.isTicketLucky("010100"));
        assertTrue(TicketUtil.isTicketLucky("828198"));

        assertFalse(TicketUtil.isTicketLucky("000111"));
        assertFalse(TicketUtil.isTicketLucky("123456"));
        assertFalse(TicketUtil.isTicketLucky("010233"));
        assertFalse(TicketUtil.isTicketLucky("0"));
        assertFalse(TicketUtil.isTicketLucky("0000000000000"));
    }

    @Test
    public void getServerTimeWithMillisByTicketNumberTest() {
        Instant serverTimeWithoutMillis = Instant.parse("2024-04-14T15:21:01Z");
        assertEquals(
                Instant.parse("2024-04-14T15:21:01.691Z"),
                Instant.ofEpochMilli(
                        TicketUtil.getServerTimeWithMillisByTicketNumber(
                                "475022",
                                serverTimeWithoutMillis.toEpochMilli(),
                                serverTimeWithoutMillis.plusMillis(1000L).toEpochMilli()
                        )
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> TicketUtil.getServerTimeWithMillisByTicketNumber(
                        "000000",
                        System.currentTimeMillis(),
                        System.currentTimeMillis()
                )
        );
    }

    @Test
    public void generateNextLuckyTimesTest() {
        List<Long> luckyTimes = TicketUtil.generateNextLuckyTimes(1000);
        assertEquals(1000, luckyTimes.size());

        for (long luckyTime : luckyTimes) {
            assertTrue(
                    TicketUtil.isTicketLucky(
                            TicketUtil.generateTicketNumberByEpochMillis(luckyTime)
                    )
            );
        }
    }

    @Test
    public void generateTicketNumberByEpochMillisTest() {
        assertEquals("498081", TicketUtil.generateTicketNumberByEpochMillis(1L));
        assertEquals("266786", TicketUtil.generateTicketNumberByEpochMillis(2L));
        assertEquals("151008", TicketUtil.generateTicketNumberByEpochMillis(3L));
        assertEquals("636829", TicketUtil.generateTicketNumberByEpochMillis(4L));
        assertEquals("243626", TicketUtil.generateTicketNumberByEpochMillis(5L));
    }

}
