package wtf.popov.ctf.luckyticket.model;

import lombok.Data;
import wtf.popov.ctf.luckyticket.util.TicketUtil;

@Data
public class Ticket {

    private int id;
    private String number;
    private String status;

    public boolean isLucky() {
        return TicketUtil.isTicketLucky(this.number);
    }

}
