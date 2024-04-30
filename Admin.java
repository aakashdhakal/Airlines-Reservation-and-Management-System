import java.io.Console;
import java.util.Scanner;

public class Admin extends User {

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
            userId = getUserId(username);
        } else {
            System.out.println("Login failed");
        }
        scanner.nextLine();
        scanner.close();
    }

}
