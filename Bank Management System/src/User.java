import java.time.LocalDate;
/*
User is the superclass of both Banker and Client and contains data members that are needed
across both types of users, like username and password.
 */
public class User {

    private String username;
    private String password;
    private LocalDate lastLoggedInDate;
    private int id;

    public User(String username, String password, LocalDate lastLoggedInDate, int id) {
        this.username = username;
        this.password = password;
        this.lastLoggedInDate = lastLoggedInDate;
        this.id = id;
    }

    public LocalDate getLastLoggedInDate() {
        return lastLoggedInDate;
    }

    public void setLastLoggedInDate(LocalDate lastLoggedInDate) {
        this.lastLoggedInDate = lastLoggedInDate;
    }

    public int getId() {
        return id;
    }
}
