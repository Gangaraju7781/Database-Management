/*
This class implements the Fiat class and is used to display the balances in Euro.
 */

public class Euro implements Fiat{

    private static Euro euro;

    @Override
    public double convertToUSD(double amount) {
        return amount * 1.13;
    }

    @Override
    public double convertFromUSD(double amount) {
        return amount * 0.88;
    }

    @Override
    public String getSymbol() {
        return "€";
    }

    public static Euro getEuro() {
        if (euro == null) {
            euro = new Euro();
        }
        return euro;
    }
}
