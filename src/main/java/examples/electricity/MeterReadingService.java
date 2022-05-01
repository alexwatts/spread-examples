package examples.electricity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeterReadingService {

    private Map<String, List<ElectricityReading>> meterReadings = new HashMap<>();

    public void submitReadings(
        String meterId,
        List<ElectricityReading> readings) {
        meterReadings.put(meterId, readings);
    }

    public BigDecimal getTotalKws(String meterId) {
        return meterReadings.get(meterId)
            .stream()
            .map(ElectricityReading::getReadingInKws)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
