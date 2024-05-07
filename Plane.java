import java.sql.ResultSet;
import java.util.Scanner;

public class Plane extends Start {
    public int flightId;
    public String origin;
    public String destination;

    public int capacity;
    public String departureDate;
    public String departureTime;
    public int fare;

    private Database database = new Database();

    private Scanner scanner = new Scanner(System.in);

    public void vline(int n, char ch) {
        for (int i = 0; i < n; i++) {
            System.out.print(ch);
        }
        System.out.println("");
    }

    // display plane information in table format
    public void showPlaneDetails(ResultSet planes) throws Exception {
        String format = "| %-5s | %-10s | %-15s | %-15s | %-15s | %-12s | %-11s |\n";

        vline(120, '═');
        System.out.printf(format, "S.N", "Name", "Origin", "Destination",
                "Departure Date",
                "Departure Time", "Fare");
        vline(120, '═');
        while (planes.next()) {
            System.out.printf(format, planes.getString("id"),
                    planes.getString("name"), planes.getString("origin"), planes.getString("destination"),
                    planes.getString("departure_date"), planes.getString("departure_time"), "Rs "
                            + planes.getString("fare"));
            vline(120, '─');
        }
        scanner.nextLine();
    }

    // check if the flight exists for the given origin and destination
    public ResultSet checkFlights(int flightId, String origin, String destination) throws Exception {

        String query = "SELECT * FROM planes WHERE origin = ? AND destination = ?";
        if (flightId != 0) {
            query += " AND id = " + flightId;
        }
        ResultSet planes = database.databaseQuery(query + ";", origin, destination);
        if (planes.next()) {
            return planes;
        } else {
            return null;
        }
    }

    public boolean checkSeatCapacity(int flightId, int numberOfSeats) throws Exception {
        ResultSet planes = database
                .databaseQuery("SELECT * FROM planes INNER JOIN reservations WHERE plane_id = ?;", flightId);
        if (planes.next()) {
            int reserved = planes.getRow();
            int capacity = planes.getInt("capacity");
            int availableSeats = capacity - reserved;
            if (availableSeats >= numberOfSeats) {
                return true;
            }
        }
        return false;
    }
}
