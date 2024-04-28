public class Start {

    public void vline(int n, char ch) {
        for (int i = 0; i < n; i++) {
            System.out.print(ch);
        }
        System.out.println("");
    }

    private void showAppTitle() {
        System.out.println("""


                \t\t\t         ███████ ██   ██ ██    ██ ██████   █████  ███████ ███████
                \t\t\t         ██      ██  ██   ██  ██  ██   ██ ██   ██ ██      ██
                \t\t\t         ███████ █████     ████   ██████  ███████ ███████ ███████
                \t\t\t              ██ ██  ██     ██    ██      ██   ██      ██      ██
                \t\t\t         ███████ ██   ██    ██    ██      ██   ██ ███████ ███████


                \t\t          !============== An online flight reservation system ===============!

                        """);
    }

    public void showStartMenu() {
        System.out.println("""
                \t\t\t           +------------------------------------------------+
                \t\t\t           |                                                |
                \t\t\t           |              1. Log in as admin                |
                \t\t\t           |                                                |
                \t\t\t           |              2. Log in as passenger            |
                \t\t\t           |                                                |
                \t\t\t           |              3. Register as passenger          |
                \t\t\t           |                                                |
                \t\t\t           +------------------------------------------------+
                    """);
    }

    public static void main(String[] args) {
        Start start = new Start();
        start.showAppTitle();
        start.showStartMenu();
    }
}
