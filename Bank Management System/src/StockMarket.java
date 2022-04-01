import javax.swing.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.*;
import java.util.List;
/*
Contains the stock market data members and methods.
 */
public class StockMarket {

    private static StockMarket stockMarket;
    private HashMap<Stock, StockPrice> stockPrices;
    private boolean isOpen;

    private StockMarket() {
        this.stockPrices = new HashMap<Stock, StockPrice>();

        Database db = BankController.getBankController().getBankDB();
        ResultSet rs;
        try{

            Statement stmt = db.getCon().createStatement();
            String sql  ="select * from stock as st, stockPrice as sp where st.ticker = sp.ticker and sp.stockPriceDate = '"+BankController.getBankController().getSystemDate().toString()+"'";

            rs = stmt.executeQuery(sql);
            while(rs.next()){
                Stock stock = new Stock(rs.getString("ticker"), rs.getString("company_name"));
                StockPrice stockPrice = new StockPrice(stock, Double.parseDouble(rs.getString("price")));
                addNewStock(stockPrice);
            }

        }catch (Exception except){
            System.out.println(except);
        }

        if (stockPrices.size() == 0)
            isOpen = false;
        else
            isOpen = true;
    }

    public HashMap<Stock, StockPrice> getStockPrices() {
        return stockPrices;
    }

    public void addNewStock(StockPrice stockPrice) {
        if (stockPrices.containsKey(stockPrice.getStock())) {
            // already exists
        } else
            stockPrices.put(stockPrice.getStock(), stockPrice);
    }

    public void updatePrice(Stock stock, double price) {

        Database db = BankController.getBankController().getBankDB();

        if (stockPrices.containsKey(stock)){

            StockPrice stockPrice = new StockPrice(stock, price);
            //stockPrices.replace(stock, new StockPrice(stock, price));
            stockPrices.replace(stock, stockPrice);
        //update price

            db.updateStockPrice(stockPrice.getStock().getTicker(), price);

            }

        else {
            StockPrice stockPrice = new StockPrice(stock, price);
            stockPrices.put(stock, stockPrice);
            //insert new price

            db.insertIntoStockPrice(stockPrice);


        }

        if (stockPrices.size() == BankController.getBankController().getBankDB().getStocksFromDB().size())
            isOpen = true;
    }

    public List<String> getTickersNotYetUpdated() {
        List<String> tickers = new ArrayList<>();

        for (Stock stock : BankController.getBankController().getBankDB().getStocksFromDB()) {
            if (!stockPrices.containsKey(stock))
                tickers.add(stock.getTicker());
        }

        return tickers;
    }

    public boolean removeStock(Stock stock) {
        if (stockPrices.containsKey(stock)) {
            stockPrices.remove(stock);
            return true;
        } else
            return false;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public StockPrice getCurrentStockPrice(Stock stock) {
        if (stockPrices.containsKey(stock))
            return stockPrices.get(stock);
        else
            return null;
    }

    public static StockMarket getStockMarket() {
        if (stockMarket == null) {
            stockMarket = new StockMarket();
        }
        return stockMarket;
    }
}
