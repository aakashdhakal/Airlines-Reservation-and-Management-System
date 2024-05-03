import java.io.Console;
import java.sql.ResultSet;
import java.util.Scanner;

public class Admin extends User {

    Database database = new Database();

    public void adminLogin() throws Exception {
        Scanner scanner = new Scanner(System.in);
        showAppTitle();
        Console console = System.console();

        System.out.print("\t\t\t   Enter your username: ");
        username = console.readLine();

        System.out.print("\t\t\t   Enter your password: ");
        char[] passwordArray = console.readPassword();
        password = new String(passwordArray);
        java.util.Arrays.fill(passwordArray, ' ');
        role = "admin";

        if (authenticateUser(username, password, role)) {
            System.out.println("Login successful");
            ResultSet user = database.databaseQuery("select * from users where username = '" + username + "';");
            user.next();
            userId = user.getInt("id");
            userFirstName = user.getString("first_name");
            userLastName = user.getString("last_name");
        } else {
            System.out.println("Login failed");
        }
        scanner.nextLine();
        scanner.close();
    }

}
