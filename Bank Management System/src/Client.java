import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/*
this class extends the User class and contains the members and instantiates the client object.
 */
public class Client extends User{

    private double totalBalance;
    private double outstandingDebt;
    private Account checkingAccount;
    private Account savingsAccount;
    private Account securitiesAccount;
    private HashMap<Integer, Loan> loans;
    private List<Transaction> txHistory;

    public Client(String username, String password, LocalDate lastLoggedInDate, int id){
        super(username, password, lastLoggedInDate, id);
        checkingAccount = new NullAccount();
        savingsAccount = new NullAccount();
        securitiesAccount = new NullAccount();
        loans = new HashMap<Integer, Loan>();
        txHistory = new ArrayList<Transaction>();
        totalBalance = 0;
        outstandingDebt = 0;
    }

    public Client(String username, String password, LocalDate lastLoggedInDate, int id, Account checkingAccount, Account savingsAccount,
                  Account securitiesAccount, HashMap<Integer, Loan> loans, ArrayList<Transaction> txHistory){

        super(username, password, lastLoggedInDate, id);
        this.checkingAccount = checkingAccount;
        this.savingsAccount = savingsAccount;
        this.securitiesAccount = securitiesAccount;
        this.loans = loans;
        this.txHistory = txHistory;

    }

    public boolean canPayFee(double fee) {
        return checkingAccount.getBalance() >= fee;
    }

    public boolean payFee(double fee) {
        if (checkingAccount.payFee(fee)) {
            Bank bank = BankController.getBankController().getBank();
            totalBalance -= fee;
            BankController.getBankController().getBankDB().updateAccount(checkingAccount);
            bank.setCashReserves(bank.getCashReserves() + fee);
            BankController.getBankController().getBankDB().updateCashReserves();
            return true;
        } else
            return false;
    }

    public void openCheckingAccount(double initialDeposit, Fiat fiat, int id){

        if (initialDeposit >= Bank.getBank().getOpenCloseFee()) {
            checkingAccount = new CheckingAccount(initialDeposit, fiat, id);
            payFee(Bank.getBank().getOpenCloseFee());
            totalBalance += fiat.convertToUSD(initialDeposit) - Bank.getBank().getOpenCloseFee();
            Utils.insertIntoTransactionTable(super.getId(), "Opened a checking account with a deposit of " +
                    fiat.getSymbol() + initialDeposit);
        } else {
            // initial deposit needs to be bigger
        }
    }

    public void openSavingsAccount(double initialDeposit, Fiat fiat, int id){

        if (payFee(Bank.getBank().getOpenCloseFee())) {
            savingsAccount = new SavingsAccount(initialDeposit, fiat, id);
            totalBalance += fiat.convertToUSD(initialDeposit);
            Utils.insertIntoTransactionTable(super.getId(), "Opened a savings account with a deposit of " +
                    fiat.getSymbol() + initialDeposit);
        } else {
            // checking balance needs to be bigger, cant cover fees. must deposit more
        }
    }

    public void openSecuritiesAccount(double initialTransfer, Fiat fiat, int id){

        if (savingsAccount.getBalance() >= Bank.getBank().getOpenSecuritiesThreshold() &&
                fiat.convertToUSD(initialTransfer) >= Bank.getBank().getMinInitialSecuritiesTransfer() &&
                savingsAccount.getBalance() - fiat.convertToUSD(initialTransfer) >= Bank.getBank().getMinSavingsBalanceWithSecurities()) {
            if (payFee(Bank.getBank().getOpenCloseFee())) {
                securitiesAccount = new SecuritiesAccount(id, 0);
                transferBetweenAccounts(savingsAccount, securitiesAccount, fiat.convertToUSD(initialTransfer));
                Utils.insertIntoTransactionTable(super.getId(), "Opened a securities account with a transfer of " +
                        fiat.getSymbol() + initialTransfer + " from savings account");
            } else {
                // checking balance needs to be bigger, cant cover fees. must deposit more
            }
        } else {
            // does not have one - have savings with 5k, initial transfer over 1k, and balance in savings must be 2500
        }
    }

    public void depositMoney(Depositable account, double amount, Fiat fiat) {
        account.depositMoney(amount, fiat);
        if (account instanceof CheckingAccount) {
            Utils.insertIntoTransactionTable(super.getId(), "Deposited " +
                    fiat.getSymbol() + amount + " into Checking account");
        }
        else
            Utils.insertIntoTransactionTable(super.getId(), "Deposited " +
                    fiat.getSymbol() + amount + " into Savings account");
    }

    public boolean withdrawMoney(Withdrawable account, double amount, Fiat fiat) {
        if (!securitiesAccount.isNull() && savingsAccount.getBalance() - fiat.convertToUSD(amount) <
                BankController.getBankController().getBank().getMinSavingsBalanceWithSecurities()) {
            if (canPayFee(Bank.getBank().getWithdrawalFee())) {
                if (account.withdrawMoney(amount, fiat)) {
                    payFee(Bank.getBank().getWithdrawalFee());
                    if (account instanceof CheckingAccount)
                        Utils.insertIntoTransactionTable(super.getId(), "Withdrew " +
                                fiat.getSymbol() + amount + " from Checking account");
                    else
                        Utils.insertIntoTransactionTable(super.getId(), "Withdrew " +
                                fiat.getSymbol() + amount + " from Savings account");
                    return true;
                } else {
                    // cant withdraw this amount, too much
                    JOptionPane.showMessageDialog(null, "Can't withdraw more than your current balance");
                    return false;
                }
            } else {
                //cant cover withdrawal fee
                JOptionPane.showMessageDialog(null, "Not enough money in Checking account " +
                        "to cover the withdrawal fee");
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "Can't withdraw because a Securities account " +
                    "is open and the Savings account must maintain at least $2500");
            return false;
        }
    }

    public void closeCheckingAccount() {

        if (!checkingAccount.isNull()) {
            // move forward with clos
            if (canPayFee(Bank.getBank().getOpenCloseFee())) {
                payFee(Bank.getBank().getOpenCloseFee());
                totalBalance = 0;
                Utils.insertIntoTransactionTable(super.getId(), "Closed checking account and cashed out $" +
                        checkingAccount.getBalance());
                checkingAccount.closeAccount();
                Database db = BankController.getBankController().getBankDB();
                db.updateAccount(checkingAccount);
                checkingAccount = new NullAccount();
            } else {
                // not enough money to close account
                JOptionPane.showMessageDialog(null, "Not enough money in Checking account " +
                        "to cover the close fee");
            }
        }
    }

    public void closeSavingsAccount() {

        if (!savingsAccount.isNull()) {
            if (canPayFee(Bank.getBank().getOpenCloseFee())) {
                payFee(Bank.getBank().getOpenCloseFee());
                checkingAccount.setBalance(checkingAccount.getBalance() + savingsAccount.getBalance());
                Utils.insertIntoTransactionTable(super.getId(), "Closed savings account and transferred $" +
                        savingsAccount.getBalance() + " to checking account");
                savingsAccount.closeAccount();
                Database db = BankController.getBankController().getBankDB();
                db.updateAccount(checkingAccount);
                db.updateAccount(savingsAccount);
                savingsAccount = new NullAccount();
            } else {
                // not enough money to close account
                JOptionPane.showMessageDialog(null, "Not enough money in Checking account " +
                        "to cover the close fee");
            }
        }
    }

    public boolean closeSecuritiesAccount() {
        if (canPayFee(Bank.getBank().getOpenCloseFee())) {
            payFee(Bank.getBank().getOpenCloseFee());
            securitiesAccount.closeAccount();
            Utils.insertIntoTransactionTable(super.getId(), "Closed all open positions and securities account and " +
                    "transferred $" + securitiesAccount.getBalance() + " to savings account");
            savingsAccount.setBalance(savingsAccount.getBalance() + securitiesAccount.getBalance());
            securitiesAccount.setBalance(0);
            Database db = BankController.getBankController().getBankDB();
            db.updateAccount(savingsAccount);
            db.updateAccount(securitiesAccount);
            securitiesAccount = new NullAccount();
            return true;
        } else {
            // not enough money to close account
            JOptionPane.showMessageDialog(null, "Not enough money in Checking account " +
                    "to cover the close fee");
            return false;
        }
    }

    public void transferBetweenAccounts(Account a1, Account a2, double amount) {
        if (canPayFee(Bank.getBank().getTransferFee())) {
            if (a1 instanceof CheckingAccount && a1.getBalance() < amount + Bank.getBank().getTransferFee()) {
                // cant withdraw this amount and pay transfer fee, must lower transfer amount
                JOptionPane.showMessageDialog(null, "Can't withdraw this amount and pay transfer " +
                        "fee, must lower transfer amount");
            } else if (a1 instanceof SavingsAccount && !securitiesAccount.isNull() &&
                    savingsAccount.getBalance() - amount < Bank.getBank().getMinSavingsBalanceWithSecurities()) {
                // cant transfer because securities account open and savings must maintain more than 2500
                JOptionPane.showMessageDialog(null, "Can't transfer because a Securities account " +
                        "is open and the Savings account must maintain at least $2500");
            }
            else if (a1.getBalance() < amount) {
                // Do not have enough available balance to transfer requested amount
                JOptionPane.showMessageDialog(null, "Do not have enough available balance to " +
                        "transfer requested amount");
            } else {
                payFee(Bank.getBank().getTransferFee());
                a1.setBalance(a1.getBalance() - amount);
                a2.setBalance(a2.getBalance() + amount);
                Utils.insertIntoTransactionTable(super.getId(), a1.getType() + " account transferred $" +
                        amount + " to " + a2.getType() + " account");
            }
        }
    }

    public void receiveInterest() {
        if (!savingsAccount.isNull() && savingsAccount.getBalance() >= BankController.getBankController().getBank().getMinSavingsBalanceToEarnInterest()) {

            double balanceWithInterest = Utils.roundDouble(savingsAccount.getBalance() * Math.pow((1 + BankController.getBankController().getBank().getSavingsInterestRate()),
                    Utils.getDateDifference(getLastLoggedInDate())));
            double interest = balanceWithInterest - savingsAccount.getBalance();
            savingsAccount.setBalance(balanceWithInterest);
            BankController.getBankController().getBankDB().updateAccount(savingsAccount);
            BankController.getBankController().getBank().setCashReserves(BankController.getBankController().getBank().getCashReserves() - interest);
            Utils.insertIntoTransactionTable(super.getId(), "Earned $" + interest + " of interest from savings account" +
                    " for " + Utils.getDateDifference(getLastLoggedInDate()) + " months");
        }
    }

    public void updateOutstandingDebt() {
        outstandingDebt = 0;
        for (Loan loan : loans.values())
            outstandingDebt += loan.getPrincipalBalance();
    }

    public boolean hasCollateral(double amount) {
        // need outstanding debt, can't double dip collateral
        updateOutstandingDebt();
        if (savingsAccount.getBalance() - outstandingDebt >= amount)
            return true;
        else
            return false;
    }

    public void requestLoan(double amount, int term, int id){
        if (canPayFee(Bank.getBank().getTxFee())) {
            if (hasCollateral(amount)) {
                payFee(Bank.getBank().getTxFee());
                loans.put(id, new Loan(amount, term, BankController.getBankController().getSystemDate(), id));
                outstandingDebt += amount;
                Utils.insertIntoTransactionTable(super.getId(), "Requested a loan of $" + amount);
            } else {
                // not enough collateral
                JOptionPane.showMessageDialog(null, "Not enough available Savings balance to " +
                        "serve as the collateral for this loan");
            }
        } else {
            // cant pay tx fee to request loan
            JOptionPane.showMessageDialog(null, "Can't pay the transaction fee to request this" +
                    " loan");
        }
    }

    public void payInterest() {
        for (Loan loan : loans.values()) {
            double interestDue = Utils.roundDouble(loan.getInterestDue(getLastLoggedInDate()));
            if (canPayFee(interestDue + Bank.getBank().getTxFee())) {
                payFee(interestDue + Bank.getBank().getTxFee());

                Utils.insertIntoTransactionTable(super.getId(), "Paid loan interest of $" +
                        interestDue + " for loan " + loan.getId());
                if (loan.getPeriodsRemaining() == 0) {
                    loans.remove(loan.getId());
                    BankController.getBankController().getBankDB().closeLoan(loan);
                }
            }
        }
        updateOutstandingDebt();
    }

    public boolean payOffLoan(int id) {

        Loan loan = loans.get(id);

        if (canPayFee(loan.getPrincipalBalance() + Bank.getBank().getTxFee())) {
            payFee(loan.getPrincipalBalance() + Bank.getBank().getTxFee());
            Bank.getBank().setCashReserves(Bank.getBank().getCashReserves()-loan.getPrincipalBalance());
            loans.remove(id);
            BankController.getBankController().getBankDB().closeLoan(loan);
            updateOutstandingDebt();
            Utils.insertIntoTransactionTable(super.getId(), "Paid off loan balance of $" +
                    loan.getPrincipalBalance());
            return true;
        } else {
            // not enough in checking to pay off rest of loan
            JOptionPane.showMessageDialog(null, "Not enough in your Checking account to pay " +
                    "off the rest of this loan's principal balance with tx fee");
            return false;
        }
    }

    public void buyStock(StockPrice stockPrice, int quantity) {
        if (canPayFee(Bank.getBank().getTxFee())) {
            payFee(BankController.getBankController().getBank().getTxFee());
            if (((SecuritiesAccount) securitiesAccount).buyStock(stockPrice, quantity)) {
                BankController.getBankController().getBankDB().updateAccount(securitiesAccount);
                Utils.insertIntoTransactionTable(super.getId(), "Bought " + quantity + " shares of " + stockPrice.getStock().getTicker() +
                        " at $" + stockPrice.getPrice() + "per share");
            } else {
                JOptionPane.showMessageDialog(null, "Can't buy " + quantity + " shares of " +
                        stockPrice.getStock().getTicker() + ", you don't have enough money in your Securities account");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Can't pay the transaction fee for buying stock");
        }
    }

    public void sellStock(StockPrice stockPrice, int quantity) {
        if (canPayFee(Bank.getBank().getTxFee())) {
            payFee(BankController.getBankController().getBank().getTxFee());
            if (((SecuritiesAccount) securitiesAccount).getPositions().containsKey(stockPrice.getStock())) {
                if (((SecuritiesAccount) securitiesAccount).sellStock(stockPrice, quantity)) {
                    BankController.getBankController().getBankDB().updateAccount(securitiesAccount);
                    Utils.insertIntoTransactionTable(super.getId(), "Sold " + quantity + " shares of " + stockPrice.getStock().getTicker() +
                            " at $" + stockPrice.getPrice() + "per share");
                } else {
                    JOptionPane.showMessageDialog(null, "Can't sell " + quantity + " shares of " +
                            stockPrice.getStock().getTicker() + ", you don't have enough shares of this stock in your " +
                            "Securities account");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Can't sell " + quantity + " shares of " +
                        stockPrice.getStock().getTicker() + ", you don't have any shares of this stock in your " +
                        "Securities account");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Can't pay the transaction fee for selling stock");
        }
    }

    public Account getCheckingAccount() {
        return checkingAccount;
    }

    public Account getSavingsAccount() {
        return savingsAccount;
    }

    public Account getSecuritiesAccount() {
        return securitiesAccount;
    }

    public HashMap<Integer, Loan> getLoans() {
        return loans;
    }

    public List<Transaction> getTxHistory() {
        return txHistory;
    }

    public double getOutstandingDebt() {
        return outstandingDebt;
    }

    public double getTotalBalance() {
        return totalBalance;
    }
}
