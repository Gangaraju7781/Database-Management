import javax.swing.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashMap;
import javax.sql.*;
/*
this is the SecuritiesAccount class which contains the members and methods for trading stocks.
 */
public class SecuritiesAccount extends Account{

    private HashMap<Stock, Position> positions;
    private double realizedGains;

    public SecuritiesAccount(int id, double realizedGains) {
        super(id);
        super.setType("Securities");
        positions = new HashMap<Stock, Position>();

        //assign positions from db value

        Database db = BankController.getBankController().getBankDB();
        ResultSet rs;
        try{
            Statement stmt = db.getCon().createStatement();
            String sql  ="select * from positions as p, stock as s where p.ticker = s.ticker and accountid = '"+id+"' and isOpen = 'true'";

            rs =stmt.executeQuery(sql);

            while(rs.next()){
                Stock stock = new Stock(rs.getString("ticker"), rs.getString("company_name"));
                Position position = new Position(stock, Double.parseDouble(rs.getString("averageCost")),
                        Integer.parseInt(rs.getString("quantity")), LocalDate.parse(rs.getString("openDate")));
                positions.put(stock,position);
            }


        }catch (Exception except){
            System.out.println(except);
        }

        setRealizedGains(realizedGains);
    }

    public SecuritiesAccount(double balance, int id, double realizedGains) {
        this(id, realizedGains);
        super.setBalance(balance);
    }

    public HashMap<Stock, Position> getPositions() {
        return positions;
    }

    @Override
    public void closeAccount() {
        Database db = BankController.getBankController().getBankDB();
        for (Position position : positions.values()) {
            StockPrice stockPrice = StockMarket.getStockMarket().getCurrentStockPrice(position.getStock());
            setRealizedGains(getRealizedGains() + getTradeRealizedGains(position, stockPrice, position.getQuantity()));
            // db update account with new realized gains
            position.takeFromPosition(position.getQuantity());
            super.setBalance(super.getBalance() + stockPrice.getPrice() * position.getQuantity());
            position.setOpen(false);
            // update position in db
            db.updatePositions(position.getStock().getTicker(), super.getId(), false, position);
        }
        positions = new HashMap<Stock, Position>();
        super.setOpen(false);
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean payFee(double fee) {
        return false;
    }

    public boolean buyStock(StockPrice stockPrice, int quantity) {

        Database db = BankController.getBankController().getBankDB();

        if (super.getBalance() >= stockPrice.getPrice() * quantity) {
            if (positions.containsKey(stockPrice.getStock())) {
                Position currentPosition = positions.get(stockPrice.getStock());
                currentPosition.addToPosition(stockPrice.getPrice(), quantity);
                // update position to db
                db.updatePositions(currentPosition.getStock().getTicker(), super.getId(), true, currentPosition);
            } else {
                Position currentPosition = new Position(stockPrice.getStock(), stockPrice.getPrice(), quantity,
                        BankController.getBankController().getSystemDate());
                positions.put(stockPrice.getStock(), currentPosition);
                // insert position to db
                db.insertPositions(super.getId(), currentPosition);
            }
            super.setBalance(super.getBalance() - stockPrice.getPrice() * quantity);
            return true;
        } else {
            // not enough balance to buy this amount of stock
            return false;
        }
    }

    public boolean sellStock(StockPrice stockPrice, int quantity) {
        Database db = BankController.getBankController().getBankDB();

        Position currentPosition = positions.get(stockPrice.getStock());
        if (currentPosition.getQuantity() >= quantity) {
            setRealizedGains(getRealizedGains() + getTradeRealizedGains(currentPosition, stockPrice, quantity));
            // db update account with new realized gains
            currentPosition.takeFromPosition(quantity);
            db.updatePositions(currentPosition.getStock().getTicker(), super.getId(), true, currentPosition);
            super.setBalance(super.getBalance() + stockPrice.getPrice() * quantity);
            // close position if no more shares left for this stock
            if (!currentPosition.isOpen()) {
                positions.remove(stockPrice.getStock());
                currentPosition.setOpen(false);
                // update position in db
                db.updatePositions(currentPosition.getStock().getTicker(), super.getId(), false, currentPosition);
            }
            return true;
        } else {
            // not enough quantity to sell this amount of stock
            return false;
        }
    }

    public double getTradeRealizedGains(Position position, StockPrice stockPrice, int quantity) {
        return (stockPrice.getPrice() - position.getAvgCost()) * quantity;
    }

    public double getRealizedGains() {
        return realizedGains;
    }

    public void setRealizedGains(double realizedGains) {
        this.realizedGains = realizedGains;
    }

    public void refreshPositions() {

        for (Position position : positions.values())
            position.refreshPosition();
    }

    public double getUnrealizedGains() {
        double totalUnrealizedGains = 0;
        for (Position position : positions.values())
            totalUnrealizedGains += position.getUnRealizedGains();
        return totalUnrealizedGains;
    }
}
