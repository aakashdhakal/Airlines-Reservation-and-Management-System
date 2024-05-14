import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

public class Passenger extends User {

    Plane flight = new Plane();
    Database database = new Database();
    AirlinesReservationSystem start = new AirlinesReservationSystem();
    Scanner scanner = new Scanner(System.in);
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy").withLocale(Locale.ENGLISH);

    public static void showAppTitle() {
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
        // If there are flights available
        if (flight.checkFlights(flight.origin, flight.destination)) {
            clearScreen();
            showAppTitle();
            // Show the details of the available planes
            flight.showPlaneDetails("available");

            System.out.print("Enter the id of the flight to reserve: ");
            flight.flightId = scanner.nextInt();
            scanner.nextLine(); // Consume the leftover newline character

            // If the selected flight is valid
            if (flight.checkFlights(flight.origin, flight.destination, flight.flightId)) {
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
            LocalTime departureTime = LocalTime.parse(reservation.getString("departure_time"));
            LocalDate departureDate = LocalDate.parse(reservation.getString("departure_date"));
            System.out.println(
                    "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.printf("║ %65s   %50s\n", reservation.getString("name"), "║");
            System.out.printf("%s %119s", "║", "║\n");
            System.out.printf("║ %s %109s ║\n",
                    "Ticket ID: " + yellow + reservation.getString("ticket_id") + reset,
                    "Flight ID: " + yellow + reservation.getString("id") + reset);
            System.out.printf("%s %119s", "║", "║\n");

            System.out.printf("║ %s %50s %55s   ║\n",
                    "Passenger Name: " + yellow + reservation.getString("firstname") + " "
                            + reservation.getString("lastname") + reset,
                    "Date: " + yellow + dateFormatter.format(departureDate) + reset,
                    "Time: " + yellow + timeFormatter.format(departureTime) + reset);
            System.out.printf("%s %119s", "║", "║\n");

            System.out.printf("║ %-33s %-30s %75s\n",
                    "From: " + yellow + reservation.getString("origin") + reset,
                    "To: " + yellow + reservation.getString("destination") + reset,
                    "║");
            System.out.printf("%s %119s", "║", "║\n");

            System.out.printf("║ %s  %43s  %74s\n",
                    "Seats: " + yellow + reservation.getString("number_of_seats") + reset,
                    "Total Fare: " + yellow + "Rs " + reservation.getInt("fare") * reservation.getInt("number_of_seats")
                            + reset,
                    "║");
            System.out.printf("%s %119s", "║", "║\n");

            System.out.println(
                    """
                            ║                       **** Please arrive at the airport 2 hours before departure time *****                          ║
                            ╚══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝

                                            """);
        }

        reservation.close();
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
        reservation.close();
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
                    flight.showPlaneDetails("available");
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
                    setDisplayMessage(green + "\tLogged out successfully" + reset);
                    return;
                default:
                    setDisplayMessage(red + "\t    ERROR ! Please enter valid option !" + reset);
            }

        } while (choice != 4);
    }

    public void main(String[] args) throws Exception {
        showTickets(database.databaseQuery(
                "select * from reservations inner join planes on reservations.plane_id = planes.id inner join users on reservations.user_id = users.id  where user_id = 4;"));
    }

}
