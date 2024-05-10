import java.sql.ResultSet;
import java.time.*;
import java.time.format.*;
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
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
    Scanner scanner = new Scanner(System.in);

    // display plane information in table format
    public void showPlaneDetails(ResultSet planes) throws Exception {
        String format = "║ %s │    %10s    │   %-10s │  %-10s  │   %-12s │   %-13s│ %-10s│   %-10s ║\n";
        System.out.print(
                """
                        ╔═══════╤══════════════════╤══════════════╤══════════════╤════════════════╤════════════════╤═══════════╤══════════════╗
                        ║   ID  │       Name       │     From     │     To       │  Depart. Date  │  Depart. Time  │   Fare    │  Seats Left  ║
                        ╠═══════╪══════════════════╪══════════════╪══════════════╪════════════════╪════════════════╪═══════════╪══════════════╣
                        """);
        while (planes.next()) {
            LocalTime departureTime = LocalTime.parse(planes.getString("departure_time"));
            System.out.printf(format, planes.getString("id"),
                    planes.getString("name"), planes.getString("origin"), planes.getString("destination"),
                    planes.getString("departure_date"), formatter.format(departureTime), "Rs "
                            + planes.getString("fare"),
                    availableSeats(planes.getInt("id")));
            System.out.print(
                    """
                            ╟───────┼──────────────────┼──────────────┼──────────────┼────────────────┼────────────────┼───────────┼──────────────╢
                                                            """);
        }
        System.out.print(
                """
                        ╙───────┴──────────────────┴──────────────┴──────────────┴────────────────┴────────────────┴───────────┴──────────────╜
                                                        """);
        planes.close();
    }

    // check if the flight exists for the given origin and destination
    public ResultSet checkFlights(String origin, String destination, Object... params) throws Exception {

        String query = "SELECT * FROM planes WHERE origin = ? AND destination = ?";
        if (params.length > 0) {
            query += " AND id = " + params[0];
        }
        ResultSet planes = database.databaseQuery(query + ";", origin, destination);
        if (planes.isBeforeFirst()) {
            return planes;
        } else {
            return null;
        }
    }

    public boolean checkSeatCapacity(int flightId, int numberOfSeats) throws Exception {
        int availableSeats = availableSeats(flightId);
        if (availableSeats >= numberOfSeats) {
            return true;
        }
        return false;
    }

    public int availableSeats(int flightId) throws Exception {
        ResultSet planes = database
                .databaseQuery(
                        "SELECT planes.capacity, COUNT(reservations.ticket_id) as reserved FROM planes LEFT JOIN reservations ON planes.id = reservations.plane_id WHERE planes.id = ? GROUP BY planes.id;",
                        flightId);
        if (planes.next()) {
            int capacity = planes.getInt("capacity");
            int reserved = planes.getInt("reserved");
            int availableSeats = capacity - reserved;
            return availableSeats;
        }
        planes.close();
        return 0;
    }

    public void addPlane() throws Exception {
        System.out.println("\n");
        printCentered("Enter Plane Details");
        printCentered("───────────────────");
        System.out.println("\n");
        System.out.print("\t\t\t\tName: ");
        String name = scanner.nextLine();
        System.out.print("\t\t\t\tDeparture From: ");
        String origin = scanner.nextLine();
        System.out.print("\t\t\t\tDestination: ");
        String destination = scanner.nextLine();
        System.out.print("\t\t\t\tSeat Capacity: ");
        int capacity = scanner.nextInt();
        scanner.nextLine();
        boolean validDateTime = false;
        do {
            System.out.print("\t\t\t\tDeparture Date (yyyy-mm-dd): ");
            departureDate = scanner.nextLine();
            try {
                if (LocalDate.parse(departureDate).isBefore(LocalDate.now())) {
                    System.out.println(red + "\t\t\t\tInvalid date. Please enter a future date." + reset);
                    continue;
                }
                validDateTime = true;
                ;

                validDateTime = true;
            } catch (DateTimeParseException e) {
                System.out.println(red + "\t\t\t\tInvalid date format. Please enter in yyyy-mm-dd format." + reset);
            }
        } while (!validDateTime);

        System.out.print("\t\t\t\tFare: ");
        fare = scanner.nextInt();
        scanner.nextLine();

        database.databaseQuery(
                "INSERT INTO planes (name, origin, destination, capacity, departure_date, departure_time, fare) VALUES (?, ?, ?, ?, ?, ?, ?);",
                name, origin, destination, capacity, departureDate, departureTime, fare);
        setDisplayMessage(green + "\tFlight added successfully !" + reset);
    }

}
