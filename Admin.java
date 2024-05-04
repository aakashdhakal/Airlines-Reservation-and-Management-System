
public class Admin extends User {

    Database database = new Database();

    @Override
    public void showAppTitle() {
        clearScreen();
        printCentered("\n");
        printCentered("╔══════════════════════════════════════════════════════╗");
        printCentered("║            Welcome to Skypass Admin Portal           ║");
        printCentered("╚══════════════════════════════════════════════════════╝");
        // show user name and role
        printCentered("Logged in as: " + userFirstName + " " + userLastName);
        showDisplayMessage();
    }

    public void adminMenu() throws Exception {
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
            System.out.print("\t\t\t\tEnter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    setDisplayMessage("Add a reservation");
                    showAppTitle();
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
