/*
This class is the subclass of the Account class and contains all the methods related to
withdrawing and depositing money from a clients savings account.
 */

public class SavingsAccount extends Account implements Withdrawable, Depositable{

    public SavingsAccount(double initialDeposit, Fiat fiat, int id) {
        super(initialDeposit, fiat, id);
        super.setType("Savings");
    }

    public SavingsAccount(double balance, int id){ this(balance,USDollar.getUSD(), id);
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
        return false;
    }

    @Override
    public boolean canWithdraw(double amount, Fiat fiat) {
        return super.getBalance() >= amount;
    }

    @Override
    public boolean withdrawMoney(double amount, Fiat fiat) {
        if (canWithdraw(amount, fiat)) {
            super.setBalance(super.getBalance() - fiat.convertToUSD(amount));
            return true;
        } else
            return false;
    }

    @Override
    public void depositMoney(double amount, Fiat fiat) {
        super.setBalance(super.getBalance() + fiat.convertToUSD(amount));
    }

}
