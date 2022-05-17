package examples.lottery;

import java.util.List;
import java.util.stream.Collectors;

public class MatchService {

    public List<Integer> matches(List<Integer> line, List<Integer> draw) {
        return line.stream()
            .distinct()
            .filter(draw::contains)
            .collect(Collectors.toList());
    }

}
