package revolut;

import java.util.Currency;

public class Account {
    private Currency accCurrency;
    private PaymentService paymentService;
    private double balance;

    public Account(String aCurrency, double balance){
        this.balance = balance;

        // Moved Currency.getInstance here as it is now used multiple times
        // Following requires GBP not STG from ISO 4217
        // https://docs.oracle.com/javase/8/docs/api/java/util/Currency.html#getInstance-java.lang.String-
        this.accCurrency = Currency.getInstance(aCurrency);

        // Revolut using Visa Debit tro process payments?
        this.paymentService = new PaymentService("VisaDebit");
    }

    public void setBalance(double newBalance) {
        // Set an initial balance to bypass Payment Services
        this.balance = newBalance;
    }

    public double getBalance() {
        return this.balance;
    }

    public boolean verifyPayment(double aPayment) {
        // Payment Service will check that there is enough in the account...
        return (this.balance >= aPayment);
    }

    public double makePayment(double aPayment) {
        // Payment Service has authorised payment
        this.balance -= aPayment;
        return aPayment;
    }

    public double takePayment(double aPayment) {
        // Payment Service is giving us money
        this.balance += aPayment;
        return aPayment;
    }

    public boolean addFunds(double aTopUpAmount, Account aTopUpAccount) {
        // Trigger Payment Service to attempt to process the
        // payment between the two accounts for given amount
        return this.paymentService.processPayment(
                aTopUpAccount, // aSource
                this, // aTarget
                aTopUpAmount // aAmount
        );
    }

    public boolean addFundsTriangulation(
            double aTopUpAmount, String aCurrency, Account aTopUpAccount) {
        return this.paymentService.processPaymentTriangulation(
                aTopUpAccount, // aSource
                this, // aTarget
                aTopUpAmount, // aAmount
                aCurrency
        );
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public Currency getAccCurrency() {
        return accCurrency;
    }

    public void setAccCurrency(String aCurrency) {
        this.accCurrency = Currency.getInstance(aCurrency);
    }
}
