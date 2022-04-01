import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;

public class SavingsAccountUI extends JFrame{
    private JPanel panel2;
    private JLabel tfBankOfBoston;
    private JLabel LaccountBalance;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton backButton;
    private JPanel Panel1;
    private JButton transferButton;
    private JComboBox depositFiat;
    private JTextField depositAmount;
    private JTextField withdrawAmount;
    private JTextField transferAmount;
    private JComboBox receivingAccount;
    private JButton closeAccountButton;
    private JComboBox transferFiat;
    private JComboBox withdrawFiat;
    private JLabel balance;
    private JLabel withdrawalFee;
    private JLabel transferFee;
    private JButton openSecuritiesAccountButton;
    private String firstname;

    SavingsAccountUI(String firstname) {
        setContentPane(Panel1);
        setTitle("Savings");
        setSize(1500, 1000);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        Client client = BankController.getBankController().getLoggedInClient();
        balance.setText(String.valueOf(client.getSavingsAccount().getBalance()));
        withdrawalFee.setText(String.valueOf(Bank.getBank().getWithdrawalFee()));
        transferFee.setText(String.valueOf(Bank.getBank().getTransferFee()));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserDashboardUI ud = new UserDashboardUI(firstname);
            }
        });
        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double amount = Utils.parseDouble(depositAmount.getText());

                if (amount <= 0)
                    JOptionPane.showMessageDialog(null, "Please enter an amount");

                else {
                    client.depositMoney((Depositable) client.getSavingsAccount(),
                            amount, Utils.parseFiat(depositFiat.getSelectedItem().toString()));

                    balance.setText(String.valueOf(client.getSavingsAccount().getBalance()));

                    Database db = BankController.getBankController().getBankDB();

                    db.updateAccount(client.getSavingsAccount());
                }
            }
        });
        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double amount = Utils.parseDouble(withdrawAmount.getText());

                if (amount <= 0)
                    JOptionPane.showMessageDialog(null, "Please enter an amount greater than 0");
                else {
                    client.withdrawMoney((Withdrawable) client.getSavingsAccount(),
                            amount, Utils.parseFiat(withdrawFiat.getSelectedItem().toString()));

                    balance.setText(String.valueOf(client.getSavingsAccount().getBalance()));

                    Database db = BankController.getBankController().getBankDB();

                    db.updateAccount(client.getSavingsAccount());
                }
            }
        });
        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double amount = Utils.parseDouble(transferAmount.getText());

                if (amount <= 0)
                    JOptionPane.showMessageDialog(null, "Please enter an amount greater than 0");
                else {
                    Account receiving;

                    if (receivingAccount.getSelectedItem().toString() == "Checkings") {
                        receiving = client.getCheckingAccount();
                        client.transferBetweenAccounts(client.getSavingsAccount(), client.getCheckingAccount(), amount);
                    } else {
                        receiving = client.getSecuritiesAccount();
                        client.transferBetweenAccounts(client.getSavingsAccount(), receiving, amount);
                    }

                    balance.setText(String.valueOf(client.getSavingsAccount().getBalance()));

                    Database db = BankController.getBankController().getBankDB();

                    db.updateAccount(client.getSavingsAccount());
                    db.updateAccount(receiving);
                }
            }
        });
        closeAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client.getSecuritiesAccount().isNull() && client.getLoans().size() == 0) {
                    client.closeSavingsAccount();
                    dispose();
                    UserDashboardUI userDashboardUI = new UserDashboardUI(firstname);
                } else if (!client.getSecuritiesAccount().isNull()) {
                    // must close securities account first and pay off all loans before closing savings
                    JOptionPane.showMessageDialog(null, "Can't close Savings account with an " +
                            "active Securities account");
                } else {
                    JOptionPane.showMessageDialog(null, "Can't close Savings account with " +
                            "active loans. Please pay them off first");
                }
            }
        });
    }
}
