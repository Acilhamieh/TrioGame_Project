import view.console.ConsoleView;

/**
 * Main entry point for Trio_UTBM game.
 * Launches the console interface.
 *
 * @author Dana SLEIMAN, Acil HAMIEH
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {
        // Display startup banner
        displayStartupBanner();

        // Launch console view
        ConsoleView consoleView = new ConsoleView();
        consoleView.start();

        System.out.println("Goodbye! 👋\n");
    }

    /**
     * Display startup banner
     */
    private static void displayStartupBanner() {
        System.out.println("\n");
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                                                          ║");
        System.out.println("║                  ████████╗██████╗ ██╗ ██████╗            ║");
        System.out.println("║                  ╚══██╔══╝██╔══██╗██║██╔═══██╗           ║");
        System.out.println("║                     ██║   ██████╔╝██║██║   ██║           ║");
        System.out.println("║                     ██║   ██╔══██╗██║██║   ██║           ║");
        System.out.println("║                     ██║   ██║  ██║██║╚██████╔╝           ║");
        System.out.println("║                     ╚═╝   ╚═╝  ╚═╝╚═╝ ╚═════╝            ║");
        System.out.println("║                                                          ║");
        System.out.println("║                        UTBM Edition                      ║");
        System.out.println("║                                                          ║");
        System.out.println("║              Graduate by Forming Course Trios!          ║");
        System.out.println("║                                                          ║");
        System.out.println("║                    Developed by:                         ║");
        System.out.println("║                 Dana SLEIMAN & Acil HAMIEH               ║");
        System.out.println("║                                                          ║");
        System.out.println("║                    Course: AP4B                          ║");
        System.out.println("║                      UTBM 2024                           ║");
        System.out.println("║                                                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();
    }
}
