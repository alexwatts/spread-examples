package examples.lottery;

import com.alwa.spread.SpreadUtil;
import com.alwa.spread.Spreader;
import com.alwa.spread.annotations.Embed;
import com.alwa.spread.annotations.In;
import com.alwa.spread.core.Spread;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class LotteryTest {

    private DrawService drawService;
    private TicketService ticketService;
    private MatchService matchService;

    @In
    @Embed(clazz = List.class, steps = 6)
    Spread<Integer> DRAWS_SEQUENCE =
        SpreadUtil.sequence(
            SpreadUtil.sequence(8, 4, 25, 36, 32, 49),
            SpreadUtil.sequence(24, 34, 46, 30, 4, 12),
            SpreadUtil.sequence(31, 27, 17, 23, 19, 13),
            SpreadUtil.sequence(37, 35, 44, 11, 36, 21),
            SpreadUtil.sequence(1, 3, 43, 47, 2, 20),
            SpreadUtil.sequence(41, 7, 10, 38, 40, 42)
        );

    @In
    @Embed(clazz = List.class, steps = 6)
    Spread<Integer> TICKET_LINES =
        SpreadUtil.sequence(
            SpreadUtil.sequence(8, 6, 45, 14, 28, 39),
            SpreadUtil.sequence(24, 34, 33, 18, 9, 28),
            SpreadUtil.sequence(31, 27, 17, 26, 28, 39),
            SpreadUtil.sequence(37, 35, 44, 11, 28, 5),
            SpreadUtil.sequence(1, 3, 43, 47, 2, 29),
            SpreadUtil.sequence(41, 7, 10, 38, 40, 42)
        );

    private Iterator<List<Integer>> draws;


    @BeforeEach
    public void setup() {
        SpreadUtil.initPackage(
            this,
            this.getClass().getPackage().getName()
        );
        matchService = new MatchService();
        drawService = new DrawService(drawMachine());
        ticketService = new TicketService();
        draws = draws();
    }

    private Supplier<List<Integer>> drawMachine() {
        return () -> draws.next();
    }

    @Test
    public void testTicketsAgainstDrawsForAllMatchCombinations() {
        Ticket ticket = ticket(TICKET_LINES);

        assertLineMatches(
            ticket,
            drawService.draw(),
            matches(8), noMatches(), noMatches(), noMatches(), noMatches(), noMatches());

        assertLineMatches(
            ticket,
            drawService.draw(),
            noMatches(), matches(24, 34), noMatches(), noMatches(), noMatches(), noMatches());

        assertLineMatches(
            ticket,
            drawService.draw(),
            noMatches(), noMatches(), matches(31, 27, 17), noMatches(), noMatches(), noMatches());

        assertLineMatches(
            ticket,
            drawService.draw(),
            noMatches(), noMatches(), noMatches(), matches(37, 35, 44, 11), noMatches(), noMatches());

        assertLineMatches(
            ticket,
            drawService.draw(),
            noMatches(), noMatches(), noMatches(), noMatches(), matches(1, 3, 43, 47, 2), noMatches());

        assertLineMatches(
            ticket,
            drawService.draw(),
            noMatches(), noMatches(), noMatches(), noMatches(), noMatches(), matches(41, 7, 10, 38, 40, 42));
    }

    private Ticket ticket(Spread<Integer> lines) {
        return ticketService.buyTicket(
            new Spreader<List<Integer>>()
                .factory(ArrayList::new)
                .mutator(list -> list.addAll(Spread.embed(lines)))
                .steps(6)
                .spread()
                .collect(Collectors.toList())
        );
    }

    private Iterator<List<Integer>> draws() {
        return new Spreader<List<Integer>>()
            .factory(() -> (List<Integer>)Spread.embed(DRAWS_SEQUENCE))
            .steps(6)
            .spread()
            .collect(Collectors.toList())
            .iterator();
    }

    private void assertLineMatches(
        Ticket ticket,
        List<Integer> draw,
        List<Integer>... lineMatchAsserts) {

            List<List<Integer>> lineMatches = getLineMatches(ticket, draw);

            IntStream
                .range(0, 6)
                .forEach(
                    i -> assertThat(lineMatches.get(i)).isEqualTo(lineMatchAsserts[i])
                );
    }

    private List<List<Integer>> getLineMatches(Ticket ticket, List<Integer> draw) {
        return IntStream
            .range(0, 6)
            .mapToObj(i -> ticket.getLines().get(i))
            .map(line -> matchService.matches(line, draw))
            .collect(Collectors.toList());
    }

    private List<Integer> noMatches() {
        return Collections.emptyList();
    }

    private List<Integer> matches(Integer... matches) {
        return Arrays.stream(matches).collect(Collectors.toList());
    }

}
