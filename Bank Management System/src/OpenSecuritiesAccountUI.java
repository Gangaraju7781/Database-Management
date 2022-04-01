import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;

public class OpenSecuritiesAccountUI extends JFrame{
    private JPanel subPanel;
    private JPanel panel2;
    private JLabel tfBankOfBoston;
    private JLabel label2;
    private JLabel label3;
    private JButton openSecuritiesAccountButton;
    private JButton backButton;
    private JPanel mainPanel;
    private JTextField openAmount;
    private JComboBox openFiat;
    private JLabel openFee;
    private JLabel minTransfer;
    private String firstname;

    public OpenSecuritiesAccountUI(String firstname) {
        setContentPane(mainPanel);
        setTitle("Open Securities Account");
        setSize(1500,1000);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        openFee.setText(String.valueOf(BankController.getBankController().getBank().getOpenCloseFee()));
        minTransfer.setText(String.valueOf(BankController.getBankController().getBank().getMinInitialSecuritiesTransfer()));
        Client client = BankController.getBankController().getLoggedInClient();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserDashboardUI ud = new UserDashboardUI(firstname);
            }
        });
        openSecuritiesAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double amount = Utils.parseDouble(openAmount.getText());

                if (amount == 0)
                    JOptionPane.showMessageDialog(null, "Please enter an amount greater than 0");
                else {
                    if (client.getSavingsAccount().getBalance() < Bank.getBank().getOpenSecuritiesThreshold()) {
                        // need Bank.getBank().getOpenSecuritiesThreshold() amount to open
                        JOptionPane.showMessageDialog(mainPanel, "need " + Bank.getBank().getOpenSecuritiesThreshold() + " amount to open");
                    } else if (amount < Bank.getBank().getMinInitialSecuritiesTransfer()) {
                        // initial transfer must be greater than Bank.getBank().getMinInitialSecuritiesTransfer()
                        JOptionPane.showMessageDialog(mainPanel, "initial transfer must be greater than " + Bank.getBank().getMinInitialSecuritiesTransfer());
                    } else if (client.getSavingsAccount().getBalance() - amount < Bank.getBank().getMinSavingsBalanceWithSecurities()) {
                        // must have at least Bank.getBank().getMinSavingsBalanceWithSecurities() in savings with open securities
                        JOptionPane.showMessageDialog(mainPanel, "must have at least " + Bank.getBank().getMinSavingsBalanceWithSecurities() + " in savings with open securities");
                    } else {

                        Database db = BankController.getBankController().getBankDB();

                        int newAccountId = db.checkingIfAccountTableIsEmpty();

                        client.openSecuritiesAccount(amount, Utils.parseFiat(openFiat.getSelectedItem().toString()), newAccountId);

                        try {

                            //need to insert into accounts then dispose and send back to the dashboard
                            PreparedStatement ps = db.getCon().prepareStatement("insert into accounts (isOpen, userid, balance, type) values (?, ?, ?, ?)");
                            ps.setString(1, "true");
                            ps.setString(2, String.valueOf(client.getId()));
                            ps.setString(3, String.valueOf(client.getSecuritiesAccount().getBalance()));
                            ps.setString(4, "Securities");
                            ps.executeUpdate();

                            //moving client to the dashboard
                            dispose();
                            UserDashboardUI ud = new UserDashboardUI(firstname);

                        } catch (Exception except) {
                            System.out.println(except);
                        }
                    }
                }
            }
        });
    }
}
