package examples.epos;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class EposService {

    private final Map<String, Product> barcodesProducts;
    private final StockEventService stockEventService;

    public EposService(Map<String, Product> barcodesProducts, StockEventService stockEventService) {
        this.barcodesProducts = barcodesProducts;
        this.stockEventService = stockEventService;
    }

    public Product getProduct(String barcode) {
        return barcodesProducts.get(barcode);
    }

    public void submitTransaction(List<String> barcodes, Instant transactionTime) {
        barcodes
            .stream()
            .map(barcode -> new StockEvent(StockEventType.SALE, transactionTime, barcodesProducts.get(barcode).getProductCode()))
            .forEach(stockEventService::injestStockEvent);
    }

}
