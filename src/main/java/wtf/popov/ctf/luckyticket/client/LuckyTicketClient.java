package wtf.popov.ctf.luckyticket.client;

import wtf.popov.ctf.luckyticket.model.EatTicket;
import wtf.popov.ctf.luckyticket.model.Interview;
import wtf.popov.ctf.luckyticket.model.Ticket;
import wtf.popov.ctf.luckyticket.model.User;

public interface LuckyTicketClient {

    void login();

    Response<User> getUserInfo();

    Response<Ticket> buyTicket();

    Response<EatTicket> eatTicket(int id);

    Response<Interview> interview();

}
