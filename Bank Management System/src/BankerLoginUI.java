import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class BankerLoginUI extends JFrame{
    private JPanel mainPanel;
    private JLabel tfPassword;
    private JLabel tfusername;
    private JLabel tfWelcome;
    private JPasswordField passwordField1;
    private JTextField username;
    private JButton loginButton;
    private JButton signuplogincust;
    private JButton createBankerAccountButton;

    BankerLoginUI() {
        setContentPane(mainPanel);
        setTitle("Banker Login");
        setSize(1500, 1000);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String Username = username.getText();
                char[] Password = passwordField1.getPassword();

                //creating Database object to connect to database bank
                Database db = BankController.getBankController().getBankDB();
                boolean resultSet_output = false;
                String pwd = "";
                ResultSet rs = null;
                String banker_or_customer = "b";
                User user;
                try {

                    //Create User object and pass in 3 parameters
                    //converting the char password to a String
                    pwd = String.valueOf(Password);
                    //Statement stmt = db.con.createStatement();
                    PreparedStatement ps = db.getCon().prepareStatement("select userid, firstname, lastLoggedInDate, " +
                            "banker_or_customer from user_table where db_username=? and db_password=?");
                    ps.setString(1, Username);
                    ps.setString(2, pwd);

                    rs = ps.executeQuery();
                    resultSet_output = rs.next();


                    if(resultSet_output && rs.getString("banker_or_customer").equals("b")) {

                        LocalDate lastLoggedInDate = LocalDate.parse(rs.getString("lastLoggedInDate"));

                        user = new Banker(Username, pwd, lastLoggedInDate, Integer.parseInt(rs.getString("userid")));
                        BankController.getBankController().setLoggedInBanker((Banker) user);

                        BankController bc = BankController.getBankController();
                        dispose();
                        BankerDashboardUI bd = new BankerDashboardUI();
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Wrong username or password, please try again");
                    }

                } catch (Exception exception_thrown) {
                    System.out.println("Exception occurred, unable to login");
                }
            }
        });
//        dis
        createBankerAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CreateBankerAccountUI cb = new CreateBankerAccountUI();
//                dispose();
            }
        });

        signuplogincust.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginUI l = new LoginUI();
            }
        });
    }
}
