import java.io.Console;
// import java.util.Scanner;

public class Passenger extends User {

    Plane flight = new Plane();
    Database database = new Database();

    private void passengerLogin() throws Exception {
        Console console = System.console();
        if (console == null) {
            throw new Exception("Couldn't get Console instance");
        }

        System.out.print("Enter your username: ");
        username = console.readLine();

        System.out.print("Enter your password: ");
        char[] passwordArray = console.readPassword();
        password = new String(passwordArray);
        java.util.Arrays.fill(passwordArray, ' ');
        role = "passenger";

        if (authenticateUser(username, password, role)) {
            System.out.println("Login successful");
        } else {
            System.out.println("Login failed");
        }
    }

    public void main(String[] args) throws Exception {
        Passenger passenger = new Passenger();
        // passenger.showPassengerMenu();
        passenger.passengerLogin();

    }

}
