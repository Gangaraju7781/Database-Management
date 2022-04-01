import java.sql.ResultSet;
import java.sql.Statement;
//This is an abstract class which is the super class of the checking, savings and securities account classes.
public abstract class Account {

    private int id;
    private double balance;
    private boolean isOpen;
    private String type;

    public Account(double initialDeposit, Fiat fiat, int id){


        this.id = id;
        initialDeposit = fiat.convertToUSD(initialDeposit);
        balance = initialDeposit;
        isOpen = true;
        type = "";
    }

    public Account(int id) {
        this(0, USDollar.getUSD(), id);
    }

    public abstract void closeAccount();

    public abstract boolean isNull();

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public abstract boolean payFee(double fee);

    public double getBalance() {
        return balance;
    }

    public int getId() {
        return id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }
}
