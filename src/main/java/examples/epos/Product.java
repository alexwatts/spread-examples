package examples.epos;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {

    private final String productCode;
    private final String productDescription;
    private final BigDecimal price;

    public Product(String productCode, String productDescription, BigDecimal price) {
        this.productCode = productCode;
        this.productDescription = productDescription;
        this.price = price;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productCode, product.productCode) && Objects.equals(productDescription, product.productDescription) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, productDescription, price);
    }

    @Override
    public String toString() {
        return "Product{" +
            "productCode='" + productCode + '\'' +
            ", productDescription='" + productDescription + '\'' +
            ", price=" + price +
            '}';
    }
}
