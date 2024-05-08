import java.io.Console;
import java.sql.*;
import java.util.Scanner;

public class User extends Start {

    public static String username;
    public static String userFirstName;
    public static String userLastName;
    public static String userContactNumber;
    public String password;
    public String role;
    public int numberOfSeats;
    public static int userId;

    private ResultSet planes;

    private Database database = new Database();
    private Plane flight = new Plane();
    private Start start = new Start();
    private Scanner scanner = new Scanner(System.in);

    // function to reserve seat
    public void reserveSeat() throws Exception {

        System.out.print("\t\t\t\tEnter the destination: ");
        flight.destination = scanner.nextLine();

        System.out.print("\t\t\t\tEnter the origin: ");
        flight.origin = scanner.nextLine();

        // Check if there are any flights available for the given origin and destination
        planes = flight.checkFlights(flight.origin, flight.destination);

        // If there are flights available
        if (planes != null) {
            clearScreen();
            showAppTitle();
            // Show the details of the available planes
            flight.showPlaneDetails(planes);

            System.out.print("Enter the id of the flight to reserve: ");
            flight.flightId = scanner.nextInt();

            // Check if the selected flight is valid
            if (flight.checkFlights(flight.origin, flight.destination, flight.flightId) != null) {
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
                    // Inform the user that the requested number of seats are not availabl
                    setDisplayMessage(red + "\tSorry ! The requested number of seats are not available." + reset);
                }
            } else {
                // Inform the user that the selected flight is not valid
                setDisplayMessage(red + "\tSorry ! The selected flight is not valid." + reset);
            }
        } else {
            setDisplayMessage(red + "\tSorry ! No flights available for the given destination." + reset);
        }
    }

    // function to handle user login
    public boolean userLogin(String role) throws Exception {
        Console console = System.console();
        // Loop until a valid username is entered
        do {
            start.showAppTitle();
            showDisplayMessage();
            System.out.print("\n\t\t\t\tEnter your username: ");
            username = scanner.nextLine();

            // If the username is not valid, display an error message
            if (!checkUsername(username)) {
                setDisplayMessage(red + "\t!!OOPS! The username is not correct!!" + reset);
            }
        } while (!checkUsername(username));
        System.out.print("\t\t\t\tEnter your password: ");
        char[] passwordArray = console.readPassword();
        password = new String(passwordArray);
        java.util.Arrays.fill(passwordArray, ' ');

        // If the user is authenticated successfully
        if (authenticateUser(username, password, role)) {
            // Query the database for the user's details
            ResultSet user = database.databaseQuery("select * from users where username = ?;", username);
            user.next();

            // Store the user's details in local variables
            userId = user.getInt("id");
            userFirstName = user.getString("firstname");
            userLastName = user.getString("lastname");

            // Return true to indicate successful login
            return true;
        } else {
            // If the authentication failed, display an error message and return false
            setDisplayMessage(red + "\tERROR! The username or password is incorrect" + reset);
            return false;
        }
    }

    // function to register a new user
    public void registerUser(String role) throws Exception {
        // Loop until a unique username is entered
        do {
            start.showAppTitle();
            showDisplayMessage();
            System.out.print("\n\t\t\t\tEnter a username: ");
            username = scanner.nextLine();

            // If the username is already taken, display an error message
            if (checkUsername(username)) {
                setDisplayMessage(red + "\t!!OOPS! The username is already taken!!" + reset);
            }
        } while (checkUsername(username));
        System.out.print("\t\t\t\tEnter your password: ");
        password = scanner.nextLine();
        System.out.print("\t\t\t\tEnter your first name: ");
        userFirstName = scanner.nextLine();
        System.out.print("\t\t\t\tEnter your last name: ");
        userLastName = scanner.nextLine();
        System.out.print("\t\t\t\tEnter your contact number: ");
        userContactNumber = scanner.nextLine();

        // Insert the new user's details into the database
        database.databaseQuery(
                "insert into users (username, password, firstname, lastname, role,phone_no) values (?,?,?,?,?,?);",
                username, password, userFirstName, userLastName, role, userContactNumber);

        // Display a success message
        setDisplayMessage(green + "\tUser registered successfully" + reset);
    }

    public void showTickets(ResultSet reservation) throws Exception {
        if (!reservation.isBeforeFirst()) {
            setDisplayMessage(red + "\t!! No reservations found  !!" + reset);
            return;
        }
        while (reservation.next()) {
            System.out.printf("%-20s %s\n", "Ticket Id:", reservation.getString("ticket_id"));
            vline(120, '-');
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
    }

    public boolean authenticateUser(String username, String password, String role) throws Exception {
        ResultSet user = database
                .databaseQuery("SELECT * FROM users WHERE username = ? AND password = ? AND role = ?;", username,
                        password, role);
        if (user.next()) {
            userId = user.getInt("id");
            return true;
        }
        return false;
    }

    public boolean checkUsername(String username) throws Exception {
        ResultSet user = database.databaseQuery("SELECT * FROM users WHERE username = ?;", username);
        if (user.next()) {
            return true;
        }
        return false;
    }
}
