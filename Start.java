import java.util.Scanner;

public class Start {

    public static String displayMessage = "";

    public static void printCentered(String message) {
        int width = 120;
        System.out.print(String.format("%" + ((width + message.length()) / 2) + "s\n", message));
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void vline(int n, char ch) {
        for (int i = 0; i < n; i++) {
            System.out.print(ch);
        }
        System.out.println("");
    }

    public void showAppTitle() {
        clearScreen();
        System.out.println("""

                \t\t\t\t███████ ██   ██ ██    ██ ██████   █████  ███████ ███████
                \t\t\t\t██      ██  ██   ██  ██  ██   ██ ██   ██ ██      ██
                \t\t\t\t███████ █████     ████   ██████  ███████ ███████ ███████
                \t\t\t\t     ██ ██  ██     ██    ██      ██   ██      ██      ██
                \t\t\t\t███████ ██   ██    ██    ██      ██   ██ ███████ ███████
                    """);

        printCentered("╠═════════════ Airlines Reservation System ════════════╣");
    }

    public void showDisplayMessage() {
        if (displayMessage.equals("")) {
            System.out.println("\n");
            return;
        }
        System.out.println("\n");
        vline(120, '─');
        printCentered(displayMessage);
        vline(120, '─');
        displayMessage = "";
    }

    public static void setDisplayMessage(String message) {
        displayMessage = message;
    }

    public void showStartMenu() {
        showDisplayMessage();
        System.out.println("""
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

    public static void main(String[] args) throws Exception {
        Start start = new Start();
        int choice;
        Scanner scanner = new Scanner(System.in);

        do {
            // clear the screen
            start.showAppTitle();
            start.showStartMenu();
            System.out.print("\t\t\t\tEnter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    Admin admin = new Admin();
                    admin.adminLogin();
                    break;
                case 2:
                    Passenger passenger = new Passenger();
                    passenger.passengerLogin();
                    break;
                case 3:
                    // Passenger passenger = new Passenger();
                    // passenger.registerPassenger();
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }

        } while (choice != 4);
        scanner.close();

    }

}
