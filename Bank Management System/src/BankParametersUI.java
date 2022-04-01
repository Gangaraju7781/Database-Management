import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class BankParametersUI extends JFrame{
    private JPanel panel2;
    private JLabel tfBankOfBoston;
    private JPanel mainPanel;
    private JLabel txFee;
    private JLabel openCloseFee;
    private JLabel loanInterestRate;
    private JLabel withDrawalFee;
    private JLabel minSecTransfer;
    private JLabel minSvngsblnceWSec;
    private JLabel OpenSecThreshold;
    private JButton backButton;
    private JButton saveChangesButton;
    private JTextField ocFee;
    private JTextField transactionFee;
    private JTextField withdrawFee;
    private JTextField loanInterest;
    private JTextField minIniTransfer;
    private JTextField minSavings;
    private JTextField threshold;
    private JTextField savingsInterest;
    private JTextField transferFee;
    private JTextField minSavingsBalanceToEarnInterest;
    private JTextField minDeposit;
    private JTextField reserve;

    public BankParametersUI() {
        setContentPane(mainPanel);
        setTitle("Bank Parameters");
        setSize(1500, 1000);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        //set default value - randomly hardcoded from db
        Database db = BankController.getBankController().getBankDB();
        ResultSet rs;
        try{
            String sql = "select * from bank";
            Statement stmt = db.getCon().createStatement();

            rs = stmt.executeQuery(sql);

            rs.next();

            ocFee.setText(rs.getString("openCloseFee"));
            transactionFee.setText(rs.getString("txFee"));
            withdrawFee.setText(rs.getString("withdrawalFee"));
            loanInterest.setText(rs.getString("loanInterestRate"));
            minDeposit.setText(rs.getString("minCheckingSavingsInitialDeposit"));
            minIniTransfer.setText(rs.getString("minInitialSecuritiesTransfer"));
            minSavings.setText(rs.getString("minSavingsBalanceWithSecurities"));
            threshold.setText(rs.getString("openSecuritiesThreshold"));
            savingsInterest.setText(rs.getString("savingsInterestRate"));
            transferFee.setText(rs.getString("transferFee"));
            minSavingsBalanceToEarnInterest.setText(rs.getString("minSavingsBalanceToEarnInterest"));

        }catch (Exception except){
            System.out.println(except);
        }

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                BankerDashboardUI bd = new BankerDashboardUI();
            }
        });
        saveChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Database db = BankController.getBankController().getBankDB();

                try{

                    String sql_ps = "update bank set openCloseFee = ?, txFee = ?, transferFee = ?, " +
                            "withdrawalFee = ?, savingsInterestRate = ?, loanInterestRate = ?, minCheckingSavingsInitialDeposit = ?, " +
                            "minSavingsBalanceToEarnInterest = ?, minInitialSecuritiesTransfer = ?," +
                            " minSavingsBalanceWithSecurities = ?, openSecuritiesThreshold = ?";

                    PreparedStatement ps = db.getCon().prepareStatement(sql_ps);

                    ps.setString(1, ocFee.getText());
                    ps.setString(2, transactionFee.getText());
                    ps.setString(3, transferFee.getText());
                    ps.setString(4, withdrawFee.getText());
                    ps.setString(5, savingsInterest.getText());
                    ps.setString(6, loanInterest.getText());
                    ps.setString(7, minDeposit.getText());
                    ps.setString(8, minSavings.getText());
                    ps.setString(9, minIniTransfer.getText());
                    ps.setString(10, minSavings.getText());
                    ps.setString(11, threshold.getText());

                    ps.executeUpdate();

                }catch (Exception except){
                    System.out.println(except);
                }

            }
        });
    }
}
