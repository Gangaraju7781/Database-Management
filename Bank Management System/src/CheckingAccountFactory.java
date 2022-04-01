/*
used to implement the factory design pattern for the CheckingAccount class.
 */

public class CheckingAccountFactory implements AccountFactoryCreator{
    @Override
    public Account createAccount(double initialDeposit, Fiat fiat, int id) {
        if (initialDeposit < Bank.getBank().getOpenCloseFee())
            return null;
        else
            return new CheckingAccount(initialDeposit, fiat, id);
    }
}
