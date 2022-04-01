import javax.swing.*;

/*
This class is the subclass of the Account class and contains all the methods related to
withdrawing and depositing money from a clients checking account.
 */

public class CheckingAccount extends Account implements Withdrawable, Depositable{
    public CheckingAccount(double initialDeposit, Fiat fiat, int id) {
        super(initialDeposit, fiat, id);
        super.setType("Checking");
    }

    public CheckingAccount(double balance, int id){
        this(balance,USDollar.getUSD(), id);
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public void closeAccount() {
        super.setOpen(false);
        super.setBalance(0);
    }

    @Override
    public boolean payFee(double fee) {
        if (super.getBalance() >= fee) {
            super.setBalance(super.getBalance() - fee);
            Bank.getBank().setCashReserves(Bank.getBank().getCashReserves() + fee);
            return true;
        } else
            return false;
    }

    @Override
    public boolean canWithdraw(double amount, Fiat fiat) {
        return super.getBalance() >= amount + Bank.getBank().getWithdrawalFee();
    }

    @Override
    public boolean withdrawMoney(double amount, Fiat fiat) {
        if (canWithdraw(amount, fiat)) {
            super.setBalance(super.getBalance() - fiat.convertToUSD(amount));
            return true;
        } else{
            JOptionPane.showMessageDialog(null,"can't withdraw more than current balance");
            return false;
        }

    }

    @Override
    public void depositMoney(double amount, Fiat fiat) {
        super.setBalance(super.getBalance() + fiat.convertToUSD(amount));
    }
}
