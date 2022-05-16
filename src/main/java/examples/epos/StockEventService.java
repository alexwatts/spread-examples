package examples.epos;

import java.util.List;

public class StockEventService {

    private final List<StockEvent> stockEvents;

    public StockEventService(List<StockEvent> stockEvents) {
        this.stockEvents = stockEvents;
    }

    public void injestStockEvent(StockEvent stockEvent) {
        this.stockEvents.add(stockEvent);
    }

    public List<StockEvent> getStockEvents() {
        return stockEvents;
    }
}
