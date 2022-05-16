package examples.weather;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class WeatherService {

    private final Map<County, List<RainfallReading>> weatherService;

    public WeatherService(Map<County, List<RainfallReading>> weatherService) {
        this.weatherService = weatherService;
    }

    public BigDecimal getTotalRainfall(County country) {
        return weatherService.get(country)
            .stream()
            .map(RainfallReading::getRainfallMM)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
