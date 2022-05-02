package examples.money;

import com.alwa.spread.Spread;
import com.alwa.spread.SpreadUtil;
import com.alwa.spread.Spreader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TransactionsTest {

    private BankingService bankingService;

    private final String SORT_CODE = "ALWA1";
    private final String ACCOUNT_NUMBER = "887700";

    private final Spread<Currency> FIXED_GBP = SpreadUtil.fixed(Currency.getInstance("GBP"));
    private final Spread<BigDecimal> TEN_THOUSAND = SpreadUtil.cumulative(BigDecimal.valueOf(10000));
    private final Spread<BigDecimal> FIVE_THOUSAND = SpreadUtil.cumulative(BigDecimal.valueOf(5000));

    private final List<Money> FIFTY_DEPOSITS =
        new Spreader<Money>()
            .factory(() -> new Money(Spread.in(FIXED_GBP), Spread.in(TEN_THOUSAND)))
            .steps(50)
            .spread()
            .collect(Collectors.toList());

    private final List<Money> TWENTY_SEVEN_WITHDRAWALS =
        new Spreader<Money>()
            .factory(() -> new Money(Spread.in(FIXED_GBP), Spread.in(FIVE_THOUSAND)))
            .steps(27)
            .spread()
            .collect(Collectors.toList());

    @BeforeEach
    public void setup() {
        bankingService = new BankingService();
    }

    @Test
    public void testDepositsAccumulate() {
        Account expectedAccount = new Account(SORT_CODE, ACCOUNT_NUMBER, new Money(Currency.getInstance("GBP"), BigDecimal.valueOf(10000)));
        FIFTY_DEPOSITS.forEach(deposit -> bankingService.deposit(SORT_CODE, ACCOUNT_NUMBER, deposit));
        assertThat(bankingService.getAccount(SORT_CODE, ACCOUNT_NUMBER)).isEqualTo(expectedAccount);
    }

    @Test
    public void testWithdrawalsAreReflected() {
        Account expectedAccount = new Account(SORT_CODE, ACCOUNT_NUMBER, new Money(Currency.getInstance("GBP"), BigDecimal.valueOf(5000)));
        FIFTY_DEPOSITS.forEach(deposit -> bankingService.deposit(SORT_CODE, ACCOUNT_NUMBER, deposit));
        TWENTY_SEVEN_WITHDRAWALS.forEach(deposit -> bankingService.withdraw(SORT_CODE, ACCOUNT_NUMBER, deposit));
        assertThat(bankingService.getAccount(SORT_CODE, ACCOUNT_NUMBER)).isEqualTo(expectedAccount);
    }
}