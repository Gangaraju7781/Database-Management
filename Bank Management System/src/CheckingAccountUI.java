import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CheckingAccountUI extends JFrame {
    private JPanel panel2;
    private JLabel tfBankOfBoston;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton closeAccountButton;
    private JButton backButton;
    private JPanel Panel1;
    private JButton openSavingsAccountButton;
    private JComboBox depositFiat;
    private JButton transferBetweenAccountsButton;
    private JTextField depositAmount;
    private JTextField withdrawAmount;
    private JTextField transferAmount;
    private JComboBox receivingAccount;
    private JComboBox withdrawFiat;
    private JComboBox transferFiat;
    private JLabel jaccountBalance;
    private JLabel balance;
    private JLabel withdrawalFee;
    private JLabel transferFee;
    private String firstname;

    CheckingAccountUI(String firstname) {
        setContentPane(Panel1);
        setTitle("Checkings");
        setSize(1500, 1000);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        Client client = BankController.getBankController().getLoggedInClient();
        balance.setText(String.valueOf(client.getCheckingAccount().getBalance()));
        withdrawalFee.setText(String.valueOf(Bank.getBank().getWithdrawalFee()));
        transferFee.setText(String.valueOf(Bank.getBank().getTransferFee()));
        this.firstname = firstname;

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
                    client.depositMoney((Depositable) client.getCheckingAccount(),
                            amount, Utils.parseFiat(depositFiat.getSelectedItem().toString()));

                    balance.setText(String.valueOf(client.getCheckingAccount().getBalance()));

                    Database db = BankController.getBankController().getBankDB();

                    try {

                        PreparedStatement ps = db.getCon().prepareStatement("update accounts set balance = ? where userid = '" + String.valueOf(client.getId()) + "' and type = '" + client.getCheckingAccount().getType() + "'");
                        ps.setString(1, String.valueOf(client.getCheckingAccount().getBalance()));
                        ps.executeUpdate();
                        //rs =ps.executeQuery();
                    } catch (Exception except) {
                        System.out.println(except);
                    }
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
                    client.withdrawMoney((Withdrawable) client.getCheckingAccount(),
                            amount, Utils.parseFiat(withdrawFiat.getSelectedItem().toString()));

                    balance.setText(String.valueOf(client.getCheckingAccount().getBalance()));

                    Database db = BankController.getBankController().getBankDB();

                    db.updateAccount(client.getCheckingAccount());
                }
            }
        });
        transferBetweenAccountsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double amount = Utils.parseDouble(transferAmount.getText());

                if (amount <= 0)
                    JOptionPane.showMessageDialog(null, "Please enter an amount greater than 0");
                else {
                    Account receiving;

                    if (receivingAccount.getSelectedItem().toString() == "Savings") {
                        receiving = client.getSavingsAccount();
                        client.transferBetweenAccounts(client.getCheckingAccount(), receiving, amount);
                    }
                    else {
                        receiving = client.getSecuritiesAccount();
                        client.transferBetweenAccounts(client.getCheckingAccount(), receiving, amount);
                    }

                    balance.setText(String.valueOf(client.getCheckingAccount().getBalance()));

                    Database db = BankController.getBankController().getBankDB();

                    db.updateAccount(client.getCheckingAccount());
                    db.updateAccount(receiving);
                }
            }
        });
        closeAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client.getSavingsAccount().isNull()) {

                    client.closeCheckingAccount();

                    dispose();
                    UserDashboardUI userDashboardUI = new UserDashboardUI(firstname);
                } else {
                    // cant close with active savings account
                    JOptionPane.showMessageDialog(null, "Can't close Checking account with an " +
                            "active Savings account");
                }
            }
        });
    }
}
