/*
used to implement the factory design pattern for the SavingsAccount class.
 */

public class SavingsAccountFactory implements AccountFactoryCreator{
    @Override
    public Account createAccount(double initialDeposit, Fiat fiat, int id) {
        if (initialDeposit < Bank.getBank().getOpenCloseFee())
            return null;
        else
            return new SavingsAccount(initialDeposit, fiat, id);
    }
}
