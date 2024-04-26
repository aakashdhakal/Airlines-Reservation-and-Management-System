import java.util.Scanner;
import java.sql.*;

public class User {

    public String username;
    public String password;
    public String phoneNumber;
    public String role;

    private String origin;
    private String destination;

    private Database database = new Database();

    public void vline(int n, char ch) {
        for (int i = 0; i < n; i++) {
            System.out.print(ch);
        }
        System.out.println("");
    }

    public void showPlaneDetails(ResultSet planes) throws Exception {
        // display plane information in table
        vline(120, '=');
        System.out.printf("%s %10s %22s %23s %20s %20s %13s\n", "S.N", "Name", "Origin", "Destination",
                "Departure Date",
                "Departure Time", "Fare");
        vline(120, '=');
        do {
            System.out.printf("%2s %15s %20s %20s %20s %18s %17s\n", planes.getString("id"),
                    planes.getString("name"), planes.getString("origin"), planes.getString("destination"),
                    planes.getString("departure_date"), planes.getString("departure_time"), "Rs "
                            + planes.getString("fare"));
            vline(120, '-');
        } while (planes.next());
    }

    private void checkFlights() throws Exception {
        // check if there are any flights available
        // if there are, display them
        // if there are not, display a message saying there are no flights available
        ResultSet planes = database.databaseGet(
                "SELECT * FROM planes WHERE origin = '" + origin + "' AND destination = '" + destination + "'");

        if (planes.next()) {
            System.out.println("Available flights: ");
            showPlaneDetails(planes);
        } else {
            System.out.println("No flights available.");
        }
    }

    private boolean checkFlight(int flightId) throws Exception {
        ResultSet planes = database.databaseGet("SELECT * FROM planes WHERE id = " + flightId);
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
        checkFlights();
        System.out.println("Enter the flight id you want to reserve: ");
        int flightId = scanner.nextInt();
        scanner.close();
        if (checkFlight(flightId)) {
            // SELECT NO OF SEATS

        } else {
            System.out.println("Sorry ! Flight does not exist.");
        }

    }

    public static void main(String[] args) throws Exception {
        User user = new User();
        user.reserveSeat();
    }

}
