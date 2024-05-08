import java.util.Scanner;

public class Passenger extends User {

    Plane flight = new Plane();
    Database database = new Database();
    Start start = new Start();
    Scanner scanner = new Scanner(System.in);

    @Override
    public void showAppTitle() {
        clearScreen();
        printCentered("\n");
        printCentered("╔══════════════════════════════════════════════════════╗");
        printCentered("║          Welcome to Skypass Passenger Portal         ║");
        printCentered("╚══════════════════════════════════════════════════════╝");
        // show user name and role
        printCentered(
                "\tLogged in as: " + yellow + userFirstName + " " + userLastName + " (" + username + ") " + reset);
        showDisplayMessage();
    }

    public void passengerMenu() throws Exception {
        int choice;
        do {
            showAppTitle();
            showDisplayMessage();
            System.out.println("""
                    \t\t\t\t╔══════════════════════════════════════════════════════╗
                    \t\t\t\t║  1. View Available Flights                           ║
                    \t\t\t\t╟──────────────────────────────────────────────────────╢
                    \t\t\t\t║  2. Add a reservation                                ║
                    \t\t\t\t╟──────────────────────────────────────────────────────╢
                    \t\t\t\t║  3. Show your reservations                           ║
                    \t\t\t\t╟──────────────────────────────────────────────────────╢
                    \t\t\t\t║  4. Cancel a reservation                             ║
                    \t\t\t\t╟──────────────────────────────────────────────────────╢
                    \t\t\t\t║  5. Logout                                           ║
                    \t\t\t\t╚══════════════════════════════════════════════════════╝
                            """);
            System.out.print("\t\t\t\tEnter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    showAppTitle();
                    flight.showPlaneDetails(database.databaseQuery("select * from planes where is_available = 1;"));
                    System.out.print("Press enter to continue...");
                    scanner.nextLine();
                    scanner.nextLine();
                    break;
                case 2:
                    showAppTitle();
                    reserveSeat();
                    break;
                case 3:
                    clearScreen();
                    showAppTitle();
                    showTickets(database.databaseQuery(
                            "select * from reservations inner join planes on reservations.plane_id = planes.id where user_id = ?;",
                            userId));
                    System.out.print("Press enter to continue...");
                    scanner.nextLine();
                    scanner.nextLine();
                    break;
                case 4:
                    cancelReservation();
                    break;
                case 5:
                    start.showStartMenu();
                    break;
                default:
                    setDisplayMessage(red + "\t    ERROR ! Please enter valid option !" + reset);

            }

        } while (choice != 4);
    }

}
