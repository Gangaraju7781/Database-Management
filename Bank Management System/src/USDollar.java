/*
This class implements the Fiat class and is used to display the balances in US dollars.
 */

public class USDollar implements Fiat{

    private static USDollar usd;

    @Override
    public double convertToUSD(double amount) {
        return amount;
    }

    @Override
    public double convertFromUSD(double amount) {
        return amount;
    }

    @Override
    public String getSymbol() {
        return "$";
    }

    public static USDollar getUSD() {
        if (usd == null) {
            usd = new USDollar();
        }
        return usd;
    }
}
