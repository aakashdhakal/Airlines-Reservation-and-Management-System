public class Passenger extends User {

    Plane flight = new Plane();

    private void showPassengerMenu() {
        System.out.println("""


                \t\t\t         ███████ ██   ██ ██    ██ ██████   █████  ███████ ███████
                \t\t\t         ██      ██  ██   ██  ██  ██   ██ ██   ██ ██      ██
                \t\t\t         ███████ █████     ████   ██████  ███████ ███████ ███████
                \t\t\t              ██ ██  ██     ██    ██      ██   ██      ██      ██
                \t\t\t         ███████ ██   ██    ██    ██      ██   ██ ███████ ███████


                  \t\t       !=================== An online flight reservation system ====================!

                        """);
        flight.vline(120, '-');
    }

    public void main(String[] args) {
        Passenger passenger = new Passenger();
        passenger.showPassengerMenu();
    }

}
