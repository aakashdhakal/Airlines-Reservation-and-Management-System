import java.util.Scanner;

// Main class for the Airlines Reservation System
public class AirlinesReservationSystem {

    // User and Passenger objects to handle user and passenger related operations
    private static User user = new User();
    private static Scanner scanner = new Scanner(System.in);
    private static Passenger passenger = new Passenger();

    // ANSI color codes for console output
    public static String red = "\033[0;31m";
    public static String cyan = "\033[0;36m";
    public static String reset = "\033[0m";
    public static String green = "\033[0;32m";
    public static String yellow = "\033[0;33m";

    // Message to be displayed to the user
    private static String displayMessage = "";

    // Setter for displayMessage
    public static void setDisplayMessage(String message) {
        displayMessage = message;
    }

    // Method to show the display message and then reset it
    public static void showDisplayMessage() {
        if (displayMessage.equals("")) {
            System.out.println("\n");
            return;
        }
        System.out.println("\n");
        printCentered(displayMessage);
        displayMessage = "";
    }

    // Method to print a message centered in the console
    public static void printCentered(String message) {
        int width = 120;
        System.out.print(String.format("%" + ((width + message.length()) / 2) + "s\n", message));
    }

    // Method to clear the console screen
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Method to show the application title
    public static void showAppTitle() {
        clearScreen();
        System.out.println(cyan + """
                // ASCII Art for the title
                \t\t\t\t███████ ██   ██ ██    ██ ██████   █████  ███████ ███████
                \t\t\t\t██      ██  ██   ██  ██  ██   ██ ██   ██ ██      ██
                \t\t\t\t███████ █████     ████   ██████  ███████ ███████ ███████
                \t\t\t\t     ██ ██  ██     ██    ██      ██   ██      ██      ██
                \t\t\t\t███████ ██   ██    ██    ██      ██   ██ ███████ ███████
                    """ + reset);

        printCentered("╠═════════════ Airlines Reservation System ════════════╣");
    }

    // Method to show the start menu
    public static void showStartMenu() {
        showDisplayMessage();
        printCentered("""
                // ASCII Art for the menu
                \t\t\t\t╔══════════════════════════════════════════════════════╗
                \t\t\t\t║  1. ADMIN login                                      ║
                \t\t\t\t╟──────────────────────────────────────────────────────╢
                \t\t\t\t║  2. Passenger login                                  ║
                \t\t\t\t╟──────────────────────────────────────────────────────╢
                \t\t\t\t║  3. Register                                         ║
                \t\t\t\t╟──────────────────────────────────────────────────────╢
                \t\t\t\t║  4. Exit                                             ║
                \t\t\t\t╚══════════════════════════════════════════════════════╝
                        """);
    }

    // Main method
    public static void main(String[] args) throws Exception {

        int choice;

        do {
            showAppTitle();
            showStartMenu();
            System.out.print("\t\t\t\tEnter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    if (user.userLogin("admin")) {
                        Admin admin = new Admin();
                        admin.adminMenu();
                    }
                    break;
                case 2:
                    if (user.userLogin("passenger")) {
                        passenger.passengerMenu();
                    }
                    break;
                case 3:
                    user.registerUser("passenger");
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    setDisplayMessage(red + "\t ERROR ! Please enter valid option !" +
                            reset);
            }

        } while (choice != 4);
        scanner.close();

    }

}