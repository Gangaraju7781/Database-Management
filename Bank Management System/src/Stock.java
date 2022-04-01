import java.util.Objects;
/*
contains the stock data members
 */
public class Stock {

    private String ticker;
    private String companyName;

    public Stock(String ticker, String companyName) {
        this.ticker = ticker;
        this.companyName = companyName;
    }

    public String getTicker() {
        return ticker;
    }

    public String getCompanyName() {
        return companyName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        else {
            Stock stock = (Stock) o;
            return stock.ticker.equals(this.ticker);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker);
    }
}
