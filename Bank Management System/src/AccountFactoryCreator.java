//This interface is used to implement the factory design pattern.

public interface AccountFactoryCreator {

    Account createAccount(double initialDeposit, Fiat fiat, int id);
}
