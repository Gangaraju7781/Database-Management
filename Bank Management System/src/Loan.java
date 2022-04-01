import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
/*
Contains data members related to the loans which a client requested.
 */
public class Loan{

    private int id;
    private double interestRate;
    private double principal;
    private int numYears;
    private int numPeriodsPerYear;
    private LocalDate requestedDate;

    public Loan(double principal, int id) {
        this(principal, Bank.getBank().getLoanInterestRate(), BankController.getBankController().getSystemDate(), id);
    }

    public Loan(double principal, int numYears, LocalDate date, int id) {
        this(principal, Bank.getBank().getLoanInterestRate(), numYears, date, id);
    }

    public Loan(double principal, double interestRate, LocalDate date, int id) {
        this(principal, interestRate, 1, date, id);
    }

    public Loan(double principal, double interestRate, int numYears, LocalDate date, int id) {
        this(principal, interestRate, numYears, 12, date, id);
    }

    public Loan(double principal, double interestRate, int numYears, int numPeriodsPerYear, LocalDate date, int id) {
        this.principal = principal;
        this.interestRate = interestRate;
        this.numYears = numYears;
        this.numPeriodsPerYear = numPeriodsPerYear;
        this.requestedDate = date;
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public double getInterestDue(LocalDate lastPaymentDate) {

        return Utils.getDateDifference(lastPaymentDate) * getInterestPayment();
    }


    public double getPrincipal() {
        return principal;
    }

    public LocalDate getRequestedDate() {
        return requestedDate;
    }


    public double getInterestRate() {
        return interestRate;
    }

    public double getInitialPrincipal() {
        return principal;
    }

    public int getNumYears() {
        return numYears;
    }

    public int getNumPeriodsPerYear() {
        return numPeriodsPerYear;
    }

    public double getInterestPayment() {
        return (interestRate * principal / numPeriodsPerYear) /
                (1 - Math.pow((interestRate / numPeriodsPerYear + 1), (-numPeriodsPerYear * numYears)));
    }

    public int getPeriodsRemaining() {
        return numYears * numPeriodsPerYear - getPeriodsPassed();
    }

    public int getPeriodsPassed() {
        return Utils.getDateDifference(requestedDate);
    }

    public double getPrincipalBalance() {
        double futureValueOfOriginalBalance = principal * Math.pow((1 + interestRate / numPeriodsPerYear), getPeriodsPassed());
        double futureValueOfAnnuity = (getInterestPayment() * (Math.pow((1 + interestRate / numPeriodsPerYear), getPeriodsPassed()) - 1))
                / (interestRate / numPeriodsPerYear);
        return Utils.roundDouble(futureValueOfOriginalBalance - futureValueOfAnnuity);

    }
}
