import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserDashboardUI extends JFrame{
    private JButton securitiesButton;
    private JButton checkingsButton;
    private JButton transactionHistory;
    private JButton logoutButton;
    private JPanel mainPanel;
    private JButton savingsButton;
    private JButton loansButton;
    private JPanel panel2;
    private JLabel tfBankOfBoston;
    private JLabel LAccountBalance;
    private String firstname;

    UserDashboardUI(String firstname) {
        setContentPane(mainPanel);
        setTitle("User Dashboard");
        setSize(900,800);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        Client client = BankController.getBankController().getLoggedInClient();
        this.firstname = firstname;

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();
                LoginUI l = new LoginUI();
            }
        });
        checkingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                OpenCheckingAccountUI checkingsAcc_ui;
                CheckingAccountUI c;
                if (client.getCheckingAccount().isNull())
                    checkingsAcc_ui = new OpenCheckingAccountUI(firstname);
                else
                    c = new CheckingAccountUI(firstname);
            }
        });

        savingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                OpenSavingsAccountUI savingsAcc_ui;
                SavingsAccountUI ss;
                if(client.getCheckingAccount().isNull()) {
                    JOptionPane.showMessageDialog(mainPanel,"Please create a checkings account");
                }
                else if (client.getSavingsAccount().isNull()) {
                    dispose();
                    savingsAcc_ui = new OpenSavingsAccountUI(firstname);
                }
                else{
                    dispose();
                    ss = new SavingsAccountUI(firstname);
                }

            }
        });
        securitiesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SecuritiesAccountUI st;
                OpenSecuritiesAccountUI openSecuritiesAccount_ui;

                if(client.getSavingsAccount().isNull()) {
                    JOptionPane.showMessageDialog(mainPanel,"Please create a savings account");
                }
                else if (client.getSecuritiesAccount().isNull()){
                    dispose();
                    openSecuritiesAccount_ui = new OpenSecuritiesAccountUI(firstname);
                }
                else{
                    dispose();
                    st = new SecuritiesAccountUI(firstname);
                }
            }
        });

        loansButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoansUI l = new LoansUI(firstname);
            }
        });
        transactionHistory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TransactionHistoryUI th = new TransactionHistoryUI(firstname);
            }
        });
    }
}
