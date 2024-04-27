import java.util.Scanner;
import java.sql.*;

public class User {

    public String username;
    public String password;
    public String role;
    public int numberOfSeats;
    public int flightId;

    private int userId = 1000;
    private String origin;
    private String destination;
    private ResultSet planes;

    private Database database = new Database();
    private Plane flight = new Plane();

    private boolean checkFlights() throws Exception {
        // check if there are any flights available
        // if there are, display them
        // if there are not, display a message saying there are no flights available
        planes = database.databaseGet(
                "SELECT * FROM planes WHERE origin = '" + origin + "' AND destination = '" + destination + "'");
        if (planes.next()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkFlightDestination(int flightId, String origin, String destination) throws Exception {
        ResultSet planes = database.databaseGet("SELECT * FROM planes WHERE id = " +
                flightId + " AND origin = '"
                + origin + "' AND destination = '" + destination + "'");
        if (planes.next()) {
            return true;
        } else {
            return false;
        }
    }

    public void reserveSeat() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the destination: ");
        destination = scanner.nextLine();
        System.out.print("Enter the origin: ");
        origin = scanner.nextLine();
        if (checkFlights()) {
            flight.showPlaneDetails(planes);
            System.out.print("Select the flight you want to reserve : ");
            flightId = scanner.nextInt();
            if (checkFlightDestination(flightId, origin, destination)) {
                // SELECT NO OF SEATS
                System.out.print("Enter the number of seats you want to reserve: ");
                numberOfSeats = scanner.nextInt();
                scanner.close();
                // generate random reservation id
                int reservationId = (int) (Math.random() * (999999 - 100000) + 100000);
                database.databaseUpdate(
                        "insert into reservations (ticket_id ,user_id, plane_id, number_of_seats) values ("
                                + reservationId + ","
                                + userId + ","
                                + flightId + ", "
                                + numberOfSeats + ");");
                System.out.println("Reservation successful !");
            } else {
                System.out.println("Sorry ! Flight does not exist.");
            }
        } else {
            System.out.println("Sorry ! No flights available.");
        }
    }

    public static void main(String[] args) throws Exception {
        User user = new User();
        user.reserveSeat();
    }

}
