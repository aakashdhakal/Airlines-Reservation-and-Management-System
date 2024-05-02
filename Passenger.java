import java.io.Console;
import java.util.Scanner;

public class Passenger extends User {

    Plane flight = new Plane();
    Database database = new Database();
    Scanner scanner = new Scanner(System.in);

    public void passengerLogin() throws Exception {

        Console console = System.console();
        do {
            showAppTitle();
            showDisplayMessage();
            System.out.print("\t\t\t   Enter your username: ");
            if (!checkUsername(username)) {
                setDisplayMessage("Invalid username");
            }
            username = console.readLine();
        } while (!checkUsername(username));
        System.out.print("\t\t\t   Enter your password: ");
        char[] passwordArray = console.readPassword();
        password = new String(passwordArray);
        java.util.Arrays.fill(passwordArray, ' ');
        role = "passenger";

        if (authenticateUser(username, password, role)) {
            setDisplayMessage("You are logged in as " + username);
            userId = getUserId(username);
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
                    \t\t\t +--------------------------------------------------------------------+
                    \t\t\t |                                                                    |
                    \t\t\t |                        1. Reserve a seat                           |
                    \t\t\t |                                                                    |
                    \t\t\t |                        2. Show tickets                             |
                    \t\t\t |                                                                    |
                    \t\t\t |                        3. Cancel reservation                       |
                    \t\t\t |                                                                    |
                    \t\t\t |                        4. Exit                                     |
                    \t\t\t |                                                                    |
                    \t\t\t +--------------------------------------------------------------------+
                    """);
            System.out.print("\t\t\tEnter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    reserveSeat();
                    break;
                case 2:
                    showTickets(database.databaseGet("select * from reservations where user_id = " + userId + ";"));
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
