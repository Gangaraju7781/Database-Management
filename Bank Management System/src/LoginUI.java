import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class LoginUI extends JFrame{
    private JLabel tfusername;
    private JLabel tfPassword;
    private JPanel mainPanel;
    private JPanel subPanel;
    private JLabel label2;
    private JLabel tfWelcome;
    private JLabel label3;
    private JPasswordField passwordField1;
    private JTextField username;
    private JButton loginButton;
    private JButton createAccountButton;
    private JButton loginAsBankerButton;
    private String firstname;

    LoginUI(){
        setContentPane(mainPanel);
//        setContentPane(subPanel);
//        ImageIcon img = new ImageIcon("bnk.png")
        setTitle("Banking");
        setSize(1700,1000);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);



        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String Username = username.getText();
                char[] Password = passwordField1.getPassword();
                //lb.setText("Login Successful");

                //creating Database object to connect to database bank
                Database db = BankController.getBankController().getBankDB();
                boolean resultSet_output = false;
                String pwd = "";
                ResultSet rs = null;
                String banker_or_customer = "c";
                try {

                    //Create User object and pass in 3 parameters
                    //converting the char password to a String
                    pwd = String.valueOf(Password);

                    PreparedStatement ps = db.getCon().prepareStatement("select userid, firstname, banker_or_customer, lastLoggedInDate from user_table where db_username=? and db_password=?");
                    ps.setString(1, Username);
                    ps.setString(2, pwd);

                    rs = ps.executeQuery();
                    resultSet_output = rs.next();
                    banker_or_customer = rs.getString("banker_or_customer");

                } catch (Exception exception_thrown) {
                    System.out.println("Exception occurred, unable to login");
                }

                User user;

                int id = 0;
                ResultSet rs_userobj, rs_transaction, rs_loan;

                //since a user can only have 1 of each type of account, only creating 1 object

                Account checkingAccount = new NullAccount();
                Account savingsAccount = new NullAccount();
                Account securitiesAccount = new NullAccount();
                LocalDate lastLoggedInDate = BankController.getBankController().getSystemDate();
                ArrayList<Transaction> transactionArrayList;// = new ArrayList<>();;
                HashMap<Integer, Loan> loanHashMap;
                if (resultSet_output && banker_or_customer.equals("c")) {
                    dispose();
                    transactionArrayList = new ArrayList<>();
                    loanHashMap = new HashMap<>();
                    try {
                        id = Integer.parseInt(rs.getString("userid"));
                        firstname = rs.getString("firstname");
                        lastLoggedInDate = LocalDate.parse(rs.getString("lastLoggedInDate"));
                        //get account details and save into objects

                        String sql_checking = "select * from accounts where userid = '" + id + "' and isOpen = 'true'";
                        Statement stmt = db.getCon().createStatement();
                        rs_userobj = stmt.executeQuery(sql_checking);

                        //do result set and split into 3 types of accounts
                        while (rs_userobj.next()) {
                            String s = rs_userobj.getString("accountid");
                            if (rs_userobj.getString("type").equals("Checking")) {
                                int i = Integer.parseInt(rs_userobj.getString("accountid"));
                                System.out.println(i);
                                checkingAccount = new CheckingAccount(Double.parseDouble(rs_userobj.getString("balance")),
                                        Integer.parseInt(rs_userobj.getString("accountid")));
                            } else if (rs_userobj.getString("type").equals("Savings")) {
                                savingsAccount = new SavingsAccount(Double.parseDouble(rs_userobj.getString("balance")),
                                        Integer.parseInt(rs_userobj.getString("accountid")));
                            } else if (rs_userobj.getString("type").equals("Securities")) {
                                securitiesAccount = new SecuritiesAccount(Double.parseDouble(rs_userobj.getString("balance")),
                                        Integer.parseInt(rs_userobj.getString("accountid")), Double.parseDouble(rs_userobj.getString("realizedGains")));
                            }
                        }

                        //transaction history

                        String sql_transaction = "select * from transaction where userid = '" + id + "'";

                        Statement stmt_transaction = db.getCon().createStatement();
                        rs_transaction = stmt_transaction.executeQuery(sql_transaction);

                        //put the result set in array list of transactions

                        while (rs_transaction.next()) {
                            transactionArrayList.add(new Transaction(id, rs_transaction.getString("transaction_log")));
                        }

                        //retrieving from loans table

                        String sql_loan = "select * from loan where userid = '" + id + "' and isOpen = 'true'";

                        Statement stmt_loan = db.getCon().createStatement();
                        rs_loan = stmt_loan.executeQuery(sql_loan);

                        //put the result set in array list of loans

                        while (rs_loan.next()) {

                            loanHashMap.put(Integer.parseInt(rs_loan.getString("loanid")),
                                    new Loan(Double.parseDouble(rs_loan.getString("principal")),
                                            Double.parseDouble(rs_loan.getString("interestRate")),
                                            Integer.parseInt(rs_loan.getString("numYears")),
                                            Integer.parseInt(rs_loan.getString("numPeriodsPerYears")),
                                            LocalDate.parse(rs_loan.getString("requestedDate")),
                                            Integer.parseInt(rs_loan.getString("loanid"))));
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }



                    //Creating user object with client details

                    //create client object

                    user = new Client(Username, pwd, lastLoggedInDate, id, checkingAccount, savingsAccount, securitiesAccount, loanHashMap, transactionArrayList);
                    BankController.getBankController().setLoggedInClient((Client) user);

                    Client client = BankController.getBankController().getLoggedInClient();
                   // System.out.println("size = "+client.getLoans().size());
                    client.receiveInterest();
                    client.payInterest();
                    client.setLastLoggedInDate(BankController.getBankController().getSystemDate());
                    BankController.getBankController().getBankDB().updateUserLastLoggedInDate();

                    UserDashboardUI ud = new UserDashboardUI(firstname);

                } else {
                    JOptionPane.showMessageDialog(null, "Wrong username or password, please try again");
                }
            }

        });

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CreateClientAccountUI c = new CreateClientAccountUI();
            }
        });

        loginAsBankerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                BankerLoginUI bl = new BankerLoginUI();
            }
        });
    }
}
