package examples.money;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class BankingService {

    private Map<String, Account> accounts = new HashMap<>();

    public void deposit(String sortCode, String accountNumber, Money money) {
        Account account = accounts
            .computeIfAbsent(
                sortCode + "-" + accountNumber,
                s -> new Account(sortCode, accountNumber, new Money(Currency.getInstance("GBP"), BigDecimal.ZERO))
            );

        accounts.put(sortCode + "-" + accountNumber, new Account(sortCode, accountNumber, account.getBalance().add(money)));
    }

    public void withdraw(String sortCode, String accountNumber, Money money) {
        Account account = accounts.get(sortCode + "-" + accountNumber);
        accounts.put(sortCode + "-" + accountNumber, new Account(sortCode, accountNumber, account.getBalance().subtract(money)));
    }

    public Account getAccount(String sortCode, String accountNumber) {
        return accounts.get(sortCode + "-" + accountNumber);
    }
}
