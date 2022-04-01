import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;

//class used for connecting to mysql database and for db commands
public class Database {

    private static Database db;
    private Connection con;

    private Database() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "shweta");
        }
        catch (Exception e){
            System.out.println("Failed to connect to database");
        }

    }

    public Connection getCon() {
        return con;
    }

    public int checkingIfAccountTableIsEmpty(){
        //Checking if account table is empty or not to assign new account id

        int acct_id=0;

        ResultSet rs = null;
        try {

            //checking if acct table is empty or not

            String sql_acct = "select * from accounts";// where userid = '" + client.getId() + "'";
            Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql_acct);

            if (rs.last() == false) {
                acct_id = 1;
            }
            else{
                //rs.last();
                acct_id = rs.getInt("accountid");
                acct_id = acct_id + 1;
            }
        }catch (Exception exception){
            System.out.println(exception);
        }

        return acct_id;


    }

    public DefaultTableModel loanFromTableDB(int uId){
        Database db = new Database();

        ResultSet rs = null;
        Vector<Vector<Object>> data = null;
        Vector<String> columnNames = null;
        try{

            Statement stmt = db.con.createStatement();

            String sql = "select loanid, principal, interestRate, numYears, requestedDate from loan where userid = '"+uId+"' and isOpen = 'true'";
            rs = stmt.executeQuery(sql);

            ResultSetMetaData metaData = rs.getMetaData();

            // names of columns
            columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();

            // data of the table
            data = new Vector<Vector<Object>>();
            while (rs.next()) {
                Vector<Object> vector = new Vector<Object>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }

        }catch (Exception except){
            System.out.println(except);
        }

        String[] columnNames1 = {"Loan ID", "Principal","Interest Rate","Term (Years)", "Requested Date"};
        Collections.addAll(columnNames, columnNames1);
        return new DefaultTableModel(data, columnNames);

    }

    public DefaultTableModel transactionHistoryFromTableDB(int uId){
        Database db = new Database();

        ResultSet rs = null;
        Vector<Vector<Object>> data = null;
        Vector<String> columnNames = null;
        try{

            Statement stmt = db.con.createStatement();

            String sql = "select time_stamp,transaction_log from transaction where userid = '"+uId+"'";
            rs = stmt.executeQuery(sql);

            ResultSetMetaData metaData = rs.getMetaData();

            // names of columns
            columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();

            // data of the table
            data = new Vector<Vector<Object>>();
            while (rs.next()) {
                Vector<Object> vector = new Vector<Object>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }

        }catch (Exception except){
            System.out.println(except);
        }

        String[] columnNames1 = {"Date","Transaction Log"};
        Collections.addAll(columnNames, columnNames1);
        return new DefaultTableModel(data, columnNames);

    }

    //need to check if required to use now otherwise delete
    public DefaultTableModel reportTransactionFromTableDB(String bankerDate){
        Database db = new Database();

        ResultSet rs = null;
        Vector<Vector<Object>> data = null;
        Vector<String> columnNames = null;
        try{

            Statement stmt = db.con.createStatement();

            String sql = "select userid, transaction_log from transaction where time_stamp = '"+bankerDate+"'";
            //String sql = "select userid, transaction_log from transaction where time_stamp = '"+txDate+"'";
            rs = stmt.executeQuery(sql);

            ResultSetMetaData metaData = rs.getMetaData();

            // names of columns
            columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();

            // data of the table
            data = new Vector<Vector<Object>>();
            while (rs.next()) {
                Vector<Object> vector = new Vector<Object>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }

        }catch (Exception except){
            System.out.println(except);
        }

        String[] columnNames1 = {"UserId","Transaction Log"};
        Collections.addAll(columnNames, columnNames1);
        return new DefaultTableModel(data, columnNames);

    }


    public void reportTransactionDownload(String bankerDate){

        String[] headers = {"UserId","Transaction Log"};
        ResultSet rs = null;
        Vector<Vector<Object>> data = null;
        Vector<String> columnNames = null;
        Map<Integer, Object[]> excel_data = new HashMap<Integer, Object[]>();
        int keyId = 0;
        try{

            Statement stmt = db.con.createStatement();

            //String sql = "select userid, transaction_log from transaction where time_stamp = '"+BankController.getBankController().getSystemDate().toString()+"'";
            String sql = "select userid, transaction_log from transaction where time_stamp = '"+bankerDate+"'";

            HSSFWorkbook new_workbook = new HSSFWorkbook(); //create a blank workbook object

            HSSFSheet sheet = new_workbook.createSheet("Transaction History for "+bankerDate);

            //executing the sql statement
            rs = stmt.executeQuery(sql);


            while(rs.next()){

                String userid = rs.getString("userid");
                String transactionLog = rs.getString("transaction_log");

                excel_data.put(keyId++, new Object[] {userid, transactionLog});


            }

            Row r = sheet.createRow(0);
            for (int rn=0; rn<headers.length; rn++) {

                r.createCell(rn).setCellValue(headers[rn]);
            }

            /* Load data into logical worksheet */
            Set<Integer> keyset = excel_data.keySet();
            int rownum = 1;
            for (Integer key : keyset) { //loop through the data and add them to the cell
                Row row = sheet.createRow(rownum++);
                Object [] objArr = excel_data.get(key);
                int cellnum = 0;
                for (Object obj : objArr) {
                    Cell cell = row.createCell(cellnum++);
                    if(obj instanceof Double)
                        cell.setCellValue((Double)obj);
                    else
                        cell.setCellValue((String)obj);
                }
            }

            FileOutputStream output_file = new FileOutputStream(new File(".\\transactionHistory"+bankerDate+".xls")); //create XLS file
            new_workbook.write(output_file);//write excel document to output stream
            output_file.close(); //close the file

        }catch (Exception except){
            System.out.println(except);
        }

    }

    public DefaultTableModel positionsFromTableDB(int clientId){

        boolean resultSet_output =  false;
        Client client = BankController.getBankController().getLoggedInClient();

        if(client == null) {
            ResultSet rs_userobj = null;
            Account securitiesAccount = null;
            //retrieving user account details where userid = clientId
            try {
                String sql_checking = "select * from accounts where userid = '" + clientId + "' and isOpen = 'true' and type='Securities'";
                Statement stmt = db.getCon().createStatement();
                rs_userobj = stmt.executeQuery(sql_checking);

                while(rs_userobj.next()){
                    securitiesAccount = new SecuritiesAccount(Double.parseDouble(rs_userobj.getString("balance")),
                            Integer.parseInt(rs_userobj.getString("accountid")), Double.parseDouble(rs_userobj.getString("realizedGains")));
                }

            }catch (Exception exception){
                System.out.println("hi "+exception);
            }

            client = new Client("dummy", "dummy", LocalDate.parse("2021-12-08"), 0, new NullAccount(), new NullAccount(), securitiesAccount, null, null);
        }
        ResultSet rs = null;
        Vector<Vector<Object>> data = null;
        Vector<String> columnNames = null;

        try{

            Statement stmt = db.con.createStatement();

            String sql  ="select openDate, ticker, quantity, averageCost from positions where userid = '"+clientId+"' and isOpen = 'true'";
            rs = stmt.executeQuery(sql);

            ResultSetMetaData metaData = rs.getMetaData();

            // names of columns
            columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();

            // data of the table
            data = new Vector<Vector<Object>>();

            SecuritiesAccount securitiesAccount = (SecuritiesAccount) client.getSecuritiesAccount();

            for (Position position : securitiesAccount.getPositions().values()) {
                Vector<Object> vector = new Vector<Object>();
                vector.add(position.getOpenDate());
                vector.add(position.getStock().getTicker());
                vector.add(position.getQuantity());
                vector.add(position.getAvgCost());
                vector.add(position.getAvgCost()*position.getQuantity());

                StockPrice todayPrice = StockMarket.getStockMarket().getCurrentStockPrice(position.getStock());
                vector.add(todayPrice.getPrice());
                vector.add(todayPrice.getPrice() * position.getQuantity());

                vector.add(position.getUnRealizedGains());

                data.add(vector);
            }



        }catch (Exception except){
            System.out.println(except);
        }

        String[] columnNames1 = {"Open Date","Ticker","Quantity","Average Cost","Original Value","Current Price","Current Value","Unrealized Gains"};

        Collections.addAll(columnNames, columnNames1);
        return new DefaultTableModel(data, columnNames);

    }

    public DefaultTableModel stockMarketFromTableDB(){
        Database db = new Database();
        boolean resultSet_output =  false;

        ResultSet rs = null;
        Vector<Vector<Object>> data = null;
        Vector<String> columnNames = null;

        try{

            Statement stmt = db.con.createStatement();

            String sql  ="select  stockPriceDate, st.ticker, company_name, price from stock as st, stockPrice as sp where st.ticker = sp.ticker and stockPriceDate = '"+BankController.getBankController().getSystemDate().toString()+"'";
            rs = stmt.executeQuery(sql);

            ResultSetMetaData metaData = rs.getMetaData();

            // names of columns
            columnNames = new Vector<String>();
            int columnCount = metaData.getColumnCount();

            // data of the table
            data = new Vector<Vector<Object>>();
            while (rs.next()) {
                Vector<Object> vector = new Vector<Object>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }

        }catch (Exception except){
            System.out.println(except);
        }

        String[] columnNames1 = {"Date", "Ticker","Company Name","Price"};
        Collections.addAll(columnNames, columnNames1);
        return new DefaultTableModel(data, columnNames);

    }

    public void updateUserLastLoggedInDate(){

            try {
                Statement stmt = con.createStatement();

                String sql = "update user_table set lastLoggedInDate = '" + BankController.getBankController().getLoggedInClient().getLastLoggedInDate().toString() + "' where userid = '"+BankController.getBankController().getLoggedInClient().getId()+"'";

                stmt.executeUpdate(sql);
            }catch (Exception exception){
                System.out.println(exception);
            }
    }

    public void updateAccount(Account account){
        try{
            PreparedStatement pstmt = con.prepareStatement("update accounts set balance = ?, isOpen = ?, realizedGains = ? where accountid = '"+account.getId()+"'");
            pstmt.setString(1, String.valueOf(account.getBalance()));
            if (!account.isOpen())
                pstmt.setString(2, "false");
            else
                pstmt.setString(2, "true");
            if(account instanceof SecuritiesAccount)
                pstmt.setString(3, String.valueOf(((SecuritiesAccount) account).getRealizedGains()));
            else
                pstmt.setString(3, "0");

            pstmt.executeUpdate();


        }catch (Exception except){
            System.out.println(except);
        }
    }

    public void updatePositions(String ticker, int id, boolean isOpen, Position currentPosition){
        try{
            PreparedStatement pstmt = con.prepareStatement("update positions set averageCost = ?, quantity = ?, isOpen = ? where ticker = '"+ticker+"' and accountid = '"+id+"' and isOpen = 'true'");
            pstmt.setString(1, String.valueOf(currentPosition.getAvgCost()));
            pstmt.setString(2, String.valueOf(currentPosition.getQuantity()));
            System.out.println("hdfs "+isOpen);
            if (!isOpen)
                pstmt.setString(3, "false");
            else
                pstmt.setString(3, "true");

            pstmt.executeUpdate();

        }catch (Exception except){
            System.out.println(except);
        }
    }

    public void insertPositions(int id, Position currentPosition){

        try{
            PreparedStatement pstmt = con.prepareStatement("insert into positions values (?, ?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, String.valueOf(BankController.getBankController().getLoggedInClient().getId()));
            pstmt.setString(2, "true");
            pstmt.setString(3, currentPosition.getOpenDate().toString());
            pstmt.setString(4, currentPosition.getStock().getTicker());
            pstmt.setString(5, String.valueOf(currentPosition.getQuantity()));
            pstmt.setString(6, String.valueOf(currentPosition.getAvgCost()));
            pstmt.setString(7, String.valueOf(id));

            pstmt.executeUpdate();

        }catch (Exception except){
            System.out.println(except);
        }
    }

    public void closeLoan(Loan loan) {
        try{

            PreparedStatement pstmt = con.prepareStatement("update loan set isOpen = ? where loanid = '"+loan.getId()+"'");
            pstmt.setString(1, "false");

            pstmt.executeUpdate();

        }catch (Exception except){
            System.out.println(except);
        }
    }

    public void updateCashReserves() {
        Bank bank = BankController.getBankController().getBank();

        try{
            PreparedStatement pstmt = con.prepareStatement("update bank set cashReserves = ? ");
            pstmt.setString(1, String.valueOf(bank.getCashReserves()));

            pstmt.executeUpdate();

        }catch (Exception exception){
            System.out.println(exception);
        }

    }

    public List<Stock> getStocksFromDB() {
        List<Stock> stocks = new ArrayList<Stock>();

        ResultSet rs;
        try{
            Statement stmt = db.getCon().createStatement();
            String sql  ="select * from stock";

            rs =stmt.executeQuery(sql);

            while(rs.next()){
                stocks.add(new Stock(rs.getString("ticker"), rs.getString("company_name")));
            }

        }catch (Exception except){
            System.out.println(except);
        }

        return stocks;
    }

    public Stock getStockFromDB(String ticker) {
        ResultSet rs;
        try{
            Statement stmt = db.getCon().createStatement();
            String sql  = "select * from stock where ticker = '" + ticker + "'";

            rs =stmt.executeQuery(sql);

            while(rs.next()){
                return new Stock(rs.getString("ticker"), rs.getString("company_name"));
            }

        }catch (Exception except){
            System.out.println(except);
        }

        return null;
    }

    public void updateStockPrice(String ticker, double price){

        try{
            PreparedStatement pstmt = con.prepareStatement("update stockPrice set price = ? where ticker = '"+ticker+"' and stockPriceDate = '"+BankController.getBankController().getSystemDate().toString()+"'");
            pstmt.setString(1, String.valueOf(price));

            pstmt.executeUpdate();

        }catch (Exception except){
            System.out.println(except);
        }
    }

    public void insertIntoStockPrice(StockPrice stockPrice){

        try{
            PreparedStatement pstmt = con.prepareStatement("insert into stockPrice (ticker, price, stockPriceDate) values (?, ?, ?)");
            pstmt.setString(1, stockPrice.getStock().getTicker());
            pstmt.setString(2, String.valueOf(stockPrice.getPrice()));
            pstmt.setString(3, BankController.getBankController().getSystemDate().toString());

            pstmt.executeUpdate();

        }catch (Exception except){
            System.out.println(except);
        }
    }

    public static Database getDB() {
        if (db == null) {
            db = new Database();
        }
        return db;
    }
}