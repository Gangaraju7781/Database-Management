/*
This interface is implemented by the CheckingAccount and SavingsAccount classes and contains the
method in which the client can withdraw money from the bank.
 */

public interface Withdrawable {

    public boolean canWithdraw(double amount, Fiat fiat);

    public boolean withdrawMoney(double amount, Fiat fiat);
}
