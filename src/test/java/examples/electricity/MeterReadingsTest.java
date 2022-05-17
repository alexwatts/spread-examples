package examples.electricity;

import com.alwa.spread.SpreadUtil;
import com.alwa.spread.Spreader;
import com.alwa.spread.annotations.In;
import com.alwa.spread.core.Spread;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MeterReadingsTest {

    private static final String METER_ID = "ALWA123";
    private MeterReadingService meterReadingService;
    private final LocalDateTime WEEK_START = LocalDateTime.of(2022, 4, 2, 1, 1);

    @In
    private final Spread<Instant> EVERY_HOUR =
        SpreadUtil
            .initial(WEEK_START)
            .step(dateTime -> dateTime.plusHours(1))
            .map(dateTime -> dateTime.toInstant(ZoneOffset.UTC));

    @In
    private final Spread<BigDecimal> TEN_THOUSAND_KWS = SpreadUtil.cumulative(BigDecimal.valueOf(10000));

    @BeforeEach
    public void setUp() {
        SpreadUtil.initPackage(
            this,
            this.getClass().getPackage().getName()
        );
        meterReadingService = new MeterReadingService();
    }

    @Test
    public void someReadingsSingleMeter() {
        List<ElectricityReading> READINGS_ACROSS_WEEK =
            new Spreader<ElectricityReading>()
                .factory(() -> new ElectricityReading(Spread.in(EVERY_HOUR), Spread.in(TEN_THOUSAND_KWS)))
                .steps(168)
                .spread()
                .collect(Collectors.toList());

        meterReadingService.submitReadings(METER_ID, READINGS_ACROSS_WEEK);
        assertThat(meterReadingService.getTotalKws(METER_ID)).isEqualTo(BigDecimal.valueOf(10000));
    }
}
