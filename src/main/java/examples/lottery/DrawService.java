package examples.lottery;

import java.util.List;
import java.util.function.Supplier;

public class DrawService {

    private final Supplier<List<Integer>> drawMachine;

    public DrawService(Supplier<List<Integer>> drawMachine) {
        this.drawMachine = drawMachine;
    }

    public List<Integer> draw() {
        return drawMachine.get();
    }
}
