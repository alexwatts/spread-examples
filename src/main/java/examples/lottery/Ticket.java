package examples.lottery;

import java.util.List;

public class Ticket {

    private final String ticketId;
    private final List<List<Integer>> lines;

    public Ticket(String ticketId, List<List<Integer>> lines) {
        this.ticketId = ticketId;
        this.lines = lines;
    }

    public String getTicketId() {
        return ticketId;
    }

    public List<List<Integer>> getLines() {
        return lines;
    }
}

