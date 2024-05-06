import java.sql.*;

public class Admin extends User {

    Database database = new Database();
    Plane flight = new Plane();
    Passenger passenger = new Passenger();

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

    public void adminMenu() throws Exception {
        int choice;
        Admin admin = new Admin();
        do {
            showAppTitle();
            showDisplayMessage();
            System.out.println("""
                    \t\t\t\t╔══════════════════════════════════════════════════════╗
                    \t\t\t\t║  1. Show Plane Details                               ║
                    \t\t\t\t╟──────────────────────────────────────────────────────╢
                    \t\t\t\t║  2. Add an administrator                             ║
                    \t\t\t\t╟──────────────────────────────────────────────────────╢
                    \t\t\t\t║  3. Show your reservations                           ║
                    \t\t\t\t╟──────────────────────────────────────────────────────╢
                    \t\t\t\t║  4. Exit                                             ║
                    \t\t\t\t╚══════════════════════════════════════════════════════╝
                            """);
            System.out.print("\t\t\t\tEnter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    flight.showPlaneDetails(database.databaseQuery("select * from planes;"));
                    System.out.println("\n\nPress enter to continue...");
                    scanner.nextLine();
                    break;
                case 2:
                    admin.addAdmin();
                    break;
                case 3:
                    // cancelReservation();
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    setDisplayMessage(red + "\t    ERROR ! Please enter valid option !" + reset);
            }
        } while (choice != 4);
        scanner.close();

    }

}
