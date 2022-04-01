import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportingViewUI extends JFrame {
    private JPanel panel2;
    private JLabel tfBankOfBoston;
    private JTable table1;
    private JPanel mainPanel;
    private JButton backToDashButton;
    private JButton backToSearchButton;
    private JLabel transactionLabel;
    private String transactionDate;

    ReportingViewUI(String txDate) {
        transactionDate = txDate;
        setContentPane(mainPanel);
        setTitle("Reporting");
        setSize(1450,1000);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        transactionLabel.setText(transactionDate);
        backToDashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                BankerDashboardUI bd = new BankerDashboardUI();
            }
        });
        backToSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ReportingSearchUI reportingSearchUI = new ReportingSearchUI();
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        Database db = BankController.getBankController().getBankDB();

        table1 = new JTable(db.reportTransactionFromTableDB(transactionDate));
        setVisible(true);
    }
}
