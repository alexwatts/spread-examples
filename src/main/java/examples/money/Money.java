package examples.money;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public class Money {

    private final Currency currency;
    private final BigDecimal balance;

    public Money(Currency currency, BigDecimal balance) {
        this.currency = currency;
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Money add(Money toAdd) {
        return new Money(this.getCurrency(), this.getBalance().add(toAdd.getBalance()));
    }

    public Money subtract(Money toSubtract) {
        return new Money(this.getCurrency(), this.getBalance().subtract(toSubtract.getBalance()));
    }

    @Override
    public String toString() {
        return "Money{" +
            "currency=" + currency +
            ", balance=" + balance +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(currency, money.currency) && Objects.equals(balance, money.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, balance);
    }

}
