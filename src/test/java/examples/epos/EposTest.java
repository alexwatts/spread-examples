package examples.epos;

import com.alwa.spread.Spread;
import com.alwa.spread.SpreadUtil;
import com.alwa.spread.Spreader;
import com.alwa.spread.annotations.In;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EposTest {

    private EposService eposService;
    private StockEventService stockEventService;

    private final LocalDateTime START_OF_DAY =
        LocalDateTime.of(2020, 5, 18, 0, 0, 0);

    @In
    private final Spread<String> BARCODES =
        SpreadUtil.sequence(
            "623456257806",
            "296739530673",
            "128656027856",
            "835795237942",
            "356578878653",
            "975545553645",
            "275672932004",
            "657894378231",
            "143254365789",
            "246890674565",
            "267432950429",
            "754325402134",
            "564739083219",
            "163784137064",
            "625379265396",
            "675803221631",
            "967823429101",
            "324317912036",
            "787235432453",
            "576542342682"
        );

    @In
    private final Spread<Product> PRODUCTS =
        SpreadUtil.sequence(
            new Product("STK1312352", "BIRO 6 PACK", BigDecimal.valueOf(2.99d)),
            new Product("STK8745524", "CHOC COOKIE", BigDecimal.valueOf(1.99d)),
            new Product("STK7636425", "PEPSI CAN 6", BigDecimal.valueOf(4.99d)),
            new Product("STK8763524", "GLOVES BLCK", BigDecimal.valueOf(9.99d)),
            new Product("STK8745234", "PASTRY ROLL", BigDecimal.valueOf(1.23d)),
            new Product("STK2367872", "BEEF A 150G", BigDecimal.valueOf(3.42d)),
            new Product("STK8745325", "SAUSAGE LRG", BigDecimal.valueOf(3.22d)),
            new Product("STK2145678", "CHEESE SLIC", BigDecimal.valueOf(1.99d)),
            new Product("STK1456576", "SALMON CAKE", BigDecimal.valueOf(2.99d)),
            new Product("STK8745676", "CHEWING GUM", BigDecimal.valueOf(0.99)),
            new Product("STK1245656", "ICING SUGAR", BigDecimal.valueOf(1.49d)),
            new Product("STK2456546", "VANNILA XCT", BigDecimal.valueOf(3.75d)),
            new Product("STK6324752", "ASPARAG 80G", BigDecimal.valueOf(0.80d)),
            new Product("STK1787654", "GREEN TEA 6", BigDecimal.valueOf(5.99d)),
            new Product("STK8762373", "MARMITE SML", BigDecimal.valueOf(1.99d)),
            new Product("STK1232435", "CHILLI MAYO", BigDecimal.valueOf(2.60d)),
            new Product("STK7365732", "EGGS 6 PACK", BigDecimal.valueOf(2.50d)),
            new Product("STK6792304", "PEANUT BUTR", BigDecimal.valueOf(3.25d)),
            new Product("STK4578423", "ICE CREAM S", BigDecimal.valueOf(6.99d)),
            new Product("STK8764576", "COFFEE PODS", BigDecimal.valueOf(8.99d))
        );

    @In
    private final Spread<Instant> FIXED_TRANSACTION_TIME =
        SpreadUtil.fixed(START_OF_DAY.plusMinutes(20).toInstant(UTC));

    @In
    private final Spread<StockEventType> SALE_STOCK_EVENT_TYPE =
        SpreadUtil.fixed(StockEventType.SALE);

    @In
    private final Spread<String> MAPPED_PRODUCT_CODE =
        SpreadUtil.related(PRODUCTS)
            .step(Product::getProductCode);

    @BeforeEach
    public void setup() {
        SpreadUtil.initPackage(
            this,
            this.getClass().getPackage().getName()
        );
        stockEventService = new StockEventService(new ArrayList<>());
        eposService = new EposService(barcodesProductsMap(), stockEventService);
    }

    @Test
    public void testGetProducts() {
        assertThat(
            IntStream.range(0, 20)
                .mapToObj(i -> eposService.getProduct((String)BARCODES.getValues()[i]))
                .collect(Collectors.toList())
        ).isEqualTo(Arrays.stream(PRODUCTS.getValues()).collect(Collectors.toList()));
    }

    @Test
    public void testSubmittedTransactionsEmitStockEvents() {
        eposService.submitTransaction(
            IntStream.range(0, 20)
                .mapToObj(i -> (String)BARCODES.getValues()[i])
                .collect(Collectors.toList()), START_OF_DAY.plusMinutes(20).toInstant(UTC));

        List<StockEvent> STOCK_EVENTS =
            new Spreader<StockEvent>()
                .factory(() -> new StockEvent(
                    Spread.in(SALE_STOCK_EVENT_TYPE),
                    Spread.in(FIXED_TRANSACTION_TIME),
                    Spread.in(MAPPED_PRODUCT_CODE))
                )
                .steps(20)
                .spread()
                .collect(Collectors.toList());

        assertThat(stockEventService.getStockEvents()).isEqualTo(STOCK_EVENTS);
    }

    private Map<String, Product> barcodesProductsMap() {
        return new Spreader<Map.Entry<String, Product>>()
            .factory(() ->
                Map.entry(
                    Spread.in(BARCODES),
                    Spread.in(PRODUCTS)
                )
            )
            .steps(20)
            .spread()
            .collect(
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
            );
    }

}
