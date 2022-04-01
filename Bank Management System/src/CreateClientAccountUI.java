import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Statement;

public class CreateClientAccountUI extends JFrame {
    private JPanel panel1;
    private JLabel tffirstname;
    private JLabel tfLastName;
    private JTextField fname;
    private JTextField lname;
    private JLabel tfDateOfBirth;
    private JTextField dob;
    private JLabel tfGender;
    private JComboBox comboBox1;
    private JLabel tfEmail;
    private JTextField email_name;
    private JPasswordField passwordField1;
    private JTextField nationality;
    private JTextField ssn_text;
    private JTextField phoneno;
    private JTextField addresss;
    private JLabel tfNationality;
    private JLabel tfSocialSecurityNumber;
    private JLabel tfPhone;
    private JLabel tfAddress;
    private JPanel panel2;
    private JLabel tfBankOfBoston;
    private JButton submitButton;
    private JButton backButton;
    private JLabel lb1;
    private JTextField username;
    private JLabel tfUsername;

    CreateClientAccountUI(){
        setContentPane(panel1);
        setTitle("Create Account");
        setSize(900,800);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginUI l = new LoginUI();

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
                String Ssn = ssn_text.getText();
                String phoneNo = phoneno.getText();
                String Address = addresss.getText();

                //saving all the newly created user account attributes into user_table table
                //creating an object of the database class to connect to the db before inserting the values
                Database db = BankController.getBankController().getBankDB();
                try {

                    //converting the char password to a String
                    String pwd = String.valueOf(Password);
                    Statement stmt = db.getCon().createStatement();

                    String sql = "INSERT INTO user_table (db_username,db_password,firstname,lastname,email,dateofbirth,gender,nationality,ssn,phone,address,banker_or_customer, lastLoggedInDate) values ('"+Username+"','"+pwd+"','"+Firstname+"','"+Lastname+"','"+Email+"','"+Dob+"','"+gender+"','"+Nationality+"','"+Ssn+"','"+phoneNo+"','"+Address+"','c', '"+BankController.getBankController().getSystemDate().toString()+"')";

                    stmt.executeUpdate(sql);
                    System.out.println("Inserted records successfully");
                }
                catch(Exception exception_thrown){
                    System.out.println("Failed to connect to database");
                }

                lb1.setText("Account Created Successfully");
            }
        });
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
