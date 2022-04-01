import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class LoansUI extends JFrame{
    private JPanel panel2;
    private JLabel tfBankOfBoston;
    private JButton requestALoanButton;
    private JTextField requestAmount;
    private JButton payOffLoanButton;
    private JButton payInterestButton;
    private JComboBox requestTerm;
    private JButton backButton;
    private JPanel mainPanel;
    private JScrollPane ScrollPane1;
    private JTable table1;
    private JComboBox payInterestLoan;
    private JComboBox payOffLoanID;
    private JLabel loanRemainingBal;
    private String firstname;

    public LoansUI(String firstname) {
        setContentPane(mainPanel);
        setTitle("Loans");
        setSize(1200,800);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        Client client = BankController.getBankController().getLoggedInClient();

        List<String> ids = new ArrayList<String>();
        for (int id : client.getLoans().keySet())
            ids.add(String.valueOf(id));

        payOffLoanID.setModel(new DefaultComboBoxModel<String>(ids.toArray(new String[ids.size()])));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserDashboardUI ud = new UserDashboardUI(firstname);
            }
        });
        requestALoanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                double amount = Utils.parseDouble(requestAmount.getText());

                if (amount < 500)
                    JOptionPane.showMessageDialog(null, "Please enter an amount greater than or " +
                            "equal to 500");
                else {

                    Database db = BankController.getBankController().getBankDB();

                    int loan_id = 0;

                    ResultSet rs = null;
                    try {

                        //checking if loan table is empty or not

                        String sql_loans = "select * from loan";// where userid = '" + client.getId() + "'";
                        Statement stmt = db.getCon().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                ResultSet.CONCUR_READ_ONLY);
                        rs = stmt.executeQuery(sql_loans);

                        if (rs.last() == false) {
                            loan_id = 1;
                        } else {
                            //rs.last();
                            loan_id = rs.getInt("loanid");
                            loan_id = loan_id + 1;
                        }
                    } catch (Exception exception) {
                        System.out.println(exception);
                    }

                    client.requestLoan(amount, Integer.parseInt(requestTerm.getSelectedItem().toString()), loan_id);

                    ResultSet ps_result = null;

                    try {

                        PreparedStatement ps = db.getCon().prepareStatement("insert into loan (userid,principal, interestRate, numYears, numPeriodsPerYears, requestedDate, isOpen) values (?, ? , ?, ?, ?, ?, ?)");
                        ps.setString(1, String.valueOf(client.getId()));
                        ps.setString(2, String.valueOf(client.getLoans().get(loan_id).getPrincipal()));
                        ps.setString(3, String.valueOf(client.getLoans().get(loan_id).getInterestRate()));
                        ps.setString(4, String.valueOf(client.getLoans().get(loan_id).getNumYears()));
                        ps.setString(5, String.valueOf(client.getLoans().get(loan_id).getNumPeriodsPerYear()));
                        ps.setString(6, String.valueOf(client.getLoans().get(loan_id).getRequestedDate()));
                        ps.setString(7, "true");
                        ps.executeUpdate();

                    } catch (Exception except) {
                        System.out.println(except);
                    }
                    dispose();
                    LoansUI loansUI = new LoansUI(firstname);
                }
            }
        });

        payOffLoanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (client.payOffLoan(Integer.parseInt(payOffLoanID.getSelectedItem().toString()))) {
                    dispose();
                    LoansUI loansUI = new LoansUI(firstname);

                    Database db = BankController.getBankController().getBankDB();

                    try {

                        PreparedStatement ps = db.getCon().prepareStatement("update accounts set balance = ? where userid = '" + String.valueOf(client.getId()) + "' and type = '" + client.getCheckingAccount().getType() + "'");
                        ps.setString(1, String.valueOf(client.getCheckingAccount().getBalance()));
                        ps.executeUpdate();

                    } catch (Exception except) {
                        System.out.println(except);
                    }
                }
            }
        });
        payOffLoanID.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Loan selectedLoan = client.getLoans().get(Integer.parseInt(payOffLoanID.getSelectedItem().toString()));

                loanRemainingBal.setText(String.valueOf(Utils.roundDouble(selectedLoan.getPrincipalBalance())));
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        Client client = BankController.getBankController().getLoggedInClient();

        Database db = BankController.getBankController().getBankDB();
        table1 = new JTable(db.loanFromTableDB(client.getId()));
        setVisible(true);
    }
}
