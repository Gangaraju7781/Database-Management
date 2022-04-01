import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BankerDashboardUI extends JFrame{
    private JPanel panel2;
    private JLabel tfBankOfBoston;
    private JButton bankParametersButton;
    private JButton customerSInformationButton;
    private JButton reportingButton;
    private JButton logoutButton;
    private JPanel mainPanel;
    private JButton setStockMarketButton;

    BankerDashboardUI(){
        setContentPane(mainPanel);
        setTitle("Banker Dashboard");
        setSize(1500,1000);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        bankParametersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                BankParametersUI bpi = new BankParametersUI();
            }
        });
        customerSInformationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                CustomerSearchPageUI ciu = new CustomerSearchPageUI();
            }
        });
        reportingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ReportingSearchUI rui = new ReportingSearchUI();

            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                BankerLoginUI bl = new BankerLoginUI();
            }
        });

        setStockMarketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                BankerStockMarketUI bankerStockMarketUI = new BankerStockMarketUI();
            }
        });
    }
}
