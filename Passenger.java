import java.io.Console;
import java.util.Scanner;
import java.sql.*;

public class Passenger extends User {

    Plane flight = new Plane();
    Database database = new Database();
    Scanner scanner = new Scanner(System.in);
    Start start = new Start();

    @Override
    public void showAppTitle() {
        clearScreen();
        System.out.println("Hello" + username);
        vline(120, '─');

    }

    public void passengerLogin() throws Exception {

        Console console = System.console();
        do {
            start.showAppTitle();
            showDisplayMessage();
            System.out.print("\t\t\t\tEnter your username: ");
            if (!checkUsername(username)) {
                setDisplayMessage("!!OOPS! The username is not correct!!");
            }
            username = scanner.nextLine();
        } while (!checkUsername(username));
        System.out.print("\t\t\t\tEnter your password: ");
        char[] passwordArray = console.readPassword();
        password = new String(passwordArray);
        java.util.Arrays.fill(passwordArray, ' ');
        role = "passenger";

        if (authenticateUser(username, password, role)) {
            ResultSet user = database.databaseQuery("select * from users where username = '" + username + "';");
            user.next();
            userId = user.getInt("id");
            userFirstName = user.getString("firstname");
            userLastName = user.getString("lastname");

        } else {
            setDisplayMessage("Login failed");
            return;
        }
        passengerMenu();
    }

    public void registerPassenger() throws Exception {

    }

    public void passengerMenu() throws Exception {
        int choice;
        do {
            showAppTitle();
            showDisplayMessage();
            System.out.println("""
                    \t\t\t\t╔══════════════════════════════════════════════════════╗
                    \t\t\t\t║  1. Add a reservation                                ║
                    \t\t\t\t╟──────────────────────────────────────────────────────╢
                    \t\t\t\t║  2. Cancel a reservation                             ║
                    \t\t\t\t╟──────────────────────────────────────────────────────╢
                    \t\t\t\t║  3. Show your reservations                           ║
                    \t\t\t\t╟──────────────────────────────────────────────────────╢
                    \t\t\t\t║  4. Exit                                             ║
                    \t\t\t\t╚══════════════════════════════════════════════════════╝
                            """);
            System.out.print("\t\t\tEnter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    reserveSeat();
                    break;
                case 2:
                    showTickets(database.databaseQuery("select * from reservations where user_id = " + userId + ";"));
                    break;
                case 3:
                    // cancelReservation();
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while (choice != 4);
        scanner.close();

    }

}
