import java.time.LocalDate;
import java.time.ZoneId;
/*
contains all methods and members related to the position of a stock.
 */
public class Position {
    private boolean isOpen;
    private double unRealizedGains;
    private int quantity;
    private Stock stock;
    private double avgCost;
    private LocalDate openDate;

    public Position(Stock stock, double cost, int quantity, LocalDate date) {
        isOpen = true;
        openDate = date;
        this.stock = stock;
        this.avgCost = cost;
        this.quantity = quantity;
        refreshPosition();
    }

    public double getUnRealizedGains() {
        return unRealizedGains;
    }

    public int getQuantity() {
        return quantity;
    }

    public Stock getStock() {
        return stock;
    }

    public double getAvgCost() {
        return avgCost;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public double getAveragePrice(double newPrice, int newQuantity) {
        double totalQuantity = this.quantity + newQuantity;
        return (this.quantity / totalQuantity) * this.avgCost + (newQuantity / totalQuantity) * newPrice;
    }

    public void addToPosition(double newPrice, int newQuantity) {

        // avg cost with most recent purchase as date for stock price
        this.avgCost = getAveragePrice(newPrice, newQuantity);
        this.quantity += newQuantity;
        refreshPosition();
    }

    public void takeFromPosition(int soldQuantity) {
        this.quantity -= soldQuantity;
        refreshPosition();
        if (this.quantity == 0)
            isOpen = false;
    }

    public void refreshPosition() {
        StockPrice todayPrice = StockMarket.getStockMarket().getCurrentStockPrice(stock);
        if (todayPrice != null) {
            double currentValue = todayPrice.getPrice() * quantity;
            double originalValue = avgCost * quantity;
            unRealizedGains = currentValue - originalValue;
        } else {
            unRealizedGains = 0;
        }
    }
}
