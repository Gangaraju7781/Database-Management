/*
This interface is implemented by the CheckingAccount and SavingsAccount classes and contains the method in
which the client can deposit money to the bank.
 */
public interface Depositable {

    public void depositMoney(double amount, Fiat fiat);
}
