import java.io.Console;
import java.sql.*;
import java.util.Scanner;

public class User extends Start {

    public static String username;
    public static String userFirstName;
    public static String userLastName;
    public static String userContactNumber;
    public String password;
    public String role;
    public int numberOfSeats;
    public static int userId;

    private Database database = new Database();
    private Start start = new Start();
    private Scanner scanner = new Scanner(System.in);

    // function to handle user login
    public boolean userLogin(String role) throws Exception {
        Console console = System.console();
        // Loop until a valid username is entered
        do {
            start.showAppTitle();
            showDisplayMessage();
            System.out.print("\n\t\t\t\tEnter your username: ");
            username = scanner.nextLine();

            // If the username is not valid, display an error message
            if (!checkUsername(username)) {
                setDisplayMessage(red + "\t!!OOPS! The username is not correct!!" + reset);
            }
        } while (!checkUsername(username));
        System.out.print("\t\t\t\tEnter your password: ");
        char[] passwordArray = console.readPassword();
        password = new String(passwordArray);
        java.util.Arrays.fill(passwordArray, ' ');

        // If the user is authenticated successfully
        if (authenticateUser(username, password, role)) {
            // Query the database for the user's details
            ResultSet user = database.databaseQuery("select * from users where username = ?;", username);
            user.next();

            // Store the user's details in local variables
            userId = user.getInt("id");
            userFirstName = user.getString("firstname");
            userLastName = user.getString("lastname");

            // Return true to indicate successful login
            return true;
        } else {
            // If the authentication failed, display an error message and return false
            setDisplayMessage(red + "\tERROR! The username or password is incorrect" + reset);
            return false;
        }
    }

    // function to register a new user
    public void registerUser(String role) throws Exception {
        // Loop until a unique username is entered
        do {
            start.showAppTitle();
            showDisplayMessage();
            System.out.print("\n\t\t\t\tEnter a username: ");
            username = scanner.nextLine();

            // If the username is already taken, display an error message
            if (checkUsername(username)) {
                setDisplayMessage(red + "\t!!OOPS! The username is already taken!!" + reset);
            }
        } while (checkUsername(username));
        System.out.print("\t\t\t\tEnter your password: ");
        password = scanner.nextLine();
        System.out.print("\t\t\t\tEnter your first name: ");
        userFirstName = scanner.nextLine();
        System.out.print("\t\t\t\tEnter your last name: ");
        userLastName = scanner.nextLine();
        System.out.print("\t\t\t\tEnter your contact number: ");
        userContactNumber = scanner.nextLine();

        // Insert the new user's details into the database
        database.databaseQuery(
                "insert into users (username, password, firstname, lastname, role,phone_no) values (?,?,?,?,?,?);",
                username, password, userFirstName, userLastName, role, userContactNumber);

        // Display a success message
        setDisplayMessage(green + "\tUser registered successfully" + reset);
    }

    public boolean authenticateUser(String username, String password, String role) throws Exception {
        ResultSet user = database
                .databaseQuery("SELECT * FROM users WHERE username = ? AND password = ? AND role = ?;", username,
                        password, role);
        if (user.next()) {
            userId = user.getInt("id");
            return true;
        }
        return false;
    }

    public boolean checkUsername(String username) throws Exception {
        ResultSet user = database.databaseQuery("SELECT * FROM users WHERE username = ?;", username);
        if (user.next()) {
            return true;
        }
        return false;
    }
}
