import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Statement;

public class CreateBankerAccountUI extends JFrame{
    private JPanel panel2;
    private JLabel tfBankOfBoston;
    private JLabel tffirstname;
    private JLabel tfLastName;
    private JLabel tfUsername;
    private JLabel tfEmail;
    private JLabel tfDateOfBirth;
    private JLabel tfGender;
    private JTextField fname;
    private JLabel tfNationality;
    private JTextField lname;
    private JComboBox comboBox1;
    private JLabel tfSocialSecurityNumber;
    private JLabel tfPhone;
    private JTextField phoneno;
    private JTextField address;
    private JButton submitButton;
    private JButton backButton;
    private JPasswordField bankerPin;
    private JPanel mainPanel;
    private JTextField ssn;
    private JTextField username;
    private JTextField email_name;
    private JTextField password;
    private JTextField dob;
    private JTextField nationality;
    private JPasswordField passwordField1;
    private JLabel bankPin;

    public CreateBankerAccountUI() {
        setContentPane(mainPanel);
        setTitle("Create Banker Account");
        setSize(900,700);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                BankerLoginUI bl = new BankerLoginUI();
            }
        });
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String Firstname = fname.getText();
                String Lastname = lname.getText();
                String Username = username.getText();
                String Email = email_name.getText();
                char[] Password = passwordField1.getPassword();
                String gender = String.valueOf(comboBox1.getSelectedItem());
                String Dob = dob.getText();
                String Nationality = nationality.getText();
                String Ssn = ssn.getText();
                String phoneNo = phoneno.getText();
                String Address = address.getText();
                char Pin[] = bankerPin.getPassword();
                //saving all the newly created user account attributes into user_table table
                //creating an object of the database class to connect to the db before inserting the values
                Database db = BankController.getBankController().getBankDB();

                try {

                    //converting the char password to a String
                    String pwd = String.valueOf(Password);
                    String bPin = String.valueOf(Pin);

                    //hardcoding pin that banker needs to know that will be provided by bank to all banker employees

                    if(bPin.equals("123")) {

                        Statement stmt = db.getCon().createStatement();

                        String sql = "INSERT INTO user_table (db_username,db_password,firstname,lastname,email,dateofbirth,gender,nationality,ssn,phone,address,banker_or_customer, lastLoggedInDate) values ('" + Username + "','" + pwd + "','" + Firstname + "','" + Lastname + "','" + Email + "','" + Dob + "','" + gender + "','" + Nationality + "','" + Ssn + "','" + phoneNo + "','" + Address + "','b', '"+BankController.getBankController().getSystemDate()+"')";

                        stmt.executeUpdate(sql);

                        dispose();
                        BankerLoginUI l = new BankerLoginUI();

                        System.out.println("Inserted records successfully");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Wrong pin, please try again");
                    }
                }
                catch(Exception exception_thrown){
                    System.out.println("Failed to connect to database");
                }

            }
        });
    }
}
