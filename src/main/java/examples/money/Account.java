package examples.money;

import java.util.Objects;

public class Account {

    private final String sortCode;
    private final String accountNumber;
    private final Money balance;

    public Account(String sortCode, String accountNumber, Money balance) {
        this.sortCode = sortCode;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getSortCode() {
        return sortCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Money getBalance() {
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(sortCode, account.sortCode) && Objects.equals(accountNumber, account.accountNumber) && Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sortCode, accountNumber, balance);
    }

    @Override
    public String toString() {
        return "Account{" +
            "sortCode='" + sortCode + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", balance=" + balance +
            '}';
    }
}
