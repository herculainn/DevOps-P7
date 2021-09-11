package revolut;

import java.util.Currency;
import java.util.HashMap;

public class Person {

    private final String name;
    //Accounts currency & balance
    // EUR 30
    // USD 50
    // STG 30 // GBP under ISO 4217
    private static final String defaultAccountCode = "EUR";
    private String selectedAccountCode;
    private HashMap<String, Account> userAccounts = new HashMap<String, Account>();

    public Person(String name){
        this.name = name;
        this.setSelectedAccountCode(defaultAccountCode);

        // Create a default account and Add to userAccounts container
        this.addAccount(selectedAccountCode);
    }

    public void setSelectedAccountCode(String aCurrencyCode) {
        this.selectedAccountCode = aCurrencyCode;
    }

    public Account addAccount(String aCurrencyCode) {
        Account tmpAccount = new Account(aCurrencyCode, 0);
        this.userAccounts.put(aCurrencyCode, tmpAccount);
        return tmpAccount;
    }

    public Account getAccount(String aAccount) {
        Account returnAccount = null;
        try {
            returnAccount = userAccounts.get(aAccount);
            if (returnAccount == null) {
                returnAccount = this.addAccount(aAccount);
            }
        } finally {
            assert returnAccount != null;
        }
        return returnAccount;
    }

    public Account getAccount() {
        return this.getAccount(selectedAccountCode);
    }

    public double getAccountBalance(String aAccount) {
        Account tmpAccount = this.getAccount(aAccount);
        return tmpAccount.getBalance();
    }

    public double getAccountBalance() {
        Account tmpAccount = this.getAccount(selectedAccountCode);
        return tmpAccount.getBalance();
    }

    public void setAccountBalance(double aStartingBalance) {
        this.setAccountBalance(selectedAccountCode, aStartingBalance);
    }

    public void setAccountBalance(String aAccount, double aStartingBalance) {
        Account tmpAccount = this.getAccount(aAccount);
        tmpAccount.setBalance(aStartingBalance);
    }

    public String getName() {
        return this.name;
    }
}
