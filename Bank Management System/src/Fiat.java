/*
This interface is implemented by the USDollar, CanadianDollar and Euro classes
 */

public interface Fiat {

    public double convertToUSD(double amount);

    public double convertFromUSD(double amount);

    public String getSymbol();
}
