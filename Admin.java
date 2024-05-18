import java.sql.*;
import java.util.Scanner;

public class Admin extends User {

    private static Plane flight = new Plane();
    private static Scanner scanner = new Scanner(System.in);

    public static void showAppTitle() {
        clearScreen();
        printCentered("\n");
        printCentered("╔══════════════════════════════════════════════════════╗");
        printCentered("║            Welcome to Skypass Admin Portal           ║");
        printCentered("╚══════════════════════════════════════════════════════╝");
        // show user name and role
        printCentered("\tLogged in as: " + yellow + userFirstName + " " + userLastName + reset);
        showDisplayMessage();
    }

    private static boolean isAdmin(String username) throws Exception {
        ResultSet resultSet = Database.databaseQuery("select role from users where username = ?;", username);
        if (resultSet.next()) {
            if (resultSet.getString("role").equals("admin")) {
                return true;
            }
        }
        return false;
    }

    // function to add a new admin user
    private static void addAdmin() throws Exception {
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
                    Database.databaseQuery("update users set role = 'admin' where username = ?;", username);
                    setDisplayMessage(green + "\t" + username + " is now an admin" + reset);
                }
            }
        } else {
            // If the username does not exist, register a new admin user
            registerUser("admin");
        }
    }

    private static void showUsers() throws Exception {
        ResultSet resultSet = Database.databaseQuery("select * from users;");
        String format = "║    %s    │   %-15s        │      %-15s       │        %-15s      │     %-10s ║";
        // show user details in table
        System.out.print(
                """
                        ╔═══════════════╤══════════════════════════╤════════════════════════════╤═════════════════════════════╤════════════════╗
                        ║       ID      │         Username         │            Name            │        Phone Number         │      Role      ║
                        ╠═══════════════╪══════════════════════════╪════════════════════════════╪═════════════════════════════╪════════════════╣
                        """);
        while (resultSet.next()) {
            System.out.printf(format, resultSet.getString("id"), resultSet.getString("username"),
                    resultSet.getString("firstname") + " " + resultSet.getString("lastname"),
                    resultSet.getString("phone_no"), resultSet.getString("role"));
            if (!resultSet.isLast()) {
                System.out
                        .print("""
                                ╟───────────────┼──────────────────────────┼────────────────────────────┼─────────────────────────────┼────────────────╢
                                """);
            }
        }

        System.out.print(
                """
                        ╙───────────────┴──────────────────────────┴────────────────────────────┴─────────────────────────────┴────────────────╜
                        """);

        resultSet.close();
    }

    private static void removeAdmin() throws Exception {
        clearScreen();
        showAppTitle();
        String username;
        System.out.print("\t\t\t\tEnter the username of the admin to remove: ");
        username = scanner.nextLine();
        // Check if the user is an admin
        if (isAdmin(username)) {
            // If the user is an admin, remove them
            Database.databaseQuery("update users set role = 'passenger' where username = ?;", username);
            setDisplayMessage(green + "\t " + username + " is no longer an admin" + reset);
        } else {
            // If the user is not an admin
            setDisplayMessage(red + "\t " + username + " is not an admin" + reset);
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
                    \t\t\t\t║  2. Add a plane                                      ║
                    \t\t\t\t╟──────────────────────────────────────────────────────╢
                    \t\t\t\t║  3. Edit plane details                               ║
                    \t\t\t\t╟──────────────────────────────────────────────────────╢
                    \t\t\t\t║  4. Add an administrator                             ║
                    \t\t\t\t╟──────────────────────────────────────────────────────╢
                    \t\t\t\t║  5. Remove an administrator                          ║
                    \t\t\t\t╟──────────────────────────────────────────────────────╢
                    \t\t\t\t║  6. Show user details                                ║
                    \t\t\t\t╟──────────────────────────────────────────────────────╢
                    \t\t\t\t║  7. Logout                                           ║
                    \t\t\t\t╚══════════════════════════════════════════════════════╝
                            """);
            System.out.print("\t\t\t\tEnter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    showAppTitle();
                    flight.showPlaneDetails("all");
                    System.out.print("Press enter to continue...");
                    scanner.nextLine();
                    break;
                case 2:
                    showAppTitle();
                    flight.addPlane();
                    break;
                case 4:
                    showAppTitle();
                    addAdmin();
                    break;

                case 3:
                    showAppTitle();
                    System.out.print("\t\t\t\tEnter Plane ID to edit: ");
                    int id = scanner.nextInt();
                    showAppTitle();
                    flight.editPlaneDetails(id);
                    break;

                case 5:
                    showAppTitle();
                    removeAdmin();
                    break;

                case 6:
                    showAppTitle();
                    showUsers();
                    System.out.print("Press enter to continue...");
                    scanner.nextLine();
                    break;
                case 7:
                    setDisplayMessage(green + "\t  Logged out successfully" + reset);
                    return;
                default:
                    setDisplayMessage(red + "\t  Invalid choice. Please try again" + reset);
            }
        } while (choice != 7);

    }

}