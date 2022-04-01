import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * helper class with lots helper functions
 */
public class Utils {

    // create a scanner object to be used for the entire application
    public static final Scanner scan = new Scanner(System.in);
    public static final Random rand = new Random();

    // helper function that returns an input integer from the user given a lower and upper bound
    // a string is also taken in so that print messages are more descriptive to the user for what the desired input
    public static int getIntInput(String str, int lowerBound, int upperBound) {
        boolean validInput = false;
        System.out.println(String.format("Enter %s :", str));
        while (!validInput) {
            // make sure input from user is integer. if it is and within bounds return it. if it is an int and not within
            // bounds, ask for another input int
            try {
                int input = scan.nextInt();
                if (input >= lowerBound && input <= upperBound) {
                    return input;
                }
                System.out.println(String.format("Please enter a valid integer for the %s, specifically an " +
                        "integer between %d and %d.", str, lowerBound, upperBound));
            }
            // catch error if user inputted something besides an int
            catch (java.util.InputMismatchException e) {
                System.out.println(String.format("Please make sure to enter an integer between %d and %d! No other " +
                        "inputs are allowed!", lowerBound, upperBound));
                scan.nextLine();
            }
        }
        return 0;
    }

    // helper function that can be used to ask the user for either an X or an O and nothing else. this is used for assigning
    // players their symbol and for order and chaos, letting each player choose which symbol for each turn
    public static String getValidInputString(String[] possibleInputs) {
        boolean validInput = false;
        String inputStr = "";
        while (!validInput) {
            System.out.print("Input: ");
            inputStr = scan.next();
            if (Arrays.asList(possibleInputs).contains(inputStr)) {
                break;
            } else {
                System.out.println(String.format("%s is not a valid input. Valid inputs are ", inputStr) + Arrays.toString(possibleInputs));
            }
            scan.nextLine();
        }
        return inputStr;
    }
    public static LocalDate parseDate(String s) {

        try {
            return LocalDate.parse(s);
        } catch (Exception except) {
            System.out.println(except);
            return null;
        }

    }
    public static Fiat parseFiat(String str) {
        switch (str) {
            case "EURO":
                return Euro.getEuro();
            case "CAD":
                return CanadianDollar.getCanadianDollar();
            default:
                return USDollar.getUSD();
        }
    }

    public static int getDateDifference(LocalDate date) {
        return (int) ChronoUnit.MONTHS.between(date, BankController.getBankController().getSystemDate());
    }

    public static double roundDouble(double d) {
        return Math.round(d * 100.0) / 100.0;
    }

    public static void insertIntoTransactionTable(int id, String log){
        Database db = BankController.getBankController().getBankDB();
        try{
            //String sql = "insert into transaction (userid, transaction_log) values (?,?)";
            String sql = "insert into transaction values (?,?,?)";

            PreparedStatement preparedStatement = db.getCon().prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(BankController.getBankController().getSystemDate().toString()));
            preparedStatement.setString(2, String.valueOf(id));
            preparedStatement.setString(3, log);

            preparedStatement.executeUpdate();

        }catch (Exception except){
            System.out.println(except);
        }
    }

    public static Stock getStockByTicker(String ticker) {
        Database db = BankController.getBankController().getBankDB();
        ResultSet rs;
        try{
            Statement stmt = db.getCon().createStatement();
            String sql  ="select * from stock where ticker = '"+ticker+"' ";

            rs =stmt.executeQuery(sql);

            Stock stock = null;

            while (rs.next()) {
                stock = new Stock(rs.getString("ticker"), rs.getString("company_name"));
            }

            return stock;

        }catch (Exception except){
            System.out.println(except);

            return null;
        }
    }

    public static double parseDouble(String s) {

        try {
            return Double.parseDouble(s);
        } catch (Exception except) {
            System.out.println(except);
            return 0;
        }

    }

    public static int parseInteger(String s) {

        try {
            return Integer.parseInt(s);
        } catch (Exception except) {
            System.out.println(except);
            return 0;
        }

    }
}
