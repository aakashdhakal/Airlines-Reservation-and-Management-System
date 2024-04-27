import java.util.Scanner;
import java.sql.*;

public class User {

    public static String username;
    public String password;
    public String role;
    public int numberOfSeats;

    private static int userId = 1;
    private ResultSet planes;

    private Database database = new Database();
    private Plane flight = new Plane();

    public void reserveSeat() throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the destination: ");
        flight.destination = scanner.nextLine();
        System.out.print("Enter the origin: ");
        flight.origin = scanner.nextLine();
        planes = flight.checkFlights(flight.flightId, flight.origin, flight.destination);

        if (planes != null) {
            flight.showPlaneDetails(planes);

            System.out.print("Select the flight you want to reserve : ");
            flight.flightId = scanner.nextInt();

            if (flight.checkFlights(flight.flightId, flight.origin, flight.destination) != null) {
                // SELECT NO OF SEATS
                System.out.print("Enter the number of seats you want to reserve: ");
                numberOfSeats = scanner.nextInt();
                scanner.close();

                if (flight.checkSeatCapacity(flight.flightId, numberOfSeats)) {
                    // generate random reservation id
                    int reservationId = (int) (Math.random() * (999999 - 100000) + 100000);
                    database.databaseUpdate(
                            "insert into reservations (ticket_id ,user_id, plane_id, number_of_seats) values ("
                                    + reservationId + ","
                                    + userId + ","
                                    + flight.flightId + ", "
                                    + numberOfSeats + ");");
                    System.out.println("Reservation successful. Your reservation id is " + reservationId);
                } else {
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
            flight.vline(120, '-');
            System.out.printf("%-20s %s\n", "Ticket Id:", reservation.getString("ticket_id"));
            System.out.printf("%-20s %s\n", "Plane Id:", reservation.getString("plane_id"));
            System.out.printf("%-20s %s\n", "Number of Seats:", reservation.getString("number_of_seats"));
            System.out.printf("%-20s %s\n", "From:", reservation.getString("origin"));
            System.out.printf("%-20s %s\n", "To:", reservation.getString("destination"));
            System.out.printf("%-20s %s\n", "Departure Date:", reservation.getString("departure_date"));
            System.out.printf("%-20s %s\n", "Departure Time:", reservation.getString("departure_time"));
            System.out.printf("%-20s Rs %d\n", "Total Cost:",
                    reservation.getInt("fare") * reservation.getInt("number_of_seats"));
            flight.vline(120, '-');
        }
    }

    public boolean authenticateUser(String username, String password, String role) throws Exception {
        ResultSet user = database.databaseGet("SELECT * FROM users WHERE username = '" + username + "' AND password = '"
                + password + "' AND role = '" + role + "';");
        if (user.next()) {
            userId = user.getInt("id");
            return true;
        }
        return false;

    }

    public void main(String[] args) throws Exception {
        User user = new User();
        // user.reserveSeat();
        ResultSet reservations = user.database
                .databaseGet(
                        "SELECT * FROM reservations INNER JOIN planes ON reservations.plane_id = planes.id INNER JOIN users ON reservations.user_id = users.id WHERE user_id = "
                                + userId + ";");
        user.showTickets(reservations);
    }

}
