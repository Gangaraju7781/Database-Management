import java.time.LocalDate;
import java.time.ZoneId;

//This class is used to implement the Singleton design pattern for the ease of creating one
// instance of specific classes that can be used during the program.

public class BankController {
    private static BankController bankController;
    private LocalDate systemDate;
    private Client loggedInClient;
    private Banker loggedInBanker;
    private Bank bank;
    private Database bankDB;
    private StockMarket stockMarket;

    private BankController() {
        systemDate = LocalDate.now( ZoneId.of( "America/Montreal" ) ) ;
//        systemDate = LocalDate.parse("2022-05-20");
    }

    public LocalDate getSystemDate() {
        return systemDate;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Client getLoggedInClient() {
        return loggedInClient;
    }

    public void setLoggedInClient(Client loggedInClient) {
        this.loggedInClient = loggedInClient;
    }

    public Banker getLoggedInBanker() {
        return loggedInBanker;
    }

    public void setLoggedInBanker(Banker loggedInBanker) {
        this.loggedInBanker = loggedInBanker;
    }


    public Database getBankDB() {
        return bankDB;
    }

    public void setBankDB(Database bankDB) {
        this.bankDB = bankDB;
    }

    public StockMarket getStockMarket() {
        return stockMarket;
    }

    public void setStockMarket(StockMarket stockMarket) {
        this.stockMarket = stockMarket;
    }

    public static BankController getBankController() {
        if (bankController == null) {
            bankController = new BankController();
        }
        return bankController;
    }

    public void runBankController() {
        setBankDB(Database.getDB());
        setBank(Bank.getBank());
        setStockMarket(StockMarket.getStockMarket());

        LoginUI lg = new LoginUI();
    }
}
