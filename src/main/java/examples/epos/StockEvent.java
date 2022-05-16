package examples.epos;

import java.time.Instant;
import java.util.Objects;

public class StockEvent {

    private final StockEventType stockEventType;
    private final Instant transactionTime;
    private final String productCode;

    public StockEvent(StockEventType stockEventType, Instant transactionTime, String productCode) {
        this.stockEventType = stockEventType;
        this.transactionTime = transactionTime;
        this.productCode = productCode;
    }

    @Override
    public String toString() {
        return "StockEvent{" +
            "stockEventType=" + stockEventType +
            ", transactionTime=" + transactionTime +
            ", productCode='" + productCode + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockEvent that = (StockEvent) o;
        return stockEventType == that.stockEventType && Objects.equals(transactionTime, that.transactionTime) && Objects.equals(productCode, that.productCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stockEventType, transactionTime, productCode);
    }
}
