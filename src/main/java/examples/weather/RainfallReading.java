package examples.weather;

import java.math.BigDecimal;
import java.time.Instant;

public class RainfallReading {

    private final Instant readingDate;

    private final BigDecimal rainfallMM;

    public RainfallReading(Instant readingDate, BigDecimal rainfallMM) {
        this.readingDate = readingDate;
        this.rainfallMM = rainfallMM;
    }

    public Instant getReadingDate() {
        return readingDate;
    }

    public BigDecimal getRainfallMM() {
        return rainfallMM;
    }

}
