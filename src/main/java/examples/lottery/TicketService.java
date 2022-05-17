package examples.lottery;

import java.util.List;
import java.util.UUID;

public class TicketService {

    public Ticket buyTicket(List<List<Integer>> lines) {
        Ticket ticket = new Ticket(UUID.randomUUID().toString(), lines);
        return ticket;
    }

}
