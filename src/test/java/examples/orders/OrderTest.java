package examples.orders;

import com.alwa.spread.Spread;
import com.alwa.spread.SpreadUtil;
import com.alwa.spread.Spreader;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class OrderTest {

    private final Product PRODUCT_ONE = new Product("ALWA1", BigDecimal.valueOf(13.99));
    private final Product PRODUCT_TWO = new Product("ALWA2", BigDecimal.valueOf(5.99));
    private final Product PRODUCT_THREE = new Product("ALWA3", BigDecimal.valueOf(24.99));

    private final Spread<Product> THREE_PRODUCTS =
        SpreadUtil.sequence(PRODUCT_ONE, PRODUCT_TWO, PRODUCT_THREE);

    private final Spread<Integer> VARIABLE_QUANTITIES = SpreadUtil.sequence(1, 2, 3);

    private final Spread<List<OrderLine>> ORDER_LINES =
        SpreadUtil.list(
            new Spreader<OrderLine>()
                .factory(() -> new OrderLine(Spread.in(THREE_PRODUCTS), Spread.in(VARIABLE_QUANTITIES)))
                .steps(3)
        );

    private final Spread<String> CUSTOMER_ID = SpreadUtil.fixed("ALWA123");

    private final Order ORDER =
        new Spreader<Order>()
            .factory(() -> new Order(Spread.in(CUSTOMER_ID), Spread.in((ORDER_LINES))))
            .steps(1)
            .spread()
            .collect(Collectors.toList())
            .get(0);

    @Test
    public void testOrderLinesTotalUpToCorrectPrice() {
        assertThat(ORDER.getOrderTotal()).isEqualTo(BigDecimal.valueOf(100.94));
    }

}
