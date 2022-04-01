import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReportingSearchUI extends JFrame {
    private JPanel panel2;
    private JLabel tfBankOfBoston;
    private JTable table1;
    private JPanel mainPanel;
    private JButton backButton;
    private JLabel cashReserves;
    private JTextField txDate;
    private JButton dateSubmit;
    private JButton viewButton;
    private String bankerChoiceDate;
    ReportingSearchUI() {

        setContentPane(mainPanel);
        setTitle("Reporting");
        setSize(1450,1000);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        cashReserves.setText(String.valueOf(BankController.getBankController().getBank().getCashReserves()));


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                BankerDashboardUI bd = new BankerDashboardUI();
            }
        });

        dateSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Database db = BankController.getBankController().getBankDB();
                if (Utils.parseDate(txDate.getText()) == null)
                    JOptionPane.showMessageDialog(null, "Please enter a date in the accepted format");
                else {
                    bankerChoiceDate = txDate.getText();
                    db.reportTransactionDownload(bankerChoiceDate);

                }
            }
        });
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Database db = BankController.getBankController().getBankDB();
                if (Utils.parseDate(txDate.getText()) == null)
                    JOptionPane.showMessageDialog(null, "Please enter a date in the accepted format");
                else {
                    dispose();
                    ReportingViewUI ci = new ReportingViewUI(txDate.getText());
                }
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        /*Database db = BankController.getBankController().getBankDB();

        table1 = new JTable(db.reportTransactionFromTableDB());
        setVisible(true);*/
    }

 /*   private void $$$setupUI$$$(){
        bankerChoiceDate = txDate.getText();
        createUIComponents();
    }*/
}
