/*
This class implements the Fiat class and is used to display the balances in Canadian dollars.
 */

public class CanadianDollar implements Fiat{

    private static CanadianDollar cad;

    @Override
    public double convertToUSD(double amount) {
        return amount * 0.78;
    }

    @Override
    public double convertFromUSD(double amount) {
        return amount * 1.28;
    }

    @Override
    public String getSymbol() {
        return "$";
    }

    public static CanadianDollar getCanadianDollar() {
        if (cad == null) {
            cad = new CanadianDollar();
        }
        return cad;
    }
}
