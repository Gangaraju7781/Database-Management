import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;

public class CustomerInformationUI extends JFrame{
    private JPanel panel2;
    private JLabel tfBankOfBoston;
    private JTable positionsTable;
    private JTable loansTable;
    private JTable txHistoryTable;
    private JPanel mainPanel;
    private JTable table2;
    private JTable table1;
    private JTable table3;
    private JScrollBar scrollBar1;
    private JButton backButton;
    private JButton backToDashboardButton;
    private JLabel customerName;
    private JLabel checkingBalance;
    private JLabel savingsBalance;
    private JLabel securitiesBalance;
    private JLabel totalBalance;
    String custIdFromTextField;


    public CustomerInformationUI(String customerId) {
        this.custIdFromTextField = customerId;
        setContentPane(mainPanel);
        setTitle("Customer Information");
        setSize(1450,1000);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        Database db = BankController.getBankController().getBankDB();

        ResultSet rs;
        try{
            String sql = "select * from user_table as u, accounts as a where u.userid = a.userid and a.userid = '"+customerId+"'";

            Statement stmt = db.getCon().createStatement();

            rs = stmt.executeQuery(sql);
            double totalBal = 0.0;
            while(rs.next()) {

                customerName.setText(rs.getString("firstname"));

                if (rs.getString("type").equals("Checkings")) {
                    checkingBalance.setText(rs.getString("balance"));
                    totalBal = totalBal + Double.parseDouble(rs.getString("balance"));
                }
                else if(rs.getString("type").equals("Savings")) {
                    savingsBalance.setText(rs.getString("balance"));
                    totalBal = totalBal + Double.parseDouble(rs.getString("balance"));
                }
                else if(rs.getString("type").equals("Securities"))
                {
                    securitiesBalance.setText(rs.getString("balance"));
                    totalBal = totalBal + Double.parseDouble(rs.getString("balance"));
                }

                totalBalance.setText(String.valueOf(totalBal));
            }

        }catch (Exception except){
            System.out.println(except);
        }


//        mainPanel.addMouseMotionListener(new MouseMotionAdapter() {
//            @Override
//            public void mouseDragged(MouseEvent e) {
//                super.mouseDragged(e);
//            }
//        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CustomerSearchPageUI bd = new CustomerSearchPageUI();
            }
        });
        backToDashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                BankerDashboardUI bd = new BankerDashboardUI();

            }
        });
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here

        Database db = BankController.getBankController().getBankDB();

        //positions
        /*String[] columnNames1 = {"Open Date","Ticker","Quantity","Average Cost","Original Value","Current Price","Current Value","Unrealized Gains"};
        Object[][] data1 = {{"12/1/21", "AAPL", 10, 150, 1500, 200, 2000, 500},
                {"12/6/21", "TSLA", "Tesla Inc.", 2, 800, 1600, 1000, 2000, 400},
                {"12/9/21", "AMZN", "Amazon Corp..", 1, 4000, 4000, 3500, 3500, -500}};
        DefaultTableModel dataModel1 = new DefaultTableModel(data1, columnNames1);
        dataModel1.setColumnIdentifiers(columnNames1);*/

        table1 = new JTable(db.positionsFromTableDB(Integer.parseInt(custIdFromTextField)));
        //table1 = new JTable(dataModel1);
        setVisible(true);

        //loan table
        /*String[] columnNames = {"Loan ID", "Principal","Interest Rate","Term (Years)"};
        Object[][] data = {{1,1000,0.05,2},
                {2,1000,0.05,3},
                {3,1000,0.05,5}};
        DefaultTableModel dataModel = new DefaultTableModel(data, columnNames);
        dataModel.setColumnIdentifiers(columnNames);*/
        table2 = new JTable(db.loanFromTableDB(Integer.parseInt(custIdFromTextField)));
        setVisible(true);

        //transaction history

        table3 = new JTable(db.transactionHistoryFromTableDB(Integer.parseInt(custIdFromTextField)));
        setVisible(true);
    }
}
