package examples.money;

import com.alwa.spread.Spread;
import com.alwa.spread.SpreadUtil;
import com.alwa.spread.Spreader;
import com.alwa.spread.annotations.In;
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

    @In
    private final Spread<Currency> FIXED_GBP = SpreadUtil.fixed(Currency.getInstance("GBP"));

    @In
    private final Spread<BigDecimal> TEN_THOUSAND = SpreadUtil.cumulative(BigDecimal.valueOf(10000));

    @In
    private final Spread<BigDecimal> FIVE_THOUSAND = SpreadUtil.cumulative(BigDecimal.valueOf(5000));

    @BeforeEach
    public void setup() {
        SpreadUtil.initPackage(
            this,
            this.getClass().getPackage().getName()
        );
        bankingService = new BankingService();
    }

    @Test
    public void testDepositsAccumulate() {
        Account expectedAccount = new Account(SORT_CODE, ACCOUNT_NUMBER, new Money(Currency.getInstance("GBP"), BigDecimal.valueOf(10000)));
        fiftyDepositsTotalling10000().forEach(deposit -> bankingService.deposit(SORT_CODE, ACCOUNT_NUMBER, deposit));
        assertThat(bankingService.getAccount(SORT_CODE, ACCOUNT_NUMBER)).isEqualTo(expectedAccount);
    }

    @Test
    public void testWithdrawalsAreReflected() {
        Account expectedAccount = new Account(SORT_CODE, ACCOUNT_NUMBER, new Money(Currency.getInstance("GBP"), BigDecimal.valueOf(5000)));
        fiftyDepositsTotalling10000().forEach(deposit -> bankingService.deposit(SORT_CODE, ACCOUNT_NUMBER, deposit));
        twentySevenWithdrawalsTotalling5000().forEach(withdrawal -> bankingService.withdraw(SORT_CODE, ACCOUNT_NUMBER, withdrawal));
        assertThat(bankingService.getAccount(SORT_CODE, ACCOUNT_NUMBER)).isEqualTo(expectedAccount);
    }

    private List<Money> fiftyDepositsTotalling10000() {
        return new Spreader<Money>()
            .factory(() -> new Money(Spread.in(FIXED_GBP), Spread.in(TEN_THOUSAND)))
            .steps(50)
            .spread()
            .collect(Collectors.toList());
    }

    private List<Money> twentySevenWithdrawalsTotalling5000() {
        return new Spreader<Money>()
            .factory(() -> new Money(Spread.in(FIXED_GBP), Spread.in(FIVE_THOUSAND)))
            .steps(27)
            .spread()
            .collect(Collectors.toList());
    }

}