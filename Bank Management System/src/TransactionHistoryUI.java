import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransactionHistoryUI extends JFrame{
    private JPanel panel2;
    private JLabel tfBankOfBoston;
    private JTable table1;
    private JScrollPane mainPanel;
    private JPanel setPanel;
    private JButton backButton;

    private String firstname;

    TransactionHistoryUI(String firstname) {
        setContentPane(setPanel);
        setTitle("Transaction History");
        setSize(1500,1000);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserDashboardUI ud = new UserDashboardUI(firstname);
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        Client client = BankController.getBankController().getLoggedInClient();
        Database db = BankController.getBankController().getBankDB();
        table1 = new JTable(db.transactionHistoryFromTableDB(client.getId()));
        setVisible(true);
    }
}
