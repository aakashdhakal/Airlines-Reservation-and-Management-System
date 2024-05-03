import java.util.Scanner;
import java.sql.*;

public class User extends Start {

    public static String username;
    public static String userFirstName;
    public static String userLastName;
    public String password;
    public String role;
    public int numberOfSeats;
    public static int userId;

    private ResultSet planes;

    private Database database = new Database();
    private Plane flight = new Plane();

    // function to reserve seat
    public void reserveSeat() throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the destination: ");
        flight.destination = scanner.nextLine();

        System.out.print("Enter the origin: ");
        flight.origin = scanner.nextLine();

        // Check if there are any flights available for the given origin and destination
        planes = flight.checkFlights(flight.flightId, flight.origin, flight.destination);

        // If there are flights available
        if (planes != null) {
            // Show the details of the available planes
            flight.showPlaneDetails(planes);

            System.out.print("Select the flight you want to reserve : ");
            flight.flightId = scanner.nextInt();

            // Check if the selected flight is valid
            if (flight.checkFlights(flight.flightId, flight.origin, flight.destination) != null) {
                System.out.print("Enter the number of seats you want to reserve: ");
                numberOfSeats = scanner.nextInt();
                scanner.close();

                // Check if the selected flight has enough seats available
                if (flight.checkSeatCapacity(flight.flightId, numberOfSeats)) {
                    // Generate a random reservation id
                    int reservationId = (int) (Math.random() * (999999 - 100000) + 100000);

                    // Insert the reservation into the database
                    database.databaseQuery(
                            "insert into reservations (ticket_id ,user_id, plane_id, number_of_seats) values ("
                                    + reservationId + ","
                                    + userId + ","
                                    + flight.flightId + ", "
                                    + numberOfSeats + ");");

                    System.out.println("Reservation successful. Your reservation id is " + reservationId);
                } else {
                    // Inform the user that the requested number of seats are not availabl
                    System.out.println("Sorry ! The requested number of seats are not available.");
                }
            } else {
                System.out.println("Sorry ! Flight does not exist.");
            }
        } else {
            System.out.println("Sorry ! No flights available.");
        }
    }

    public void showTickets(ResultSet reservation) throws Exception {
        while (reservation.next()) {
            vline(120, '-');
            System.out.printf("%-20s %s\n", "Ticket Id:", reservation.getString("ticket_id"));
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

    public boolean authenticateUser(String username, String password, String role) throws Exception {
        ResultSet user = database
                .databaseQuery("SELECT * FROM users WHERE username = '" + username + "' AND password = '"
                        + password + "' AND role = '" + role + "';");
        if (user.next()) {
            userId = user.getInt("id");
            return true;
        }
        return false;
    }

    public boolean checkUsername(String username) throws Exception {
        ResultSet user = database.databaseQuery("SELECT * FROM users WHERE username = '" + username + "';");
        if (user.next()) {
            return true;
        }
        return false;
    }

}
