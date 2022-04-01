import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;

public class OpenCheckingAccountUI extends JFrame{
    private JPanel subPanel;
    private JPanel panel2;
    private JLabel label2;
    private JLabel label3;
    private JLabel tfBankOfBoston;
    private JButton openCheckingsAccountButton;
    private JButton backButton;
    private JPanel mainPanel;
    private JTextField openAmount;
    private JComboBox openFiat;
    private JLabel openFee;
    private JLabel minDeposit;
    private String firstname;

    public OpenCheckingAccountUI(String firstname) {
        setContentPane(mainPanel);
        setTitle("Open Checkings Account");
        setSize(1500,1000);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        openFee.setText(String.valueOf(BankController.getBankController().getBank().getOpenCloseFee()));
        minDeposit.setText(String.valueOf(BankController.getBankController().getBank().getMinCheckingSavingsInitialDeposit()));
        Client client = BankController.getBankController().getLoggedInClient();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserDashboardUI ud = new UserDashboardUI(firstname);
            }
        });
        openCheckingsAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //call check if account table is empty

                double amount = Utils.parseDouble(openAmount.getText());

                if (amount < BankController.getBankController().getBank().getMinCheckingSavingsInitialDeposit())
                    JOptionPane.showMessageDialog(null, "Please enter an amount greater than or " +
                            "equal to " + BankController.getBankController().getBank().getMinCheckingSavingsInitialDeposit());
                else {

                    Database db = BankController.getBankController().getBankDB();

                    int newAccountId = db.checkingIfAccountTableIsEmpty();

                    client.openCheckingAccount(amount, Utils.parseFiat(openFiat.getSelectedItem().toString()), newAccountId);

                    try {

                        //need to insert into accounts then dispose and send back to the dashboard
                        PreparedStatement ps = db.getCon().prepareStatement("insert into accounts (isOpen, userid, balance, type) values (?, ?, ?, ?)");
                        ps.setString(1, "true");
                        ps.setString(2, String.valueOf(client.getId()));
                        ps.setString(3, String.valueOf(client.getCheckingAccount().getBalance()));
                        ps.setString(4, "Checking");
                        ps.executeUpdate();

                        //moving client to the dashboard
                        dispose();
                        UserDashboardUI ud = new UserDashboardUI(firstname);

                    } catch (Exception except) {
                        System.out.println(except);
                    }
                }
            }
        });
    }
}
