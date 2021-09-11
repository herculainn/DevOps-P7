package revolut;

import java.util.Currency;

public class PaymentService {
    private String serviceName;

    // Rates rounded from actual for ease of testing
    public static final double EUR_TO_GBP = 0.8; // 0.85; (actual)
    public static final double EUR_TO_USD = 1.2; //1.18; (actual)
    // Following will have been triangulated:
    // GBP_TO_USD = 1.2 / 0.8 = 1.5; // 0.85 / 1.18 = 1.38; (actual)

    // TODO: Add some fancy Exchange Rate API...

    public PaymentService(String name){
        this.serviceName = name;
    }

    public String getType() {
        return serviceName;
    }

    public boolean processPayment(
            Account aSource,
            Account aTarget,
            Double aAmount) {
        if (aSource.verifyPayment(aAmount)) {

            System.out.printf("Money In: %s%s%n",
                    aSource.getAccCurrency().getSymbol(),
                    aAmount);

            double exAmount = exchangeCurrency(aAmount,
                    aSource.getAccCurrency(), aTarget.getAccCurrency());

            System.out.printf("Money In: %s%s%n",
                    aTarget.getAccCurrency().getSymbol(),
                    exAmount);


            System.out.println("Source Gives: " + aAmount);
            System.out.println("Target Receives: " + exAmount);

            // Transaction...
            aSource.makePayment(aAmount);
            aTarget.takePayment(exAmount);

            // Profit...
            return true;
        }
        return false;
    }

    public boolean processPaymentTriangulation(
            Account aSource,
            Account aTarget,
            Double aAmount,
            String aCurrency) {

        double exAmount = PaymentService.exchangeCurrency(aAmount,
                Currency.getInstance(aCurrency),
                aSource.getAccCurrency());

        return processPayment(aSource, aTarget, exAmount);

    }

    public static double getRate(Currency aFrom, Currency aTo) {
        // Use EUR as a base rate by converting GBP and USD to EUR
        double baseRate = switch (aFrom.getCurrencyCode()) {
            case "GBP" -> 1 / EUR_TO_GBP;
            case "USD" -> 1 / EUR_TO_USD;
            default -> 1; // "EUR"
        };

        // Now convert the base rate into the target rate
        return switch (aTo.getCurrencyCode()) {
            case "GBP" -> baseRate * EUR_TO_GBP;
            case "USD" -> baseRate * EUR_TO_USD;
            default -> baseRate; // "EUR"
        };
    }

    public static double exchangeCurrency(Double aAmount, Currency aSource, Currency aTarget) {
        double exRate = getRate(aSource, aTarget);
        return aAmount * exRate;
    }

}
