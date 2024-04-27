import java.sql.ResultSet;

public class Plane {
    public int flightId;
    public String origin;
    public String destination;

    public int capacity;
    public String departureDate;
    public String departureTime;
    public int fare;

    private Database database = new Database();

    public void vline(int n, char ch) {
        for (int i = 0; i < n; i++) {
            System.out.print(ch);
        }
        System.out.println("");
    }

    // display plane information in table format
    public void showPlaneDetails(ResultSet planes) throws Exception {
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

    // check if the flight exists for the given origin and destination
    public ResultSet checkFlights(int flightId, String origin, String destination) throws Exception {

        String query = "SELECT * FROM planes WHERE origin = '" + origin + "' AND destination = '" + destination + "'";
        if (flightId != 0) {
            query += " AND id = " + flightId;
        }
        ResultSet planes = database.databaseGet(query + ";");
        if (planes.next()) {
            return planes;
        } else {
            return null;
        }
    }

}
