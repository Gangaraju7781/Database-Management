import java.time.LocalDate;
import java.time.ZoneId;
/*
Contains data members related to the stock prices in the market.
 */
public class StockPrice {
    private Stock stock;
    private double price;
    private LocalDate date;

    public StockPrice(Stock stock, double price) {
        this.stock = stock;
        this.price = price;
        this.date = BankController.getBankController().getSystemDate();
    }

    public Stock getStock() {
        return stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }
}
