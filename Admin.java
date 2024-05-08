import java.sql.*;
import java.util.Scanner;

public class Admin extends User {

    Database database = new Database();
    Plane flight = new Plane();
    Passenger passenger = new Passenger();
    Scanner scanner = new Scanner(System.in);

    @Override
    public void showAppTitle() {
        clearScreen();
        printCentered("\n");
        printCentered("╔══════════════════════════════════════════════════════╗");
        printCentered("║            Welcome to Skypass Admin Portal           ║");
        printCentered("╚══════════════════════════════════════════════════════╝");
        // show user name and role
        printCentered("\tLogged in as: " + yellow + userFirstName + " " + userLastName + reset);
        showDisplayMessage();
    }

    private boolean isAdmin(String username) throws Exception {
        ResultSet resultSet = database.databaseQuery("select role from users where username = ?;", username);
        if (resultSet.next()) {
            if (resultSet.getString("role").equals("admin")) {
                return true;
            }
        }
        return false;
    }

    // function to add a new admin user
    private void addAdmin() throws Exception {
        clearScreen();
        showAppTitle();
        String username;
        System.out.print("\t\t\t\tEnter the username of the new admin: ");
        username = scanner.nextLine();
        // Check if the username already exists
        if (checkUsername(username)) {
            // If the user is already an admin
            if (isAdmin(username)) {
                setDisplayMessage(red + "\t     " + username + " is already an admin" + reset);
            } else {
                // If the user is not an admin, ask if they should be made an admin
                System.out.print(
                        "\t\t\t" + username + " is already a user. Do you want to make them an admin? (y/n): ");
                String choice = scanner.nextLine();
                if (choice.equals("y")) {
                    // Update the user's role in the database to 'admin'
                    database.databaseQuery("update users set role = 'admin' where username = ?';", username);
                    setDisplayMessage(green + "\t" + username + " is now an admin" + reset);
                }
            }
        } else {
            // If the username does not exist, register a new admin user
            registerUser("admin");
        }
    }

    public void showUsers() throws Exception {
        ResultSet resultSet = database.databaseQuery("select * from users;");
        // show user details in table
        System.out.println("\t\t\t\t" + yellow + "ID\t\tUsername\t\tFirst Name\t\tLast Name\t\tRole" + reset);
        while (resultSet.next()) {
            System.out.println("\t\t\t\t" + resultSet.getInt("id") + "\t\t" + resultSet.getString("username") + "\t\t"
                    + resultSet.getString("firstname") + "\t\t" + resultSet.getString("lastname") + "\t\t"
                    + resultSet.getString("role"));
        }
    }

    public void adminMenu() throws Exception {
        int choice;
        do {
            showAppTitle();
            showDisplayMessage();
            System.out.println("""
                    \t\t\t\t╔══════════════════════════════════════════════════════╗
                    \t\t\t\t║  1. Show Plane Details                               ║
                    \t\t\t\t╟──────────────────────────────────────────────────────╢
                    \t\t\t\t║  2. Add or Edit the plane details                    ║
                    \t\t\t\t╟──────────────────────────────────────────────────────╢
                    \t\t\t\t║  3. Add an administrator                             ║
                    \t\t\t\t╟──────────────────────────────────────────────────────╢
                    \t\t\t\t║  4. Show user details                                ║
                    \t\t\t\t╟──────────────────────────────────────────────────────╢
                    \t\t\t\t║  5. Change to Passenger Mode                         ║
                    \t\t\t\t╟──────────────────────────────────────────────────────╢
                    \t\t\t\t║  6. Logout                                           ║
                    \t\t\t\t╚══════════════════════════════════════════════════════╝
                            """);
            System.out.print("\t\t\t\tEnter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    clearScreen();
                    showAppTitle();
                    flight.showPlaneDetails(database.databaseQuery("select * from planes;"));
                    System.out.print("Press enter to continue...");
                    scanner.nextLine();
                    scanner.nextLine();
                    break;
                case 2:
                    // clearScreen();
                    // showAppTitle();
                    // flight.addEditPlaneDetails();
                    // break;
                case 3:
                    clearScreen();
                    showAppTitle();
                    addAdmin();
                    break;
                case 4:
                    clearScreen();
                    showAppTitle();
                    showUsers();
                    System.out.print("Press enter to continue...");
                    scanner.nextLine();
                    scanner.nextLine();
                    break;
                case 5:
                    clearScreen();
                    showAppTitle();
                    passenger.passengerMenu();
                    break;
                case 6:
                    clearScreen();
                    showAppTitle();
                    setDisplayMessage(green + "\t\t\t\tLogged out successfully" + reset);
                    break;
                default:
                    setDisplayMessage(red + "\t\t\t\tInvalid choice. Please try again" + reset);
            }
        } while (choice != 4);

    }

}