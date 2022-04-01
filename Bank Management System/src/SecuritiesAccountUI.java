import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class SecuritiesAccountUI extends JFrame{
    private JPanel panel2;
    private JLabel tfBankOfBoston;
    private JButton backButton;
    private JButton buyButton;
    private JButton accountTransferButton;
    private JPanel mainPanel;
    private JButton sellButton;
    private JButton closeAccountButton;
    private JTextField sellQuantity;
    private JTextField buyQuantity;
    private JComboBox buyStock;
    private JComboBox receivingAccount;
    private JTable table1;
    private JTable table2;
    private JTextField transferAmount;
    private JComboBox transferFiat;
    private JComboBox sellStock;
    private JLabel buyPrice;
    private JLabel sellPrice;
    private JLabel balance;
    private JLabel realizedGains;
    private JLabel txFee2;
    private JLabel txFee1;
    private JLabel transferFee;
    private JLabel isOpen;
    private String firstname;

    SecuritiesAccountUI(String firstname) {
        setContentPane(mainPanel);
        setTitle("Securities");
        setSize(700,500);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        Client client = BankController.getBankController().getLoggedInClient();
        balance.setText(String.valueOf(client.getSecuritiesAccount().getBalance()));
        realizedGains.setText(String.valueOf(((SecuritiesAccount) client.getSecuritiesAccount()).getRealizedGains()));
        txFee1.setText(String.valueOf(BankController.getBankController().getBank().getTxFee()));
        txFee2.setText(String.valueOf(BankController.getBankController().getBank().getTxFee()));
        transferFee.setText(String.valueOf(BankController.getBankController().getBank().getTransferFee()));
        if (!BankController.getBankController().getStockMarket().isOpen()) {
            isOpen.setText("Contact the banker to update the stock prices. You cannot trade without the system knowing " +
                    "today's price");
        }

        List<String> tickers = new ArrayList<String>();
        for (Stock stock : BankController.getBankController().getStockMarket().getStockPrices().keySet())
            tickers.add(stock.getTicker());
        buyStock.setModel(new DefaultComboBoxModel<String>(tickers.toArray(new String[tickers.size()])));
        sellStock.setModel(new DefaultComboBoxModel<String>(tickers.toArray(new String[tickers.size()])));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                UserDashboardUI ud = new UserDashboardUI(firstname);
            }
        });
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (BankController.getBankController().getStockMarket().isOpen()) {
                    int quantity = Utils.parseInteger(buyQuantity.getText());

                    if (quantity <= 0)
                        JOptionPane.showMessageDialog(null, "Please enter an amount greater than 0");
                    else {
                        Stock selectedStock = Utils.getStockByTicker(buyStock.getSelectedItem().toString());
                        StockPrice stockPrice = BankController.getBankController().getStockMarket().getCurrentStockPrice(selectedStock);
                        client.buyStock(stockPrice, quantity);
                        dispose();
                        SecuritiesAccountUI securitiesAccountUI = new SecuritiesAccountUI(firstname);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Contact the banker to update the " +
                            "stock prices. You cannot trade without the system knowing today's price");
                }
            }
        });
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (BankController.getBankController().getStockMarket().isOpen()) {
                    int quantity = Utils.parseInteger(sellQuantity.getText());

                    if (quantity == 0)
                        JOptionPane.showMessageDialog(null, "Please enter an amount greater than 0");
                    else {
                        Stock selectedStock = Utils.getStockByTicker(sellStock.getSelectedItem().toString());
                        StockPrice stockPrice = BankController.getBankController().getStockMarket().getCurrentStockPrice(selectedStock);
                        client.sellStock(stockPrice, quantity);
                        dispose();
                        SecuritiesAccountUI securitiesAccountUI = new SecuritiesAccountUI(firstname);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Contact the banker to update the " +
                            "stock prices. You cannot trade without the system knowing today's price");
                }
            }
        });
        accountTransferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double amount = Utils.parseDouble(transferAmount.getText());

                if (amount <= 0)
                    JOptionPane.showMessageDialog(null, "Please enter an amount greater than 0");
                else {
                    Account receiving;
                    if (receivingAccount.getSelectedItem().toString() == "Checking") {
                        receiving = client.getCheckingAccount();
                        client.transferBetweenAccounts(client.getSecuritiesAccount(), receiving, amount);
                    } else {
                        receiving = client.getSavingsAccount();
                        client.transferBetweenAccounts(client.getSecuritiesAccount(), receiving, amount);
                    }

                    Database db = BankController.getBankController().getBankDB();

                    db.updateAccount(client.getSecuritiesAccount());
                    db.updateAccount(receiving);
                }
            }
        });
        closeAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (BankController.getBankController().getStockMarket().isOpen() ||
                        ((SecuritiesAccount) BankController.getBankController().
                                getLoggedInClient().getSecuritiesAccount()).getPositions().size() == 0) {
                    client.closeSecuritiesAccount();

                    dispose();
                    UserDashboardUI userDashboardUI = new UserDashboardUI(firstname);
                } else {
                    JOptionPane.showMessageDialog(null, "Contact the banker to update the " +
                            "stock prices. You cannot close the account with open positions and no prices set for the day");
                }
            }
        });
        buyStock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Stock selectedStock = Utils.getStockByTicker(buyStock.getSelectedItem().toString());
                StockPrice stockPrice = BankController.getBankController().getStockMarket().getCurrentStockPrice(selectedStock);

                buyPrice.setText(String.valueOf(stockPrice.getPrice()));
            }
        });
        sellStock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Stock selectedStock = Utils.getStockByTicker(sellStock.getSelectedItem().toString());
                StockPrice stockPrice = BankController.getBankController().getStockMarket().getCurrentStockPrice(selectedStock);

                sellPrice.setText(String.valueOf(stockPrice.getPrice()));
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        Client client = BankController.getBankController().getLoggedInClient();
        Database db = BankController.getBankController().getBankDB();
        //stockMarket JTable
        table1 = new JTable(db.stockMarketFromTableDB());

        setVisible(true);

        //postions JTable
        DefaultTableModel p = db.positionsFromTableDB(client.getId());
        table2 = new JTable(p);
        setVisible(true);

        }
}
