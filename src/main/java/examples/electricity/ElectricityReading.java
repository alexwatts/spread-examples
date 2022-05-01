package examples.electricity;

import java.math.BigDecimal;
import java.time.Instant;

public class ElectricityReading {

    private final Instant readingTime;
    private final BigDecimal readingInKws;

    public ElectricityReading(Instant readingTime, BigDecimal readingInKws) {
        this.readingTime = readingTime;
        this.readingInKws = readingInKws;
    }

    public Instant getReadingTime() {
        return readingTime;
    }

    public BigDecimal getReadingInKws() {
        return readingInKws;
    }
}
