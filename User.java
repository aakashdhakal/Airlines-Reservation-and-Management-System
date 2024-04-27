import java.util.Scanner;
import java.sql.*;

public class User {

    public String username;
    public String password;
    public String role;
    public int numberOfSeats;
    public int flightId = 0;

    private int userId = 1000;
    private ResultSet planes;

    private Database database = new Database();
    private Plane flight = new Plane();

    public void reserveSeat() throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the destination: ");
        flight.destination = scanner.nextLine();
        System.out.print("Enter the origin: ");
        flight.origin = scanner.nextLine();
        planes = flight.checkFlights(flightId, flight.origin, flight.destination);
        if (planes != null) {
            flight.showPlaneDetails(planes);

            System.out.print("Select the flight you want to reserve : ");
            flightId = scanner.nextInt();

            if (flight.checkFlights(flightId, flight.origin, flight.destination) != null) {
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
