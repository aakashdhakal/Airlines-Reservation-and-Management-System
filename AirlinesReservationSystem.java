import java.util.Scanner;

public class AirlinesReservationSystem {

    String red = "\033[0;31m";
    String reset = "\u001B[0m";
    String green = "\033[0;32m";
    String yellow = "\033[0;33m";
    String blue = "\033[0;34m";
    String purple = "\033[0;35m";
    String cyan = "\033[0;36m";

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
        System.out.println(cyan + """

                \t\t\t\t███████ ██   ██ ██    ██ ██████   █████  ███████ ███████
                \t\t\t\t██      ██  ██   ██  ██  ██   ██ ██   ██ ██      ██
                \t\t\t\t███████ █████     ████   ██████  ███████ ███████ ███████
                \t\t\t\t     ██ ██  ██     ██    ██      ██   ██      ██      ██
                \t\t\t\t███████ ██   ██    ██    ██      ██   ██ ███████ ███████
                    """ + reset);

        printCentered("╠═════════════ Airlines Reservation System ════════════╣");
    }

    public void showDisplayMessage() {
        if (displayMessage.equals("")) {
            System.out.println("\n");
            return;
        }
        System.out.println("\n");
        printCentered(displayMessage);
        displayMessage = "";
    }

    public static void setDisplayMessage(String message) {
        displayMessage = message;
    }

    public void showStartMenu() {
        showDisplayMessage();
        printCentered("""

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
        AirlinesReservationSystem start = new AirlinesReservationSystem();
        User user = new User();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            // clear the screen
            start.showAppTitle();
            start.showStartMenu();
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
                        Passenger passenger = new Passenger();
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
                    setDisplayMessage(start.red + "\t ERROR ! Please enter valid option !" +
                            start.reset);
            }

        } while (choice != 4);
        scanner.close();

    }

}
