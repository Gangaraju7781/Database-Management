import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerSearchPageUI extends JFrame{
    private JPanel panel2;
    private JLabel tfBankOfBoston;
    private JTextField custID;
    private JButton searchButton;
    private JButton backButton;
    private JPanel mainPanel;

    public CustomerSearchPageUI() {
        setContentPane(mainPanel);
        setTitle("Customer(s) Search Page");
        setSize(1500, 1000);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                BankerDashboardUI bd = new BankerDashboardUI();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                CustomerInformationUI ci = new CustomerInformationUI(custID.getText());
            }
        });
    }
}
