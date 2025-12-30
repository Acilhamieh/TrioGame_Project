package view.gui;

import controller.*;
import enums.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Main window for the Trio_UTBM GUI - MEMORY GAME VERSION.
 * Contains all GUI panels and manages the overall interface.
 *
 * @author Acil HAMIEH, Dana SLEIMAN
 * @version 2.0 - Memory game
 */
public class MainWindow extends JFrame {
    private GameController gameController;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Panels
    private MenuPanel menuPanel;
    private GamePanel gamePanel;

    // Constants
    private static final int WINDOW_WIDTH = 1400;
    private static final int WINDOW_HEIGHT = 900;
    private static final String TITLE = "Trio_UTBM - Memory Game Edition!";

    /**
     * Constructor for MainWindow
     */
    public MainWindow() {
        this.gameController = new GameController();

        initializeWindow();
        createPanels();
        setupLayout();

        setVisible(true);
    }

    /**
     * Initialize window properties
     */
    private void initializeWindow() {
        setTitle(TITLE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        setResizable(true);
        setMinimumSize(new Dimension(1200, 800));
    }

    /**
     * Create all panels
     */
    private void createPanels() {
        // Create menu panel
        menuPanel = new MenuPanel(this);

        // Game panel will be created when game starts
        gamePanel = null;
    }

    /**
     * Setup layout with CardLayout for switching between panels
     */
    private void setupLayout() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add menu panel
        mainPanel.add(menuPanel, "MENU");

        add(mainPanel);

        // Show menu initially
        showMenu();
    }

    /**
     * Show the menu panel
     */
    public void showMenu() {
        cardLayout.show(mainPanel, "MENU");
    }

    /**
     * Start a new game with configuration
     * @param numPlayers Number of players
     * @param mode Game mode
     * @param difficulty Difficulty level
     * @param playerNames List of player names
     */
    public void startNewGame(int numPlayers, GameMode mode, Difficulty difficulty, List<String> playerNames) {
        // Initialize game
        boolean success = gameController.initializeGame(numPlayers, mode, difficulty, playerNames);

        if (success) {
            gameController.startGame();

            // Create new game panel
            gamePanel = new GamePanel(this, gameController);

            // Add game panel to card layout
            mainPanel.add(gamePanel, "GAME");

            // Switch to game view
            cardLayout.show(mainPanel, "GAME");

            // Update display
            gamePanel.updateDisplay();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to initialize game!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * End current game and return to menu
     */
    public void endGame() {
        int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to quit this game?",
                "Quit Game",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            gameController.endGame();

            // Remove game panel
            if (gamePanel != null) {
                mainPanel.remove(gamePanel);
                gamePanel = null;
            }

            // Return to menu
            showMenu();
        }
    }

    /**
     * Get the game controller
     * @return Game controller instance
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * Show rules dialog - UPDATED FOR MEMORY GAME
     */
    public void showRules() {
        String rules = "🎓 TRIO_UTBM - MEMORY GAME RULES\n\n" +
                "🎯 OBJECTIVE:\n" +
                "Be the first to earn 6 ECTS credits by forming trios!\n\n" +
                "🎴 WHAT IS A TRIO?\n" +
                "Three cards with the SAME course code\n\n" +
                "🧠 MEMORY GAME MECHANICS:\n" +
                "• Cards are sorted by ID (highest → lowest)\n" +
                "• Your hand: Only FIRST & LAST cards visible\n" +
                "• Neighbor's hand: Only FIRST & LAST cards visible\n" +
                "• Lecture hall: ALL cards visible with IDs\n" +
                "• Middle cards are HIDDEN [?] - you must remember them!\n\n" +
                "🎴 CARD IDs (memorize these!):\n" +
                "SY41=12, IA41=11, SY48=10, AP4B=9\n" +
                "GI21=8, GI28=7, GI41=6\n" +
                "MQ18=5, MQ41=4, MQ51=3\n" +
                "EN21=2, PFE=1\n\n" +
                "👥 NUMBER OF PLAYERS:\n" +
                "• 3-6 players recommended for memory game\n" +
                "• Team mode requires even number (2, 4, or 6)\n\n" +
                "📚 GAME MODES:\n" +
                "• Individual: Play alone\n" +
                "• Teams: Play in teams of 2 (pairs chosen in order)\n\n" +
                "⚙️  DIFFICULTY LEVELS:\n" +
                "• Simple: Any 3 matching cards = 2 ECTS\n" +
                "• Advanced: 3 matching cards from same branch = 3 ECTS\n\n" +
                "🏫 BRANCHES:\n" +
                "• 💻 Computer Science (CS)\n" +
                "• 🏭 Industrial Engineering (IE)\n" +
                "• ⚙️ Mechanical Engineering (ME)\n" +
                "• ⚡ Energy Engineering (EE)\n" +
                "• ⭐ PFE (Special)\n\n" +
                "⭐ SPECIAL:\n" +
                "• PFE Trio (3 PFE cards) = 6 ECTS = Instant Win!\n\n" +
                "🔄 HOW TO PLAY (NEW RULES!):\n" +
                "1. Select 1 card from YOUR hand (position 0-8)\n" +
                "2. Select 1 card from NEIGHBOR's hand (position 0-7)\n" +
                "3. Select 1 card from LECTURE HALL (all visible)\n" +
                "4. Click 'Form Trio' button\n" +
                "5. If valid trio → Earn ECTS + Bonus turn!\n" +
                "6. If invalid → Turn passes to next player\n" +
                "7. Both YOUR and NEIGHBOR's hands refill\n" +
                "8. Cards re-sort by ID automatically\n\n" +
                "🧠 STRATEGY:\n" +
                "• Remember which cards you drew\n" +
                "• Watch what neighbors draw\n" +
                "• Track IDs to deduce hidden cards\n" +
                "• Use lecture hall (all visible) to plan moves\n\n" +
                "🏆 WIN:\n" +
                "First player/team to 6 ECTS graduates!";

        JTextArea textArea = new JTextArea(rules);
        textArea.setEditable(false);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 600));

        JOptionPane.showMessageDialog(this,
                scrollPane,
                "Memory Game Rules",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show about dialog
     */
    public void showAbout() {
        String about = "🎓 TRIO_UTBM - MEMORY GAME EDITION\n\n" +
                "A memory-based card game about graduating from UTBM!\n\n" +
                "🎮 NEW FEATURES:\n" +
                "• Card ID system (1-12)\n" +
                "• Hidden cards (only first & last visible)\n" +
                "• Memory-based gameplay\n" +
                "• Neighbor card selection\n" +
                "• Auto-sorting by ID\n\n" +
                "👨‍💻 Developed by:\n" +
                "• Dana SLEIMAN\n" +
                "• Acil HAMIEH\n\n" +
                "📚 Course: AP4B\n" +
                "🏫 UTBM - Université de Technologie\n" +
                "    de Belfort-Montbéliard\n\n" +
                "📅 Session 1: Model & UML Design\n" +
                "📅 Session 2: Controllers & Console Interface\n" +
                "📅 Session 3: Graphical User Interface\n" +
                "📅 Session 4: Memory Game Enhancement\n\n" +
                "Version 2.0 - Memory Game - 2024";

        JOptionPane.showMessageDialog(this,
                about,
                "About Trio_UTBM",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Main entry point for GUI version
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel
        }

        // Create and show main window
        SwingUtilities.invokeLater(() -> new MainWindow());
    }
}
