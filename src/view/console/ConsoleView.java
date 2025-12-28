package view.console;

import controller.*;
import enums.*;
import java.util.List;

/**
 * Main console view - coordinates the console-based user interface.
 * Entry point for console mode gameplay.
 *
 * @author Dana SLEIMAN
 * @version 1.0
 */
public class ConsoleView {
    private GameController gameController;
    private MenuView menuView;
    private GameView gameView;
    private InputReader inputReader;

    /**
     * Constructor for ConsoleView
     */
    public ConsoleView() {
        this.gameController = new GameController();
        this.menuView = new MenuView();
        this.gameView = new GameView();
        this.inputReader = new InputReader();
    }

    /**
     * Start the console interface
     */
    public void start() {
        displayWelcomeBanner();

        boolean running = true;

        while (running) {
            int choice = menuView.showMainMenu();

            switch (choice) {
                case 1:
                    startNewGame();
                    break;
                case 2:
                    showRules();
                    break;
                case 3:
                    showAbout();
                    break;
                case 4:
                    running = false;
                    System.out.println("\n👋 Thanks for playing Trio_UTBM!");
                    System.out.println("See you next time!\n");
                    break;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }

        inputReader.close();
    }

    /**
     * Display welcome banner
     */
    private void displayWelcomeBanner() {
        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║                                                    ║");
        System.out.println("║              🎓 TRIO_UTBM 🎓                      ║");
        System.out.println("║                                                    ║");
        System.out.println("║         Graduate by Forming Course Trios!         ║");
        System.out.println("║                                                    ║");
        System.out.println("╚════════════════════════════════════════════════════╝");
    }

    /**
     * Start a new game
     */
    private void startNewGame() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           🎮 NEW GAME SETUP");
        System.out.println("=".repeat(50));

        // Get game configuration from user
        int numPlayers = menuView.getPlayerCount();
        if (numPlayers == -1) return; // User cancelled

        GameMode mode = menuView.getGameMode();
        if (mode == null) return;

        // Team mode validation
        if (mode.isTeamMode() && numPlayers % 2 != 0) {
            System.out.println("\n❌ Team mode requires an even number of players!");
            System.out.println("Please select 2, 4, or 6 players for team mode.\n");
            inputReader.waitForEnter();
            return;
        }

        Difficulty difficulty = menuView.getDifficulty();
        if (difficulty == null) return;

        List<String> playerNames = menuView.getPlayerNames(numPlayers);
        if (playerNames == null || playerNames.isEmpty()) return;

        // Initialize game
        boolean success = gameController.initializeGame(numPlayers, mode, difficulty, playerNames);

        if (success) {
            gameController.startGame();
            runGameLoop();
        } else {
            System.out.println("\n❌ Failed to initialize game!");
            inputReader.waitForEnter();
        }
    }

    /**
     * Main game loop
     */
    private void runGameLoop() {
        while (gameController.isGameRunning()) {
            // Clear and display game state
            inputReader.clearScreen();
            gameView.displayGameState(gameController);

            // Get player action
            System.out.println("\n" + "─".repeat(50));
            System.out.println("What would you like to do?");
            System.out.println("1. Form a Trio (select 3 cards)");
            System.out.println("2. View Scores");
            System.out.println("3. View Rules");
            System.out.println("4. Quit Game");
            System.out.print("\nChoice: ");

            String input = inputReader.readLine();

            switch (input.trim()) {
                case "1":
                    executePlayerTurn();
                    break;
                case "2":
                    gameView.displayDetailedScores(gameController);
                    inputReader.waitForEnter();
                    break;
                case "3":
                    showQuickRules();
                    inputReader.waitForEnter();
                    break;
                case "4":
                    if (confirmQuit()) {
                        gameController.endGame();
                        return;
                    }
                    break;
                default:
                    System.out.println("❌ Invalid choice!");
                    inputReader.waitForEnter();
            }
        }

        // Game ended
        System.out.println("\nPress Enter to return to main menu...");
        inputReader.waitForEnter();
    }

    /**
     * Execute a player's turn
     */
    private void executePlayerTurn() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           🎯 FORM A TRIO");
        System.out.println("=".repeat(50));

        System.out.println("\nSelect 3 cards:");
        System.out.println("• 2 cards from your hand");
        System.out.println("• 1 card from the Lecture Hall");
        System.out.println("\nEnter card indices (e.g., 0 1 4):");
        System.out.print("> ");

        String input = inputReader.readLine();

        if (input.trim().equalsIgnoreCase("cancel")) {
            return;
        }

        InputHandler handler = new InputHandler();
        int[] selection = handler.parseCardSelection(input);

        if (selection != null) {
            boolean success = gameController.executeTurn(selection);

            if (success) {
                System.out.println("\n✅ Valid trio formed! You get a bonus turn!");
            } else {
                System.out.println("\n❌ Invalid trio. Turn passes to next player.");
            }

            inputReader.waitForEnter();
        } else {
            inputReader.waitForEnter();
        }
    }

    /**
     * Show game rules
     */
    private void showRules() {
        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║                   📖 GAME RULES                    ║");
        System.out.println("╚════════════════════════════════════════════════════╝\n");

        System.out.println("🎯 OBJECTIVE:");
        System.out.println("   Be the first to earn 6 ECTS credits by forming trios!\n");

        System.out.println("🎴 WHAT IS A TRIO?");
        System.out.println("   Three cards with the SAME course code\n");

        System.out.println("📚 GAME MODES:");
        System.out.println("   • Simple Mode: Any 3 matching cards = 2 ECTS");
        System.out.println("   • Advanced Mode: 3 matching cards from same branch = 3 ECTS\n");

        System.out.println("👥 TEAM MODE:");
        System.out.println("   • Play in teams of 2");
        System.out.println("   • Share ECTS credits");
        System.out.println("   • First team to 6 ECTS wins!\n");

        System.out.println("⭐ SPECIAL:");
        System.out.println("   • PFE Trio (3 PFE cards) = 6 ECTS = Instant Win!\n");

        System.out.println("🔄 HOW TO PLAY:");
        System.out.println("   1. Select 2 cards from your hand");
        System.out.println("   2. Select 1 card from the Lecture Hall");
        System.out.println("   3. If valid trio → Earn ECTS + Bonus turn!");
        System.out.println("   4. If invalid → Turn passes to next player\n");

        inputReader.waitForEnter();
    }

    /**
     * Show quick rules during game
     */
    private void showQuickRules() {
        System.out.println("\n📖 Quick Rules:");
        System.out.println("• Trio = 3 cards with same course code");
        System.out.println("• Select 2 from hand + 1 from hall");
        System.out.println("• Valid trio = ECTS credits + bonus turn");
        System.out.println("• First to 6 ECTS graduates!");
    }

    /**
     * Show about information
     */
    private void showAbout() {
        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║                   ℹ️  ABOUT                        ║");
        System.out.println("╚════════════════════════════════════════════════════╝\n");

        System.out.println("🎓 TRIO_UTBM");
        System.out.println("   A card game about graduating from UTBM!\n");

        System.out.println("👨‍💻 Developed by:");
        System.out.println("   • Dana SLEIMAN");
        System.out.println("   • Acil HAMIEH\n");

        System.out.println("📚 Course: AP4B");
        System.out.println("🏫 UTBM - Université de Technologie");
        System.out.println("    de Belfort-Montbéliard\n");

        System.out.println("📅 Session 1: Model & UML Design");
        System.out.println("📅 Session 2: Controllers & Console Interface");
        System.out.println("📅 Session 3: Graphical User Interface\n");

        inputReader.waitForEnter();
    }

    /**
     * Confirm quit action
     * @return true if user confirms quit
     */
    private boolean confirmQuit() {
        System.out.print("\nAre you sure you want to quit? (y/n): ");
        String response = inputReader.readLine().trim().toLowerCase();
        return response.equals("y") || response.equals("yes");
    }
}
