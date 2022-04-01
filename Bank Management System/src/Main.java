/*
contains the main method from where the program starts.
 */

public class Main {

    public static void main(String[] args) {
        BankController bankController = BankController.getBankController();
        bankController.runBankController();
    }
}