import java.time.LocalDate;
import java.time.ZoneId;
/*
Contains data members related to the transaction history of a client.
 */
public class Transaction {
    private LocalDate timestamp;
    private int userID;
    private String log;

    public Transaction(int userID, String log){
        this.timestamp = BankController.getBankController().getSystemDate();
        this.userID = userID;
        this.log = log;
    }
}
