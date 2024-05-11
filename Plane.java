import java.sql.ResultSet;
import java.time.*;
import java.time.format.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Plane extends Start {
    public int flightId;
    public String origin;
    public String destination;
    public String capacity;
    public String departureDate;
    public String departureTime;
    public String fare;
    public String available;
    public String name;

    private Database database = new Database();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
    private Scanner scanner = new Scanner(System.in);

    // display plane information in table format
    public void showPlaneDetails(Object... params) throws Exception {
        ResultSet planes;
        String query = "SELECT * FROM planes";
        if (params.length > 0) {
            query += " WHERE id = " + params[0];
        }
        planes = database.databaseQuery(query + ";");
        if (planes == null) {
            setDisplayMessage(red + "\t!! Plane not found !!" + reset);
            return;
        }
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

            if (!planes.isLast()) {
                System.out.print(
                        """
                                ╟───────┼──────────────────┼──────────────┼──────────────┼────────────────┼────────────────┼───────────┼──────────────╢
                                """);
            }
        }

        System.out.print(
                """
                        ╙───────┴──────────────────┴──────────────┴──────────────┴────────────────┴────────────────┴───────────┴──────────────╜
                        """);

        planes.close();
    }

    // check if the flight exists for the given origin and destination
    public boolean checkFlights(String origin, String destination, Object... params) throws Exception {

        String query = "SELECT * FROM planes WHERE origin = ? AND destination = ? AND available = 1;";
        if (params.length > 0) {
            query += " AND id = " + params[0];
        }
        if (database.databaseQuery(query + ";", origin, destination).isBeforeFirst()) {
            return true;
        } else {
            return false;
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
            planes.close();
            return availableSeats;
        }
        planes.close();
        return 0;
    }

    public Plane enterPlaneDetails() {
        Plane flight = new Plane();
        System.out.print("\n\t\t\t\tName: ");
        flight.name = scanner.nextLine();
        System.out.print("\t\t\t\tDeparture From: ");
        flight.origin = scanner.nextLine();
        System.out.print("\t\t\t\tDestination: ");
        flight.destination = scanner.nextLine();
        System.out.print("\t\t\t\tSeat Capacity: ");
        flight.capacity = scanner.nextLine();
        System.out.print("\t\t\t\tDeparture Date (yyyy-mm-dd): ");
        flight.departureDate = scanner.nextLine();
        System.out.print("\t\t\t\tDeparture Time (hh:mm AM/PM): ");
        flight.departureTime = scanner.nextLine();
        System.out.print("\t\t\t\tFare: ");
        flight.fare = scanner.nextLine();
        System.out.print("\t\t\t\tIs the plane available for booking? (y/n): ");
        flight.available = scanner.nextLine();
        flight.available = flight.available.equals("y") ? "1" : "0";

        return flight;
    }

    public void addPlane() throws Exception {
        System.out.println("\n");
        printCentered("Enter Plane Details");
        printCentered("───────────────────");
        Plane flight = enterPlaneDetails();
        // generate random id

        flight.flightId = 1000 + (int) (Math.random() * ((99999 - 1000) + 1));
        database.databaseQuery(
                "INSERT INTO planes (id,name, origin, destination, capacity, departure_date, departure_time, fare, available) VALUES (?,?,?,?,?,?,?,?,?);",
                flight.flightId,
                flight.name, flight.origin, flight.destination, Integer.parseInt(flight.capacity), flight.departureDate,
                flight.departureTime,
                Integer.parseInt(flight.fare), Boolean.parseBoolean(flight.available));
        setDisplayMessage(green + "\tFlight added successfully !" + reset);
    }

    public void editPlaneDetails(int id) throws Exception {
        if (database.databaseQuery("select * from planes where id = ?;", id) == null) {
            setDisplayMessage(red + "\t!! Plane not found !!" + reset);
            return;
        } else {

            showPlaneDetails(id);
            printCentered("Enter new details (Press Enter to keep the old value)");
            printCentered("──────────────────────────────────────────────────────────");
            Plane flight = enterPlaneDetails();

            String query = "UPDATE planes SET ";
            List<Object> params = new ArrayList<>();
            String parameters[] = { flight.name, flight.origin, flight.destination, String.valueOf(flight.capacity),
                    flight.departureDate, flight.departureTime, String.valueOf(flight.fare),
                    String.valueOf(flight.available) };
            String columns[] = { "name", "origin", "destination", "capacity", "departure_date", "departure_time",
                    "fare", "available" };
            for (int i = 0; i < parameters.length; i++) {
                if ((i == 3 || i == 6) && !parameters[i].isEmpty()) {
                    params.add(Integer.parseInt(parameters[i]));
                    query += columns[i] + " = ?, ";
                } else if (i == 7 && !parameters[i].isEmpty()) {
                    params.add(Boolean.parseBoolean(parameters[i]));
                    query += columns[i] + " = ?, ";

                } else if (!parameters[i].isEmpty()) {
                    params.add(parameters[i]);
                    query += columns[i] + " = ?, ";
                }

                // convert into int and boolean

            }
            query = query.substring(0, query.length() - 2);
            query += " WHERE id = ?;";
            params.add(id);
            database.databaseQuery(query, params.toArray());
            setDisplayMessage(green + "\t Flight details updated successfully !" + reset);
        }

    }

}
