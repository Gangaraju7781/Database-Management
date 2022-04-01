import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/*
This is the banker class which extends the User class and contains the data members and methods
for the banker manager.
 */

public class Banker extends User{

    Banker(String username, String password, LocalDate lastLoggedInDate, int id){
        super(username, password, lastLoggedInDate,id);
    }

    public void setOpenCloseFee(double openCloseFee) {
        Bank.getBank().setOpenCloseFee(openCloseFee);
    }

    public void setTxFee(double txFee) {
        Bank.getBank().setTxFee(txFee);
    }

    public void setTransferFee(double transferFee) {
        Bank.getBank().setTransferFee(transferFee);
    }

    public void setWithdrawalFee(double withdrawalFee) {
        Bank.getBank().setWithdrawalFee(withdrawalFee);
    }

    public void setSavingsInterestRate(double savingsInterestRate) {
        Bank.getBank().setSavingsInterestRate(savingsInterestRate);
    }

    public void setLoanInterestRate(double loanInterestRate) {
        Bank.getBank().setLoanInterestRate(loanInterestRate);
    }

    public void setMinInitialSecuritiesTransfer(double minInitialSecuritiesTransfer) {
        Bank.getBank().setMinInitialSecuritiesTransfer(minInitialSecuritiesTransfer);
    }

    public void setMinSavingsBalanceWithSecurities(double minSavingsBalanceWithSecurities) {
        Bank.getBank().setMinSavingsBalanceWithSecurities(minSavingsBalanceWithSecurities);
    }

    public void setOpenSecuritiesThreshold(double openSecuritiesThreshold) {
        Bank.getBank().setOpenSecuritiesThreshold(openSecuritiesThreshold);
    }

    private void checkOnCustomer() {

    }

    private void checkOnCustomers() {

    }

    private void getDailyReport() {

    }
}
