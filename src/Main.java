import view.console.ConsoleView;


public class Main {

    public static void main(String[] args) {
        // Display startup banner
        displayStartupBanner();

        // Launch console view
        ConsoleView consoleView = new ConsoleView();
        consoleView.start();

        System.out.println("Goodbye! \n");
    }


    private static void displayStartupBanner() {
        System.out.println("TRIO\n");
        System.out.println();
    }
}
