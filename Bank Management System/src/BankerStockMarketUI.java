import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class BankerStockMarketUI extends JFrame{
    private JPanel panel2;
    private JLabel tfBankOfBoston;
    private JButton backButton;
    private JComboBox stockToUpdate;
    private JTextField newPrice;
    private JButton updatePriceButton;
    private JPanel mainPanel;

    BankerStockMarketUI() {
        setContentPane(mainPanel);
        setTitle("Set Stock Market");
        setSize(1500,1000);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        List<String> tickers = new ArrayList<String>();
        for (Stock stock : BankController.getBankController().getBankDB().getStocksFromDB())
            tickers.add(stock.getTicker());
        stockToUpdate.setModel(new DefaultComboBoxModel<String>(tickers.toArray(new String[tickers.size()])));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (BankController.getBankController().getStockMarket().isOpen()) {
                    dispose();
                    BankerDashboardUI bankerDashboardUI = new BankerDashboardUI();
                } else
                    JOptionPane.showMessageDialog(null, "You still have to update the prices of " +
                            String.join(",", BankController.getBankController().getStockMarket().getTickersNotYetUpdated()));
            }
        });
        updatePriceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double price = Utils.parseDouble(newPrice.getText());

                if (price <= 0)
                    JOptionPane.showMessageDialog(null, "Please enter an amount greater than 0");
                else {
                    BankController.getBankController().getStockMarket().updatePrice(
                            BankController.getBankController().getBankDB().
                                    getStockFromDB(stockToUpdate.getSelectedItem().toString()), price);
                }
            }
        });
        stockToUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Stock selectedStock = Utils.getStockByTicker(stockToUpdate.getSelectedItem().toString());
                StockPrice stockPrice = BankController.getBankController().getStockMarket().getCurrentStockPrice(selectedStock);

                if (stockPrice != null)
                    newPrice.setText(String.valueOf(stockPrice.getPrice()));
                else
                    newPrice.setText("0");
            }
        });
    }
}
