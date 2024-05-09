import java.sql.ResultSet;
import java.util.Scanner;

public class Passenger extends User {

    Plane flight = new Plane();
    Database database = new Database();
    Start start = new Start();
    Scanner scanner = new Scanner(System.in);
    private ResultSet planes;

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

    // function to reserve seat
    public void reserveSeat() throws Exception {

        System.out.print("\t\t\t\tEnter the destination: ");
        flight.destination = scanner.nextLine();

        System.out.print("\t\t\t\tEnter the origin: ");
        flight.origin = scanner.nextLine();

        // Check if there are any flights available for the given origin and destination
        planes = flight.checkFlights(flight.origin, flight.destination);

        // If there are flights available
        if (planes != null) {
            clearScreen();
            showAppTitle();
            // Show the details of the available planes
            flight.showPlaneDetails(planes);

            System.out.print("Enter the id of the flight to reserve: ");
            flight.flightId = scanner.nextInt();

            // Check if the selected flight is valid
            if (flight.checkFlights(flight.origin, flight.destination, flight.flightId) != null) {
                System.out.print("Enter the number of seats you want to reserve: ");
                numberOfSeats = scanner.nextInt();
                scanner.close();

                // Check if the selected flight has enough seats available
                if (flight.checkSeatCapacity(flight.flightId, numberOfSeats)) {
                    // Generate a random reservation id
                    int reservationId = (int) (Math.random() * (999999 - 100000) + 100000);

                    // Insert the reservation into the database
                    database.databaseQuery(
                            "insert into reservations (ticket_id ,user_id, plane_id, number_of_seats) values (?,?,?,?);",
                            reservationId, userId, flight.flightId, numberOfSeats);

                    setDisplayMessage(
                            green + "\t Reservation successful. Your reservation id is " + reservationId + reset);
                } else {
                    setDisplayMessage(red + "\tSorry ! The requested number of seats are not available." + reset);
                }
            } else {
                setDisplayMessage(red + "\tSorry ! The selected flight is not valid." + reset);
            }
        } else {
            setDisplayMessage(red + "\tSorry ! No flights available for the given destination." + reset);
        }
    }

    public void showTickets(ResultSet reservation) throws Exception {
        if (!reservation.isBeforeFirst()) {
            setDisplayMessage(red + "\t!! No reservations found  !!" + reset);
            return;
        }
        while (reservation.next()) {
            System.out.printf("%-20s %s\n", "Ticket Id:", reservation.getString("ticket_id"));
            vline(120, '-');
            System.out.printf("%-20s %s\n", "Plane Id:", reservation.getString("plane_id"));
            System.out.printf("%-20s %s\n", "Number of Seats:", reservation.getString("number_of_seats"));
            System.out.printf("%-20s %s\n", "From:", reservation.getString("origin"));
            System.out.printf("%-20s %s\n", "To:", reservation.getString("destination"));
            System.out.printf("%-20s %s\n", "Departure Date:", reservation.getString("departure_date"));
            System.out.printf("%-20s %s\n", "Departure Time:", reservation.getString("departure_time"));
            System.out.printf("%-20s Rs %d\n", "Total Cost:",
                    reservation.getInt("fare") * reservation.getInt("number_of_seats"));
            vline(120, '-');
        }
    }

    // function to cancel a reservation
    public void cancelReservation() throws Exception {
        // Query the database for reservations associated with the current user
        ResultSet reservation = database.databaseQuery(
                "select * from reservations inner join planes on reservations.plane_id = planes.id where user_id = ?;",
                userId);
        // If the user has no reservations
        if (!reservation.isBeforeFirst()) {
            setDisplayMessage(red + "\t!! No reservations found !!" + reset);
            return;
        }
        // Display the user's reservations
        showTickets(reservation);
        System.out.print("Enter the ticket id of the reservation you want to cancel: ");
        int ticketId = scanner.nextInt();
        scanner.nextLine(); // Consume the leftover newline character
        System.out.print("Are you sure you want to cancel the reservation? (y/n): ");
        String choice = scanner.nextLine();
        if (choice.equals("y")) {
            // Delete the reservation from the database
            database.databaseQuery("delete from reservations where ticket_id = ?;", ticketId);
            setDisplayMessage(green + "\tReservation cancelled successfully" + reset);
        }
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
                    \t\t\t\t║  2. Reserve a Flight                                 ║
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
                    scanner.nextLine();
                    reserveSeat();
                    break;
                case 3:
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
                    scanner.nextLine();
                    scanner.nextLine();

                    break;
                default:
                    setDisplayMessage(red + "\t    ERROR ! Please enter valid option !" + reset);

            }

        } while (choice != 4);
    }

}
