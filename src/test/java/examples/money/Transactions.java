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

public class Transactions {

    private BankingService bankingService;

    @BeforeEach
    public void setup() {
        bankingService = new BankingService();
    }

    private String SORT_CODE = "ALWA1";
    private String ACCOUNT_NUMBER = "887700";

    private Spread<Currency> FIXED_GPB = SpreadUtil.fixed(Currency.getInstance("GBP"));
    private Spread<BigDecimal> TEN_THOUSAND = SpreadUtil.cumulative(BigDecimal.valueOf(10000));
    private Spread<BigDecimal> FIVE_THOUSAND = SpreadUtil.cumulative(BigDecimal.valueOf(5000));

    private List<Money> fiftyDeposits =
        new Spreader<Money>()
            .factory(() -> new Money(Spread.in(FIXED_GPB), Spread.in(TEN_THOUSAND)))
            .steps(50)
            .spread()
            .collect(Collectors.toList());

    private List<Money> TwentySevenWithdrawals =
        new Spreader<Money>()
            .factory(() -> new Money(Spread.in(FIXED_GPB), Spread.in(FIVE_THOUSAND)))
            .steps(27)
            .spread()
            .collect(Collectors.toList());

    @Test
    public void testDepositsAccumulate() {
        Account expectedAccount = new Account(SORT_CODE, ACCOUNT_NUMBER, new Money(Currency.getInstance("GBP"), BigDecimal.valueOf(10000)));
        fiftyDeposits.forEach(deposit -> bankingService.deposit(SORT_CODE, ACCOUNT_NUMBER, deposit));
        assertThat(bankingService.getAccount(SORT_CODE, ACCOUNT_NUMBER)).isEqualTo(expectedAccount);
    }

    @Test
    public void testWithdrawalsAreReflected() {
        Account expectedAccount = new Account(SORT_CODE, ACCOUNT_NUMBER, new Money(Currency.getInstance("GBP"), BigDecimal.valueOf(5000)));
        fiftyDeposits.forEach(deposit -> bankingService.deposit(SORT_CODE, ACCOUNT_NUMBER, deposit));
        TwentySevenWithdrawals.forEach(deposit -> bankingService.withdraw(SORT_CODE, ACCOUNT_NUMBER, deposit));
        assertThat(bankingService.getAccount(SORT_CODE, ACCOUNT_NUMBER)).isEqualTo(expectedAccount);
    }
}
