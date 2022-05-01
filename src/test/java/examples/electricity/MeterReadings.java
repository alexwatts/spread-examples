package examples.electricity;

import com.alwa.spread.Spread;
import com.alwa.spread.SpreadUtil;
import com.alwa.spread.Spreader;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MeterReadings {

    private static final String METER_ID = "ALWA123";

    MeterReadingService subject = new MeterReadingService();

    LocalDateTime WEEK_START = LocalDateTime.of(2022, 4, 2, 1, 1);

    Spread<Instant> EVERY_HOUR =
        SpreadUtil
            .initial(WEEK_START)
            .step(dateTime -> dateTime.plusHours(1))
            .map(dateTime -> dateTime.toInstant(ZoneOffset.UTC));

    Spread<BigDecimal> tenThousandKws = SpreadUtil.cumulative(BigDecimal.valueOf(10000));

    List<ElectricityReading> READINGS_ACROSS_WEEK =
        new Spreader<ElectricityReading>()
            .factory(() -> new ElectricityReading(Spread.in(EVERY_HOUR), Spread.in(tenThousandKws)))
            .steps(168)
            .debug()
            .spread()
            .collect(Collectors.toList());

    @Test
    public void someReadingsSingleMeter() {
        subject.submitReadings(METER_ID, READINGS_ACROSS_WEEK);
        assertThat(subject.getTotalKws(METER_ID)).isEqualTo(BigDecimal.valueOf(10000));
    }
}
