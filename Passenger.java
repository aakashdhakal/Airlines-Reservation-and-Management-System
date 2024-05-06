import java.util.Scanner;

public class Passenger extends User {

    Plane flight = new Plane();
    Database database = new Database();
    Scanner scanner = new Scanner(System.in);
    Start start = new Start();

    @Override
    public void showAppTitle() {
        clearScreen();
        printCentered("\n");
        printCentered("╔══════════════════════════════════════════════════════╗");
        printCentered("║          Welcome to Skypass Passenger Portal         ║");
        printCentered("╚══════════════════════════════════════════════════════╝");
        // show user name and role
        printCentered("\tLogged in as: " + yellow + userFirstName + " " + userLastName + reset);
        showDisplayMessage();
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
                    \t\t\t\t║  4. Logout                                           ║
                    \t\t\t\t╚══════════════════════════════════════════════════════╝
                            """);
            System.out.print("\t\t\t\tEnter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    showAppTitle();
                    reserveSeat();
                    break;
                case 2:
                    showTickets(database.databaseQuery("select * from reservations where user_id = ?;", userId));
                    break;
                case 3:
                    // cancelReservation();
                    break;
                case 4:
                    start.showStartMenu();
                    break;
                default:
                    setDisplayMessage(red + "\t    ERROR ! Please enter valid option !" + reset);

            }
        } while (choice != 4);
        scanner.close();
    }

}
