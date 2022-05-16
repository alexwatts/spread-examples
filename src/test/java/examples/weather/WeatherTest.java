package examples.weather;

import com.alwa.spread.Spread;
import com.alwa.spread.SpreadUtil;
import com.alwa.spread.Spreader;
import com.alwa.spread.annotations.Dynamic;
import com.alwa.spread.annotations.Embed;
import com.alwa.spread.annotations.In;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class WeatherTest {

    private static WeatherService weatherService;

    private final LocalDateTime START_OF_YEAR =
        LocalDateTime.of(2020, 1, 1, 0, 0, 0);

    @In
    private final Spread<BigDecimal> COUNTY_RAINFALL =
        SpreadUtil.sequence(
            SpreadUtil.cumulative(BigDecimal.valueOf(13.1)), //Avon,
            SpreadUtil.cumulative(BigDecimal.valueOf(3.7)),  //Bath_and_North_East_Somerset,
            SpreadUtil.cumulative(BigDecimal.valueOf(8.7)),  //Bedfordshire,
            SpreadUtil.cumulative(BigDecimal.valueOf(18.7)), //Bedford,
            SpreadUtil.cumulative(BigDecimal.valueOf(7.1))   //Berkshire,
        );

    @In
    private final Spread<Instant> EVERY_DAY =
        SpreadUtil.initial(START_OF_YEAR)
            .step(localDateTime -> localDateTime.plusDays(1))
            .map(localDateTime -> localDateTime.toInstant(ZoneOffset.UTC));

    @In
    private final Spread<County> COUNTIES = SpreadUtil.sequence(County.values());

    @In
    @Dynamic
    @Embed(clazz = List.class, steps = 365)
    private final Spread<RainfallReading> RAINFALL_READINGS =
        SpreadUtil.complexType(
            new Spreader<RainfallReading>()
                .factory(() ->
                    new RainfallReading(
                        Spread.in(EVERY_DAY),
                        Spread.in(COUNTY_RAINFALL, 365)
                    )
                )
        );

    @BeforeEach
    public void setup() {
        SpreadUtil.initPackage(
            this,
            this.getClass().getPackage().getName()
        );
        weatherService = new WeatherService(countyRainfallMap());
    }

    @Test
    public void avonRainFallReadingsCorrect() {
        BigDecimal totalRainfallAvon = weatherService.getTotalRainfall(County.Avon);
        assertThat(totalRainfallAvon).isEqualTo(BigDecimal.valueOf(13.1).setScale(2, RoundingMode.DOWN));
    }

    @Test
    public void bathRainFallReadingsCorrect() {
        BigDecimal totalRainfallBath = weatherService.getTotalRainfall(County.Bath_and_North_East_Somerset);
        assertThat(totalRainfallBath).isEqualTo(BigDecimal.valueOf(3.7).setScale(2, RoundingMode.DOWN));
    }

    @Test
    public void bedfordshireRainFallReadingsCorrect() {
        BigDecimal totalRainfallBedfordshire = weatherService.getTotalRainfall(County.Bedfordshire);
        assertThat(totalRainfallBedfordshire).isEqualTo(BigDecimal.valueOf(8.7).setScale(2, RoundingMode.DOWN));
    }

    @Test
    public void bedfordRainFallReadingsCorrect() {
        BigDecimal totalRainfallBedford = weatherService.getTotalRainfall(County.Bedford);
        assertThat(totalRainfallBedford).isEqualTo(BigDecimal.valueOf(18.7).setScale(2, RoundingMode.DOWN));
    }

    @Test
    public void berkshireRainFallReadingsCorrect() {
        BigDecimal totalRainfallBerkshire = weatherService.getTotalRainfall(County.Berkshire);
        assertThat(totalRainfallBerkshire).isEqualTo(BigDecimal.valueOf(7.1).setScale(2, RoundingMode.DOWN));
    }

    private Map<County, List<RainfallReading>> countyRainfallMap() {
        return new Spreader<Map.Entry<County, List<RainfallReading>>>()
            .factory(() ->
                Map.entry(Spread.in(COUNTIES),
                    (List<RainfallReading>)Spread.embed(RAINFALL_READINGS))
            )
            .steps(5)
            .spread()
            .collect(
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
            );
    }

}
