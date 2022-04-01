import javax.swing.plaf.nimbus.State;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

//This class contains the specific attributes which the bank manager can update

public class Bank {
    private static Bank bank;
    private double cashReserves;
    private double openCloseFee;
    private double txFee;
    private double transferFee;
    private double withdrawalFee;
    private double savingsInterestRate;
    private double loanInterestRate;
    private double minCheckingSavingsInitialDeposit;
    private double minSavingsBalanceToEarnInterest;
    private double minInitialSecuritiesTransfer;
    private double minSavingsBalanceWithSecurities;
    private double openSecuritiesThreshold;
    public static CheckingAccountFactory checkingAccountFactory;
    public static SavingsAccountFactory savingsAccountFactory;

    private Bank() {

        //retrieve from database each time a new Bank object is created

        Database db = BankController.getBankController().getBankDB();
        ResultSet rs;
        try{

            Statement stmt = db.getCon().createStatement();
            String sql  ="select * from bank";

            rs =stmt.executeQuery(sql);
            rs.next();

            cashReserves = Double.parseDouble(rs.getString("cashReserves"));
            txFee = Double.parseDouble(rs.getString("txFee"));
            transferFee = Double.parseDouble(rs.getString("transferFee"));
            withdrawalFee = Double.parseDouble(rs.getString("withdrawalFee"));
            savingsInterestRate = Double.parseDouble(rs.getString("savingsInterestRate"));
            loanInterestRate = Double.parseDouble(rs.getString("loanInterestRate"));
            minCheckingSavingsInitialDeposit = Double.parseDouble(rs.getString("minCheckingSavingsInitialDeposit"));
            minSavingsBalanceToEarnInterest = Double.parseDouble(rs.getString("minSavingsBalanceToEarnInterest"));;
            minInitialSecuritiesTransfer = Double.parseDouble(rs.getString("minInitialSecuritiesTransfer"));
            minSavingsBalanceWithSecurities = Double.parseDouble(rs.getString("minSavingsBalanceWithSecurities"));
            openSecuritiesThreshold = Double.parseDouble(rs.getString("openSecuritiesThreshold"));



        }catch (Exception except){
            System.out.println(except);
        }
        checkingAccountFactory = new CheckingAccountFactory();
        savingsAccountFactory = new SavingsAccountFactory();
    }

    public double getCashReserves() {
        return cashReserves;
    }

    public void setCashReserves(double cashReserves) {
        this.cashReserves = cashReserves;
    }

    public double getOpenCloseFee() {
        return openCloseFee;
    }

    public void setOpenCloseFee(double openCloseFee) {
        this.openCloseFee = openCloseFee;
    }

    public double getTxFee() {
        return txFee;
    }

    public void setTxFee(double txFee) {
        this.txFee = txFee;
    }

    public double getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(double transferFee) {
        this.transferFee = transferFee;
    }

    public double getWithdrawalFee() {
        return withdrawalFee;
    }

    public void setWithdrawalFee(double withdrawalFee) {
        this.withdrawalFee = withdrawalFee;
    }

    public double getSavingsInterestRate() {
        return savingsInterestRate;
    }

    public void setSavingsInterestRate(double savingsInterestRate) {
        this.savingsInterestRate = savingsInterestRate;
    }

    public double getLoanInterestRate() {
        return loanInterestRate;
    }

    public void setLoanInterestRate(double loanInterestRate) {
        this.loanInterestRate = loanInterestRate;
    }

    public double getMinCheckingSavingsInitialDeposit() {
        return minCheckingSavingsInitialDeposit;
    }

    public void setMinCheckingSavingsInitialDeposit(double minCheckingSavingsInitialDeposit) {
        this.minCheckingSavingsInitialDeposit = minCheckingSavingsInitialDeposit;
    }

    public double getMinSavingsBalanceToEarnInterest() {
        return minSavingsBalanceToEarnInterest;
    }

    public void setMinSavingsBalanceToEarnInterest(double minSavingsBalanceToEarnInterest) {
        this.minSavingsBalanceToEarnInterest = minSavingsBalanceToEarnInterest;
    }

    public double getMinInitialSecuritiesTransfer() {
        return minInitialSecuritiesTransfer;
    }

    public void setMinInitialSecuritiesTransfer(double minInitialSecuritiesTransfer) {
        this.minInitialSecuritiesTransfer = minInitialSecuritiesTransfer;
    }

    public double getMinSavingsBalanceWithSecurities() {
        return minSavingsBalanceWithSecurities;
    }

    public void setMinSavingsBalanceWithSecurities(double minSavingsBalanceWithSecurities) {
        this.minSavingsBalanceWithSecurities = minSavingsBalanceWithSecurities;
    }

    public double getOpenSecuritiesThreshold() {
        return openSecuritiesThreshold;
    }

    public void setOpenSecuritiesThreshold(double openSecuritiesThreshold) {
        this.openSecuritiesThreshold = openSecuritiesThreshold;
    }

    public static CheckingAccountFactory getCheckingAccountFactory() {
        return checkingAccountFactory;
    }

    public static SavingsAccountFactory getSavingsAccountFactory() {
        return savingsAccountFactory;
    }

    public static Bank getBank() {
        if (bank == null) {
            bank = new Bank();
        }
        return bank;
    }
}
